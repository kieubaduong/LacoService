package org.example;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static org.example.Main.LOGGER;

public class MouseScrollHandler extends AbstractHandler {
    private static final Gson GSON = new Gson();
    private final Robot robot;

    public MouseScrollHandler(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        LOGGER.log(Level.INFO, "Received request body: %s".formatted(requestBody));

        ScrollAmount scrollAmount = GSON.fromJson(requestBody, ScrollAmount.class);
        int amount = scrollAmount.getAmount();

        robot.mouseWheel(amount);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Mouse scrolled: " + amount + "</h1>");

        LOGGER.log(Level.INFO, "Mouse scrolled: %d".formatted(amount));
    }

    private static class ScrollAmount {
        private int amount;

        public int getAmount() {
            return amount;
        }
    }
}