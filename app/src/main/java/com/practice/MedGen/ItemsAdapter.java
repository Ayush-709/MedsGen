package com.practice.MedGen;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.practice.MedGen.CartData.CartDatabaseHelper;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder>{
    Context context;
    ArrayList<Items> list;

    public ItemsAdapter(Context context, ArrayList<Items> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Items items = list.get(position);
        holder.name.setText(items.nonGeneric);
        holder.itemView.setOnClickListener(v -> dialogShow(items.getGeneric(), items.getNonGeneric(), items.getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medName);
        }
    }
    void dialogShow(String gName, String ngName, int id){
        final Dialog dialog = new Dialog(context);
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.detail_layout);
        TextView gShow = dialog.findViewById(R.id.showGeneric);
        TextView ngShow = dialog.findViewById(R.id.showNonGeneric);
        Button btn = dialog.findViewById(R.id.addMedToCartBtn);
        Button addMore = dialog.findViewById(R.id.addMoreMed);
        Button subtractMore = dialog.findViewById(R.id.removeMoreMed);
        LinearLayout container = dialog.findViewById(R.id.countButtonContainer);
        TextView medCount = dialog.findViewById(R.id.medCountIncart);

        if(cartDatabaseHelper.checkExist(id)){
            btn.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
            int count = cartDatabaseHelper.getMedCount(id);
            medCount.setText(String.valueOf(count));
        }
        btn.setOnClickListener(v->{
            cartDatabaseHelper.addMed(id, gName, ngName);
            btn.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
            int count = cartDatabaseHelper.getMedCount(id);
            medCount.setText(String.valueOf(count));
        });
        addMore.setOnClickListener(v->{
            cartDatabaseHelper.increaseMedCount(id,gName,ngName);
            int count = cartDatabaseHelper.getMedCount(id);
            medCount.setText(String.valueOf(count));
        });
        subtractMore.setOnClickListener(v->{
            int count = cartDatabaseHelper.getMedCount(id);
            if(count==1){
                cartDatabaseHelper.removeMedFromCart(id);
                container.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
            }else{
                cartDatabaseHelper.decreaseMedCount(id,gName,ngName);
                int medCount1 = cartDatabaseHelper.getMedCount(id);
                medCount.setText(String.valueOf(medCount1));
            }
        });

        gShow.setText(gName);
        ngShow.setText(ngName);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}
