package com.example.image_zoomin_zoomout;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;	
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity  implements  OnTouchListener{

	/**Variables Declaration*/
	
	private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ImageView imv = (ImageView)findViewById(R.id.imageView1);
        imv.setOnTouchListener(this);
        
    
    }
    
    public boolean onTouch(View v, MotionEvent event)
    {
    	ImageView imv = (ImageView) v ;
    	imv.setScaleType(ImageView.ScaleType.MATRIX);
    	float scale;
    	
    	dumpEvent(event);
    	
    	switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
							
							savedMatrix.set(matrix);
							start.set(event.getX(), event.getY());							
							Log.d(TAG,"Mode =Drage");
							mode = DRAG;
							break;
							
		case MotionEvent.ACTION_UP:
			
		case MotionEvent.ACTION_POINTER_UP:
			
							mode =NONE;
							Log.d(TAG, "Mode = None");
							break;
							
		case MotionEvent.ACTION_POINTER_DOWN:
							oldDist = spacing(event);
							Log.d(TAG, "oldDist=" + oldDist);
							if(oldDist > 10f)
							{
								savedMatrix.set(matrix);
								midPoint(mid, event);
								mode = ZOOM;
								Log.d(TAG, "mode=ZOOM");
							}
							break;
							
		 case MotionEvent.ACTION_MOVE:		
			 				if (mode == DRAG) 
				 				{
				 					matrix.set(savedMatrix);
				 					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
								}
			 				else if (mode == ZOOM) 
			 					{
			 						float newDist = spacing (event);
			 						Log.d(TAG, "newDistance=" +newDist);
			 							if (newDist > 5f) 
			 							{
			 								matrix.set(savedMatrix);
			 								scale = newDist / oldDist;
			 								 // setting the scaling of the matrix...if scale > 1 means zoom in...if scale < 1 means zoom out
			 								matrix.postScale(scale, scale, mid.x, mid.y);
										}
			 					}
			 				break;

		}
    	imv.setImageMatrix(matrix);
    	
		return true;
    	
    }


	/*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */
    
    private float spacing(MotionEvent event) {
		// TODO Auto-generated method stub
		
    	float x = event.getX(0) - event.getX(1);
    	float y = event.getY(0) - event.getY(1);
    	
    	return FloatMath.sqrt(x * x + y *y);
	}
    

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */
    
    private void midPoint(PointF point, MotionEvent event) {
		// TODO Auto-generated method stub
    	
    	float x = event.getX(0) + event.getX(1);
    	float y = event.getY(0) + event.getY(1);
    	
    	point.set(x/2, y/2);    
    }
    
    /** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new  StringBuilder();
		
		int action = event.getAction();
		int actionCode =  action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
	
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) 
		{
			sb.append("(paid").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) 
		{
				sb.append("#").append(i);
				sb.append("(pid ").append(event.getPointerId(i));
		        sb.append(")=").append((int) event.getX(i));
		        sb.append(",").append((int) event.getY(i));
		            if (i + 1 < event.getPointerCount())
		                sb.append(";");			
		}
		sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
		
	}

	


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
