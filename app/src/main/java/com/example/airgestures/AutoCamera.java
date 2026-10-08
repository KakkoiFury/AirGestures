// @java.lang.annotation.Target
// @com.example.airgestures

// This special instruction tells Java 27 to automatically download OpenCV from the internet
// grab: org.openpnp:opencv:4.9.0-0

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class AutoCamera {
    static {
        // Automatically resolves paths and mounts the camera bridge locally
        nu.pattern.OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        System.out.println("📸 POWERING UP YOUR HCL WEBCAM VIA WEB DEPENDENCY HOOK...");
        VideoCapture camera = new VideoCapture(0); 
        
        if (!camera.isOpened()) {
            System.out.println("❌ ERROR: Camera pipeline failed. Check if hardware is engaged.");
            return;
        }

        JFrame window = new JFrame("Air Gestures Real-Time Workspace");
        JLabel screen = new JLabel();
        window.add(screen);
        window.setSize(640, 480);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        Mat frame = new Mat();
        System.out.println("🚀 SUCCESS: Camera stream online! Wave at your screen.");

        while (window.isVisible()) {
            if (camera.read(frame)) {
                BufferedImage image = matToBufferedImage(frame);
                screen.setIcon(new ImageIcon(image));
                window.repaint();
            }
            try { Thread.sleep(33); } catch (Exception e) {}
        }
        camera.release();
        System.exit(0);
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
