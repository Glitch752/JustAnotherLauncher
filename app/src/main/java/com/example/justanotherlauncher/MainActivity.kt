package com.example.justanotherlauncher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.pm.ResolveInfo
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    private lateinit var appList: List<ResolveInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appList = packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN,null).addCategory(Intent.CATEGORY_LAUNCHER),0)

        for(appInfo in appList) {
            if(appInfo.activityInfo.packageName != this.packageName) { // Exclude the launcher itself
                // TODO
            }
        }
    }
}