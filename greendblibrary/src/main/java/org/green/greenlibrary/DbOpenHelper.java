package org.green.greenlibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.geen.greenlibrary.gen.DaoMaster;
import org.geen.greenlibrary.gen.UserDao;

public class DbOpenHelper  extends DaoMaster.OpenHelper {


    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion < newVersion) {
            MigrationHelper.migrate(db,
                    UserDao.class
            );
         }

    }

}
