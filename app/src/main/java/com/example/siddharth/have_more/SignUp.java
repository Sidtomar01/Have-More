package com.example.siddharth.have_more;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
    MaterialEditText edtPhone,edtName,edtPassword,edtsecureCode;
    Button btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName= (MaterialEditText) findViewById(R.id.edtname);
        edtPhone= (MaterialEditText) findViewById(R.id.edtphone);
        edtPassword= (MaterialEditText) findViewById(R.id.edtPassword);
        edtsecureCode= (MaterialEditText) findViewById(R.id.edtsecureCode);
        btnsignup= (Button) findViewById(R.id.btnSignUP);
        //init firebase
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Comman.isConnectedtoInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Processing......");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check whether alredy present phone number
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                                mDialog.dismiss();
                                ;
                                Toast.makeText(SignUp.this, "Phone Number Already registered!!!", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString(),edtsecureCode.getText().toString());
                                table_user.child(edtPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Successfully registered!!!!", Toast.LENGTH_SHORT).show();
                                finish();


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(SignUp.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    return;

                }
            }
        });

    }
}
