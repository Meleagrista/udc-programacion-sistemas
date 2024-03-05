package es.udc.ps.p21dorio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends AppCompatActivity {

    String TAG = "_TAG";
    String ACTIV = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(TAG, ACTIV + " onCreate");
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