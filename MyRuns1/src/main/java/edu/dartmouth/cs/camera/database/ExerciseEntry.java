package edu.dartmouth.cs.camera.database;

import java.util.ArrayList;
import java.util.Calendar;

public class ExerciseEntry {

    private Long id;

    private Integer mInputType;        // Manual, GPS or automatic
    private Integer mActivityType;     // Running, cycling etc.
    private Calendar mDateTime;    // When does this entry happen
    private Integer mDuration;         // Exercise duration in seconds
    private Double mDistance;      // Distance traveled. Either in meters or feet.
    private Double mAvgPace;       // Average pace
    private Double mAvgSpeed;      // Average speed
    private Integer mCalorie;          // Calories burnt
    private Double mClimb;         // Climb. Either in meters or feet.
    private Integer mHeartRate;        // Heart rate
    private String mComment;       // Comments
    private Integer mPrivacy;          // Privacy
    private ArrayList<String> mLocationList; // Location list

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getmInputType() {
        return mInputType;
    }

    public void setmInputType(Integer mInputType) {
        this.mInputType = mInputType;
    }

    public Integer getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(Integer mActivityType) {
        this.mActivityType = mActivityType;
    }

    public Calendar getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(Calendar mDateTime) {
        this.mDateTime = mDateTime;
    }

    public Integer getmDuration() {
        return mDuration;
    }

    public void setmDuration(Integer mDuration) {
        this.mDuration = mDuration;
    }

    public Double getmDistance() {
        return mDistance;
    }

    public void setmDistance(Double mDistance) {
        this.mDistance = mDistance;
    }

    public Double getmAvgPace() {
        return mAvgPace;
    }

    public void setmAvgPace(Double mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    public Double getmAvgSpeed() {
        return mAvgSpeed;
    }

    public void setmAvgSpeed(Double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public Integer getmCalorie() {
        return mCalorie;
    }

    public void setmCalorie(Integer mCalorie) {
        this.mCalorie = mCalorie;
    }

    public Double getmClimb() {
        return mClimb;
    }

    public void setmClimb(Double mClimb) {
        this.mClimb = mClimb;
    }

    public Integer getmHeartRate() {
        return mHeartRate;
    }

    public void setmHeartRate(Integer mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public Integer getmPrivacy() {
        return mPrivacy;
    }

    public void setmPrivacy(Integer mPrivacy) {
        this.mPrivacy = mPrivacy;
    }

    public ArrayList<String> getmLocationList() {
        return mLocationList;
    }

    public void setmLocationList(ArrayList<String> mLocationList) {
        this.mLocationList = mLocationList;
    }
}
