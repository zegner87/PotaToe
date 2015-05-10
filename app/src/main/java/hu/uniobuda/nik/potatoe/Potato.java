package hu.uniobuda.nik.potatoe;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class Potato
{

	private int optDifficulty;
	private int IvHeight;
	private int IvWidth;
	private int ImageWidth;
	private int ImageHeight;
	Context ctx;
	 	Random rnd = new Random();
		public float asc=0f;
	public float ascGyr=0f;
		public int elTime =0;
	public int tickTime =0;
		private float difficulty = 0.0f;
		public boolean start = true;
		Timer t = null;
		TimerTask task;
		
		private TextView tvTime;
		private ImageView ivPotato;
		IPotato potatoListener = null;
		private Activity act;

	private int ScreenWidth;
	private int ScreenHeight;
		
		public void setListener(IPotato listener) {
	        this.potatoListener = listener;
	    }

	//timer inditasa
		private void StartTimer()
		{
			t = new Timer();
		  task = new TimerTask() {  
		  
			
		   @Override  
		   public void run() {  
		    act.runOnUiThread(new Runnable() {  
		  
		     @Override  
		     public void run() {  
		        
		   //gyorsulas szamitasa
		    	 AscInit();
				 //forgatas elvegzese eredmenyben az uj szog
		    	 float angle = RotatePotato(asc);

		    	 if(Math.abs(angle) >60)
		    	 {
		    		 t.cancel();

		    		 if (potatoListener != null)
		    		 {
		    			 potatoListener.onEndGame();
		    			 RotatePotato(0);

		    		 }

		    	 }
		    	 else
		    	 {

		    		 if(start) {
						 tickTime--;

						 //50 millisec miatt minden 2 ticket szamolunk (1xubb a vegen mp-be atszamolni)
						 if (tickTime % 2 == 0) {
							 elTime--;
						 }
		    		 }
		    		 else
		    		 {
						 tickTime++;
						 if (tickTime % 2 == 0) {
							 elTime++;
						 }
		    		 }


			    	 if(elTime % 10 == 0)
			    	 {
			    		 WriteTime();

			    		 if(elTime % 50 == 0)
			    		 {
			    			 difficulty += 0.5f;

			    		 }

			    		 if(elTime % 600 == 0)
			    		 {
			    			 difficulty = difficulty / 2;

			    		 }

			    		 if(elTime == 0)
			    		 {
			    			 start = false;

			    		 }
			    	 }

		    	 }
		     }
		    });
		   }
		  };
		}

		public float RotatePotato(float x)
	    {
		/*	Log.d("Potato", "ivwidth: " + IvWidth);
			Log.d("Potato", "imagewidth: " + ImageWidth);
			Log.d("Potato", "screenwidth: " + ScreenWidth);
*/

				//segedvonalak szamitasa
	           float l1 = IvHeight*0.78f;
	           float l2 = x;
	           float l3 = (float)Math.sqrt(l1*l1+l2*l2);

		/*	Log.d("Potato", "l1: " + l1);
			Log.d("Potato", "l2: " + l2);
			Log.d("Potato", "l3: " + l3);
			*/
				//forditasi szog a fuggoleges tengelyhez kepest
	           double angle = Math.toDegrees((double)Math.asin(l2 / l3));
		/*	Log.d("Potato", "angle: " + angle);
			Log.d("Potato", "x: " + x);*/


			//a fordulasi szog fv-eben mas-mas kepet toltunk be
	           if(angle>60 || angle < -60)
	           {
				   ivPotato.setImageLevel(6);
	           }
	           else
	           {
		           if(angle>45)
		           {
					   ivPotato.setImageLevel(5);
		           }
		           else
		           {
		        	   if(angle<-45)
		               {
						   ivPotato.setImageLevel(3);
		               }
		        	   else
		        	   {
		        		   if(angle>25) {
							   ivPotato.setImageLevel(4);
		        		   }
		        		   else
		        		   {
		        			   if(angle<-25)
		        			   {
								   ivPotato.setImageLevel(2);
							   }
		        			   else {
								   ivPotato.setImageLevel(1);
		        				}
		        		   }

		        	   }

		           }
	           }




			//forditasi matrix deklaralasa
	           Matrix matrix=new Matrix();
	           ivPotato.setScaleType(ScaleType.MATRIX);   //required

			//a forgatas elvegzese a matrixon
	           matrix.postRotate((float)angle, ImageWidth/2, ImageHeight*0.78f);

			//kep kicsinyitese, ha nagyobb mint a kijelzo
				   if(ImageWidth>ScreenWidth)
				   {



				   float scalewidth = (float)ScreenWidth   / (float) ImageWidth;
				   matrix.postScale(scalewidth,scalewidth);
				   }

			//matrix alkalmazasa az imageview-n
	           ivPotato.setImageMatrix(matrix);

	           return (float)angle;
	    }

	//digitalis ora szamolasa millisecundumbol
		public String ParseTimeFromElTime(int score)
		{
			 int tempTime = score /10;

			  String hourStr ="";
			  String minStr ="";
			  String secStr ="";

			  int hourInt =0;
			  int minInt =0;
			  int secInt =0;

			  hourInt = tempTime /3600;

			  tempTime -= hourInt*3600;

			  if(score >0)
			  {
				  minInt = tempTime/60;
				  tempTime -= minInt*60;

				  if(score >0)
				  {
					secInt = tempTime;
				  }
			  }

			  if(hourInt <10)
			  {
				  hourStr = "0";
			  }

			  if(minInt <10)
			  {
				  minStr = "0";
			  }

			  if(secInt <10)
			  {
				  secStr = "0";
			  }

			  hourStr += hourInt;
			  minStr += minInt;
			  secStr += secInt;

			  return hourStr+":"+minStr+":"+secStr;

		}
//ido kiiratasa
		private void WriteTime()
		  {

			String timeStr =  ParseTimeFromElTime(elTime);
			  if(start)
			  {
				  tvTime.setTextColor(Color.RED);
			  }
			  else
			  {
				  tvTime.setTextColor(Color.BLACK);
			  }

			  tvTime.setText(timeStr);

		  }

		 public void Start()
		  {

			 elTime = 31;
	  		 asc = 0;
	  		 start = true;
	  		 difficulty = optDifficulty*10;


			  this.IvWidth = ivPotato.getWidth();
			  this.IvHeight = ivPotato.getHeight();

	  		 RotatePotato(0);
	  		 
	  		 AscInit();
	  		StartTimer();  
	  		 t.scheduleAtFixedRate(task, 0, 50);
	  	    
		  }
		  private void AscInit()
		  {
			  if(!start)
		        {
				  float diff=1f+difficulty;

					//a gravitaciot legjobban logaritmikus fv-el sikerult kozeliteni
				 if(Math.abs(asc)<40 && asc != 0)
				 {

					 diff =  (float)Math.log(Math.abs(asc))+difficulty;
					 
				 }
				 else
				 {
					 diff=(float)Math.log(Math.abs(asc))+difficulty*2;
				 }

				/*	Log.d("Potato", "ascGyr: " + ascGyr);

					Log.d("Potato", "asc: " + asc);
					Log.d("Potato", "diff: " + diff);
*/
				 	 if (asc <0)
			    	 {
			    		 asc -=diff;
			    	 }
			    	 
				 	 else
				 	 {
				    	 if(asc>0)
				    	 {
				    		 
				    		 asc+=diff;
				    	 }
				    	 else
				    	 {
				    		 
				    		  asc = rnd.nextInt(10)-5;
				    		  if(asc == 0)
				    		  {
				    			  asc = -1;
				    		  }
				    	 }
				 	 }

//az aktualis logaritmikus eredmenyhez hozzadjuk a gyroscop erteket
					asc+=ascGyr;
		        }	
			  
			  

		  }
		  
		  public Potato(Activity act, TextView tvTime, ImageView ivPotato, Context ctx, int optDifficulty)
		  {
			  this.tvTime = tvTime;
			  this.ivPotato = ivPotato;
			  this.act = act;
			  this.ctx = ctx;

			  elTime = 31;
			  asc = 0;
			  start = false;
		  	  this.optDifficulty = optDifficulty;

			  WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
			  Display display = wm.getDefaultDisplay();

			  this.ScreenWidth = display.getWidth();  // deprecated
			  this.ScreenHeight  = display.getHeight();


			  //a kep meretenek lekerese
			  BitmapDrawable bd=(BitmapDrawable) ctx.getResources().getDrawable(R.drawable.potato_mid);
			  this.ImageHeight=bd.getBitmap().getHeight();
			  this.ImageWidth=bd.getBitmap().getWidth();

			  //imageview meretei
			  this.IvWidth = ivPotato.getWidth();
			  this.IvHeight = ivPotato.getHeight();
		  }
		  
		  public void Begin()
		  {
			  AscInit();
		 	  StartTimer();
		 	  t.scheduleAtFixedRate(task, 0, 100);  
		  }

	
		
}
