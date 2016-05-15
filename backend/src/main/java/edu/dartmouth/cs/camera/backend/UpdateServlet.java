package edu.dartmouth.cs.camera.backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.camera.backend.data.ExerciseEntry;
import edu.dartmouth.cs.camera.backend.data.ExerciseEntryDatastore;

/**
 * Created by oubai on 5/14/16.
 */
public class UpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("id");
        String input = req.getParameter("input_type");
        String activity = req.getParameter("activity_type");
        String time = req.getParameter("time");
        String duration = req.getParameter("duration");
        String distance = req.getParameter("distance");
        String avg = req.getParameter("avg_speed");
        String calorie = req.getParameter("calorie");
        String climb = req.getParameter("climb");
        String heart = req.getParameter("heart_rate");
        String comment = req.getParameter("comment");

        if(Long.parseLong(id) != -1L) {
            ExerciseEntry exerciseEntry = new ExerciseEntry();
            exerciseEntry.setId(Long.parseLong(id));
            exerciseEntry.setmInputType(input);
            exerciseEntry.setmActivityType(activity);
            exerciseEntry.setmDateTime(time);
            exerciseEntry.setmDuration(duration);
            exerciseEntry.setmDistance(distance);
            exerciseEntry.setmAvgSpeed(avg);
            exerciseEntry.setmCalorie(calorie);
            exerciseEntry.setmClimb(climb);
            exerciseEntry.setmHeartRate(heart);
            exerciseEntry.setmComment(comment);

            ExerciseEntryDatastore.update(exerciseEntry);
        }

        resp.sendRedirect("/query.do");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
