package edu.dartmouth.cs.camera.backend;

import org.json.simple.JSONObject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.camera.backend.data.ExerciseEntryDatastore;

public class DeleteServlet extends HttpServlet {

    private static final long serivalVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("id");
        ExerciseEntryDatastore.delete(id);
        MessagingEndpoint msg = new MessagingEndpoint();
        JSONObject obj = new JSONObject();
        obj.put("type", "delete");
        obj.put("code", 0);
        JSONObject data = new JSONObject();
        data.put("id", id);
        obj.put("data", data);
        msg.sendMessage(obj.toJSONString());
        resp.sendRedirect("/query.do");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
