package com.practice.MedGen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.MedGen.ExploreStoreData.StoreDatabaseHelper;
import com.practice.MedGen.ExploreStoreData.StoreItems;
import com.practice.MedGen.utility.NetworkChangeListener;


@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    StoreDatabaseHelper storeDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        storeDatabaseHelper =new StoreDatabaseHelper(this);
        storeDatabaseHelper.dropTable();

        addStoreIntoDataBase();


        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            Intent i = new Intent(SplashScreen.this, HomeActivity.class);

            startActivity(i);
            finish();
        }, 2000);
    }

    private void addStoreIntoDataBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                        storeDatabaseHelper.addStoreItems(items);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}