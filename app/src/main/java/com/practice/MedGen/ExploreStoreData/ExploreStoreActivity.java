package com.practice.MedGen.ExploreStoreData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.practice.MedGen.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExploreStoreActivity extends AppCompatActivity {
    Spinner stateSpinner;
    List<String > stateList;
    StoreDatabaseHelper databaseHelper;
    RecyclerView rv;
    StoreAdapter storeAdapter;
    List<StoreItems> storeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_store);
        Objects.requireNonNull(getSupportActionBar()).hide();

        stateSpinner = findViewById(R.id.stateInputSpinner);
        databaseHelper = new StoreDatabaseHelper(this);
        stateList = new ArrayList<>();
        rv = findViewById(R.id.storeRV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        storeList = new ArrayList<>();
        storeAdapter = new StoreAdapter(this,storeList);

        Cursor cursor= databaseHelper.fetchState();
        if(cursor.getCount()!=0) {
            while (cursor.moveToNext()) {
                String s = cursor.getString(0);
                stateList.add(s);
            }
        }

        stateList.add(0,"All");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,stateList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(arrayAdapter);
        EventChangeListener("All");
        rv.setAdapter(storeAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EventChangeListener(stateSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void EventChangeListener(String s) {
        Cursor cursor;
        if(s.equals("All")){
            cursor = databaseHelper.fetchAll();
        }else{
            s= "'"+s+ "'";
            cursor = databaseHelper.fetchAllByState(s);
        }
        if(cursor.getCount()!=0){
            storeList.clear();
            while (cursor.moveToNext()){
                StoreItems items = new StoreItems();
                items.setStore_code(cursor.getString(0));
                items.setAddress(cursor.getString(1));
                items.setDistrict(cursor.getString(2));
                items.setState(cursor.getString(3));
                items.setPin_code(cursor.getString(4));
                storeList.add(items);
            }
        }
        storeAdapter.notifyDataSetChanged();
        rv.setAdapter(storeAdapter);
    }
}