package dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import constant.GlobalData;
import dao.DaoMaster;
import dao.DaoSession;
import dao.DaoMaster.DevOpenHelper;

/**
 * Created by danu on 5/30/17.
 */

public class DaoOpenHelper {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static SQLiteDatabase db;

    public DaoOpenHelper(Context context){
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, GlobalData.db, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * you can use DaoSession.clear(). However, it will clear all all objects from the session.
     * If you want to avoid that, you have to execute a regular query and delete the result entities
     * (for example with deleteInTx).
     */
    public static void clear(){
        if(daoSession!=null)
            daoSession.clear();
    }

    public static DaoSession getDaoSession(Context context) {
        if(daoSession==null){
            new DaoOpenHelper(context);
        }
        return daoSession;
    }

    public static void closeAll() {
        if(daoSession!=null) {
            daoSession.clear();
            db.close();
            daoSession=null;
        }
    }

    public static SQLiteDatabase getDb(Context context){
        if(daoMaster==null){
            new DaoOpenHelper(context);
        }
        return daoMaster.getDatabase();
    }

    public static DaoMaster getDaoMaster(Context context){
        if(daoMaster==null){
            new DaoOpenHelper(context);
        }
        return daoMaster;
    }
}
