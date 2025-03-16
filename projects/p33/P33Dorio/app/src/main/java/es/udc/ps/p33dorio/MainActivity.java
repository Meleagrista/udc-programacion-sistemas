package es.udc.ps.p33dorio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String TAG = "_TAG";

    Button user_but;
    EditText user_et;
    SharedPreferences sharedPreferences;
    String KEY_USER = "USER";
    String filename = "userNames.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SetUI();
        checkUser();
    }

    private void SetUI() {
        user_but = findViewById(R.id.user_but);
        user_et = findViewById(R.id.user_et);

        user_but.setOnClickListener(v -> {
            String user_tmp = user_et.getText().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_USER, user_tmp);
            editor.apply();

            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
            saveToFile(user_tmp + " - " + currentDate);

            Toast.makeText(MainActivity.this, "User saved successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkUser() {
        String user_name = sharedPreferences.getString(KEY_USER, getString(R.string.default_user));
        if (!user_name.isEmpty())
            user_et.setText(user_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_share) {
            shareFile();
            return true;
        } else if (itemId == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToFile(String data) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, Context.MODE_APPEND);
            fos.write((data + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving user to file", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void shareFile() {
        File file = new File(getFilesDir(), filename);
        if (file.exists()) {
            Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share file"));
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        }
    }
}

