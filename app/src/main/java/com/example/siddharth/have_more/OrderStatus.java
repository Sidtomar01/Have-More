package com.example.siddharth.have_more;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Model.Request;
import com.example.siddharth.have_more.viewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.siddharth.have_more.Comman.Comman.convertCodeToStatus;

public class OrderStatus extends AppCompatActivity {
     public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView= (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
       // if (getIntent()==null)
         //   if(Comman.isConnectedtoInternet(getBaseContext())) {
                loadOrder(Comman.currentUser.getPhone());
          //  }
         //   else {
              //  Toast.makeText(OrderStatus.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            //    return;
            }
       // else
          //  loadOrder(getIntent().getStringExtra("userPhone"));


   // }

    private void loadOrder(String phone) {
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)

        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Comman.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());



            }
        };
        recyclerView.setAdapter(adapter);

    }


}
