package es.udc.ps.p26dorio;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import es.udc.ps.p26dorio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver br = new MyBroadcastReceiver(this);
    private WorkManager workManager;
    MyBoundCountingService myBoundService;
    Boolean mBound = false;
    public ActivityMainBinding binding; // Declare data binding object
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MyBoundCountingService.MyBinder binder = (MyBoundCountingService.MyBinder) service;
            myBoundService = binder.getService();
            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        workManager = WorkManager.getInstance(this);

        setupBroadcastReceiver();
        setupBroadcastButton();
        setupSwitchListeners();
        setupGetAndSetButtons();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void setupBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("es.udc.PSI.broadcast.GENERAL");
        filter.addAction("es.udc.PSI.service.local.SWITCH");
        filter.addAction("es.udc.PSI.service.bind.SWITCH");
        filter.addAction("es.udc.PSI.service.background.SWITCH");
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(this.br, filter, Context.RECEIVER_EXPORTED);
    }

    private void setupBroadcastButton() {
        binding.broadcastButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("es.udc.PSI.broadcast.GENERAL");
            intent.putExtra("data", "Mi propio tipo de notificación");
            sendBroadcast(intent);
        });
    }

    private void setupSwitchListeners() {
        binding.firstSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(isChecked, MyCountingService.class));

        binding.secondSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> handleSwitchChange(isChecked, MyBoundCountingService.class));

        binding.thirdSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> handleThirdSwitchChange(isChecked));
    }

    private void handleSwitchChange(boolean isChecked, Class<?> serviceClass) {
        if (isChecked) {
            String inputText = binding.countingEdit.getText().toString();
            if (!inputText.isEmpty()) {
                int input = Integer.parseInt(inputText);
                if (input > 0) {
                    Intent serviceIntent = new Intent(this, serviceClass);
                    serviceIntent.putExtra("count", input);
                    if (serviceClass == MyBoundCountingService.class && !mBound) {
                        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
                    } else {
                        startService(serviceIntent);
                    }
                } else {
                    Log.d("_TAG", "Invalid input.");
                    if (serviceClass == MyBoundCountingService.class && !mBound) {
                        binding.secondSwitch.setChecked(false);
                    } else {
                        binding.firstSwitch.setChecked(false);
                    }
                }
            } else {
                Log.d("_TAG", "Invalid input.");
                if (serviceClass == MyBoundCountingService.class && !mBound) {
                    binding.secondSwitch.setChecked(false);
                } else {
                    binding.firstSwitch.setChecked(false);
                }
            }
        } else {
            if (serviceClass == MyBoundCountingService.class && mBound) {
                myBoundService.stopCounting();
                unbindService(connection);
                mBound = false;
            } else {
                stopService(new Intent(this, serviceClass));
            }
        }
    }

    private void handleThirdSwitchChange(boolean isChecked) {
        if (isChecked) {
            String inputText = binding.countingEdit.getText().toString();
            if (!inputText.isEmpty()) {
                int input = Integer.parseInt(inputText);
                if (input > 0) {
                    startCountingWithWorkManager(input);
                } else {
                    Log.d("_TAG", "Invalid input.");
                    binding.thirdSwitch.setChecked(false);
                }
            } else {
                Log.d("_TAG", "Invalid input.");
                binding.thirdSwitch.setChecked(false);
            }
        } else {
            binding.progressBar.setProgress(0);
            workManager.cancelAllWork();
        }
    }


    private void setupGetAndSetButtons() {
        binding.setBut.setOnClickListener(v -> handleSetButtonClick());

        binding.getBut.setOnClickListener(v -> handleGetButtonClick());
    }

    private void handleSetButtonClick() {
        String inputText = binding.countingEdit.getText().toString();
        if (!inputText.isEmpty()) {
            int input = Integer.parseInt(inputText);
            if (input > 0 && mBound) {
                myBoundService.setCount(input);
            } else {
                Log.d("_TAG", "Invalid input or service not bound.");
            }
        } else {
            Log.d("_TAG", "Invalid input.");
        }
    }

    private void handleGetButtonClick() {
        if (mBound) {
            int count = myBoundService.getCount();
            binding.getText.setText(String.valueOf(count));
        } else {
            Log.d("_TAG", "Service not bound.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.br);
    }

    private void startCountingWithWorkManager(int count) {
        // Create constraints for the worker
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create a Data object with input parameters
        Data inputData = new Data.Builder()
                .putInt("count", count)
                .build();

        // Create a OneTimeWorkRequest to execute CountingWorker
        OneTimeWorkRequest countingWork = new OneTimeWorkRequest.Builder(MyCountingWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(countingWork.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING) {
                        int progress = workInfo.getProgress().getInt("progress", 0);
                        binding.progressBar.setProgress(progress);
                    }
                });

        // Enqueue the work request
        workManager.enqueue(countingWork);
    }
}