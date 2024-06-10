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

public class MouseClickHandler extends AbstractHandler {
    private static final Gson GSON = new Gson();
    private final Robot robot;

    public MouseClickHandler(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        LOGGER.log(Level.INFO, "Received request body: %s".formatted(requestBody));

        Scale scale = GSON.fromJson(requestBody, Scale.class);
        double scaleX = scale.getScaleX();
        double scaleY = scale.getScaleY();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        LOGGER.log(Level.INFO, "Screen size: %dx%d".formatted(screenSize.width, screenSize.height));

        int x = (int) (screenSize.width * scaleX);
        int y = (int) (screenSize.height * scaleY);

        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Mouse click performed at: " + x + ", " + y + "</h1>");

        LOGGER.log(Level.INFO, "Mouse click performed at: %d, %d".formatted(x, y));
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