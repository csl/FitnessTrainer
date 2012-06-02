package com.fitness;

import java.io.IOException; 
import android.app.Activity; 
import android.media.MediaPlayer; 
import android.media.MediaPlayer.OnCompletionListener; 
import android.os.Bundle; 

public class medplayer
{ 
  /*�]�wbIsReleased�@�}�l��false */
  private boolean bIsReleased = false;
  /*�]�wbIsPaused�@�}�l��false */
  private boolean bIsPaused = false; 
  public MediaPlayer mp;

  medplayer()
  {
    mp = new MediaPlayer();    
  }
  
  void play_voice(String filename, boolean loop)
  {
    try 
    { 
      if(mp.isPlaying()==true) 
      {
        mp.reset();            
      }
      mp.setDataSource( "/sdcard/" + filename);
      mp.setLooping(loop);
      mp.prepare();
      mp.start(); 
    } 
    catch (IllegalStateException e) 
    { 
      // TODO Auto-generated catch block 
      e.printStackTrace(); 
    } 
    catch (IOException e) 
    { 
      // TODO Auto-generated catch block 
      e.printStackTrace(); 
    } 
     
    mp.setOnCompletionListener(new OnCompletionListener() 
    { 
      // @Override 
      public void onCompletion(MediaPlayer arg0) 
      {  
      } 
    });
    
  }
  
  void pause_voice()
  {
    if (mp != null) 
    { 
      if(bIsReleased == false) 
      { 
        if(bIsPaused==false) 
        { 
          /*�]�w MediaPlayer�Ȱ�����*/
          mp.pause(); 
          bIsPaused = true; 
        } 
        else if(bIsPaused==true) 
        { 
          /*�]�w MediaPlayer����*/
          mp.start(); 
          bIsPaused = false; 
        } 
      } 
    }
  }
  
  public void stop_voice()
  {
    if(mp.isPlaying()==true) 
    { 
      /*�N MediaPlayer���]*/
      mp.reset(); 
    } 
  }
}
