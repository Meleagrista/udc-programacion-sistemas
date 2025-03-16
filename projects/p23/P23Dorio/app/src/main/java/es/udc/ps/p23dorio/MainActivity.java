package es.udc.ps.p23dorio;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements TopFragment.OnButtonListener {

    static String TAG = "_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TopFragment fragment = new TopFragment();
        fragmentTransaction.replace(R.id.top_fragment, fragment,"TopFragment");

        DownFragment downFragment = new DownFragment();
        fragmentTransaction.add(R.id.down_fragment, downFragment, "DownFragment");

        fragmentTransaction.commit();
    }

    @Override
    public void onText(String text) {
        DownFragment fragment = (DownFragment) getSupportFragmentManager().findFragmentById(R.id.down_fragment);

        Log.d(TAG, "Evento texto: " + text);

        if(fragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            DownFragment downFragment = new DownFragment();
            downFragment.setText(text);
            fragmentTransaction.add(R.id.down_fragment, downFragment, "DownFragment");

            fragmentTransaction.commit();
        } else {
            fragment.loadText(text);
        }
    }

    @Override
    public void onUrl(String url) {
        DownFragment fragment = (DownFragment) getSupportFragmentManager().findFragmentById(R.id.down_fragment);

        Log.d(TAG, "Evento URL: " + url);

        if(fragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            DownFragment downFragment = new DownFragment();
            downFragment.setUrl(url);
            fragmentTransaction.add(R.id.down_fragment, downFragment, "DownFragment");

            fragmentTransaction.commit();
        } else {
            fragment.loadUrl(url);
        }
    }

    @Override
    public void onDestroyDown() {

        DownFragment fragment = (DownFragment) getSupportFragmentManager().findFragmentById(R.id.down_fragment);

        Log.d(TAG, "Evento destruir.");

        if (fragment != null) {
            fragment.onDestroyItself();
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }
}