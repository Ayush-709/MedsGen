package com.practice.MedGen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    ArrayList<Items> list;
    ItemsAdapter adapter;
    RecyclerView rv;
    EditText input;
    ProgressDialog progressDialog;


    @Override
    @SuppressLint("NotifyDataSetChanged")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        input = findViewById(R.id.inputID);
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemsAdapter(this, list);
        EventChangeListener("");
        rv.setAdapter(adapter);

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
    }

    @SuppressLint("NotifyDataSetChanged")
    public void EventChangeListener(String num) {
        if (num.isEmpty()) {
                firestore.collection("medicine").orderBy("nonGeneric").addSnapshotListener((value, error) -> {
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
            firestore.collection("medicine").orderBy("nonGeneric").startAt(num).endAt(num+"\uf8ff").addSnapshotListener((value, error) -> {
                if(error!=null){
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("found", error.getMessage());
                    Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
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


}