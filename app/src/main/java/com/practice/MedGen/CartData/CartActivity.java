package com.practice.MedGen.CartData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.practice.MedGen.Items;
import com.practice.MedGen.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    RecyclerView rv;
    CartAdapter adapter;
    List<Items> list;
    int pageHeight = 1120;
    int pagewidth = 900;
    CartDatabaseHelper cartDatabaseHelper;
    int i=0;
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

        btn.setOnClickListener(v->{
            if(list.size()==0){
                Toast.makeText(this, "There are no items",Toast.LENGTH_SHORT).show();
            }else{
                generatePDF();
            }

        });
    }

    private void generatePDF() {
        PdfDocument doc = new PdfDocument();
        Paint title = new Paint();

        //adding the starting page of the pdf
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = doc.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();
        int x1Pos=35;
        int x2Pos=730;
        int yPos=140;

        Paint head = new Paint();
        head.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        head.setTextSize(16);
        canvas.drawText("GENERIC NAME",x1Pos,100,head);
        canvas.drawText("COUNT",x2Pos,100,head);
        for(int j=0;j<list.size();j++,yPos+=30){
            title.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
            title.setTextSize(14);
            canvas.drawText(list.get(j).getGeneric(),x1Pos,yPos,title);
            canvas.drawText(String.valueOf(cartDatabaseHelper.getMedCount(list.get(j).getId())),x2Pos,yPos,title);

        }
        doc.finishPage(myPage);
        String format = ".pdf";
        String pathName;
        if(i!=0){
            pathName = "/storage/emulated/0/Download/cart"+ i;
        }else{
            pathName = "/storage/emulated/0/Download/cart";
        }
        File file = new File(pathName+format);
        if(!file.exists()){
            try {
                // after creating a file name we will
                // write our PDF file to that location.
                doc.writeTo(new FileOutputStream(file));

                // below line is to print toast message
                // on completion of PDF generation.
                Toast.makeText(CartActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // below line is used
                // to handle error
                e.printStackTrace();
                Log.e("failHoGya", e.getLocalizedMessage());
            }

        }else{
            String old = String.valueOf(i);
            if(pathName.endsWith(old)){
                pathName=pathName.replace(old,String.valueOf(++i));
            }else{
                pathName = pathName+ ++i;
            }
            file = new File(pathName+format);
            try {
                // after creating a file name we will
                // write our PDF file to that location.
                doc.writeTo(new FileOutputStream(file));

                // below line is to print toast message
                // on completion of PDF generation.

                Toast.makeText(CartActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // below line is used
                // to handle error
                e.printStackTrace();
                Log.e("failHoGya", e.getLocalizedMessage());
            }
        }

        Log.d("pathMy", file.toString());
        doc.close();
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