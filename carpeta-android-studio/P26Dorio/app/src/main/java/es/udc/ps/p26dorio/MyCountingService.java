package es.udc.ps.p26dorio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyCountingService extends Service {
    private int counter = 0;
    private int limit = 0;
    private boolean isCounting = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Methods for clients */
    public void startCounting(int countLimit) {
        limit = countLimit;
        counter = 0;
        isCounting = true;
        doCounting();
    }

    public void stopCounting() {
        isCounting = false;
    }

    public void doCounting() {
        new Thread(() -> {
            while (isCounting && counter < limit) {
                counter++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                String msg = this + " - " + counter;
                Log.d("_TAG", msg);
            }
            this.notifyFinish();
            stopSelf(); // Stop the service when counting is finished
        }).start();
    }

    public void notifyFinish() {
        Intent intent = new Intent();
        intent.setAction("es.udc.PSI.service.local.SWITCH");
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("count")) {
            int count = intent.getIntExtra("count", 0);
            this.startCounting(count);
        } else {
            Log.d("_TAG", "Intent is null or doesn't contain 'count' extra.");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        this.stopCounting();
    }
}
