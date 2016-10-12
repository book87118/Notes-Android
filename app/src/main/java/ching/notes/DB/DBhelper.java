package ching.notes.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by book871181 on 16/5/19.
 */
public class DBhelper extends SQLiteOpenHelper {



    // 資料庫名稱
    // DATA/data/APP_NAME/databases/FILENAME
    public static final String DATABASE_NAME = "mydata.db";
    public static final int VERSION = 3 ;

    public static final String TABLE_NAME = "Notes";
    public static final String TABLE_HISTORY_NAME = "History";





    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
       // String sql = "create table if not exists " + TABLE_NAME + DATABASE_CREATE;
        String sql = "create table " + TABLE_NAME +
                " (Id integer primary key, Title TEXT, Notes TEXT );";

        db.execSQL(sql);
        String sql_history = "create table " + TABLE_HISTORY_NAME +
                " (Id integer primary key, Title TEXT, Notes TEXT, Points INTEGER );";
        db.execSQL(sql_history);

    }
    // Method is called during an upgrade of the database, e.g. if you increase
    // if you increase database version

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_NAME);

        onCreate(db);
    }
}
