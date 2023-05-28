package com.practice.MedGen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.practice.MedGen.CartData.CartActivity;
import com.practice.MedGen.utility.NetworkChangeListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    ArrayList<Items> list;
    ItemsAdapter adapter;
    RecyclerView rv;
    EditText input;
    ProgressDialog progressDialog;

    ImageView cart;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    @SuppressLint("NotifyDataSetChanged")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Progress Dialog initializing and enabling
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        input = findViewById(R.id.inputID);
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemsAdapter(this, list);
        EventChangeListener("");
        rv.setAdapter(adapter);

        //Text Watcher on searchbar
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                EventChangeListener(s.toString());
                rv.setAdapter(adapter);
            }
        });

        //Cart open
        cart = findViewById(R.id.cart);
        cart.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CartActivity.class)));


    }

    //Updating Recyclerview based on search text
    @SuppressLint("NotifyDataSetChanged")
    public void EventChangeListener(String num) {
        if (num.isEmpty()) {
                firestore.collection("AllMed").orderBy("nonGeneric").addSnapshotListener((value, error) -> {
                    if(error!=null){
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("found", error.getMessage());
                        return;
                    }
                    assert value != null;
                        for(DocumentChange dc: value.getDocumentChanges()){
                            list.add(dc.getDocument().toObject(Items.class));
                        }
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                });
        }else{
            firestore.collection("AllMed").orderBy("nonGeneric").startAt(num).endAt(num+"\uf8ff").addSnapshotListener((value, error) -> {
                if(error!=null){
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("foundError", error.getMessage());
                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert value != null;
                for(DocumentChange dc: value.getDocumentChanges()){
                    list.add(dc.getDocument().toObject(Items.class));
                }
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            });
        }

    }


    //Internet connection check
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