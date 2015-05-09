package hu.uniobuda.nik.potatoe;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class Potato
{

		Context ctx;
	 	Random rnd = new Random();
		public float asc=0f;
		public int elTime =0;
		private float difficulty = 0.0f;
		public boolean start = true;
		Timer t = null;
		TimerTask task;
		
		private TextView tvTime;
		private ImageView ivPotato;
		private ImageView ivToe;
		IPotato potatoListener = null;
		private Activity act;
		
		public void setListener(IPotato listener) {
	        this.potatoListener = listener;
	    }
		private void StartTimer()
		{
			t = new Timer();
		  task = new TimerTask() {  
		  
			
		   @Override  
		   public void run() {  
		    act.runOnUiThread(new Runnable() {  
		  
		     @Override  
		     public void run() {  
		        
		   
		    	 AscInit();
		    	 float angle = RotatePotato2(asc);
		    	 
		    	 if(Math.abs(angle) >60)
		    	 {
		    		 t.cancel();
		    		 
		    		 if (potatoListener != null)
		    		 {
		    			 potatoListener.onEndGame();
		    			 RotatePotato2(0);
		    			 
		    		 }
		    		

		    		 
		    		 
		    		

		    	 }
		    	 else
		    	 {
		    	 
		    		 if(start)
		    		 {
		    			elTime--;
		    		 }
		    		 else
		    		 {
		    			 elTime++;
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
	    	
	    	   Paint paint = new Paint();
	           paint.setColor(Color.parseColor("#CD5C5C"));
	           Paint paint2 = new Paint();
	           paint2.setColor(Color.parseColor("#FFFFFF"));


//	           Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
		//	Log.d("Potato", "RotatePotato — width: " + ivPotato.getWidth());
			Bitmap bg = Bitmap.createBitmap((int)(ivPotato.getWidth()),(int)(ivPotato.getHeight()), Bitmap.Config.ARGB_8888);
	           Canvas canvas = new Canvas(bg);


	           float l1 = 0.83f*ivPotato.getHeight();
	           float l2 = x*2;
	           float l3 = (float)Math.sqrt(l1*l1+l2*l2);
	           
	           float angle = (float)Math.asin(l2/l3)*80;
	           
	           if(angle>60 || angle < -60)
	           {
	        	   ivPotato.setImageResource(R.drawable.potato_dead);

				   ivPotato.setBackgroundColor(0xFF0000);
	           }
	           else
	           {
		           if(angle>45)
		           {
		        	   ivPotato.setImageResource(R.drawable.potato_mid_scary_right);
					   ivPotato.setBackgroundColor(0xFF0000);
		        	   
		           }
		           else
		           {
		        	   if(angle<-45)
		               {
		        		   ivPotato.setImageResource(R.drawable.potato_mid_scary_left);
						   ivPotato.setBackgroundColor(0xFF0000);
		            	   
		               }   
		        	   else
		        	   {
		        		   if(angle>25)
		        		   {
		        			   ivPotato.setImageResource(R.drawable.potato_right);
							   ivPotato.setBackgroundColor(0xFFFF00);
		        		   }
		        		   else
		        		   {
		        			   if(angle<-25)
		        			   {
		        				   ivPotato.setImageResource(R.drawable.potato_left);
								   ivPotato.setBackgroundColor(0xFFFF00);


							   }
		        			   else
		        			   {
		        				   ivPotato.setImageResource(R.drawable.potato_mid);
								   ivPotato.setBackgroundColor(0xffffff);

							   }
		        		   }
		        		   
		        	   }
		        	   
		           }
	           }
	           
	          // canvas.drawRect(0,0,480,800,paint2);
	          // canvas.drawLine(a1, b1, a2, b2, paint);
	           //LinearLayout ll = (LinearLayout) findViewById(R.id.rect);
	           //ll.setBackgroundDrawable(new BitmapDrawable(bg));   
	           
	      //    tv.setText("asc: "+asc +" ,angle: "+ Float.toString(angle));
	         
	           int oldHeight = ivPotato.getHeight();
	           int oldWidth = ivPotato.getWidth();
	           

	           Matrix matrix=new Matrix();
	          // Matrix matrix2=new Matrix();
	           ivPotato.setScaleType(ScaleType.MATRIX);   //required
	           //ivToe.setScaleType(ScaleType.MATRIX);   //required
	           matrix.postRotate(angle,oldWidth,oldHeight*2*0.8f);
	          // matrix2.postRotate(0,oldWidth,oldHeight*2*0.8f);
	           if(oldWidth >0 && oldHeight >0)
	           {
	           
	           
	           double radians = Math.toRadians(angle);
	           double sin = Math.abs(Math.sin(angle));
	           double cos = Math.abs(Math.cos(angle));
	        
	           
	           int newWidth = (int)(oldWidth/cos);
	           int newHeight = (int)(oldHeight /sin);
	          
	           
	   //        tvDebug.setText(oldWidth+" "+newWidth);
	           //matrix.postScale((float)((float)oldWidth /(float)newWidth),(float)((float)oldHeight /(float)newHeight));
	        //   matrix.postTranslate((newWidth - oldWidth) / 2, (newHeight - oldHeight) / 2);
	    //       tvDebug.setText(iv.getWidth());
				   float scalewidth = (float) oldWidth  / (float) newWidth;
				   float scaleheight = (float) oldHeight / (float) newHeight;

				   //matrix.postScale(scalewidth,scaleheight);
				   matrix.postScale(0.5f,0.5f);

				 //  matrix2.postScale(0.50f,0.53f);

	           }


	           ivPotato.setImageMatrix(matrix);
	           //ivToe.setImageMatrix(matrix2);
	           
	        //   ivPotato.setScaleType(ScaleType.FIT_END);
	        //   ivToe.setScaleType(ScaleType.FIT_END);
	      
	           
	           return angle; 
	    }

	public float RotatePotato2(float x) {

		return RotatePotato(x);
	}
	   
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
	  		 difficulty = 0f;
	  		 
	  		 
	  		 RotatePotato2(0);
	  		 
	  		 AscInit();
	  		StartTimer();  
	  		 t.scheduleAtFixedRate(task, 0, 100);
	  	    
		  }
		  private void AscInit()
		  {
			  if(!start)
		        {
				  float diff=1f+difficulty;
				  
				 if(Math.abs(asc)<40 && asc != 0)
				 {
					 diff = Math.abs(asc)/10+difficulty;
					 
				 }
				 else
				 {
					 diff=5+difficulty;
				 }
			    
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
			    	 
		        }	
			  
			  

		  }
		  
		  public Potato(Activity act, TextView tvTime, ImageView ivPotato, ImageView ivToe, Context ctx)
		  {
			  this.tvTime = tvTime;
			  this.ivPotato = ivPotato;
			  this.ivToe = ivToe;
			  this.act = act;
			  this.ctx = ctx;

			  elTime = 31;
			  asc = 0;
			  start = false;
		  	  difficulty = 0f;
		  	  
		  
		  }
		  
		  public void Begin()
		  {
			  AscInit();
		 	  StartTimer();
		 	  t.scheduleAtFixedRate(task, 0, 100);  
		  }

	
		
}
