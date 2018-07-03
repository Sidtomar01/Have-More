package com.example.siddharth.have_more.Remote;



import com.example.siddharth.have_more.Model.MyResponse;
import com.example.siddharth.have_more.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Siddharth on 24-12-2017.
 */

public interface APIservice {
    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAKzyXgKA:APA91bHJ8j1AR2OUAMi0hRKDV_8qpIA0nm_7l-Tb_17xanOuqcKCv3hSJZCc9mzLe8bFx0iKkOiGeq9XRyhyKqw8zi-QZ4oSQXJaWaBEp3qVBJURp6OMgG1NaHy14Wz2JmqycB_3sAB-"
            }
    )
    @POST("fcm/send")
Call<MyResponse> sendNotification(@Body Sender sender);

}
