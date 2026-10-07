import java.util.ArrayList;

class GestureCalculator {
    private final float SWIPE_THRESHOLD = 0.15f;
    private float initialWristX = -1f;

    // Simulates a hand frame entering the calculations
    public String processFrame(float currentWristX) {
        if (initialWristX == -1f) {
            initialWristX = currentWristX;
            return "Hand detected. Initial position set at X: " + initialWristX;
        }

        float deltaX = currentWristX - initialWristX;

        if (deltaX > SWIPE_THRESHOLD) {
            initialWristX = -1f; // Reset tracking after a match
            return "🔥 SUCCESS: Swipe Right detected!";
        } else if (deltaX < -SWIPE_THRESHOLD) {
            initialWristX = -1f; // Reset tracking after a match
            return "🔥 SUCCESS: Swipe Left detected!";
        }

        return "Analyzing frame... Current displacement Delta X: " + deltaX;
    }
}

public class TestLogic {
    public static void main(String[] args) {
        System.out.println("🚀 STARTING AIR GESTURE SIMULATOR...");
        GestureCalculator calculator = new GestureCalculator();

        // SIMULATION 1: A rapid hand movement from left to right (Swipe Right)
        System.out.println("\n--- RUNNING SIMULATION: SWIPE RIGHT ---");
        System.out.println(calculator.processFrame(0.2f)); // Frame 1: Hand starts at left side
        System.out.println(calculator.processFrame(0.25f)); // Frame 2: Moving slightly
        System.out.println(calculator.processFrame(0.5f));  // Frame 3: Moving fast
        System.out.println(calculator.processFrame(0.75f)); // Frame 4: Crosses threshold!
    }
}
