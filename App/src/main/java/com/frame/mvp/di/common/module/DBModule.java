package com.frame.mvp.di.common.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.frame.mvp.entity.DaoMaster;
import com.frame.mvp.entity.DaoSession;
import com.frame.mvp.entity.UserDao;
import com.tool.common.db.DBContextWrapper;
import com.tool.common.db.MigrationHelper;
import com.tool.common.utils.ProjectUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DBModule
 */
@Module
public class DBModule {

    @Singleton
    @Provides
    public DBOpenHelper provideDevOpenHelper() {
        return new DBOpenHelper(new DBContextWrapper(), ProjectUtils.PROJECT_NAME + ".db", null);
    }

    @Singleton
    @Provides
    public DaoMaster provideDaoMaster(DBOpenHelper devOpenHelper) {
        return new DaoMaster(devOpenHelper.getWritableDb());
    }

    @Singleton
    @Provides
    public DaoSession provideDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    /**
     * 数据库升级模块
     */
    public class DBOpenHelper extends DaoMaster.OpenHelper {

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            MigrationHelper.migrate(db, UserDao.class);
        }
    }

//    //输出日志
//    public void setDebug() {
//        QueryBuilder.LOG_SQL = true;
//        QueryBuilder.LOG_VALUES = true;
//    }
//
//    public void close() {
//        closeHelper();
//        closeSession();
//    }
//    public void closeHelper() {
//        if (helper != null) {
//            helper.close();
//            helper = null;
//        }
//    }
//    //关闭session
//    public void closeSession() {
//        if (daoSession != null) {
//            daoSession.clear();
//            daoSession = null;
//        }
//    }
}