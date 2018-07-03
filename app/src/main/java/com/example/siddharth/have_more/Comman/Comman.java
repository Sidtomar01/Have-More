package com.example.siddharth.have_more.Comman;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.siddharth.have_more.Model.User;
import com.example.siddharth.have_more.Remote.APIservice;
import com.example.siddharth.have_more.Remote.RetrofitClient;

import retrofit2.Retrofit;

/**
 * Created by Siddharth on 16-12-2017.
 */

public class Comman {
    public static final String BASE_URL="http://fcm.googleapis.com/";
    public static APIservice getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIservice.class);
    }

    public static User currentUser;
    public static final String DELETE ="Delete";
    public static final String USER_KEY ="User";
    public static final String PWD_KEY ="Password";
    public static String convertCodeToStatus(String status) {

        if(status.equals("0"))
        {
            return "Placed";
        }
        else if (status.equals("1"))
        {
            return "On The Way";
        }
        else
            return "Shipped";
    }
public static boolean isConnectedtoInternet(Context context)
{
    ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager!=null)
    {
        NetworkInfo[] info=connectivityManager.getAllNetworkInfo();
        if(info!=null)
        {
            for (int i=0;i<info.length;i++)
            {
                if (info[i].getState()==NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
    }
    return false;
}


}
