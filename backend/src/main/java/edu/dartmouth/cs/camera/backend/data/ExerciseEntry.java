package edu.dartmouth.cs.camera.backend.data;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by oubai on 5/14/16.
 */
public class ExerciseEntry {
    public static final String EXERCISEENTRY_PARENT_ENTITY_NAME = "ExerciseEntryParent";
    public static final String EXERCISEENTRY_PARENT_KEY_NAME = "ExerciseEntryParent";

    public static final String EXERCISEENTRY_ENTITY_NAME = "ExerciseEntry";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_INPUT = "input_type";
    public static final String FIELD_NAME_ACTIVITY = "activity_type";
    public static final String FIELD_NAME_TIME = "time";
    public static final String FIELD_NAME_DURATION = "duration";
    public static final String FIELD_NAME_DISTANCE = "distance";
    public static final String FIELD_NAME_AVG = "avg_speed";
    public static final String FIELD_NAME_CALORIE = "calorie";
    public static final String FIELD_NAME_CLIMB = "climb";
    public static final String FIELD_NAME_HEART = "heart_rate";
    public static final String FIELD_NAME_COMMENT = "comment";
    public static final String KEY_NAME = FIELD_NAME_ID;

    @Id
    public long id;

    @Index
    public String mInputType;
    public String mActivityType;
    public String mDateTime;
    public String mDuration;
    public String mDistance;
    public String mAvgSpeed;
    public String mCalorie;
    public String mClimb;
    public String mHeartRate;
    public String mComment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
