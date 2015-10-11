package com.affordablehomesindia.mandikart;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

public class AndroidId extends Application{

    String id;

    public AndroidId(Context context) {
        id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public String getId(){
        return id;
    }
}
