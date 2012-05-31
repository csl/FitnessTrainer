package com.fp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import java.util.HashMap;

import com.db.ft_item;
import com.db.SQLiteHelper;
import com.fitness.R;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PushUpTouch extends Activity {

	private TextView show_view;

    private static int DB_VERSION = 1;
    
	private SQLiteDatabase db;
	private SQLiteOpenHelper dbHelper;
	private Cursor cursor;
	
	String TAG = "PushUpTouch";
	SimpleDateFormat sdf;
	Timer timer;
	String date;
	int start = 0;
	int times, goal_times;
	private medplayer mp;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showview);
        
        times = 0;
        mp = null;
        
        
        
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
          show_view = (TextView)findViewById(R.id.showd);
          timer = new Timer();
          show_view.setText("目前次數, 點一下開始: " + times);
          
          
          show_view.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{
				if (start == 1)
				{
					timer.schedule(new DateTask(), 10000);
			        show_view.setText("請開始...");
					start = 0;
					return true;
				}
				
				// TODO Auto-generated method stub
		        if (mp != null)
		        {
		          mp.stop_voice();
		          mp = null;
		        }		        
				timer.cancel();
				
				times++;
				
				if (times >= goal_times)
				{
					show_view.setText("完成次數: " + times + ", 請重新點一下開始...");
					update(1, times);
					start = 1;
				}
				else
				{
					show_view.setText("目前次數: " + times);
			        timer.schedule(new DateTask(), 10000);
				}
		        
				return false;
			}

      });


	}
	
	public class DateTask extends TimerTask {
	    public void run() 
	    {
	        if (mp == null)
            {
	          //voice
	          mp = new medplayer();
	          mp.play_voice("warn.mp3");
	        }
	    }
	        
	}
	
	public void update(int types, int times)
	{
    	try{
        	Date today = Calendar.getInstance().getTime();
        	sdf = new SimpleDateFormat("yyyy/MM/dd");
            String sdate = sdf.format(today);    		
            ContentValues contentValues = new ContentValues();
            contentValues.put(ft_item.DATE, sdate);
            contentValues.put(ft_item.TYPE, types);
            contentValues.put(ft_item.TIMES, times);
            contentValues.put(ft_item.COMMIT, "");
            //Log.i(TAG, sname + "," +  saddress);
    		
    	}catch(IllegalArgumentException e){
    		e.printStackTrace();
    		++ DB_VERSION;
    		dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
    	}			        	
	}
}