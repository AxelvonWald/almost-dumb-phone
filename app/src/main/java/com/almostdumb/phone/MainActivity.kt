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
        val allowedPackages = listOf(
            "com.almostdumb.phone", // Our launcher
            "org.thoughtcrime.securesms", // Signal (confirmed in logs)
            "com.whatsapp", // WhatsApp (confirmed in logs)
            "com.sec.android.app.popupcalculator", // Calculator (confirmed in logs)
            "com.sec.android.app.clockpackage", // Clock (confirmed in logs)
            "nz.co.mbit.topo", // NZ Topo (confirmed in logs)
            "nz.co.nzpost", // NZ Post (confirmed in logs)
            "nz.org.geonet.quake", // GeoNet (confirmed in logs)
            "com.zoho.mail", // Zoho Mail (confirmed in logs)
            "eu.faircode.email", // FairEmail (confirmed in logs)
            "ch.protonmail.android", // ProtonMail (confirmed in logs)
            "ak.alizandro.smartaudiobookplayer", // Smart Audiobook (confirmed in logs)

            // Samsung system apps (confirmed working)
            "com.android.settings", // Settings
            "com.samsung.android.dialer", // Phone
            "com.samsung.android.messaging", // Messages
            "com.sec.android.app.camera", // Camera
            "com.sec.android.gallery3d", // Gallery
            "com.sec.android.app.myfiles" // My Files
        )

        val packageManager = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val allApps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

        val filteredApps = allApps.filter { resolveInfo ->
            allowedPackages.contains(resolveInfo.activityInfo.packageName)
        }

        val appAdapter = AppGridAdapter(this, filteredApps, packageManager)
        appGridView.adapter = appAdapter
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Do nothing - prevent back button from exiting launcher
    }
}