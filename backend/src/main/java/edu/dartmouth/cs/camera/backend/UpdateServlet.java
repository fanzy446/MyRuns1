package edu.dartmouth.cs.camera.backend;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.camera.backend.data.ExerciseEntry;
import edu.dartmouth.cs.camera.backend.data.ExerciseEntryDatastore;


public class UpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ExerciseEntryDatastore.deleteAll();
        String json = req.getParameter("Entries");
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(json);
            for (int i = 0; i < jsonArray.size(); i++) {
                ExerciseEntry entry = new ExerciseEntry();
                JSONObject obj = (JSONObject) jsonArray.get(i);
                entry.setId(obj.get(ExerciseEntry.FIELD_NAME_ID).toString());
                entry.setmInputType(obj.get(ExerciseEntry.FIELD_NAME_INPUT).toString());
                entry.setmActivityType(obj.get(ExerciseEntry.FIELD_NAME_ACTIVITY).toString());

                JSONObject calendar = (JSONObject) obj.get(ExerciseEntry.FIELD_NAME_TIME);
                String time = String.format("%s:%s:%s %s/%s/%s", calendar.get("hourOfDay"), calendar.get("minute"),
                        calendar.get("second"), calendar.get("month"), calendar.get("dayOfMonth"), calendar.get("year"));
                entry.setmDateTime(time);

                if (obj.get(ExerciseEntry.FIELD_NAME_DURATION) != null) {
                    int seconds = Integer.parseInt(obj.get(ExerciseEntry.FIELD_NAME_DURATION).toString());
                    double min = seconds / 60;
                    String duration = String.format("%.2f mins", min);
                    entry.setmDuration(duration);
                }
                if (obj.get(ExerciseEntry.FIELD_NAME_DISTANCE) != null) {
                    entry.setmDistance(String.format("%.2f Miles", Float.parseFloat(obj.get(ExerciseEntry.FIELD_NAME_DISTANCE).toString())));
                }
                if (obj.get(ExerciseEntry.FIELD_NAME_AVG) != null) {
                    entry.setmAvgSpeed(String.format("%.2f m/h", Float.parseFloat(obj.get(ExerciseEntry.FIELD_NAME_AVG).toString())));
                }
                if (obj.get(ExerciseEntry.FIELD_NAME_CALORIE) != null) {
                    entry.setmCalorie(obj.get(ExerciseEntry.FIELD_NAME_CALORIE).toString());
                }
                if (obj.get(ExerciseEntry.FIELD_NAME_CLIMB) != null) {
                    entry.setmClimb(obj.get(ExerciseEntry.FIELD_NAME_CLIMB).toString());
                }
                if (obj.get(ExerciseEntry.FIELD_NAME_HEART) != null) {
                    entry.setmHeartRate(obj.get(ExerciseEntry.FIELD_NAME_HEART).toString());
                }
                if (obj.get(ExerciseEntry.FIELD_NAME_COMMENT) != null) {
                    entry.setmComment(obj.get(ExerciseEntry.FIELD_NAME_COMMENT).toString());
                }
                ExerciseEntryDatastore.add(entry);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MessagingEndpoint msg = new MessagingEndpoint();
        JSONObject obj = new JSONObject();
        obj.put("type", "update");
        obj.put("code", 0);
        msg.sendMessage(obj.toJSONString());
        resp.sendRedirect("/query.do");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
