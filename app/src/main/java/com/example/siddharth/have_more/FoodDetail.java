package com.example.siddharth.have_more;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Model.Category;
import com.example.siddharth.have_more.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class FoodDetail extends AppCompatActivity {

    TextView food_name,food_price;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btncart;
    ElegantNumberButton numberButton;
     String foodId="";
    FirebaseDatabase database;
    DatabaseReference Category;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //firebase
        database = FirebaseDatabase.getInstance();
        Category = database.getReference("Category");

        //initView

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btncart = (FloatingActionButton) findViewById(R.id.btncart);


        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Mydatabase(getBaseContext()).addToCart(new Order(
                        foodId,category.getName(),numberButton.getNumber(),
                        category.getPrice()

                ));

                Toast.makeText(FoodDetail.this, "ADDED TO CART", Toast.LENGTH_SHORT).show();
            }
        });




        food_name = (TextView) findViewById(R.id.food_name);
        food_image = (ImageView) findViewById(R.id.img_food);
        food_price = (TextView) findViewById(R.id.food_price);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        //getfood id from intent

        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
            if (!foodId.isEmpty()) {
                if(Comman.isConnectedtoInternet(getBaseContext())) {
                    getDetailFood(foodId);
                }
                else {
                    Toast.makeText(FoodDetail.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

        }
    }

        private void getDetailFood(String foodId)
    {
        Category.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                category=dataSnapshot.getValue(com.example.siddharth.have_more.Model.Category.class);
                ///imgse set
                Picasso.with(getBaseContext()).load(category.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(category.getName());
                food_price.setText(category.getPrice());
                food_name.setText(category.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    }

