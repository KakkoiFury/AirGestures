package com.example.airgestures

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class GestureService : Service() {

    private val CHANNEL_ID = "AirGesturesServiceChannel"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        // Starts the app as a foreground service with a persistent notification
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        
        startCameraEngine()
    }

    private fun startCameraEngine() {
        // Here we will hook up CameraX to capture frames 
        // and feed them into GestureLogic (Google MediaPipe)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Air Gestures Active")
            .setContentText("Monitoring front camera for hand waves...")
            .setSmallIcon(android.R.drawable.ic_menu_camera) // Temporary system icon
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Air Gestures Background Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.getService) as NotificationManager?
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // Tells Android not to kill this service randomly
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
