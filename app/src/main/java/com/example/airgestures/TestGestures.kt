package com.example.airgestures

import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import org.mockito.Mockito.mock // If errors occur, we will use a raw data array instead

fun main() {
    println("🚀 Starting Gesture Math Simulator...")
    
    // Initialize our logic class
    val engine = GestureLogic(object : GestureLogic.GestureListener {
        override fun onSwipeLeft() {
            println("🔥 SUCCESS: [Swipe Left] detected by math logic!")
        }

        override fun onSwipeRight() {
            println("🔥 SUCCESS: [Swipe Right] detected by math logic!")
        }
    })

    println("Simulating hand moving rapidly to the right...")
    // We will pass dummy points into your logic loop here to verify calculations instantly
}
