package com.gwu.studentservicesapp.buyPage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.databinding.ItemLayoutBinding;
import com.gwu.studentservicesapp.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{
    private List<Item> mData;
    private Context context;
    private myClickListener myClickListener;

    public ItemAdapter(List<Item> itemsList,Context context, myClickListener myClickListener) {
        mData = itemsList;
        this.context = context;
        this.myClickListener = myClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View itemView;
        protected Item thisItem;
        ItemLayoutBinding binding;
        myClickListener myClickListener;

        public MyViewHolder(@NonNull ItemLayoutBinding binding,myClickListener myClickListener){
            super(binding.getRoot());
            itemView = binding.getRoot();
            this.binding = binding;
            this.myClickListener = myClickListener;
            itemView.setOnClickListener(this);
        }

//        public void bindData(items thisItem){
//            binding.setItemData(thisItem);
//            binding.executePendingBindings();
//        }

        @Override
        public void onClick(View view){
            myClickListener.myClickListener(getAdapterPosition());
        }
    }

    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       ItemLayoutBinding itemBinding = DataBindingUtil.inflate(layoutInflater,R.layout.item_layout, parent, false);
       MyViewHolder VH = new MyViewHolder(itemBinding,myClickListener);
       return VH;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Item thisItem = mData.get(position);
        holder.binding.setItemData(thisItem);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface myClickListener{
        void myClickListener(int position);
    }

    @BindingAdapter("bind:imageBitmap")
    public static void loadImage(ImageView iv, Bitmap bitmap) {
        iv.setImageBitmap(bitmap);
    }
}
