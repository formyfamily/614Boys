package news;

/**
 * Created by thinkpad on 2017/9/7.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thinkpad on 2017/9/6.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context, String name, int version){
        super(context, name, null, version);
    }
    public DatabaseHelper(Context context, String name){
        super(context, name, null, VERSION);
    }
    public void onCreate(SQLiteDatabase db){
        System.out.println("Creating database");
        db.execSQL("create table if not exists newsHistory (id text primary key, category text, content text, journal text," +
                "picturesLocal text, pictures text, author text, langType text, classTag text, intro text, source text," +
                "time text, title text, url text, video text)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        System.out.println("Updating database");
    }
}
