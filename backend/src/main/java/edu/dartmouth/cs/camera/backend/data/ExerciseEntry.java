package edu.dartmouth.cs.camera.backend.data;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

public class ExerciseEntry {
    public static final String EXERCISEENTRY_PARENT_ENTITY_NAME = "ExerciseEntryParentEntity";
    public static final String EXERCISEENTRY_PARENT_KEY_NAME = "ExerciseEntryParentKey";

    public static final String EXERCISEENTRY_ENTITY_NAME = "ExerciseEntry";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_INPUT = "mInputType";
    public static final String FIELD_NAME_ACTIVITY = "mActivityType";
    public static final String FIELD_NAME_TIME = "mDateTime";
    public static final String FIELD_NAME_DURATION = "mDuration";
    public static final String FIELD_NAME_DISTANCE = "mDistance";
    public static final String FIELD_NAME_AVG = "mAvgSpeed";
    public static final String FIELD_NAME_CALORIE = "mCalorie";
    public static final String FIELD_NAME_CLIMB = "mClimb";
    public static final String FIELD_NAME_HEART = "mHeartRate";
    public static final String FIELD_NAME_COMMENT = "mComment";
    public static final String KEY_NAME = FIELD_NAME_ID;

    @Id
    private String id;

    @Index
    private String mInputType;        // Manual, GPS or automatic
    private String mActivityType;     // Running, cycling etc.
    private String mDateTime;    // When does this entry happen
    private String mDuration;         // Exercise duration in seconds
    private String mDistance;      // Distance traveled. Either in meters or feet.
    private String mAvgSpeed;      // Average speed
    private String mCalorie;          // Calories burnt
    private String mClimb;         // Climb. Either in meters or feet.
    private String mHeartRate;        // Heart rate
    private String mComment;       // Comments

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmInputType() {
        return mInputType;
    }

    public void setmInputType(String mInputType) {
        this.mInputType = mInputType;
    }

    public String getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(String mActivityType) {
        this.mActivityType = mActivityType;
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmDistance() {
        return mDistance;
    }

    public void setmDistance(String mDistance) {
        this.mDistance = mDistance;
    }

    public String getmAvgSpeed() {
        return mAvgSpeed;
    }

    public void setmAvgSpeed(String mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public String getmCalorie() {
        return mCalorie;
    }

    public void setmCalorie(String mCalorie) {
        this.mCalorie = mCalorie;
    }

    public String getmClimb() {
        return mClimb;
    }

    public void setmClimb(String mClimb) {
        this.mClimb = mClimb;
    }

    public String getmHeartRate() {
        return mHeartRate;
    }

    public void setmHeartRate(String mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }
}
