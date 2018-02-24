package dataaccess;

import android.content.Context;

import java.util.List;

import dao.DaoSession;
import dao.SensorMessage;
import dao.SensorMessageDao;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by danu on 5/30/17.
 */

public class SensorMessageDataAccess {
    /**
     * use to generate dao session that you can access modelDao
     *
     * @param context --> context from activity
     * @return
     */
    protected static DaoSession getDaoSession(Context context){
        return DaoOpenHelper.getDaoSession(context);
    }

    /**
     * get collectionActivity dao and you can access the DB
     *
     * @param context
     * @return
     */
    protected static SensorMessageDao getSensorMessageDao(Context context){
        return getDaoSession(context).getSensorMessageDao();
    }

    /**
     * Clear session, close db and set daoOpenHelper to null
     *
     */
    public static void closeAll(){
        DaoOpenHelper.closeAll();
    }

    public static void addOrReplace(Context context, SensorMessage sensorMessage){

        getSensorMessageDao(context).insertOrReplaceInTx(sensorMessage);
        getDaoSession(context).clear();
    }

    public static void addOrReplace(Context context, List<SensorMessage> sensorMessagesList){

        getSensorMessageDao(context).insertOrReplaceInTx(sensorMessagesList);
        getDaoSession(context).clear();
    }

    public static SensorMessage getOne (Context context, String uuid) {
        QueryBuilder<SensorMessage> qb = getSensorMessageDao(context).queryBuilder();
        qb.where(SensorMessageDao.Properties.Uuid_sensor_message.eq(uuid));
        qb.build();
        if(qb.list().size()==0)
            return null;
        return qb.list().get(0);
    }

    public static List<SensorMessage> getAll (Context context) {
        QueryBuilder<SensorMessage> qb = getSensorMessageDao(context).queryBuilder();
        qb.build();
        if(qb.list().size()==0)
            return null;
        return qb.list();
    }

    public static List<SensorMessage> getAllIsNotSent (Context context) {
        try{
            QueryBuilder<SensorMessage> qb = getSensorMessageDao(context).queryBuilder();
            qb.where(SensorMessageDao.Properties.Is_sent.eq("false"));
            qb.build();
            if(qb.list().size()==0)
                return null;
            return qb.list();
        }catch(Exception e){
            return null;
        }

    }

    public static void delete(Context context, SensorMessage sensorMessage){
        getSensorMessageDao(context).delete(sensorMessage);
        getDaoSession(context).clear();
    }

}
