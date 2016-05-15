package edu.dartmouth.cs.camera.backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.camera.backend.data.ExerciseEntryDatastore;

/**
 * Created by oubai on 5/14/16.
 */
public class DeleteServlet extends HttpServlet{

    private static final long serivalVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("id");
        ExerciseEntryDatastore.delete(Long.valueOf(id));
        resp.sendRedirect("/query.do");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
