package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetOrderJson {

    DatabaseHelper databaseHelper;
    Cursor cursor;
    Context context;
    SharedPreferences pref_personal;
    SharedPreferences pref;
    SharedPreferences.Editor editor_peronal;
    SharedPreferences.Editor editor;
    String phn;
    String phn_screen;
    String token;
    String user_name;
    String landmark;
    String city;
    String address;
    String firm_name;
    String delivery_mode;
    String payment_option;
    String firm_tin;
    String email_id;


    public GetOrderJson(Context context){
        databaseHelper= new DatabaseHelper(context);
        databaseHelper.open();
        pref_personal= context.getSharedPreferences("PERSONAL_DETAIL",Context.MODE_PRIVATE);
        pref = context.getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
        editor_peronal= pref_personal.edit();

        token = pref.getString("token", "");

        phn= pref_personal.getString("phone_no", "");
        phn_screen= pref.getString("phn_screen", "");
        user_name = pref_personal.getString("user_name", "");
        landmark= pref_personal.getString("landmark", "");
        city=pref_personal.getString("city", "");
        address= pref_personal.getString("address","");
        firm_name= pref_personal.getString("firm_name","");
        delivery_mode = pref_personal.getString("delivery_mode","");
        payment_option =pref_personal.getString("payment_option","");
        firm_tin= pref_personal.getString("firm_tin","");
        email_id = pref_personal.getString("email_id","");

    }

    public String makeJson() throws JSONException {
        String JSONOutPut= null;

        JSONObject mainObject = new JSONObject();
        JSONObject customerObject = new JSONObject();
        JSONArray cartArray= new JSONArray();

        mainObject.put("phone_no",phn_screen);
        mainObject.put("token",token);

        customerObject.put("name",user_name);
        customerObject.put("phone_no",phn);
        customerObject.put("city",city);
        customerObject.put("address",address);
        customerObject.put("landmark",landmark);
        customerObject.put("delivery_mode",delivery_mode);
        customerObject.put("payment_option",payment_option);
        customerObject.put("firm_name",firm_name);
        customerObject.put("firm_tin",firm_tin);
        customerObject.put("email_id",email_id);

        mainObject.put("customer",customerObject);

        cursor= databaseHelper.getData("shop_cart");
        cursor.moveToFirst();
        for(int i=0; i < cursor.getCount();i++) {
            cursor.moveToPosition(i);
            JSONObject cartObject = new JSONObject();
            cartObject.put("product_id", cursor.getString(cursor.getColumnIndex("product_id")));
            cartObject.put("size", cursor.getString(cursor.getColumnIndex("pack_size")));
            cartObject.put("quantity", cursor.getString(cursor.getColumnIndex("quantity")));
            cartArray.put(cartObject);
        }

        mainObject.put("cart",cartArray);
        JSONOutPut =mainObject.toString();
        return  JSONOutPut;
    }
}
