package com.fitness;


import java.net.URL;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FitnessTrainerActivity extends Activity 
{
	
	private Button rec, sport, setup, exit;
	int typeindex;
	
	static FitnessTrainerActivity fta;
	
	EditText mpushup, msitup, mchinup;
	
	String pushup = "30", situp = "30", chinup = "30";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        rec = (Button) findViewById(R.id.rec);
        sport = (Button) findViewById(R.id.widget35);
        setup = (Button) findViewById(R.id.widget36);
        exit = (Button) findViewById(R.id.widget37);
        
        fta = this;
        
        if (rec != null)
        {
	        rec.setOnClickListener(new Button.OnClickListener() 
	        { 
	          public void onClick(View v) 
	          { 
		    	   Intent intent = new Intent();
		    	   intent.setClass(FitnessTrainerActivity.this, FTList.class);
		    	   //intent.putExtras(bundle);
		    	   startActivity(intent);
	          } 
	        });
        }
        
        sport.setOnClickListener(new Button.OnClickListener() 
        { 
          public void onClick(View v) 
          { 
        	  showSport();
          } 
        }); 
        
        setup.setOnClickListener(new Button.OnClickListener() 
        { 
          public void onClick(View v) 
          { 
              AlertDialog.Builder alert = new AlertDialog.Builder(fta);

              alert.setTitle("設定");
              alert.setMessage("請輸入");
              
              ScrollView sv = new ScrollView(fta);
              LinearLayout ll = new LinearLayout(fta);
              ll.setOrientation(LinearLayout.VERTICAL);
              sv.addView(ll);

              TextView tlogin = new TextView(fta);
              tlogin.setText("伏地挺身 次數: ");
              mpushup = new EditText(fta);      
              mpushup.setText(pushup);
              ll.addView(tlogin);
              ll.addView(mpushup);

              TextView tpwd = new TextView(fta);
              tpwd.setText("仰臥起坐 次數: ");
              msitup = new EditText(fta);
              msitup.setText(situp);
              ll.addView(tpwd);
              ll.addView(msitup);
 
              TextView tcd = new TextView(fta);
              tcd.setText("拉單槓 次數: ");
              mchinup = new EditText(fta);
              mchinup.setText(chinup);
              ll.addView(tcd);
              ll.addView(mchinup);
              // Set an EditText view to get user input 
              alert.setView(sv);
              
              alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) 
              {
            	
                
                if (mpushup.getText().toString().equals("") || msitup.getText().toString().equals("") || mchinup.getText().toString().equals(""))
                {
                    return;
                }
                
                pushup = mpushup.getText().toString();
                situp = msitup.getText().toString();
                chinup = mchinup.getText().toString();
                
              }
              });
              
             
            
              alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) 
                  {
                    
                  }
                });
            
                alert.show();      
          } 
        }); 
        
        exit.setOnClickListener(new Button.OnClickListener() 
        { 
          public void onClick(View v) 
          { 
        	  android.os.Process.killProcess(android.os.Process.myPid());           
              finish();
          } 
        }); 

    	
    }
    
	  public void showSport()
	  {
	    final CharSequence[] type_id = this.getResources().getStringArray(R.array.sport_list);
	    int checked = 0;
	    
	    //mSelectedMapLocation = mSMapLocation;
	    
	    typeindex = checked;
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("choice");
	     //builder.setCancelable(false);
	    
	    builder.setSingleChoiceItems(type_id, checked, new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which)
	      {
	        typeindex = which;
	      }
	   });
	    
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which)
	      {

	        if (typeindex == 0)
	        {
	 	    	   Intent intent = new Intent();
	 	    	   intent.setClass(FitnessTrainerActivity.this, PushUpTouch.class);
	 	    	   //intent.putExtras(bundle);
	        	   Bundle bundle = new Bundle();
		    	   bundle.putInt("goal_times", Integer.valueOf(pushup));
		    	   intent.putExtras(bundle);

	 	    	   startActivity(intent);
	        	
	        }
	        else if (typeindex == 1)
	        {
	 	    	   Intent intent = new Intent();
	 	    	   intent.setClass(FitnessTrainerActivity.this, SitUp.class);
	 	    	   //intent.putExtras(bundle);
	        	   Bundle bundle = new Bundle();
		    	   bundle.putInt("goal_times", Integer.valueOf(situp));
		    	   intent.putExtras(bundle);

	 	    	   startActivity(intent);	            
	            
	        }
	        else if (typeindex == 2)
	        {
	 	    	   Intent intent = new Intent();
	 	    	   intent.setClass(FitnessTrainerActivity.this, ChinUp.class);
	 	    	   //intent.putExtras(bundle);
	        	   Bundle bundle = new Bundle();
		    	   bundle.putInt("goal_times", Integer.valueOf(chinup));
		    	   intent.putExtras(bundle);

	 	    	   startActivity(intent);	           
	        }
	        
	        dialog.cancel();
	     }
	   });
	     
	   AlertDialog alert = builder.create();
	   alert.show();
	      
	  }	       
	  
	    public boolean onKeyDown(int keyCode, KeyEvent event) 
	    {
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{  
	    		openOptionsDialog();
	    		return true;
	    	}
			
			return super.onKeyDown(keyCode, event);  
	    }
	    
	    //show message, ask exit yes or no
	    private void openOptionsDialog() {
	      
	      new AlertDialog.Builder(this)
	        .setTitle("Exit?")
	        .setMessage("Exit?")
	        .setNegativeButton("No",
	            new DialogInterface.OnClickListener() {
	            
	              public void onClick(DialogInterface dialoginterface, int i) 
	              {
	              }
	        }
	        )
	     
	        .setPositiveButton("Yes",
	            new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialoginterface, int i) 
	            {
	           
	              android.os.Process.killProcess(android.os.Process.myPid());           
	              finish();
	            }
	            
	        }
	        )
	        
	        .show();
	    }
	        
}