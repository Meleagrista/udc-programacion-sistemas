package es.udc.ps.p22dorio;

import static es.udc.ps.p22dorio.MainActivity.KEY_TEXT;
import static es.udc.ps.p22dorio.MainActivity.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    TextView text_view;

    Button button1, button2, button3;
    // Name of the activity
    String ACTIV = "SecondActivity";

    @Override
    public void onClick(View v) {
        if (v == button1) {
            Log.d(TAG, "Botón 1");
            Intent intent = new Intent();
            intent.putExtra(KEY_TEXT, ((Button) v).getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        } else if (v == button2) {
            Log.d(TAG, "Botón 2");
            Intent intent = new Intent();
            intent.putExtra(KEY_TEXT, ((Button) v).getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        } else if (v == button3) {
            Log.d(TAG, "Botón 3");
            Intent intent = new Intent();
            intent.putExtra(KEY_TEXT, ((Button) v).getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Log.d(TAG, "Fallo en el botón");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.d(TAG, ACTIV + " onCreate");

        text_view = findViewById(R.id.text_view);
        button1 = findViewById(R.id.button_1);
        button1.setOnClickListener(this);
        button2 = findViewById(R.id.button_2);
        button2.setOnClickListener(this);
        button3 = findViewById(R.id.button_3);
        button3.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String text = bundle.getString(KEY_TEXT, getString(R.string.empty_text));
            text_view.setText(text);
        }
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