package com.practice.MedGen.ExploreStoreData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.checkerframework.checker.nullness.qual.Nullable;

public class StoreDatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "Store.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "store_table";
    private static final String COLUMN_STORE_CODE= "_storeCode";
    private static final String COLUMN_ADDRESS = "storeAddress";
    private static final String COLUMN_CONTACT_NUMBER = "contactNumber";
    private static final String COLUMN_CONTACT_PERSON = "contactPerson";
    private static final String COLUMN_DISTRICT = "storeDistrict";
    private static final String COLUMN_STATE= "storeState";
    private static final String COLUMN_PIN_CODE = "storePinCode";
    public StoreDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_STORE_CODE + " TEXT PRIMARY KEY, " +
                        COLUMN_ADDRESS + " TEXT, " +
                        COLUMN_DISTRICT + " TEXT, " +
                        COLUMN_STATE + " TEXT, " +
                        COLUMN_PIN_CODE + " TEXT, " +
                        COLUMN_CONTACT_PERSON + " TEXT, " +
                        COLUMN_CONTACT_NUMBER + " TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addStoreItems(StoreItems items){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STORE_CODE, items.getStore_code());
        cv.put(COLUMN_ADDRESS, items.getAddress());
        cv.put(COLUMN_DISTRICT, items.getDistrict());
        cv.put(COLUMN_STATE, items.getState());
        cv.put(COLUMN_PIN_CODE, items.getPin_code());
        cv.put(COLUMN_CONTACT_NUMBER, items.getPhone_number());
        cv.put(COLUMN_CONTACT_PERSON, items.getContact_person());
        try {
            sqLiteDatabase.insert(TABLE_NAME,null, cv);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.println(Log.DEBUG,"dataFetchGalti",e.getMessage());
        }
    }
    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor fetchAllByState(String state){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATE + " = " + state;
        Cursor cursor = null;
        if(sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor fetchAll(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = null;
        if(sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }

    Cursor fetchState(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "SELECT DISTINCT "+COLUMN_STATE+" FROM " + TABLE_NAME;
        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }
}
