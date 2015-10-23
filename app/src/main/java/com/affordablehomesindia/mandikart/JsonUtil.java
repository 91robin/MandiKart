package com.affordablehomesindia.mandikart;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

    String device = "device";
    String id="id";
    String name="name";
    String type= "type";
    String token = "token";
    String device_token;
    String device_id;
    String search_id;
    String search_name;
    String search_type;

    public  String makeJson(String id) {

        device_id = id;
        JSONObject object = new JSONObject();
        try {
            object.put(device, device_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }


    public String makeJson(String _id, String _token, String type_id, String name, String type) {

        device_id = _id;
        device_token = _token;
        search_id=type_id;
        search_name=name;
        search_type=type;

        JSONObject object = new JSONObject();
        JSONObject child = new JSONObject();
        try {
            object.put(device, device_id);
            object.put(token, device_token);
            child.put(id,search_id);
            child.put(this.name,search_name);
            child.put(this.type,search_type);
            object.put("search_field",child);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String makeCheapestProductJson(String id, String token){
        device_id = id;
        device_token = token;
        JSONObject object = new JSONObject();
        try {
            object.put(device, device_id);
            object.put(token, device_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }

}
