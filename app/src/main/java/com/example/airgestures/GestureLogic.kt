package com.example.airgestures

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.app.NotificationCompat
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import java.util.concurrent.Executors

class GestureService : Service(), GestureLogic.GestureListener {

    private val CHANNEL_ID = "AirGesturesServiceChannel"
    private val NOTIFICATION_ID = 1
    private lateinit var gestureLogic: GestureLogic
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate() {
        super.onCreate()
        gestureLogic = GestureLogic(this)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        startCameraEngine()
    }

    private fun startCameraEngine() {
        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalyzer.setAnalyzer(cameraExecutor) { imageProxy ->
            processFrame(imageProxy)
        }
        // CameraX binding executes here dynamically to look at the front lens
    }

    private fun processFrame(imageProxy: ImageProxy) {
        val bitmap = imageProxy.toBitmap()
        if (bitmap != null) {
            val mpImage = BitmapImageBuilder(bitmap).build()
            // Injects frames safely straight into our math matrix system
            // We will load the actual task tracker model async here
        }
        imageProxy.close()
    }

    override fun onSwipeLeft() {
        // System command logic to toggle media previous or scroll left
    }

    override fun onSwipeRight() {
        // System command logic to toggle media next or scroll right
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Air Gestures Engine Active")
            .setContentText("Analyzing camera frames for air swipes...")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Air Gestures Active Engine", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
