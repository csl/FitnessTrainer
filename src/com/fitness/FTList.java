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

public class FTList extends Activity {

	
	private ArrayList<HashMap<String, Object>> cstore_list;
    private ArrayList<String> list_ft;

	private ListView show_view;

    private static int DB_VERSION = 1;
    
	private SQLiteDatabase db;
	private SQLiteOpenHelper dbHelper;
	private Cursor cursor;
	
	String TAG = "StoreList";
	SimpleDateFormat sdf;
	
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist);
        
        list_ft = new ArrayList<String>(); 
	    
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
                
        
        //Display: create ListView class
        show_view = (ListView)findViewById(R.id.listview);

        //取得目前找到的項目，並放到List中
        cstore_list = getStoreList(); 
    
        
        if (cstore_list != null)
        {
          //加入到ListView中，顯示給使用者
	        SimpleAdapter listitemAdapter=new SimpleAdapter(this,  
	        		cstore_list, 
	    										R.layout.no_listview_style,
	    										new String[]{"ItemTitle","ItemText"}, 
	    										new int[]{R.id.topTextView,R.id.bottomTextView}  
	    										);  
	    
	        show_view.setAdapter(listitemAdapter);
	        show_view.setOnItemClickListener(new OnItemClickListener() 
	        {          
	    	   @Override  
	    	   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	    	     long arg3) 
	    	   {
	               //ft_item addr = list_id.get(arg2);

	               Bundle bundle = new Bundle();
	    	       bundle.putString("date", list_ft.get(arg2));
	    	          
               //將資料送給MapLocationView去顯示google map地圖
    	    		Intent intent = new Intent();
    	    		//intent.setClass(StoreList.this, MapLocationView.class);
    	    		intent.putExtras(bundle);
    	    		startActivity(intent);
    	    		//finish();
	    	   }  
	        });
        }
        else{
        	finish();
        }
	}

	public ArrayList<HashMap<String, Object>> getStoreList() 
	{
		//int search_list_size = MyGoogleMap.my.search_list.size();
		ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();
 
		list_ft.clear();
		
    	Date today = Calendar.getInstance().getTime();
    	Date date;
    	sdf = new SimpleDateFormat("yyyy/MM/dd");
        String sdate = sdf.format(today);
        String term = "";
        
		Calendar scalendar = new GregorianCalendar(2010,1,1);
		scalendar.setTime(today);
        
		int times=0;
        for (int i=1; i<=30; i++)
        {
        	//Query DATABASE
        	try{
        		if (i == 0)
        			cursor = db.query(SQLiteHelper.TB_NAME, null, ft_item.DATE + "='" + sdate + "'", null, null, null, null);
        		else
        			cursor = db.query(SQLiteHelper.TB_NAME, null, ft_item.DATE + "='" + term + "'", null, null, null, null);

        		cursor.moveToFirst();
        		
        		
        		//no data
        		if (cursor.isAfterLast())
        		{
		        	scalendar.add(Calendar.DATE, -1);
		        	date=scalendar.getTime();
		        	term = sdf.format(date);			        			
        			continue;
        		}
        		
        		times = 0;
            	while(!cursor.isAfterLast())
            	{    
            		times = times + Integer.parseInt(cursor.getString(2)); 
            		cursor.moveToNext();
            	}
        	}catch(IllegalArgumentException e){
        		e.printStackTrace();
        		++ DB_VERSION;
        		dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
        	}			        	

        	//date+1
        	try
        	{
            	HashMap<String, Object> map = new HashMap<String, Object>();
    	        //Log.i("VALUE", MyGoogleMap.my.search_list.get(j).getStoreItem().id);
            	if (i == 0)
            	{
            		map.put("ItemTitle", sdate);
            		list_ft.add(sdate);
            	}
            	else
            	{
            		map.put("ItemTitle", term);
            		list_ft.add(term);
            	}
    	        
            	map.put("ItemText", "總次數: " + times);

    	        listitem.add(map);
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();	       		
        	}
        	
        	scalendar.add(Calendar.DATE, -1);
        	date=scalendar.getTime();
        	term = sdf.format(date);
        }
        
		return listitem;
	}

}