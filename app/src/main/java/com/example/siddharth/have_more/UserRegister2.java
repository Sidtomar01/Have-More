package com.example.siddharth.have_more;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Siddharth on 25-12-2017.
 */

public class UserRegister2 {
    public static String Create_Table="CREATE TABLE `Favourites` (\n" +
            "\t`FoodId`\tTEXT UNIQUE,\n" +
            "\tPRIMARY KEY(`FoodId`)\n" +
            ");";
    public static String FoodId="FoodId";
    public static String user="Favourites";


    public static long insert(SQLiteDatabase sd, ContentValues cv) {
        return sd.insert(user, null, cv);


    }

    }
