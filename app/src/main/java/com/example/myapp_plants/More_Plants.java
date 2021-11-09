package com.example.myapp_plants;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class More_Plants extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plants_more);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Маълумоти бештар");
        String eng  = getIntent().getExtras().getString("tj_Name");
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.create_db();
        db = databaseHelper.open();
        Plants plant;
        cursor= db.rawQuery("select * from "+ DatabaseHelper.TABLE+" where tj_Name='"+eng+"'", null);
        String S="";
        if (cursor.moveToFirst()) {
            int tj_Name = cursor.getColumnIndex("tj_Name");
            int ru_Name = cursor.getColumnIndex("ru_Name");
            int eng_Name = cursor.getColumnIndex("eng_Name");
            int photo = cursor.getColumnIndex("photo");
            int note=cursor.getColumnIndex("notes");
            do {
                plant=new Plants();
                plant.setTj_name(cursor.getString(tj_Name).trim());
                plant.setRus_name(cursor.getString(ru_Name).trim());
                plant.setEng_name(cursor.getString(eng_Name).trim());
                plant.setImage(cursor.getString(photo).trim());
                plant.setNotes(cursor.getString(note).trim());
               int resID = this.getResources().getIdentifier(plant.getImage(),
                        "drawable", this.getPackageName());

                StringBuilder sb = new StringBuilder();
                sb.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><link rel=\"stylesheet\" href=\"file:///android_asset/styles.css\"></head>"+
                        "<body><div class=\"Main\">");
                sb.append(plant.getTj_name()+" "+plant.getRus_name()+" " +plant.getEng_name());
                sb.append("</div>");
                sb.append("<img class=\"imk\"");
                sb.append(" src=\"");
                sb.append("file:///android_asset/"+plant.getImage()+".jpg");
                sb.append("\"/>");
                sb.append("<div class=\"Notes\">");
                sb.append(plant.getNotes());
                sb.append("</div>");
                sb.append("</body></html>");
                String temp = sb.toString();

                ((WebView) findViewById(R.id.txt_Notes)).loadDataWithBaseURL(null, temp, "text/html",
                                "UTF-8", "");
            } while (cursor.moveToNext());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.open();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        cursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Toast toast = Toast.makeText(getApplicationContext(), "Тугмачаи ба қафо пахш карда шуд!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(More_Plants.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
