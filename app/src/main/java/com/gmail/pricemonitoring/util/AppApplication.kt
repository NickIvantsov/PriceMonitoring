package com.gmail.pricemonitoring.util

import android.app.Application
import android.content.Context
//import com.facebook.stetho.Stetho


class AppApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

        /*// Create an InitializerBuilder
        val initializerBuilder = Stetho.newInitializerBuilder(this)

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
            Stetho.defaultInspectorModulesProvider(this)
        )


        // Enable command line interface
        initializerBuilder.enableDumpapp(
            Stetho.defaultDumperPluginsProvider(this)
        )


        // Use the InitializerBuilder to generate an Initializer
        val initializer = initializerBuilder.build()

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer)*/
    }

    companion object {
        private var mContext: Context? = null
            private set

        fun getAppContext(): Context? {
            return mContext
        }
    }
}