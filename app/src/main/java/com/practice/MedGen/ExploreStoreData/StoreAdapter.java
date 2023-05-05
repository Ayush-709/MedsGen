package com.practice.MedGen.ExploreStoreData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.MedGen.R;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    Context context;
    List<StoreItems> list;

    public StoreAdapter(Context context, List<StoreItems> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StoreAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.store_item_layout,parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.StoreViewHolder holder, int position) {
        StoreItems items = list.get(position);
        holder.address.setText(items.getAddress());
        holder.storeCode.setText(items.getStore_code());
        String district = items.getDistrict();
        String state = items.getState();
        String pin = items.getPin_code();
        String locale = district+", "+state+" - "+pin;
        holder.location.setText(locale);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView address, storeCode, location;
        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.showAddress);
            storeCode = itemView.findViewById(R.id.showStoreCode);
            location = itemView.findViewById(R.id.showDistrictPinCodeState);
        }
    }
}
