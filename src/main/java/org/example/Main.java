package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static boolean running = true;
    private static final String END_SIGNAL = "END";

    private static void sendScreenShot(Socket socket) throws AWTException, IOException {
        while (running) {
            LOGGER.info("Sending screenshot...");
            String encodedString = captureScreenShot();
            sendEncodedString(socket, encodedString);
            LOGGER.info("Screenshot sent");

        }
    }

    private static String captureScreenShot() throws AWTException, IOException {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = robot.createScreenCapture(screenRect);

        // Get the mouse pointer's location
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

        // Get the mouse pointer's image
        BufferedImage cursorImage = getMousePointerImage();

        // Draw the mouse pointer image on the screenshot at the mouse pointer's location
        Graphics2D graphics2D = capture.createGraphics();
        graphics2D.drawImage(cursorImage, mouseLocation.x, mouseLocation.y, null);
        graphics2D.dispose();

        return encodeToString(capture);
    }

    private static BufferedImage getMousePointerImage() {
        // This method should return an image of the mouse pointer.
        // You can either create this image yourself, or load it from a file or resource.
        // For the sake of this example, we'll just create a simple 10x10 red square.
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 10, 10);
        g.dispose();
        return image;
    }

    private static String encodeToString(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    private static void sendEncodedString(Socket socket, String encodedString) throws IOException {
        socket.getOutputStream().write((encodedString + "\n").getBytes());
        socket.getOutputStream().write((END_SIGNAL + "\n").getBytes());
        socket.getOutputStream().flush();
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5657)) {
            while (running) {
                LOGGER.info("Waiting for connection...");
                Socket socket = serverSocket.accept();
                sendScreenShot(socket);
            }
        } catch (IOException | AWTException e) {
            LOGGER.log(Level.SEVERE, "Exception caught", e);
            running = false;
        }
    }
}
