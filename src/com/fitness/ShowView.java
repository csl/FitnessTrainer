package com.fitness;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.HashMap;

import com.db.ft_item;
import com.db.SQLiteHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShowView extends Activity {

	private ArrayList<HashMap<String, Object>> cstore_list;
    private ArrayList<String> list_ft;

	private TextView cshow_view;

    private static int DB_VERSION = 1;
    
	private SQLiteDatabase db;
	private SQLiteOpenHelper dbHelper;
	private Cursor cursor;
	
	String TAG = "StoreList";
	SimpleDateFormat sdf;
	
	String date;
	
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showview);

        final CharSequence[] type_id = this.getResources().getStringArray(R.array.sport_list);
	    
        //開資料庫
        try{
            dbHelper = new SQLiteHelper(this, SQLiteHelper.DB_NAME, null, DB_VERSION);
            db = dbHelper.getWritableDatabase();
          }
          catch(IllegalArgumentException e){
            e.printStackTrace();
            ++ DB_VERSION;
            dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
          }        
                
        Intent intent=this.getIntent();
        Bundle bunde = intent.getExtras();
        if (bunde != null)
        {
        	date = bunde.getString("date");
        }
        
        //Display: create ListView class
        cshow_view = (TextView)findViewById(R.id.showd);

    	try{
    			cursor = db.query(SQLiteHelper.TB_NAME, null, ft_item.DATE + "='" + date + "'", null, null, null, null);

    		cursor.moveToFirst();
    		
    		
    		//no data
    		if (cursor.isAfterLast())
    		{
    			cshow_view.setText("無健身資料");
    			return;
    		}
    		
    		String str = "";
        	while(!cursor.isAfterLast())
        	{
        		int type = Integer.valueOf(cursor.getString(2));
        		str = str + "日期: " + date + " ,類別: " + type_id[type-1] + " ,次數" + cursor.getString(3) + "\n\n";	
        		cursor.moveToNext();
        	}
        	cshow_view.setText(str);      		
    	}catch(IllegalArgumentException e){
    		e.printStackTrace();
    		++ DB_VERSION;
    		dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
    	}			        	
	}
}