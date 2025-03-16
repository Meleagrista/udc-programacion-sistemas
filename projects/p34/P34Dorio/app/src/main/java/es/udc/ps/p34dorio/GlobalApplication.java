package es.udc.ps.p34dorio;

import android.app.Application;

import butterknife.ButterKnife;
import es.udc.ps.p34dorio.BuildConfig;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        initButterKnifeDebug();
    }

    private void initButterKnifeDebug() {

        ButterKnife.setDebug(BuildConfig.DEBUG);
    }
}
