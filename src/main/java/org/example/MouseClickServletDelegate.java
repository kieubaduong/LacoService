package org.example;

import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MouseClickServletDelegate extends HttpServlet {
    private final MouseClickHandler clickHandler = new MouseClickHandler();
    private final MouseDoubleClickHandler doubleClickHandler = new MouseDoubleClickHandler();
    private final MouseRightClickHandler rightClickHandler = new MouseRightClickHandler();
    private final MouseDragHandler dragHandler = new MouseDragHandler();

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
            }
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}