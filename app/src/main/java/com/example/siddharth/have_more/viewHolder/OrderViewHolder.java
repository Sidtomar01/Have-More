package com.example.siddharth.have_more.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.siddharth.have_more.Interface.ItemClickListener;
import com.example.siddharth.have_more.R;

/**
 * Created by Siddharth on 17-12-2017.
 */

public class OrderViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtOrderId ,txtOrderStatus,txtOrderPhone,txtOrderAddress;

    private ItemClickListener itemClickListener;


    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddress= (TextView) itemView.findViewById(R.id.order_address);
        txtOrderId= (TextView) itemView.findViewById(R.id.order_id);
        txtOrderPhone= (TextView) itemView.findViewById(R.id.order_phone);
        txtOrderStatus= (TextView) itemView.findViewById(R.id.order_status);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
