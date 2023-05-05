package com.practice.MedGen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.practice.MedGen.ExploreStoreData.ExploreStoreActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CardView searchMedicineButton = findViewById(R.id.searchMedCustomButton);
        CardView exploreStoreButton = findViewById(R.id.exploreStoreCustomButton);
        searchMedicineButton.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });
        exploreStoreButton.setOnClickListener(v->{
            startActivity(new Intent(HomeActivity.this, ExploreStoreActivity.class));
        });
    }
}