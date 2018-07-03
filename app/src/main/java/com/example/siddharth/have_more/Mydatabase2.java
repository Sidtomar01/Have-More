package com.example.siddharth.have_more;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Siddharth on 25-12-2017.
 */

public class Mydatabase2 extends SQLiteOpenHelper {
    public static String MYDATABASE="FAV.db";
    public static int VERSION=1;
    public Mydatabase2(Context context) {
        super(context, MYDATABASE,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserRegister2.Create_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addtoFavourite(String foodId)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("INSERT INTO Favourites(FoodId) VALUES ('%s')",foodId);
        db.execSQL(query);
    }
    public void removeFromFavourite(String foodId)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM Favourites WHERE FoodId='%s'",foodId);
        db.execSQL(query);
    }
    public boolean isFavourite(String foodId)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("SELECT * FROM Favourites WHERE FoodId='%s'",foodId);
        Cursor cursor=db.rawQuery(query,null);
        if (cursor.getCount() <=0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
