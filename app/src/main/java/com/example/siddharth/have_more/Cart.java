package com.example.siddharth.have_more;

import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Model.MyResponse;
import com.example.siddharth.have_more.Model.Notification;
import com.example.siddharth.have_more.Model.Order;
import com.example.siddharth.have_more.Model.Request;
import com.example.siddharth.have_more.Model.Sender;
import com.example.siddharth.have_more.Model.Token;
import com.example.siddharth.have_more.Remote.APIservice;
import com.example.siddharth.have_more.viewHolder.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    TextView txtTotalPrice;
    FButton btnPlace;
    List<Order> cart=new ArrayList<>();
    CartAdapter adapter;
    APIservice mservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //init service
        mservice=Comman.getFCMService();


        //firebase

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");


        //init
        recyclerView= (RecyclerView) findViewById(R.id.listcart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice= (TextView) findViewById(R.id.total);
        btnPlace= (FButton) findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (cart.size()>0)
                showAlertDialog();
                else
            {
                Toast.makeText(Cart.this, "Your Cart Is Empty  !!!!   :(", Toast.LENGTH_SHORT).show();
            }
            }


        });
        loadlistfood();

    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more Step");
        alertDialog.setMessage("Enter your address: ");

        final EditText  edtAddress=new EditText(Cart.this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request=new Request(
                        Comman.currentUser.getName(),
                        Comman.currentUser.getPhone(),
                        edtAddress.getText().toString(),
                       txtTotalPrice.getText().toString(),
                        cart
                );

                //submit to firebase
                //in this we use System.CurrentMilli to key
                String order_number=String.valueOf(System.currentTimeMillis());
                requests.child(order_number).setValue(request);
                //clean cart

                new Mydatabase(getBaseContext()).CleanCart();
                sendNotification(order_number);


                Toast.makeText(Cart.this, "Thank You !!! Order Placed   :D", Toast.LENGTH_SHORT).show();
               finish();
            }
        });
          alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
              }
          });
           alertDialog.show();

    }

    private void sendNotification(final String order_number) {

        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query data=tokens.orderByChild("isServerToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
           for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
           {

               Token ServerToken=postSnapshot.getValue(Token.class);
               Notification notification=new Notification("COM EXAMPLE Siddharth"+order_number);
               Sender content=new Sender(ServerToken.getToken(),notification);
               mservice.sendNotification(content).enqueue(new Callback<MyResponse>() {
                   @Override
                   public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                       if(response.code()==200) {
                           if (response.body().success == 1) {
                               Toast.makeText(Cart.this, "Thank You !!! Order Placed   :D", Toast.LENGTH_SHORT).show();
                             finish();
                           } else {
                               Toast.makeText(Cart.this, "Failed", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }

                   @Override
                   public void onFailure(Call<MyResponse> call, Throwable t) {
                       Log.e("Error",t.getMessage());
                   }
               });

           }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void loadlistfood() {

        cart =new Mydatabase(Cart.this).getCarts();

        adapter=new CartAdapter(cart,Cart.this);

        recyclerView.setAdapter(adapter);



        float total=0;
        for(Order order:cart) {
           total+=((Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity())));
            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            txtTotalPrice.setText(fmt.format(total));

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Comman.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new Mydatabase(this).CleanCart();
        for (Order item:cart)
        {
            new Mydatabase(this).addToCart(item);
            adapter.notifyDataSetChanged();

            loadlistfood();

        }

    }
}
