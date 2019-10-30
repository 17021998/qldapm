package com.example.hearthrate.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hearthrate.Model.ItemHistory;
import com.example.hearthrate.Filter;
import com.example.hearthrate.Model.ItemHistory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "_histories1";
    private static final String col_id = "ID";
    private static final String col_logoId = "LogoId";
    private static final String col_result = "Result";
    private static final String col_date = "Date";
    private static final String col_state = "State";
    private static final String col_remove = "Remove";

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                col_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                col_logoId + " INTEGER," +
                col_result + " TEXT NOT NULL," +
                col_date + " TEXT NOT NULL," +
                col_state + " TEXT NOT NULL," +
                col_remove + " BOOLEAN NOT NULL CHECK (" + col_remove + " IN (0,1))" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public boolean addData(int logoId, String result, String date, String state){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_logoId, logoId);
        contentValues.put(col_result, result);
        contentValues.put(col_date, date);
        contentValues.put(col_state, state);
        contentValues.put(col_remove, 1);
        long response = db.insert(TABLE_NAME, null, contentValues);
        return response != -1;
    }

    public boolean addData(ItemHistory item){
        int logoId = item.getImage();
        String result = item.getResult(), date = item.getDate(), state = item.getState();
        return this.addData(logoId, result, date, state);
    }

    public int detele(ItemHistory item){
        int logoId = item.getImage(), remove = 0;
        String result = item.getResult(), date = item.getDate(), state = item.getState();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_remove, remove);
        String clauses = col_result + " = ? and " + col_date + " = ? and " + col_state + " = ?";
        int x = db.update(TABLE_NAME, contentValues, clauses,new String[]{result, date, state});
        return x!=-1?1:0;
    }

    public List<ItemHistory> getHistoryFromDB(){
        List<ItemHistory> response = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = (Cursor) db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + col_remove + " = 1", null);
        while(cursor.moveToNext()){
            String state = null;
            try {
                state = cursor.getString(4);
            } catch (Throwable e){
                state = "Default";
            }
            ItemHistory item = new ItemHistory(cursor.getInt(1), cursor.getString(3), cursor.getString(2), state);
            if(item.checkOk() == true) response.add(0, item);
        }
        cursor.close();
        return response;
    }
    private Boolean check(Filter filter, ItemHistory item){
        if(filter.getMode() == 1) return true;
        if(filter.getMode() == 2){
            return item.isState(filter.getState());
        }
        if(filter.getMode() == 3){
            return item.isTime(filter.getTime());
        }
        return true;
    }

    public List<ItemHistory> getHistorybyFilter(Filter filter){
        List<ItemHistory> response = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = (Cursor) db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + col_remove + " = 1", null);
        while(cursor.moveToNext()){
            Log.d(TAG, String.valueOf(cursor.getInt(5)));
            ItemHistory item = new ItemHistory(cursor.getInt(1), cursor.getString(3), cursor.getString(2), cursor.getString(4));
            if(item.checkOk() == true & check(filter, item)) response.add(0, item);
        }
        cursor.close();
        return response;
    }
}
