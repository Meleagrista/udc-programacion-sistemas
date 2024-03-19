package es.udc.ps.p25dorio;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import es.udc.ps.p25dorio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Snackbar mySnackbar;
    Snackbar undoSnackbar;
    Toast myToast;

    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;

    static int requestCode = 1;
    static String CHANNEL_ID = "2";

    static int notificationId = 3;

    private ActivityMainBinding binding;

    ArrayList<String> dataList;

    private String lastDeleted;
    private int positionDeleted;
    private ArrayAdapter<String> adapter;
    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE );

        this.createNotificationChannel();

        this.builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(getString(R.string.intent1))
                        .setContentText(getString(R.string.intent2))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        //.setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .addAction(R.drawable.ic_launcher_background, getString(R.string.intent3) + " 1",
                                pendingIntent)
                        .addAction(R.drawable.ic_launcher_background, getString(R.string.intent3) + " 2",
                                pendingIntent)
                        .addAction(R.drawable.ic_launcher_background, getString(R.string.intent3) + " 3",
                                pendingIntent);

        this.notificationManager =
                NotificationManagerCompat.from(this);

        String[] data = {"zero","one", "two", "three", "four", "five", "six", "seven"};
        this.dataList = new ArrayList<>(Arrays.asList(data));
        this.adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                dataList);
        this.binding.listView.setAdapter(this.adapter);
        registerForContextMenu(this.binding.listView);

        int duration = Snackbar.LENGTH_SHORT;
        this.mySnackbar = Snackbar.make(this.binding.getRoot(), R.string.snack, duration);

        this.undoSnackbar = Snackbar.make(this.binding.getRoot(), R.string.snack, duration);
        this.undoSnackbar.setAction(R.string.undo,this);

        Context context = getApplicationContext();
        duration = Toast.LENGTH_SHORT; // Toast.LENGTH_LONG
        this.myToast = Toast.makeText(context, R.string.toast, duration);
        this.myToast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        assert info != null;
        if(item.getItemId() == R.id.menu_delete) {
            this.lastDeleted = this.dataList.get(info.position);
            this.positionDeleted = info.position;

            this.dataList.remove(info.position);
            this.adapter.notifyDataSetChanged();

            this.undoSnackbar.show();

            return true;
        } else if(item.getItemId() == R.id.menu_share) {
            int position = info.position;
            //Toast.makeText(this, getString(R.string.share) + " " + position, Toast.LENGTH_SHORT).show();
            Log.d("_TAG", "Botón compartir");
            Intent intentSend = new Intent(Intent.ACTION_SEND);
            intentSend.setType("text/plain");
            intentSend.putExtra(Intent.EXTRA_TEXT, position);
            startActivity(intentSend);
            Log.d("_TAG", "menu: " + Objects.requireNonNull(item.getTitle()) + " item: " + info.id);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_snack) {
            this.mySnackbar.show();
            return true;
        } else if(item.getItemId() == R.id.menu_notification){
            this.notificationManager.notify(notificationId, builder.build());
            return true;
        } else if(item.getItemId() == R.id.menu_cancel) {
            this.notificationManager.cancel(notificationId);
            return true;
        } else if(item.getItemId() == R.id.menu_toast) {
            this.myToast.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                name, importance);
        NotificationManager notifManager = getSystemService(NotificationManager.class);
        notifManager.createNotificationChannel(channel);
    }

    @Override
    public void onClick(View v) {
        if(this.lastDeleted != null) {
            this.dataList.add(this.positionDeleted, this.lastDeleted);
            this.adapter.notifyDataSetChanged();
        }
        this.lastDeleted = null;
    }
}