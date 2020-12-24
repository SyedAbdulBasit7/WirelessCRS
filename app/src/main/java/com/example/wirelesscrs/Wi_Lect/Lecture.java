package com.example.wirelesscrs.Wi_Lect;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Main application class.
 *
 */
public class Lecture extends Application {

    public static final String CHANNEL_ID = "lrkFM";
    private static WeakReference<Lecture> context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = new WeakReference<>(this);
    }

    public static Context getContext() {
        return context.get();
    }


}
