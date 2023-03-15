package com.practice.MedGen.CartData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.MedGen.Items;
import com.practice.MedGen.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    List<Items> list;
    CartDatabaseHelper db;

    public CartAdapter(Context context, List<Items> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = new CartDatabaseHelper(context);
        View v = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewHolder(v);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.genName.setText(list.get(position).getGeneric());
        holder.nonGenName.setText(list.get(position).getNonGeneric());
        holder.medsCount.setText(String.valueOf(db.getMedCount(list.get(position).getId())));
        holder.removeBtn.setOnClickListener(v->{
            int count = db.getMedCount(list.get(position).getId());
            if(count==1){
                db.removeMedFromCart(list.get(position).getId());
                list.remove(position);
                notifyDataSetChanged();

            }else {
                db.decreaseMedCount(list.get(position).getId(),list.get(position).getGeneric(),list.get(position).getNonGeneric());
                holder.medsCount.setText(String.valueOf(db.getMedCount(list.get(position).getId())));
            }
        });
        try {
            holder.addBtn.setOnClickListener(view -> {
                db.increaseMedCount(list.get(position).getId(),list.get(position).getGeneric(),list.get(position).getNonGeneric());
                holder.medsCount.setText(String.valueOf(db.getMedCount(list.get(position).getId())));
            });
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView genName, nonGenName, medsCount;
        Button addBtn, removeBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            genName = itemView.findViewById(R.id.cartGenericMed);
            nonGenName = itemView.findViewById(R.id.cartNonGenericMed);
            medsCount = itemView.findViewById(R.id.cartMedsCount);
            addBtn = itemView.findViewById(R.id.cartIncreaseMeds);
            removeBtn = itemView.findViewById(R.id.cartDecreaseMeds);
        }
    }
}
