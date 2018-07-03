package com.example.siddharth.have_more;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Siddharth on 17-12-2017.
 */

public class UserRegister {

    public static String CreateTable="CREATE TABLE `OrderDetail` (\n" +
            "\t`ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t`ProductId`\tTEXT,\n" +
            "\t`ProductName`\tTEXT,\n" +
            "\t`Quantity`\tTEXT,\n" +
            "\t`Price`\tTEXT\n" +
            ");";

    public static String ProductId="ProductId";
    public static String ProductName="ProductName";
    public static String Quantity="Quantity";
    public static String Price="Price";
    public static String user="OrderDetail";

    public static long insert(SQLiteDatabase sd, ContentValues cv) {
        return sd.insert(user, null, cv);


    }






}
