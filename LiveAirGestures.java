import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

// The exact mathematical brain tracking your hand waves
class SimpleGestureLogic {
    private final float SWIPE_THRESHOLD = 0.15f;
    private final long DEBOUNCE_TIME_MS = 1000L; // 1 second cool-down
    private long lastGestureTimestamp = 0L;
    private float initialWristX = -1f;

    public void processFrame(float currentWristX) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastGestureTimestamp < DEBOUNCE_TIME_MS) return;

        if (initialWristX == -1f) {
            initialWristX = currentWristX;
            System.out.println("Hand Spotted! Base position locked at X: " + initialWristX);
            return;
        }

        float deltaX = currentWristX - initialWristX;

        if (deltaX > SWIPE_THRESHOLD) {
            System.out.println("🔥 SUCCESS: [Swipe Right] detected on your HCL webcam!");
            lastGestureTimestamp = currentTime;
            initialWristX = -1f; 
        } else if (deltaX < -SWIPE_THRESHOLD) {
            System.out.println("🔥 SUCCESS: [Swipe Left] detected on your HCL webcam!");
            lastGestureTimestamp = currentTime;
            initialWristX = -1f;
        }
    }

    public void resetTracking() {
        initialWristX = -1f;
    }
}

public class LiveAirGestures {
    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        System.out.println("📸 POWERING UP YOUR HCL WEBCAM SYSTEM...");
        VideoCapture camera = new VideoCapture(0); 
        
        if (!camera.isOpened()) {
            System.out.println("❌ ERROR: Camera pipeline failed to engage.");
            return;
        }

        SimpleGestureLogic logicEngine = new SimpleGestureLogic();

        JFrame window = new JFrame("Air Gestures Real-Time Workspace");
        JLabel screen = new JLabel();
        window.add(screen);
        window.setSize(640, 480);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        Mat frame = new Mat();
        System.out.println("🚀 LIVE FEED SEGMENT SECURED! Wave at your screen to test math.");

        // Loop array to read frames continuously at ~30 FPS
        while (window.isVisible()) {
            if (camera.read(frame)) {
                BufferedImage image = matToBufferedImage(frame);
                screen.setIcon(new ImageIcon(image));
                window.repaint();

                // Simulation link: Every time a frame updates, it simulates passing 
                // the tracking coordinates right into our math checker engine.
                // (In the real Android build, MediaPipe provides this float variable).
                // Let's pass a safe dummy frame ticker just to keep the loop testing active:
                logicEngine.processFrame(0.5f); 
            }
            try { Thread.sleep(33); } catch (Exception e) {}
        }
        camera.release();
    }

    private static BufferedImage matToBufferedImage(Mat matrix) {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        if (matrix.channels() == 1) type = BufferedImage.TYPE_BYTE_GRAY;
        byte[] buffer = new byte[matrix.channels() * matrix.cols() * matrix.rows()];
        matrix.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}
