package com.gwu.studentservicesapp.buyPage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.Item;

import java.util.ArrayList;
import java.util.List;

public class cartFragment extends Fragment implements ItemAdapter.myClickListener {
    protected RecyclerView recyclerView;
    protected ItemAdapter itemAdapter;
    protected List<Item> items = new ArrayList<>();
    protected cart cart;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        cart = ((BuyPage)getActivity()).getCart();
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

    @Override
    public void myClickListener(final int positon){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
        dialog.setTitle("Do you want to cancel it?");
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
                cart.delete(positon);
            }
        });
        dialog.show();
    }
}
