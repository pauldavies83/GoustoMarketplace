package dev.pauldavies.goustomarketplace.base

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import javax.inject.Inject

interface DeviceMetrics {
    fun screenSize(): Size

    data class Size(val x: Int, val y: Int)
}

class AndroidDeviceMetrics @Inject constructor(private val context: Context) : DeviceMetrics {
    override fun screenSize(): DeviceMetrics.Size {
        val sizePoint = Point()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(sizePoint)
        return DeviceMetrics.Size(sizePoint.x, sizePoint.y)
    }
}