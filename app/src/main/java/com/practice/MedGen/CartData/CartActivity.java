package com.practice.MedGen.CartData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import com.practice.MedGen.Items;
import com.practice.MedGen.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity  {
    RecyclerView rv;
    CartAdapter adapter;
    List<Items> list;
    CartDatabaseHelper cartDatabaseHelper;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Objects.requireNonNull(getSupportActionBar()).show();

        cartDatabaseHelper = new CartDatabaseHelper(this);
        btn = findViewById(R.id.convertToPDFBtn);

        rv = findViewById(R.id.cartRV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = storeDataInList();
        adapter = new CartAdapter(this, list);
        rv.setAdapter(adapter);
    }

    private List<Items> storeDataInList() {
        List<Items> itemsList = new ArrayList<>();
        Cursor cursor = cartDatabaseHelper.getAllMeds();
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                String gname = cursor.getString(1);
                String ngname = cursor.getString(2);
                int id = cursor.getInt(0);
                Items items = new Items(gname, ngname, id);
                itemsList.add(items);
            }
        }
        return itemsList;
    }

}