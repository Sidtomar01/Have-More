package com.example.siddharth.have_more;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.siddharth.have_more.Model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siddharth on 17-12-2017.
 */

public class Mydatabase extends SQLiteOpenHelper {
    public static String MYDATABASE="sqlite.db";
    public static int VERSION=1;
    public Mydatabase(Context context) {
        super(context, MYDATABASE,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserRegister.CreateTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        String[] sqlSelect = {"ProductId", "ProductName", "Quantity", "Price"};
        String sqlTable = "OrderDetail";
        qb.setTables(sqlTable);

        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price"))
                ));
            }
            while (c.moveToNext());
        }
        return result;
    }
    public void addToCart(Order order)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price) VALUES ('%s','%s','%s','%s');"
                ,order.getProductId(),order.getProductName(),order.getQuantity(),order.getPrice());

        db.execSQL(query);

    }



    public void CleanCart()
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM OrderDetail");
        db.execSQL(query);

    }


}
