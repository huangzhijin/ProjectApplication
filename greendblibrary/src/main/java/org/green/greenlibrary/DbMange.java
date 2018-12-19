package org.green.greenlibrary;

import android.content.Context;

import org.geen.greenlibrary.gen.DaoMaster;
import org.geen.greenlibrary.gen.DaoSession;
import org.geen.greenlibrary.gen.UserDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

import java.util.List;

public class DbMange {

   private static DbMange dbMange;
   private DaoSession daoSession;

    private DbMange() {
    }

    public static DbMange getInstance(){
           if(dbMange==null){
               dbMange=new DbMange();
           }
       return dbMange;
   }




    public  void initDb(Context context) {

        DbOpenHelper helper=new DbOpenHelper(new GreenDaoContext(context), "user-db.db",null);
        Database db =  helper.getWritableDb();;

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {

        return daoSession;
    }



    public UserDao   getUserDao(){

        return daoSession.getUserDao();
    }


    public Long   addUserDaoItem(User user){

        return daoSession.getUserDao().insert(user);
    }

    public void   deleteUserDaoItem(Long id){

         daoSession.getUserDao().deleteByKey(id);
    }

    public void   updateUserDaoItem(Long id,User user){
        User user1 = daoSession.getUserDao().load(id);
        if (user1 != null) {
            user1.setName(user.getName());
            daoSession.getUserDao().update(user1);
        }

    }


    public List<User> selectUserDao(){

      Query<User> userQuery= daoSession.getUserDao().queryBuilder().orderAsc(UserDao.Properties.UserId).build();
        return userQuery.list();

    }

}
