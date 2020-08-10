package com.gwu.studentservicesapp.buyPage;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.Item;

import java.util.ArrayList;
import java.util.List;

public class BuyActivity extends AppCompatActivity implements ItemAdapter.myClickListener {
    private RecyclerView recyclerView;
    private List<Item> itemsList = new ArrayList<>();
    private ItemAdapter adapter;

//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;
//    private  binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new ItemAdapter(itemsList,this,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void myClickListener(int positon){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Do you want to buy it?");
        dialog.setMessage(itemsList.get(positon).getProductName());
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
}
