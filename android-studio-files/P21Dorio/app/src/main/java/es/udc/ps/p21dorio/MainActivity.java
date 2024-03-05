package es.udc.ps.p21dorio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String TAG = "_TAG";
    String ACTIV = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, ACTIV + " onCreate");

        Button button = findViewById(R.id.button_app_name2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ACTIV + " onClick");
                ((Button) v).setText(R.string.button_name);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, ACTIV + " onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, ACTIV + " onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, ACTIV + " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, ACTIV + " onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, ACTIV + " onDestroy");
    }


}