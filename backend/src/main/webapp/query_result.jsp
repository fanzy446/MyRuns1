<%@ page import="edu.dartmouth.cs.camera.backend.data.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Exercise-Entry-Type" content="text/html; charset=ISO-8859-1">
<title>Query Result</title>
</head>
<body>
    <%
        String retStr = (String) request.getAttribute("_retStr");
        if(retStr != null) {
    %>
    <%=retStr%><br>
    <%
        }
    %>
    <title>Exercise Entries for your device:</title>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <th>id</th>
            <th>inputType</th>
            <th>activityType</th>
            <th>dateTime</th>
            <th>duration</th>
            <th>distance</th>
            <th>avgSpeed</th>
            <th>calorie</th>
            <th>climb</th>
            <th>heartrate</th>
            <th>comment</th>
            <th></th>
        </tr>
        <b>
        Exercise Entries for your device:<br>
        <%
            ArrayList<ExerciseEntry> resultList = (ArrayList<ExerciseEntry>) request.getAttribute("result");
            if (resultList != null) {
            for(ExerciseEntry exerciseEntry : resultList) {
        %>
        <tr>
            <th><%=exerciseEntry.getId()%></th>
            <th><%=exerciseEntry.getmInputType()%></th>
            <th><%=exerciseEntry.getmActivityType()%></th>
            <th><%=exerciseEntry.getmDateTime()%></th>
            <th><%=exerciseEntry.getmDuration()%></th>
            <th><%=exerciseEntry.getmDistance()%></th>
            <th><%=exerciseEntry.getmAvgSpeed()%></th>
            <th><%=exerciseEntry.getmCalorie()%></th>
            <th><%=exerciseEntry.getmClimb()%></th>
            <th><%=exerciseEntry.getmHeartRate()%></th>
            <th><%=exerciseEntry.getmComment()%></th>
            <th><a
                href="/delete.do?id=<%=exerciseEntry.getId()%>">delete</a></th>
        </tr>
        <%
            }
            }
            %>
    </table>
</body>
</html>
