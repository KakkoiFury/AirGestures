package com.example.airgestures

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.KeyEvent
import android.content.Intent

class GestureAccessibilityService : AccessibilityService(), GestureLogic.GestureListener {

    private lateinit var gestureLogic: GestureLogic

    override fun onServiceConnected() {
        super.onServiceConnected()
        gestureLogic = GestureLogic(this)
        
        // Broadcast to your background camera service that accessibility is active
        val intent = Intent(this, GestureService::class.java)
        startService(intent)
    }

    // 🎯 Triggered instantly when your math engine confirms a SWIPE RIGHT
    override fun onSwipeRight() {
        // Physically tells your M51 system to skip to the NEXT media track
        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS) // Visual confirmation placeholder
        sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT)
    }

    // 🎯 Triggered instantly when your math engine confirms a SWIPE LEFT
    override fun onSwipeLeft() {
        // Physically tells your M51 system to play the PREVIOUS media track
        sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS)
    }

    private fun sendMediaKeyEvent(keyCode: Int) {
        val eventDown = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
        val eventUp = KeyEvent(KeyEvent.ACTION_UP, keyCode)
        
        // Injects the keypress directly into your Samsung system layer
        val audioManager = getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
        audioManager.dispatchMediaKeyEvent(eventDown)
        audioManager.dispatchMediaKeyEvent(eventUp)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
}
