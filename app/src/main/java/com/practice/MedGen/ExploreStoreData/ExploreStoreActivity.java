package com.practice.MedGen.ExploreStoreData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.MedGen.CartData.CartDatabaseHelper;
import com.practice.MedGen.R;

import org.spongycastle.util.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ExploreStoreActivity extends AppCompatActivity {
    FirebaseDatabase database;
    List<StoreItems> storeItemsList;
    Spinner stateSpinner;
    ProgressDialog progressDialog;
    RecyclerView rv;
    StoreAdapter storeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_store);

        database = FirebaseDatabase.getInstance();
        storeItemsList = new ArrayList<>();
        stateSpinner = findViewById(R.id.stateInputSpinner);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        rv =findViewById(R.id.storeRV);
        rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        // Fetch the data and populate the list
        fetchStoreItemsList(storeItemsList -> {
            // Now storeItemsList has data and can be used in the activity

            HashSet<String > state = new HashSet<>();
            for(int i=0;i<storeItemsList.size();i++){
                state.add(storeItemsList.get(i).getState());
            }
            List<String > list = new ArrayList<>(state);
            Collections.sort(list);
            list.add(0,"All");

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            stateSpinner.setAdapter(adapter);

            storeAdapter = new StoreAdapter(getApplicationContext(),storeItemsList);
            rv.setAdapter(storeAdapter);
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = stateSpinner.getSelectedItem().toString();
                System.out.println(val);
                EventChangeListener(val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                EventChangeListener("");
            }
        });


    }

    private void EventChangeListener(String s) {
        List<StoreItems> list = new ArrayList<>();
        if (s.equals("All")){
            return;
        }
        for(StoreItems i : storeItemsList){
            if(i.getState().equals(s)){
                list.add(i);
            }
        }
        storeAdapter = new StoreAdapter(getApplicationContext(), list);
        rv.setAdapter(storeAdapter);

    }

    private void fetchStoreItemsList(StoreItemsListCallback callback) {
        DatabaseReference reference = database.getReference("data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        String address = String.valueOf(snap.child("Address").getValue());
                        String contactNo = String.valueOf(snap.child("Contact No").getValue());
                        String person = String.valueOf(snap.child("Contact Person").getValue());
                        String district = String.valueOf(snap.child("District").getValue());
                        String pinCode = String.valueOf(snap.child("Pin Code").getValue());
                        String state = String.valueOf(snap.child("State").getValue());
                        String storeCode = String.valueOf(snap.child("Store Code").getValue());
                        StoreItems items = new StoreItems(address, person, district, state, storeCode, contactNo, pinCode);
                        storeItemsList.add(items);
                    }
                    callback.onStoreItemsListFetched(storeItemsList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("dataFetchFailure", error.getMessage());
                if(progressDialog.isShowing())
                    progressDialog.dismiss();

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, Collections.singletonList("Error Occurred"));
                stateSpinner.setAdapter(adapter);
            }
        });
    }

    interface StoreItemsListCallback {
        void onStoreItemsListFetched(List<StoreItems> storeItemsList);
    }
}