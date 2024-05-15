package org.example;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static org.example.Main.LOGGER;

public class MouseClickHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        LOGGER.log(Level.INFO, "Received request body: " + requestBody);

        Gson gson = new Gson();
        Scale scale = gson.fromJson(requestBody, Scale.class);
        double scaleX = scale.getScaleX();
        double scaleY = scale.getScaleY();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        LOGGER.log(Level.INFO, "Screen size: " + screenSize.width + "x" + screenSize.height);

        int x = (int) (screenSize.width * scaleX);
        int y = (int) (screenSize.height * scaleY);

        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "Error performing mouse click", e);
            throw new ServletException("Error performing mouse click", e);
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Mouse click performed at: " + x + ", " + y + "</h1>");

        LOGGER.log(Level.INFO, "Mouse click performed at: " + x + ", " + y);
    }

    private static class Scale {
        private double scaleX;
        private double scaleY;

        public double getScaleX() {
            return scaleX;
        }

        public double getScaleY() {
            return scaleY;
        }
    }
}