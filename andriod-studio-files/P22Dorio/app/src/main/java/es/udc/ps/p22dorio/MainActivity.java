package es.udc.ps.p22dorio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "_TAG";
    String ACTIV = "MainActivity";
    static String KEY_TEXT = "asdfgh";

    Button share_button, continue_button;
    EditText edit_text;

    @Override
    public void onClick(View v) {
        String text = edit_text.getText().toString();

        if (! text.isEmpty()) {
            if (v == share_button)
            {
                Log.d(TAG, "Botón compartir");
            } else if (v == continue_button) {
                Log.d(TAG, "Botón continuar");
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra(KEY_TEXT, text);
                startActivity(intent);
            } else {
                Log.d(TAG, "Fallo en el botón");
            }
        } else {
            Toast.makeText(this, R.string.empty_text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, ACTIV + " onCreate");

        edit_text = findViewById(R.id.edit_text);
        share_button = findViewById(R.id.share_button_id);
        share_button.setOnClickListener(this);
        continue_button = findViewById(R.id.continue_button_id);
        continue_button.setOnClickListener(this);

        /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ACTIV + " onClick");
                ((Button) v).setText(R.string.share_button);
            }
        }); */
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