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

public class ApartmentListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> ID;
    ArrayList<String> ApartmentDescription;
    ArrayList<String> ApartmentRent;
    ArrayList<String> ApartmentLocation;
    ArrayList<byte[]> itemView;

    public class Holder {
        TextView aptDesc_TextView;
        TextView aptprice_textview;
        TextView aptlocation_textview;
        ImageView ItemImage;
    }

    public ApartmentListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> ApartmentDescription,
            ArrayList<String> ApartmentRent,
            ArrayList<String> ApartmentLocation,
            ArrayList<byte[]> itemView
    )
    {

        this.context = context2;
        this.ID = id;
        this.ApartmentDescription = ApartmentDescription;
        this.ApartmentRent = ApartmentRent;
        this.ApartmentLocation = ApartmentLocation;
        this.itemView = itemView;
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

        final ApartmentListAdapter.Holder holder;
        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.apartment_listview_item_row, null);
            holder = new ApartmentListAdapter.Holder();
            holder.aptDesc_TextView =  child.findViewById(R.id.apt_description);
            holder.aptprice_textview = child.findViewById(R.id.apt_rent);
            holder.aptlocation_textview = child.findViewById(R.id.apt_location);
            holder.ItemImage = child.findViewById(R.id.list_image);
            child.setTag(holder);

        } else {
            holder = (ApartmentListAdapter.Holder) child.getTag();
        }
        holder.aptDesc_TextView.setText(ApartmentDescription.get(position));
        holder.aptprice_textview.setText(ApartmentRent.get(position));
        holder.aptlocation_textview.setText(ApartmentLocation.get(position));
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
