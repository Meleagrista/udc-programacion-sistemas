package es.udc.ps.p26dorio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MyBroadcastReceiver extends BroadcastReceiver {

    // Declaring a WeakReference to hold a reference to the MainActivity
    private final WeakReference<MainActivity> activityRef;

    // Constructor for MyBroadcastReceiver, takes MainActivity as a parameter
    public MyBroadcastReceiver(MainActivity activity) {
        // Creating a WeakReference to MainActivity to avoid memory leaks
        activityRef = new WeakReference<>(activity);
    }

    // Overriding the onReceive method of BroadcastReceiver
    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieving the action from the received Intent
        String action = intent.getAction();

        // Getting the MainActivity instance from the WeakReference
        MainActivity activity = activityRef.get();

        // Checking if the action is not null
        if (action != null) {
            // Handling action when the user unlocks the device
            switch (action) {
                case Intent.ACTION_USER_PRESENT:
                    // Showing a Toast message indicating the user is present
                    Toast.makeText(context, "Usuario presente.", Toast.LENGTH_SHORT).show();
                    // Logging a debug message
                    Log.d("_TAG", "Pantalla desbloqueada.");
                    break;
                // Handling custom action "es.udc.PSI.broadcast.GENERAL"
                case "es.udc.PSI.broadcast.GENERAL":
                    // Extracting extra data from the Intent
                    String message = intent.getStringExtra("data");
                    // Checking if the message is not null
                    if (message != null) {
                        // Showing a Toast message with the received data
                        Toast.makeText(context, "Emisión propia: " + message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                // Handling custom action "es.udc.PSI.service.local.SWITCH"
                case "es.udc.PSI.service.local.SWITCH":
                    // Running the following code on the UI thread of the MainActivity
                    activity.runOnUiThread(() -> {
                        // Accessing the countingSwitch from MainActivity and turning it off
                        activity.binding.firstSwitch.setChecked(false);
                    });
                    break;
                case "es.udc.PSI.service.bind.SWITCH":
                    // Running the following code on the UI thread of the MainActivity
                    activity.runOnUiThread(() -> {
                        // Accessing the countingSwitch from MainActivity and turning it off
                        activity.binding.secondSwitch.setChecked(false);
                    });
                    break;
                case "es.udc.PSI.service.background.SWITCH":
                    // Running the following code on the UI thread of the MainActivity
                    activity.runOnUiThread(() -> {
                        // Accessing the countingSwitch from MainActivity and turning it off
                        activity.binding.thirdSwitch.setChecked(false);
                    });
                    break;
            }
        }
    }
}

