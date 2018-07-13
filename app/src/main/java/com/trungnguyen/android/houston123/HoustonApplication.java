package com.trungnguyen.android.houston123;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.trungnguyen.android.houston123.base.AppManager;
import com.trungnguyen.android.houston123.injection.Injector;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class HoustonApplication extends DaggerApplication {

    private static HoustonApplication mInstance;
    private static RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        Injector.getInstance().init(this);

        registerActivityLifecycleCallbacks(mCallbacks);

        if (isDebugBuild()) {
            StrictMode.enableDefaults();
        }

        initLeakCanary();

    }

    @NonNull
    public static boolean isDebugBuild() {
		return BuildConfig.BUILD_TYPE.equals("debug");
    }

    private void initLeakCanary() {
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            mRefWatcher = LeakCanary.install(this);
        }
    }

    public static RefWatcher getRefWatcher() {
        return HoustonApplication.mRefWatcher;
    }


    public static HoustonApplication getInstance() {
        return mInstance;
    }

    private ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.getAppManager().addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            AppManager.getAppManager().removeActivity(activity);
        }
    };

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return Injector.getInstance().applicationInjector();

    }
}