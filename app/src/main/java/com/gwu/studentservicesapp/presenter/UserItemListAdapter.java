package com.gwu.studentservicesapp.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.Item;

import java.util.ArrayList;


public class UserItemListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> ID;
    ArrayList<String> Name;
    ArrayList<String> Price;
    ArrayList<String> Category;
    ArrayList<byte[]> itemView;
    Item item;

    public class Holder {
        TextView Name_TextView,ID_TextView;
        TextView Price_TextView;
        TextView Category_TextView;
        ImageView ItemImage;
    }

    public UserItemListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> name,
            ArrayList<String> price,
            ArrayList<String> Category,
            ArrayList<byte[]> itemView
    )
    {

        this.context = context2;
        this.ID = id;
        this.Name = name;
        this.Price = price;
        this.Category = Category;
        this.itemView = itemView;
        item = new Item();
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return  ID.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View child, ViewGroup parent) {

        final UserItemListAdapter.Holder holder;
        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.item_listview_item_row, null);
            holder = new UserItemListAdapter.Holder();
            holder.Name_TextView =  child.findViewById(R.id.item_name);
            holder.Price_TextView = child.findViewById(R.id.item_price);
            holder.Category_TextView = child.findViewById(R.id.item_cat);
            holder.ItemImage = child.findViewById(R.id.list_image);
            child.setTag(holder);

        } else {
            holder = (UserItemListAdapter.Holder) child.getTag();
        }
        holder.Name_TextView.setText(Name.get(position));
        holder.Price_TextView.setText(Price.get(position));
        holder.Category_TextView.setText(Category.get(position));
        byte[] blob;
        blob = itemView.get(position);
        if(blob!=null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            holder.ItemImage.setImageBitmap(bmp);
        }
        else{
            holder.ItemImage.setImageResource(R.drawable.no_image_available);
        }
        return child;
    }
}
