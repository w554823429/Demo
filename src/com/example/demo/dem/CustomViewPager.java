package com.example.demo.dem;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CustomViewPager extends ViewPager {
	public CustomViewPager(Context context) {    
        super(context);    
    }    
    
    public CustomViewPager(Context context, AttributeSet attrs) {    
        super(context, attrs);    
    }    
      
      
    @Override    
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {    
        if(v.getClass().getName().equals("com.amap.api.maps2d.MapView")) {    
            return true;    
        }    
        //if(v instanceof MapView){    
        //    return true;    
        //}    
        return super.canScroll(v, checkV, dx, x, y);   
   }
}
