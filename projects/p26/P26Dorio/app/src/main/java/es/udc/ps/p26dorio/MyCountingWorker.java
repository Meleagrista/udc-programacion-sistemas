package es.udc.ps.p26dorio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyCountingWorker extends Worker {

    public MyCountingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int limit = getInputData().getInt("count", 0);
        int counter = 0;
        while (counter < limit && !isStopped()) {
            counter++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            Log.d("_TAG", this + " - " + counter);
            int progress = (int) ((float) counter / limit * 100);
            setProgressAsync(new Data.Builder().putInt("progress", progress).build()); // Report progress
        }
        if (isStopped()) {
            // Handle cancellation
            return Result.failure();
        } else {
            notifyFinish(getApplicationContext());
            setProgressAsync(new Data.Builder().putInt("progress", 0).build()); // Report progress
            return Result.success();
        }
    }

    private void notifyFinish(Context context) {
        Intent intent = new Intent();
        intent.setAction("es.udc.PSI.service.background.SWITCH");
        context.sendBroadcast(intent);
    }
}
