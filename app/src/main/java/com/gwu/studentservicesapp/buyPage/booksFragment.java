package com.gwu.studentservicesapp.buyPage;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.Item;
import com.gwu.studentservicesapp.model.db.ItemDB;
import com.gwu.studentservicesapp.view.SellPageActivity;

import java.util.ArrayList;
import java.util.List;

public class booksFragment extends Fragment implements ItemAdapter.myClickListener {
    protected RecyclerView recyclerView;
    protected ItemAdapter itemAdapter;
    protected List<Item> items = new ArrayList<>();
    protected cart cart;
    ItemDB itemDB;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        onAttach(getActivity());
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.recycler_view,container,false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        itemAdapter = new ItemAdapter(items,getActivity(),this);
        recyclerView.setAdapter(itemAdapter);
        return rootView;

    }

    public void getData(){
        itemDB = new ItemDB(getActivity());
        items = itemDB.getItems("Books");
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        cart = ((BuyPage)activity).getCart();
    }

    @Override
    public void myClickListener(final int positon){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
        dialog.setTitle("Do you want to buy it?");
        dialog.setMessage(items.get(positon).getProductName()+"\n");
        dialog.setMessage("Location:"+items.get(positon).getProductLocation()+"\n");
        dialog.setMessage("Description:"+items.get(positon).getProductDescription());
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cart.add(items.get(positon));
            }
        });
        dialog.show();
    }

}
