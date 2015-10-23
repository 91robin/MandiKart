package com.affordablehomesindia.mandikart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {

    InnerHelper helper;
    String APP_DATABASE = "Mandikart.db";
    String TABLE_NAME = "shop_cart";
    String SEARCH_TABLE = "search";
    SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        helper = new InnerHelper(context, APP_DATABASE, null, 1);
    }

    public DatabaseHelper open() {
        database = helper.getWritableDatabase();
        return this;
    }

    public long insertData(String table, ContentValues values) {
        System.out.println("Inserted " + values.get("selected_size") + "  " + values.get("quantity"));
        return database.insert(table, null, values);
    }

    public Cursor getData(String table) {
        return database.query(table, null, null, null, null, null, null);
    }

    public Cursor getSelect(String id) {
        return database.rawQuery("select * from shop_cart where product_id =" + id + ";", null);
    }

    public void updateValue(int id, float quantity, float packsize, String size, String image, String brand, String name, String unit_name) {
        database.execSQL("update shop_cart set selected_size =" + packsize + ",quantity = " + quantity + ",pack_size = '"+size+"' where product_id = " + id + ";");
        database.execSQL("delete from " + TABLE_NAME + " where quantity=0;");
        database.execSQL("delete from " + TABLE_NAME + " where selected_size=0;");
    }

    public void deleteTable(String table){
        database.execSQL("DELETE FROM " + table +";");
    }

    public void close() {
        database.close();
    }

    public void deleteValue(String id) {
        database.execSQL("delete from " + TABLE_NAME + " where product_id=" + id + ";");
    }

    public void updateQuantity(Float quantity, String id) {
        database.execSQL("update shop_cart set quantity = "+quantity+" where product_id = "+id+";");
    }

    public class InnerHelper extends SQLiteOpenHelper {

        public InnerHelper(Context context, String name, CursorFactory factory,
                           int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("create table 'search'(id integer not null,name String not null,category_id string,sub_category String,type String not null,brand_name String);");
            db.execSQL("create table " + TABLE_NAME + "(product_id integer not null, selected_size real not null,unit_name String not null, quantity real not null,ammount integer not null,image String,brand String,name String, pack_size String);");
            db.execSQL("create table 'deliver_city'(name String not null,code String not null, position integer not null);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXITS "+SEARCH_TABLE);
        }

    }
}
