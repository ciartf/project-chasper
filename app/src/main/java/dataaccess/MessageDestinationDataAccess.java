package dataaccess;

import android.content.Context;

import java.util.List;

import dao.DaoSession;
import dao.MessageDestination;
import dao.MessageDestinationDao;
import de.greenrobot.dao.query.QueryBuilder;

import static dataaccess.SensorMessageDataAccess.getSensorMessageDao;

/**
 * Created by danu on 6/8/17.
 */

public class MessageDestinationDataAccess {
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
    protected static MessageDestinationDao getMessageDestinationDao(Context context){
        return getDaoSession(context).getMessageDestinationDao();
    }

    /**
     * Clear session, close db and set daoOpenHelper to null
     *
     */
    public static void closeAll(){
        DaoOpenHelper.closeAll();
    }

    public static void addOrReplace(Context context, MessageDestination messageDestination){

        getMessageDestinationDao(context).insertOrReplaceInTx(messageDestination);
        getDaoSession(context).clear();
    }

    public static List<MessageDestination> getAll(Context context){
        QueryBuilder<MessageDestination> qb = getMessageDestinationDao(context).queryBuilder();
        qb.build();
        if(qb.list().size()==0)
            return null;
        return qb.list();
    }

    public static void clean(Context context){
        getMessageDestinationDao(context).deleteAll();
        getDaoSession(context).clear();
    }

    public static void deleteOne(Context context, MessageDestination messageDestination){
        getMessageDestinationDao(context).delete(messageDestination);
        getDaoSession(context).clear();
    }
}
