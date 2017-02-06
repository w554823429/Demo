package com.example.demo.dem;

import com.example.demo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BusRouteFragment extends Fragment {
	
	private View routeView;
	private TextView tv_desc;
	
	public static BusRouteFragment getRouteFragmentInstance(String str){
		BusRouteFragment busRouteFragment=new BusRouteFragment();
		Bundle args = new Bundle();
		args.putString("route", str);
		busRouteFragment.setArguments(args);
        return busRouteFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		routeView=inflater.inflate(R.layout.fragment_busroute, container, false);
		init();
		return routeView;
	}
	
	public void init(){
		tv_desc=(TextView) routeView.findViewById(R.id.tv_desc);
		tv_desc.setText(getArguments().getString("route")+"");
	}

}
