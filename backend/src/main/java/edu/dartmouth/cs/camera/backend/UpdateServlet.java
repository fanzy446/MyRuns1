package edu.dartmouth.cs.camera.backend;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

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
        JSONArray arr = null;
        try {
            arr = new JSONArray(req.getParameter("entry"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < arr.length(); i++) {
            try {
                JSONObject object = arr.getJSONObject(i);
                String id = object.getString("id");
                String input = object.getString("input_type");
                String activity = object.getString("activity_type");
                String time = object.getString("time");
                String duration = object.getString("duration");
                String distance = object.getString("distance");
                String avg = object.getString("avg_speed");
                String calorie = object.getString("calorie");
                String climb = object.getString("climb");
                String heart = object.getString("heart_rate");
                String comment = object.getString("comment");
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        resp.sendRedirect("/query.do");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
