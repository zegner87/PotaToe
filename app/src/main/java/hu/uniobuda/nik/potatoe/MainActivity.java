package hu.uniobuda.nik.potatoe;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity  implements SensorEventListener
{

	Potato pot;
	SensorManager sManager;
	  Toolbar toolbar;
	    TextView tvTime;
	    TextView tvTap;

	    boolean guiStarted=false;
	  //  FacebookAPI fbApi = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTap = (TextView) findViewById(R.id.tvTap);
        
        
        ImageView ivPotato = (ImageView)findViewById(R.id.ivPotato);  
        ImageView ivToe = (ImageView)findViewById(R.id.ivToe);


		sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        pot = new Potato(this, tvTime, ivPotato, ivToe,MainActivity.this);
    
        
        pot.potatoListener = new IPotato() {
			
			@Override
			public void onEndGame() {
			
				boolean res = SetHighScore();
				if(!res)
				{
					
					ShowTime();
				}
			}
		};
		//	fbApi = new FacebookAPI(MainActivity.this);
	
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
    	 if(!pot.start  && !guiStarted)
    	 {
         //v.getId() will give you the image id
			tvTap.setVisibility(View.INVISIBLE);
			guiStarted = true;
    	    pot.Start();
    	 }
          
        return super.onTouchEvent(event);
    }
    
    
    @Override
   	protected void onResume() 
   	{
   		super.onResume();
   		/*register the sensor listener to listen to the gyroscope sensor, use the 
   		 * callbacks defined in this class, and gather the sensor information as  
   		 * quick as possible*/
   		sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);
   	}

       //When this Activity isn't visible anymore
   	@Override
   	protected void onStop() 
   	{
   		//unregister the sensor listener
   		sManager.unregisterListener(this);
   		super.onStop();
   	}

    
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) 
    {  
        // Inflate the menu; this adds items to the action bar if it is present.  
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu  
        return true;  
    }  
    
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {
            case R.id.highScore:
            	ShowHighScore(false);
             
            return true;     

            case R.id.Exit:
                Exit();  
              return true;     
  
              default:  
                return super.onOptionsItemSelected(item);  
        }

    }
    
    public void ShowHighScore(boolean newScore)
    {
    	SharedPreferences prefs = this.getSharedPreferences("PotaToe", Context.MODE_PRIVATE);
    	int score = prefs.getInt("HighScore", 0); //0 is the default value
    	
    	//Toast.makeText(getApplicationContext(),Integer.toString(score),Toast.LENGTH_LONG).show();
    	
    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	 
    	 
    	 String title = "Your high score";
    	 if(newScore)
    	 {
    		 title = "New high score"; 
    		 
    	 }
    		 
			// set title
				alertDialogBuilder.setTitle(title);
	 
				// set dialog message
				alertDialogBuilder
					.setMessage(pot.ParseTimeFromElTime(score))
					.setCancelable(false);
				
				if(!newScore)
				{
					alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					  });
				}
				else
				{
					alertDialogBuilder.setPositiveButton("Restart",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						
							
						}})
						.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								Exit();
							}
						})
						.setNeutralButton("Share", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								ShareToFaceBook();	
								
							}
						})
						;
					
				}
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
    }
    
    
    public void ShareToFaceBook()
    {
    	/*
		Intent postOnFacebookWallIntent = new Intent(MainActivity.this, MainActivity.class);
		postOnFacebookWallIntent.putExtra("facebookMessage", "PotaToe score: "+pot.ParseTimeFromElTime(pot.elTime));
		startActivity(postOnFacebookWallIntent);
		*/
    	
    //	fbApi.messageToPost = "PotaToe score: "+pot.ParseTimeFromElTime(pot.elTime);
   // 	fbApi.loginAndPostToWall();
    	
    	/*Intent postOnFacebookWallIntent = new Intent(this, FacebookAPI.class);
    	postOnFacebookWallIntent.putExtra("facebookMessage", "is integrating stuff again.");
    	startActivity(postOnFacebookWallIntent);
    	*/
    }
    public void ShowTime()
    {
    	//Toast.makeText(getApplicationContext(),Integer.toString(score),Toast.LENGTH_LONG).show();
    	
    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

				// set title
				alertDialogBuilder.setTitle("The potato is dead. Your time:");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage(pot.ParseTimeFromElTime(pot.elTime))
					.setCancelable(false)
					.setPositiveButton("Restart",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
							tvTap.setVisibility(View.VISIBLE);
							guiStarted = false;
						}})
						.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								Exit();
							}
						})
									.setNeutralButton("Share", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								
								
								ShareToFaceBook();
								
								
								/* Intent shareIntent = new Intent(Intent.ACTION_SEND);
								  shareIntent.setType("text/plain");
								  shareIntent.putExtra(Intent.EXTRA_TEXT, "http://google.com");
								  startActivity(Intent.createChooser(shareIntent, "Share PotaToe..."));*/								
								
							}
						})
						;
				
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
    }
    
    public boolean SetHighScore()
    {
    	int score = pot.elTime;
    	
    	SharedPreferences prefs = this.getSharedPreferences("PotaToe", Context.MODE_PRIVATE);
    	int oldScore = prefs.getInt("HighScore", 0); //0 is the default value
    	
    	if(oldScore<score)
    	{
    		Editor editor = prefs.edit();
        	editor.putInt("HighScore", score);
        	editor.commit();	
        		
        	ShowHighScore(true);
        	
        	return true;
    	}
    	
    	return false;
    	
//    	Toast.makeText(getApplicationContext(),Integer.toString(score),Toast.LENGTH_LONG).show();  

    }
    
    @Override
	public void onSensorChanged(SensorEvent event) 
	{
		//if sensor is unreliable, return void
		if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
		{
			return;
		}
		
  		
  		//else it will output the Roll, Pitch and Yawn values
  		//tvDebug.setText("Orientation X (Roll) :"+ Float.toString(event.values[2]) +"\n"+
  			//	   "Orientation Y (Pitch) :"+ Float.toString(event.values[1]) +"\n"+
  				//   "Orientation Z (Yaw) :"+ Float.toString(event.values[0]));
  		
  		float[] maxArray = new float[]{Math.abs(event.values[1]),Math.abs(event.values[2])};
  		
  		float max = maxArray[0];
  		int maxIndex =0;
  		
  		for(int i=1;i<maxArray.length;i++)
  		{
  				if(max<maxArray[i])
  				{
  					max = maxArray[i];
  					maxIndex = i;
  				}
  		}
  		
  		if(!pot.start)
  		{
  			pot.asc += event.values[2];
  		}
	}
  			 
    public void Exit()
    {
    	this.finish();
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
