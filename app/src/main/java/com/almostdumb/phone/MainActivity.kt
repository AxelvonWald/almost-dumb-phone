package com.almostdumb.phone

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appGridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appGridView = findViewById(R.id.app_grid)
        loadAllowedApps()

        Toast.makeText(this@MainActivity, "Almost Dumb Phone Launcher Active", Toast.LENGTH_SHORT).show()
    }

    private fun loadAllowedApps() {
        val packageManager = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        val appAdapter = AppGridAdapter(this, resolveInfoList, packageManager)
        appGridView.adapter = appAdapter
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Do nothing - prevent back button from exiting launcher
    }
}