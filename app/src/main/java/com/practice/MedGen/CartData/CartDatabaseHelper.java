package com.practice.MedGen.CartData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CartDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "Cart.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "medicine_cart";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NON_GENERIC = "nonGeneric";
    private static final String COLUMN_GENERIC = "generic";
    private static final String COLUMN_COUNT = "count";

    public CartDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query =
                "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_GENERIC + " TEXT, " +
                        COLUMN_NON_GENERIC + " TEXT, " +
                        COLUMN_COUNT + " INTEGER);";
        sqLiteDatabase.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addMed(int id, String gen, String nonGen){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_GENERIC, gen);
        cv.put(COLUMN_NON_GENERIC, nonGen);

        cv.put(COLUMN_COUNT,1);
        long result = db.insert(TABLE_NAME,null,cv);
        if(result==-1){
            Toast.makeText(context, "Failed to add med", Toast.LENGTH_SHORT).show();
        }
    }

    public void increaseMedCount(int id, String gen, String nonGen){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_GENERIC, gen);
        cv.put(COLUMN_NON_GENERIC, nonGen);

        int count = getMedCount(id)+1;
        cv.put(COLUMN_COUNT, count);
        db.update(TABLE_NAME,cv,COLUMN_ID+ " = ?",new String[]{String.valueOf(id)});
    }

    public boolean checkExist(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME+ " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor =  db.rawQuery(query,null);
        if(cursor.getCount()<=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getMedCount(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        int count=0;
        String query = "SELECT * FROM "+ TABLE_NAME+ " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            count = cursor.getInt(3);
        }
        cursor.close();
        return count;
    }

    public void removeMedFromCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            long result = db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
            if (result==-1){
                Toast.makeText(context, "FAILED TO DELETE", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void decreaseMedCount(int id, String gen, String nonGen){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_GENERIC, gen);
        cv.put(COLUMN_NON_GENERIC, nonGen);

        int count = getMedCount(id)-1;
        cv.put(COLUMN_COUNT, count);
        db.update(TABLE_NAME,cv,COLUMN_ID+ " = ?",new String[]{String.valueOf(id)});
    }

    Cursor getAllMeds(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
}
