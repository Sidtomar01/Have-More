package com.example.siddharth.have_more.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.siddharth.have_more.Interface.ItemClickListener;
import com.example.siddharth.have_more.R;

/**
 * Created by Siddharth on 16-12-2017.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName;
    public ImageView imageView,fav;
    private ItemClickListener itemClickListener;
    public MenuViewHolder(View itemView) {
        super(itemView);


        txtMenuName= (TextView) itemView.findViewById(R.id.menu_name);
        imageView= (ImageView) itemView.findViewById(R.id.menu_image);
        fav= (ImageView) itemView.findViewById(R.id.fav);
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
