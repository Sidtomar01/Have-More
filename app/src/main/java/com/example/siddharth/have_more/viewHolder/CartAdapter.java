package com.example.siddharth.have_more.viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Interface.ItemClickListener;
import com.example.siddharth.have_more.Model.Order;
import com.example.siddharth.have_more.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Siddharth on 16-12-2017.
 */

class  CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView txt_cart_name;
    public TextView txt_price;
    public ImageView image_cart_count;


    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name= (TextView) itemView.findViewById(R.id.cart_item_name);
        txt_price= (TextView) itemView.findViewById(R.id.cart_item_price);
        image_cart_count= (ImageView) itemView.findViewById(R.id.cart_item_count);
            itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select Action");
        menu.add(0,0,getAdapterPosition(), Comman.DELETE);

    }
}


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listdata=new ArrayList<>();

    private Context context;

    public CartAdapter(List<Order> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable=TextDrawable.builder().buildRound(""+listdata.get(position).getQuantity(), Color.RED);
        holder.image_cart_count.setImageDrawable(drawable);



        Locale locale =new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listdata.get(position).getPrice()))*(Integer.parseInt(listdata.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listdata.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}
