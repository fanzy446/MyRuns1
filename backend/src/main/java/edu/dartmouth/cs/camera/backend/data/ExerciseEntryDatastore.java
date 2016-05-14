package edu.dartmouth.cs.camera.backend.data;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

/**
 * Created by oubai on 5/14/16.
 */
public class ExerciseEntryDatastore {

    private static final Logger mLogger = Logger.getLogger(ExerciseEntryDatastore.class.getName());
    private static final DatastoreService mDatastore = DatastoreServiceFactory.getDatastoreService();

    private static Key getKey() {
        return KeyFactory.createKey(ExerciseEntry.EXERCISEENTRY_PARENT_ENTITY_NAME, ExerciseEntry.EXERCISEENTRY_PARENT_KEY_NAME);
    }

    private static void createParentEntity() {
        Entity entity = new Entity(getKey());
        mDatastore.put(entity);
    }

    public static boolean add(ExerciseEntry exerciseEntry) {
        return true;
    }

    public static boolean update(ExerciseEntry exerciseEntry) {
        Entity result = null;
        try {
            result = mDatastore.get(KeyFactory.createKey(getKey(), ExerciseEntry.EXERCISEENTRY_PARENT_ENTITY_NAME, exerciseEntry.getId()));
            result.setProperty(ExerciseEntry.FIELD_NAME_ID, exerciseEntry.getId());
            result.setProperty(ExerciseEntry.FIELD_NAME_INPUT, exerciseEntry.getmInputType());
            result.setProperty(ExerciseEntry.FIELD_NAME_INPUT, exerciseEntry.getmInputType());
            result.setProperty(ExerciseEntry.FIELD_NAME_ACTIVITY, exerciseEntry.getmActivityType());
            result.setProperty(ExerciseEntry.FIELD_NAME_TIME, exerciseEntry.getmDateTime());
            result.setProperty(ExerciseEntry.FIELD_NAME_DURATION, exerciseEntry.getmDuration());
            result.setProperty(ExerciseEntry.FIELD_NAME_DISTANCE, exerciseEntry.getmDistance());
            result.setProperty(ExerciseEntry.FIELD_NAME_AVG, exerciseEntry.getmAvgSpeed());
            result.setProperty(ExerciseEntry.FIELD_NAME_CALORIE, exerciseEntry.getmCalorie());
            result.setProperty(ExerciseEntry.FIELD_NAME_CLIMB, exerciseEntry.getmClimb());
            result.setProperty(ExerciseEntry.FIELD_NAME_HEART, exerciseEntry.getmHeartRate());
            result.setProperty(ExerciseEntry.FIELD_NAME_COMMENT, exerciseEntry.getmComment());
            mDatastore.put(result);
        } catch (Exception ex) {

        }
        return false;
    }

    public static boolean delete(Long id) {
        Query.Filter filter = new Query.FilterPredicate(ExerciseEntry.FIELD_NAME_ID, Query.FilterOperator.EQUAL, id);
        Query query = new Query(ExerciseEntry.EXERCISEENTRY_ENTITY_NAME);
        query.setFilter(filter);

        PreparedQuery pq = mDatastore.prepare(query);

        Entity result = pq.asSingleEntity();
        boolean ret = false;
        if(result != null) {
            mDatastore.delete(result.getKey());
            ret = true;
        }

        return ret;
    }

    public static ArrayList<ExerciseEntry> query(Long id) {
        ArrayList<ExerciseEntry> resultList = new ArrayList<>();
        if(!id.equals(1L)) {
            ExerciseEntry exerciseEntry = getExerciseEntryById(id, null);
            if(exerciseEntry != null) {
                resultList.add(exerciseEntry);
            }
        }
        else {
            Query query = new Query(ExerciseEntry.EXERCISEENTRY_ENTITY_NAME);
            query.setFilter(null);
            query.setAncestor(getKey());

            PreparedQuery pq = mDatastore.prepare(query);

            for(Entity entity : pq.asIterable()) {
                ExerciseEntry exerciseEntry = getExerciseFromEntity(entity);
                if(exerciseEntry != null) {
                    resultList.add(exerciseEntry);
                }
            }
        }
        return  resultList;
    }

    public static ExerciseEntry getExerciseEntryById(Long id, Transaction txn) {
        Entity result = null;
        try {
            result = mDatastore.get(KeyFactory.createKey(getKey(), ExerciseEntry.EXERCISEENTRY_ENTITY_NAME, id));
        } catch (Exception ex) {

        }

        return getExerciseFromEntity(result);
    }

    private static ExerciseEntry getExerciseFromEntity(Entity entity) {
        if(entity == null) return null;
        ExerciseEntry exerciseEntry = new ExerciseEntry();
        exerciseEntry.setId((Long) entity.getProperty(ExerciseEntry.FIELD_NAME_ID));
        exerciseEntry.setmInputType((String) entity.getProperty(ExerciseEntry.FIELD_NAME_INPUT));
        exerciseEntry.setmActivityType((String) entity.getProperty(ExerciseEntry.FIELD_NAME_ACTIVITY));
        exerciseEntry.setmDateTime((String) entity.getProperty(ExerciseEntry.FIELD_NAME_TIME));
        exerciseEntry.setmDuration((String) entity.getProperty(ExerciseEntry.FIELD_NAME_DURATION));
        exerciseEntry.setmDistance((String) entity.getProperty(ExerciseEntry.FIELD_NAME_DISTANCE));
        exerciseEntry.setmAvgSpeed((String) entity.getProperty(ExerciseEntry.FIELD_NAME_AVG));
        exerciseEntry.setmCalorie((String) entity.getProperty(ExerciseEntry.FIELD_NAME_CALORIE));
        exerciseEntry.setmClimb((String) entity.getProperty(ExerciseEntry.FIELD_NAME_CLIMB));
        exerciseEntry.setmHeartRate((String) entity.getProperty(ExerciseEntry.FIELD_NAME_HEART));
        exerciseEntry.setmComment((String) entity.getProperty(ExerciseEntry.FIELD_NAME_COMMENT));
        return exerciseEntry;
    }
}
