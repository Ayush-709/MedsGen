package com.practice.MedGen.ExploreStoreData;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.practice.MedGen.R;

public class StoreDetailActivity extends AppCompatActivity {
    String store_code;
    TextView address, name, number, storeCode, stateDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        store_code = getIntent().getStringExtra("store_code");
        store_code = "'"+store_code+"'";


        address = findViewById(R.id.detailPageAddress);
        name = findViewById(R.id.detailPageContactPerson);
        number = findViewById(R.id.detailPageContactNumber);
        storeCode = findViewById(R.id.detailPageStoreCode);
        stateDistrict = findViewById(R.id.detailPageStateDistrict);


        StoreDatabaseHelper databaseHelper = new StoreDatabaseHelper(this);
        Cursor cursor = databaseHelper.fetchAllByStoreCode(store_code);

        String storeSC="", storeAddress="", storeDistrict="", storeState="", storePin="", storePerson="", storeNumber="";

        if(cursor.getCount()!=0){
            while (cursor.moveToNext()) {
                storeSC = cursor.getString(0);
                storeAddress = cursor.getString(1);
                storeDistrict = cursor.getString(2);
                storeState = cursor.getString(3);
                storePin = cursor.getString(4);
                storePerson = cursor.getString(5);
                storeNumber = cursor.getString(6);
            }
        }
        name.setText(storePerson);
        storeCode.setText(storeSC);
        number.setText(storeNumber);
        String ad = storeAddress +", "+ storePin;
        address.setText(ad);
        String state_district = storeDistrict+", "+storeState;
        stateDistrict.setText(state_district);

    }
}