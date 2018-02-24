package dataaccess;

import android.content.Context;

import java.util.List;

import dao.DaoSession;
import dao.SensorMessage;
import dao.SensorMessageDao;
import dao.User;
import dao.UserDao;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by danu on 5/30/17.
 */

public class UserDataAccess {
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
    protected static UserDao getUserDao(Context context){
        return getDaoSession(context).getUserDao();
    }

    /**
     * Clear session, close db and set daoOpenHelper to null
     *
     */
    public static void closeAll(){
        DaoOpenHelper.closeAll();
    }

    public static void add(Context context, User user){

        getUserDao(context).insert(user);
        getDaoSession(context).clear();
    }


    public static User get (Context context) {
        try{
            QueryBuilder<User> qb = getUserDao(context).queryBuilder();
            qb.build();
            if(qb.list().size()==0)
                return null;
            return qb.list().get(0);
        }catch(Exception e){
            return null;
        }

    }

    public static void clean(Context context){
        getUserDao(context).deleteAll();
        getDaoSession(context).clear();
    }

}
