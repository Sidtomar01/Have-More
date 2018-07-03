package com.example.siddharth.have_more.Service;

import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Siddharth on 24-12-2017.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String TokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        super.onTokenRefresh();
        if(Comman.currentUser!=null)
            updateTokenFirebase(TokenRefreshed);
    }

    private void updateTokenFirebase(String TokenRefreshed) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Tokens");
        Token token=new Token(TokenRefreshed,false);
        tokens.child(Comman.currentUser.getPhone()).setValue(token);


    }
}
