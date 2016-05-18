package edu.dartmouth.cs.camera.backend;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.camera.backend.data.ExerciseEntry;
import edu.dartmouth.cs.camera.backend.data.ExerciseEntryDatastore;

public class QueryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("id");
        ArrayList<ExerciseEntry> result = ExerciseEntryDatastore.query(id);
        req.setAttribute("result", result);
        getServletContext().getRequestDispatcher("/query_result.jsp").forward(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
