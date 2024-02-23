package es.udc.ps.p22dorio;

import static es.udc.ps.p22dorio.MainActivity.KEY_TEXT;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    TextView text_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        text_view = findViewById(R.id.text_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String text = bundle.getString(KEY_TEXT, getString(R.string.empty_text));
            text_view.setText(text);
        }
    }
}