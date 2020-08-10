package com.gwu.studentservicesapp.buyPage;

import com.gwu.studentservicesapp.model.Item;

import java.util.ArrayList;
import java.util.List;

public class cart {
    private List<Item> carts= new ArrayList<>();


    public cart(){

    }

    public boolean add(Item item){
        for(int i=0;i<carts.size();i++){
            if(carts.get(i).getProductId()==item.getProductId()){
                return false;
            }
        }
        carts.add(item);
        return true;
    }

    public void delete(int id){
        for(int i=0;i<carts.size();i++){
            if(carts.get(i).getProductId()==id){
                carts.remove(i);
            }
        }
    }
}
