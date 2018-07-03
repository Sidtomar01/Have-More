package com.example.siddharth.have_more;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
    EditText edtphone,edtPassword;
    Button btnSignIn;
    CheckBox cbkRemember;
    TextView txtForgotPwd;
    FirebaseDatabase database;
     DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);
        edtphone=(MaterialEditText)findViewById(R.id.edtphone);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        cbkRemember= (CheckBox) findViewById(R.id.ckbRemember);
        txtForgotPwd= (TextView) findViewById(R.id.txtforgotpasswd);
        //init paper
        Paper.init(this);

        //init firebase
        database=FirebaseDatabase.getInstance();
        table_user=database.getReference("User");


        txtForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgetDialog();
            }
        });








        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //save User Name and Password


            if(cbkRemember.isChecked())
            {
                Paper.book().write(Comman.USER_KEY,edtphone.getText().toString());
                Paper.book().write(Comman.PWD_KEY,edtPassword.getText().toString());
            }



                if (Comman.isConnectedtoInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Processing......");
                    mDialog.show();
                    ;


                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if user does not present in the database
                            if (dataSnapshot.child(edtphone.getText().toString()).exists()) {

                                //get user information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtphone.getText().toString()).getValue(User.class);

                                user.setPhone(edtphone.getText().toString());
                                if (user.getPassword().equals(edtPassword.getText().toString())) {

                                    Intent ht = new Intent(SignIn.this, Home.class);
                                    Comman.currentUser = user;

                                    startActivity(ht);
                                    finish();
                                    table_user.removeEventListener(this);

                                } else {
                                    Toast.makeText(SignIn.this, "WRONG PASSWORD!!!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "User does not exist!!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });
                }
                else
                {
                    Toast.makeText(SignIn.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void showForgetDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("FORGET PASSWORD");
        builder.setMessage("Enter your Secure-Code");
        LayoutInflater inflater=this.getLayoutInflater();
        View view =inflater.inflate(R.layout.forget_passwd_layout,null);
        builder.setView(view);
        builder.setIcon(R.drawable.ic_security_black_24dp);
        final MaterialEditText edtphone= (MaterialEditText) view.findViewById(R.id.edtphone);
        final MaterialEditText securecode= (MaterialEditText) view.findViewById(R.id.edtsecureCode);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //check user is present
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.child(edtphone.getText().toString()).getValue(User.class);
                        if(user.getSecureCode().equals(securecode.getText().toString()))
                        {
                            Toast.makeText(SignIn.this, "Your Password is:-"+user.getPassword(), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(SignIn.this, "Wrong Secure Code !!   :(", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();



    }
}
