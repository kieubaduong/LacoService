package org.example;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static org.example.Main.LOGGER;

public class MouseDragHandler extends AbstractHandler {
    private static final Gson GSON = new Gson();

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        LOGGER.log(Level.INFO, "Received request body: %s".formatted(requestBody));

        DragCoordinates dragCoordinates = GSON.fromJson(requestBody, DragCoordinates.class);
        double startX = dragCoordinates.getStartX();
        double startY = dragCoordinates.getStartY();
        double endX = dragCoordinates.getEndX();
        double endY = dragCoordinates.getEndY();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        LOGGER.log(Level.INFO, "Screen size: %dx%d".formatted(screenSize.width, screenSize.height));

        int x1 = (int) (screenSize.width * startX);
        int y1 = (int) (screenSize.height * startY);
        int x2 = (int) (screenSize.width * endX);
        int y2 = (int) (screenSize.height * endY);

        try {
            Robot robot = new Robot();
            robot.mouseMove(x1, y1);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(x2, y2);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "Error performing mouse drag", e);
            throw new ServletException("Error performing mouse drag", e);
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Mouse dragged from: " + x1 + ", " + y1 + " to " + x2 + ", " + y2 + "</h1>");

        LOGGER.log(Level.INFO, "Mouse dragged from: %d, %d to %d, %d".formatted(x1, y1, x2, y2));
    }

    private static class DragCoordinates {
        private double startX;
        private double startY;
        private double endX;
        private double endY;

        public double getStartX() {
            return startX;
        }

        public double getStartY() {
            return startY;
        }

        public double getEndX() {
            return endX;
        }

        public double getEndY() {
            return endY;
        }
    }
}