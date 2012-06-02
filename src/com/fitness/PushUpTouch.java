package com.fitness;

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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
	int typeindex;
	int times, goal_times;
	private medplayer mp;
	
	private float mGX = 0;
	private float mGY = 0;
	private float mGZ = 0;

	Sensor mSensor = null;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showview);
        
        times = 0;
        start = 0;
        goal_times = 10;
        mp = null;
        timer = null;
        
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
          	//date = bunde.getString("date");
          	goal_times = bunde.getInt("goal_times");
          }
          
          //Display: create ListView class
          show_view = (TextView) findViewById(R.id.showd);
          
          show_view.setText("目前次數, 點一下開始: " + times);
          
          show_view.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{
				if (start == 1)
				{
					if (timer == null)
					{
						
						timer = new Timer();
						timer.schedule(new DateTask(), 10000);
					}
					Log.i(TAG, "onTouch");
					times = 0;
			        show_view.setText("請開始...");
					start = 0;
					return true;
				}
				
				// TODO Auto-generated method stub
		        if (mp != null)
		        {
			      Log.i(TAG, "stop voice");

		          mp.stop_voice();
		          mp = null;
		        }
		        
		        if (timer != null)
				{
		        	timer.cancel();
		        	timer = null;
				}
		        
				times++;
				
				if (times == goal_times)
				{
					show_view.setText("完成次數: " + times + ", 請重新點一下開始...");
					update(1, times);
					start = 1;
					if (mp == null)
		            {
			          //voice
			          Log.i(TAG, "start voice");
			          mp = new medplayer();
			          mp.play_voice("2.mp3", false);
			        }
				}
				else
				{
					show_view.setText("目前次數: " + times + ", 離目標還有：" + Integer.toString(goal_times-times));
					if (timer == null)
					{
						timer = new Timer();
						timer.schedule(new DateTask(), 10000);
					}
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
	          Log.i(TAG, "start voice");
	          mp = new medplayer();
	          mp.play_voice("1.mp3", true);
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
            Log.i(TAG, sdate + "," +  types);
            
            db.insert(SQLiteHelper.TB_NAME, null, contentValues);
    		
    	}catch(IllegalArgumentException e){
    		e.printStackTrace();
    		++ DB_VERSION;
    		dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
    	}			        	
	}
	

	
	@Override
    protected void onDestroy() 
	{
       super.onDestroy();
       if (dbHelper != null)
    	   dbHelper.close();
    }
}