package hu.uniobuda.nik.potatoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		//controlok lekerese
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTap = (TextView) findViewById(R.id.tvTap);
        ImageView ivPotato = (ImageView)findViewById(R.id.ivPotato);

		//szenor inicializalas
		sManager = (SensorManager) getSystemService(SENSOR_SERVICE);


		SharedPreferences prefs = this.getSharedPreferences("PotaToe", Context.MODE_PRIVATE);
		int diff = prefs.getInt("Difficulty", 0); //0 is the default value


		//Potato deklaralasa
        pot = new Potato(this, tvTime, ivPotato, MainActivity.this,diff);
    

		//feliratkozas
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

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
		//tuch esemenyre indul a jatek

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

//szenozrra feliratkozas
   		sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
   	}

       //When this Activity isn't visible anymore
   	@Override
   	protected void onStop() 
   	{
   		//unregister the sensor listener
		//szenzorrol leiratkozas
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

		//menu kezelese
        switch (item.getItemId()) {
            case R.id.highScore:
            	ShowHighScore(false);
             
            return true;
			case R.id.options:
				Intent myIntent = new Intent(MainActivity.this, OptionsActivity.class);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainActivity.this.startActivity(myIntent);
			case R.id.Exit:
                Exit();  
              return true;     
  
              default:  
                return super.onOptionsItemSelected(item);  
        }

    }
    
    public void ShowHighScore(boolean newScore)
    {
		//legjobb eredmenyek olvasasa a keszulekrol
    	SharedPreferences prefs = this.getSharedPreferences("PotaToe", Context.MODE_PRIVATE);
    	int score = prefs.getInt("HighScore", 0); //0 is the default value
    	
    	//Toast.makeText(getApplicationContext(),Integer.toString(score),Toast.LENGTH_LONG).show();
    	
    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	 
    	 
    	 String title = getResources().getString(R.string.YourHighScore);
    	 if(newScore)
    	 {
    		 title = getResources().getString(R.string.NewHighScore);
    		 
    	 }
    		 
			// set title
				alertDialogBuilder.setTitle(title);
	 
				// set dialog message
				alertDialogBuilder
					.setMessage(pot.ParseTimeFromElTime(score))
					.setCancelable(false);
				
				if(!newScore)
				{
					alertDialogBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					  });
				}
				else
				{
					alertDialogBuilder.setPositiveButton(R.string.Restart,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						
							
						}})
						.setNegativeButton(R.string.Exit,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								Exit();
							}
						})
						;
					
				}
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
    }
    
    

    public void ShowTime()
    {
    	//Toast.makeText(getApplicationContext(),Integer.toString(score),Toast.LENGTH_LONG).show();
    	
    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

				// set title
				alertDialogBuilder.setTitle(R.string.DeadPotato);
	 
				// set dialog message
				alertDialogBuilder
					.setMessage(pot.ParseTimeFromElTime(pot.elTime))
					.setCancelable(false)
					.setPositiveButton(R.string.Restart, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							tvTap.setVisibility(View.VISIBLE);
							guiStarted = false;
						}
					})
						.setNegativeButton(R.string.Exit, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Exit();
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

		//legjobb eredmeny olvasasa
    	SharedPreferences prefs = this.getSharedPreferences("PotaToe", Context.MODE_PRIVATE);
    	int oldScore = prefs.getInt("HighScore", 0); //0 is the default value


		//rekordot dontottunk, felulirjuk
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

		//szenzor olvasasa : a 2-es indexu x koordinatat vesszuk figyelembe

  		
  		if(!pot.start)
  		{
			//Potato gyorsulasahoz adjuk a szenzor erteket
  			pot.ascGyr = event.values[2];



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
