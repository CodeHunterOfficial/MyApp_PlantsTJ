package com.example.myapp_plants;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CustomAdapter adapter;
    ListView lv;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.create_db();
    }
    private ArrayList getDataPlants()
    {
        ArrayList<Plants> plants=new ArrayList<>();

        Plants plant;
        //=new Plants();
        db = databaseHelper.open();
        Cursor c = db.rawQuery("select *from "+ DatabaseHelper.TABLE, null);
        String S="";
        if (c.moveToFirst()) {
            int tj_Name = c.getColumnIndex("tj_Name");
            int ru_Name = c.getColumnIndex("ru_Name");
           // int eng_Name = c.getColumnIndex("eng_Name");
            int photo = c.getColumnIndex("photo");
            do {
                plant=new Plants();
                plant.setTj_name(c.getString(tj_Name).trim());
                plant.setRus_name(c.getString(ru_Name).trim());
              //  plant.setEng_name(c.getString(eng_Name).trim());
                plant.setImage(c.getString(photo).trim());
                plants.add(plant);
            } while (c.moveToNext());
        }
        return plants;
    }

    private ArrayList getDataPlants(String keyWord)
    {
        ArrayList<Plants> plants=new ArrayList<>();
        Plants plant;
        db = databaseHelper.open();
        String sql="select * from "+ DatabaseHelper.TABLE+" where  " +DatabaseHelper.COLUMN_tj_NAME +" Like '%"+keyWord+"%'";// Like '%"+keyWord+"%'";
        Log.d("sql",sql);
        Cursor c = db.rawQuery(sql, null);
        String S="";
        if (c.moveToFirst()) {
            int tj_Name = c.getColumnIndex("tj_Name");
            int ru_Name = c.getColumnIndex("ru_Name");
       //     int eng_Name = c.getColumnIndex("eng_Name");
            int photo = c.getColumnIndex("photo");
            do {
                plant=new Plants();
                plant.setTj_name(c.getString(tj_Name).trim());
                plant.setRus_name(c.getString(ru_Name).trim());
             //   plant.setEng_name(c.getString(eng_Name).trim());
                plant.setImage(c.getString(photo).trim());
                plants.add(plant);
            } while (c.moveToNext());
        }
        return plants;
    }

    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.open();
        lv= (ListView) findViewById(R.id.lv);
        adapter=new CustomAdapter(this,getDataPlants());
        lv.setAdapter(adapter);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.main_menu, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search_item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Ҷустуҷӯ");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                db = databaseHelper.open();
                lv= (ListView) findViewById(R.id.lv);
                adapter=new CustomAdapter(MainActivity.this,getDataPlants(s.toString()));
                lv.setAdapter(adapter);
                return false;
            }
        });
        return true;

    }

}
