package com.almostdumb.phone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class AppGridAdapter(
    private val context: Context,
    private val apps: List<ResolveInfo>,
    private val packageManager: PackageManager
) : BaseAdapter() {

    override fun getCount(): Int = apps.size

    override fun getItem(position: Int): Any = apps[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.app_item, parent, false)

        val appInfo = apps[position]
        val appIcon = view.findViewById<ImageView>(R.id.app_icon)
        val appName = view.findViewById<TextView>(R.id.app_name)

        appIcon.setImageDrawable(appInfo.loadIcon(packageManager))
        appName.text = appInfo.loadLabel(packageManager)

        view.setOnClickListener {
            val launchIntent = packageManager.getLaunchIntentForPackage(appInfo.activityInfo.packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }

        return view
    }
}