package com.almostdumb.phone

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
class MainActivity : AppCompatActivity() {

    private lateinit var appGridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = android.graphics.Color.BLACK

        appGridView = findViewById(R.id.app_grid)
        loadAllowedApps()

        Toast.makeText(this@MainActivity, "Almost Dumb Phone Launcher Active", Toast.LENGTH_SHORT).show()
    }

    private fun loadAllowedApps() {
        val packageManager = packageManager

        // Method 1: Standard launcher apps
        val launcherIntent = Intent(Intent.ACTION_MAIN, null)
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val launcherApps = packageManager.queryIntentActivities(launcherIntent, 0)

        // Method 2: Try to find specific packages directly
        val wantedPackages = listOf(
            "org.thoughtcrime.securesms", // Signal
            "com.whatsapp", // WhatsApp
            "com.sec.android.app.popupcalculator", // Calculator
            "com.sec.android.app.clockpackage", // Clock
            "nz.co.mbit.topo", // NZ Topo
            "nz.co.nzpost", // NZ Post
            "nz.org.geonet.quake", // GeoNet
            "com.zoho.mail", // Zoho Mail
            "eu.faircode.email", // FairEmail
            "ch.protonmail.android", // ProtonMail
            "ak.alizandro.smartaudiobookplayer", // Smart Audiobook
            "nz.co.kiwibank.mobile", // Kiwibank
            "com.google.android.apps.maps", // Google Maps
            "com.ionalanguages.app" // Iona Languages
        )

        val foundApps = mutableListOf<ResolveInfo>()

        // Add launcher apps we want
        val systemApps = listOf(
            "com.android.settings", "com.samsung.android.dialer",
            "com.samsung.android.messaging", "com.sec.android.app.camera",
            "com.sec.android.gallery3d", "com.sec.android.app.myfiles"

        )

        foundApps.addAll(launcherApps.filter {
            systemApps.contains(it.activityInfo.packageName)
        })

        // Try to find missing packages directly
        for (packageName in wantedPackages) {
            try {
                val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                val launchIntent = packageManager.getLaunchIntentForPackage(packageName)

                if (launchIntent != null) {
                    // Create a fake ResolveInfo for this app
                    val resolveInfo = ResolveInfo()
                    resolveInfo.activityInfo = packageInfo.activities?.firstOrNull()
                    if (resolveInfo.activityInfo != null) {
                        foundApps.add(resolveInfo)
                        println("FOUND MISSING APP: $packageName")
                    }
                }
            } catch (e: Exception) {
                println("PACKAGE NOT FOUND: $packageName - ${e.message}")
            }
        }

        println("TOTAL APPS FOUND: ${foundApps.size}")
        foundApps.forEach { app ->
            val name = app.loadLabel(packageManager).toString()
            val pkg = app.activityInfo.packageName
            println("FINAL APP: $name ($pkg)")
        }

        val appAdapter = AppGridAdapter(this, foundApps, packageManager)
        appGridView.adapter = appAdapter
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Do nothing - prevent back button from exiting launcher
    }
}