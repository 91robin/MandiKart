package com.mandikart.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {

    Context context;
    public NetworkConnection(Context context){
        this.context=context;
    }

    Boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null) {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if(networkInfo!=null){
                for(int i=0; i< networkInfo.length;i++){
                    if(networkInfo[i].getState()== NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
