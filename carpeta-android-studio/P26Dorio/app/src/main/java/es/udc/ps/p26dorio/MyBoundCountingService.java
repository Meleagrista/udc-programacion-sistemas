package es.udc.ps.p26dorio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyBoundCountingService extends Service {
    private int counter = 0;
    private int limit = 0;
    private boolean isCounting = false;

    private final Object lock = new Object(); // Lock object for synchronization

    private final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MyBoundCountingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyBoundCountingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null && intent.hasExtra("count")) {
            int count = intent.getIntExtra("count", 0);
            this.startCounting(count);
        } else {
            Log.d("_TAG", "Intent is null or doesn't contain 'count' extra.");
        }
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /** Methods for clients */
    public void startCounting(int countLimit) {
        synchronized (lock) {
            limit = countLimit;
            counter = 0;
            isCounting = true;
        }
        doCounting();
    }

    public void stopCounting() {
        synchronized (lock) {
            isCounting = false;
        }
    }

    public int getCount() {
        synchronized (lock) {
            Log.d("_TAG", "Accessing the counter...");
            return counter;
        }
    }

    public void setCount(int newCount) {
        synchronized (lock) {
            Log.d("_TAG", "Modifying the counter...");
            counter = newCount;
        }
    }

    private void doCounting() {
        new Thread(() -> {
            while (isCounting && counter < limit) {
                synchronized (lock) {
                    counter++;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                Log.d("_TAG", this + " - " + counter);
            }
            this.notifyFinish();
            stopSelf(); // Stop the service when counting is finished
        }).start();
    }

    public void notifyFinish() {
        Intent intent = new Intent();
        intent.setAction("es.udc.PSI.service.bind.SWITCH");
        sendBroadcast(intent);
    }
}
