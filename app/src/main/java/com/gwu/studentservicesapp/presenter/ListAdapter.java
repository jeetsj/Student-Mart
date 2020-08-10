package com.gwu.studentservicesapp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gwu.studentservicesapp.R;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> ID;
    ArrayList<String> Name;
    ArrayList<String> Description;
    ArrayList<String> Price;
    ArrayList<String> Location;
    ArrayList<String> Category;


    public ListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> name,
            ArrayList<String> description,
            ArrayList<String> price,
            ArrayList<String> location,
            ArrayList<String> Category
    )
    {

        this.context = context2;
        this.ID = id;
        this.Name = name;
        this.Description = description;
        this.Price = price;
        this.Location = location;
        this.Category = Category;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            child = layoutInflater.inflate(R.layout.item_individual_list, null);

            holder = new Holder();

            holder.ID_TextView =  child.findViewById(R.id.item_id);
            holder.Name_TextView =  child.findViewById(R.id.item_name);
            holder.Description_TextView =  child.findViewById(R.id.item_description);
            holder.Price_TextView = child.findViewById(R.id.item_price);
            holder.Location_TextView = child.findViewById(R.id.item_location);
            holder.Category_TextView = child.findViewById(R.id.item_cat);
            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.ID_TextView.setText(ID.get(position));
        holder.Name_TextView.setText(Name.get(position));
        holder.Description_TextView.setText(Description.get(position));
        holder.Price_TextView.setText(Price.get(position));
        holder.Location_TextView.setText(Location.get(position));
        holder.Category_TextView.setText(Category.get(position));
        return child;
    }

    public class Holder {

        TextView ID_TextView;
        TextView Name_TextView;
        TextView Description_TextView;
        TextView Price_TextView;
        TextView Location_TextView;
        TextView Category_TextView;
    }

}
