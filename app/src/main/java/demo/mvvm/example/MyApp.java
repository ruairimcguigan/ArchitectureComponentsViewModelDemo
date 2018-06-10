package demo.mvvm.example;

import android.app.Application;
import android.content.Context;

import demo.mvvm.example.common.AppComponent;
import demo.mvvm.example.common.DaggerAppComponent;

public class MyApp extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.create();
    }

    public static AppComponent getComponent(Context context) {
        return ((MyApp) context.getApplicationContext()).component;
    }
}
