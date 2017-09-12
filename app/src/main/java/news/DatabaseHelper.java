package news;

/**
 * Created by thinkpad on 2017/9/7.
 */

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by thinkpad on 2017/9/6.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    static private SQLiteDatabase db = null;
    static private DatabaseHelper dbHelper = null;
    private static final int VERSION = 1;
    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    private DatabaseHelper(Context context, String name, int version){
        super(context, name, null, version);
    }
    private DatabaseHelper(Context context, String name){
        super(context, name, null, VERSION);
    }
    public static synchronized void init(Activity thisActivity) {
        dbHelper = new DatabaseHelper(thisActivity, "local.db");
    }
    public static synchronized DatabaseHelper getDbHelper() {
        if (dbHelper == null) {
            System.out.println("Error! DatabaseHelper is null.");
        }
        db = dbHelper.getWritableDatabase();
        while(db.isDbLockedByCurrentThread()) {
            //db is locked, keep looping
        }
        return dbHelper;
    }
    public void onCreate(SQLiteDatabase db){
        System.out.println("Creating database");
        db.execSQL("create table if not exists newsHistory (id text primary key, category text, content text, journal text," +
                "picturesLocal text, pictures text, author text, langType text, classTag text, intro text, source text," +
                "time text, title text, url text, video text, noPictureMode integer)");
        db.execSQL("create table if not exists favorite (id text primary key)");
        db.execSQL("create table if not exists NLP (word text primary key, score double)");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        System.out.println("Updating database");
    }
    @Override
    public void finalize() throws Throwable {
        if(null != dbHelper)
            dbHelper.close();
        if(null != db)
            db.close();
        super.finalize();
    }
}
