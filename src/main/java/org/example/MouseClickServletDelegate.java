package org.example;

import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public class MouseClickServletDelegate extends HttpServlet {
    private final Robot robot = new Robot();
    private final MouseClickHandler clickHandler = new MouseClickHandler(robot);
    private final MouseDoubleClickHandler doubleClickHandler = new MouseDoubleClickHandler(robot);
    private final MouseRightClickHandler rightClickHandler = new MouseRightClickHandler(robot);
    private final MouseDragHandler dragHandler = new MouseDragHandler(robot);
    private final MouseScrollHandler scrollHandler = new MouseScrollHandler(robot);

    public MouseClickServletDelegate() throws AWTException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        Request baseRequest = (Request) req;
        try {
            if ("/singleClick".equals(path)) {
                clickHandler.handle(path, baseRequest, req, resp);
            } else if ("/doubleClick".equals(path)) {
                doubleClickHandler.handle(path, baseRequest, req, resp);
            } else if ("/rightClick".equals(path)) {
                rightClickHandler.handle(path, baseRequest, req, resp);
            } else if ("/drag".equals(path)) {
                dragHandler.handle(path, baseRequest, req, resp);
            } else if ("/scroll".equals(path)) {
                scrollHandler.handle(path, baseRequest, req, resp);
            }
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}