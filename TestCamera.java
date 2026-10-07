import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class TestCamera {
    static {
        // Loads the native camera access files automatically
        nu.pattern.OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        System.out.println("📸 INITIALIZING DESKTOP WEBCAM LOOP...");
        
        // 0 opens your default built-in PC webcam or active phone-camera driver
        VideoCapture camera = new VideoCapture(0); 
        
        if (!camera.isOpened()) {
            System.out.println("❌ ERROR: Computer cannot access any camera device!");
            return;
        }

        // Creates a real GUI Window on your desktop screen
        JFrame window = new JFrame("Air Gestures Live Camera Check");
        JLabel screen = new JLabel();
        window.add(screen);
        window.setSize(640, 480);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        Mat frame = new Mat();
        System.out.println("🚀 SUCCESS: Camera stream online! Wave at your screen.");

        while (window.isVisible()) {
            if (camera.read(frame)) {
                // 1. Capture the image frame matrix from the lens
                BufferedImage image = matrixToBuffer(frame);
                
                // 2. Refresh the display window in real-time
                screen.setIcon(new ImageIcon(image));
                window.repaint();
                
                // This line mimics your Android logging loop
                System.out.println("Frame processed successfully! Pixels analyzed: " + frame.cols() + "x" + frame.rows());
            }
            
            try { Thread.sleep(33); } catch (Exception e) {} // Locks analysis at ~30 FPS
        }
        camera.release();
    }

    // Helper method to convert raw lens camera matrix directly into displayable visual pixels
    private static BufferedImage matrixToBuffer(Mat matrix) {
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
