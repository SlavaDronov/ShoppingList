package com.dron.shoppinglist

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class MyApp : Application() {

    private val tag = "AppLifecycle"

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "Application.onCreate() — процесс запущен")

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.d(tag, "${activity.javaClass.simpleName}.onCreate(savedInstanceState=${savedInstanceState != null})")
            }
            override fun onActivityStarted(activity: Activity) {
                Log.d(tag, "${activity.javaClass.simpleName}.onStart()")
            }
            override fun onActivityResumed(activity: Activity) {
                Log.d(tag, "${activity.javaClass.simpleName}.onResume()")
            }
            override fun onActivityPaused(activity: Activity) {
                Log.d(tag, "${activity.javaClass.simpleName}.onPause()")
            }
            override fun onActivityStopped(activity: Activity) {
                Log.d(tag, "${activity.javaClass.simpleName}.onStop()")
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.d(tag, "${activity.javaClass.simpleName}.onSaveInstanceState()")
            }
            override fun onActivityDestroyed(activity: Activity) {
                Log.d(tag, "${activity.javaClass.simpleName}.onDestroy()")
            }
        })
    }
}