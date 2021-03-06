package com.example.siddharth.have_more;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.example.siddharth.have_more.R.id.edtPassword;
import static com.example.siddharth.have_more.R.id.edtphone;

public class MainActivity extends AppCompatActivity {
    Button btnsignup,btnsignin;
    TextView Slogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Slogan= (TextView) findViewById(R.id.txtSlogan);
        btnsignin= (Button) findViewById(R.id.btnSignIn);
        btnsignup= (Button) findViewById(R.id.btnSignUp);
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");
        Slogan.setTypeface(face);


        //INIT PAPAER
        Paper.init(this);





        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

            }
        });
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
            }
        });

        //Check remember
        String user=Paper.book().read(Comman.USER_KEY);
        String pwd=Paper.book().read(Comman.PWD_KEY);
        if(user!=null&&pwd!=null)
        {
            if(!user.isEmpty()&&!pwd.isEmpty())
            {

                login(user,pwd);
            }
        }
    }

    private void login(final String phone, final String pwd) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
        if (Comman.isConnectedtoInternet(getBaseContext())) {
            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Processing......");
            mDialog.show();
            ;


            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //check if user does not present in the database
                    if (dataSnapshot.child(phone).exists()) {

                        //get user information
                        mDialog.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);

                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd)) {

                            Intent ht = new Intent(MainActivity.this, Home.class);
                            Comman.currentUser = user;

                            startActivity(ht);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "WRONG PASSWORD!!!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "User does not exist!!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }
        else
        {
            Toast.makeText(MainActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
