package es.udc.ps.p22dorio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // TAG for logging
    static String TAG = "_TAG";
    // Name of the activity
    String ACTIV = "MainActivity";
    // Key for passing text between activities
    static String KEY_TEXT = "asdfgh";

    // Buttons and views
    Button share_button, continue_button;
    TextView text_view;
    EditText edit_text;

    // Activity result launcher for handling activity results
    private ActivityResultLauncher<Intent> myStartActivityForResult;

    // Handling click events
    @Override
    public void onClick(View v) {
        String text = edit_text.getText().toString();

        if (!text.isEmpty()) {
            if (v == share_button) {
                // Share button clicked
                Log.d(TAG, "Botón compartir");
                Intent intentSend = new Intent(Intent.ACTION_SEND);
                intentSend.setType("text/plain");
                intentSend.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intentSend);
            } else if (v == continue_button) {
                // Continue button clicked
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra(KEY_TEXT, text);
                myStartActivityForResult.launch(intent);
            } else {
                // Unknown button clicked
                Log.d(TAG, "Fallo en el botón");
            }
        } else {
            // Text is empty
            Toast.makeText(this, R.string.empty_text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, ACTIV + " onCreate");

        // Initialize views
        edit_text = findViewById(R.id.edit_text);
        text_view = findViewById(R.id.main_text_view);
        share_button = findViewById(R.id.share_button_id);
        continue_button = findViewById(R.id.continue_button_id);

        // Set click listeners
        share_button.setOnClickListener(this);
        continue_button.setOnClickListener(this);

        // Initialize the ActivityResultLauncher
        myStartActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        Bundle bundle = data != null ? data.getExtras() : null;
                        if (bundle != null) {
                            String str_text = bundle.getString(KEY_TEXT, getString(R.string.void_text));
                            text_view.setText(str_text);
                        }
                    }
                }
        );
    }

    // Lifecycle methods
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