package ching.notes.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by book871181 on 16/6/4.
 * use for contain SQL data
 */
public class NotesDao {


    Context context;
    DBhelper notesDBHelper;
    private static final String TAG = "NotesDao";

    private final String[] Bouns_COLUMNS = new String[] {"Id", "Title", "Notes"};


    public NotesDao(Context context, DBhelper bounsDBHelper){
        this.context = context;
        this.notesDBHelper = bounsDBHelper;

    }

    public boolean isDataExist(){
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = notesDBHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(DBhelper.TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }


    public void initTable(){
        SQLiteDatabase db = null;

        try {
            Log.d(TAG,"Try to init Table");

            db = notesDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + DBhelper.TABLE_NAME + " (Id, Title, Notes) values (1, 'myfirstData', 'I spend soooo much time')");
            db.execSQL("insert into " + DBhelper.TABLE_NAME + " (Id, Title, Notes) values (2, 'mySecondData', 'second')");
            db.execSQL("insert into " + DBhelper.TABLE_NAME + " (Id, Title, Notes) values (3, 'myThird', 'myThird')");
            db.execSQL("insert into " + DBhelper.TABLE_NAME + " (Id, Title, Notes) values (4, 'myFore', 'fore')");
//            db.execSQL("insert into " + DBhelper.TABLE_NAME + " (Id, Title, Notes, Points) values (6, 'F', qwe, '2')");

            db.setTransactionSuccessful();


        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();

            }
        }
    }



    public  boolean insertDate(Notes notes){
        SQLiteDatabase db = null;

        try {
            Log.d(TAG,"insertData");
            db = notesDBHelper.getWritableDatabase();
            db.beginTransaction();

            // insert into Orders(Id, CustomName, OrderPrice, Country) values (7, "Jne", 700, "China");
            ContentValues contentValues = new ContentValues();
            contentValues.put("Title", notes.title);
            contentValues.put("Notes", notes.notes);
            db.insertOrThrow(notesDBHelper.TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "Id repeat", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    public int getPositionId(int position){
        int id = 0;
        SQLiteDatabase db = null;
        db = notesDBHelper.getReadableDatabase();


        return id;
    }
    public boolean DeletData(int id){
        SQLiteDatabase db = null;

        try {
            db = notesDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.delete(notesDBHelper.TABLE_NAME, "Id = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    public String getTextByPositon(int postion){
        SQLiteDatabase db = null;

        String text = "null";
        db = notesDBHelper.getReadableDatabase();
        String where = "Id" + "=" + postion;

        Cursor cursor = db.query(notesDBHelper.TABLE_NAME, null, where , null, null, null, null);

        while (cursor.moveToNext()){
            text = cursor.getString(2);
         //   text = String.valueOf(parseOrder(cursor));
        }


        return text;
    }


    public void editData(Notes note){
        SQLiteDatabase db = notesDBHelper.getWritableDatabase();

        ContentValues args = new ContentValues();
        //Id, Title, Notes
        args.put("Id",note.id);
        args.put("Title", note.title);
        args.put("Notes", note.notes);
        db.update(notesDBHelper.TABLE_NAME, args, "id=" + note.id, null);
    }

    /*
    * get All data in the DataBase
    * */
    public  List<Notes> getAllDate(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            Log.e(TAG,"trying to get Data");

            db = notesDBHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(notesDBHelper.TABLE_NAME, Bouns_COLUMNS, null, null, null, null, null);


            if (cursor.getCount() > 0) {
                //get How many count in the list

                List<Notes> notesList = new ArrayList<Notes>(cursor.getCount());
                while (cursor.moveToNext()) {
                    notesList.add(parseOrder(cursor));
                }
                return notesList;
            }

        }
        catch (Exception e) {
            Log.e(TAG, "  ", e);
        }

        finally {

            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }



    private Notes parseOrder(Cursor cursor){
        int id = (cursor.getInt(cursor.getColumnIndex("Id")));
        String title = (cursor.getString(cursor.getColumnIndex("Title")));
        String notes = (cursor.getString(cursor.getColumnIndex("Notes")));
        Notes order = new Notes(id,title,notes);

        return order;
    }


}
