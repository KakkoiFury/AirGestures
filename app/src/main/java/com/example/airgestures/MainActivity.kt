package com.example.airgestures

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Simple programmatic layout since we are skipping heavy XML files for now
        val startButton = Button(this).apply {
            text = "Start Air Gestures"
        }
        setContentView(startButton)

        startButton.setOnClickListener {
            checkCameraPermissionAndStart()
        }
    }

    private fun checkCameraPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
            == PackageManager.PERMISSION_GRANTED) {
            // Permission is already given, boot up the background service
            startGestureService()
        } else {
            // Ask the user for permission via the system pop-up
            ActivityCompat.requestPermissions(
                this, 
                arrayOf(Manifest.permission.CAMERA), 
                CAMERA_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, 
        permissions: Array<out String>, 
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted!", Toast.LENGTH_SHORT).show()
                startGestureService()
            } else {
                Toast.makeText(this, "Camera permission is required for Air Gestures.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startGestureService() {
        val serviceIntent = Intent(this, GestureService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}
