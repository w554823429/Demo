package com.example.demo;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.demo.popup.StationInfoPopupWindow;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BaseMapActivity extends Activity implements LocationSource, AMapLocationListener, OnMarkerClickListener, OnMapClickListener{
	
	private MapView mapView;
	private AMap aMap;
	private LocationManagerProxy mAMapLocationManager;
	private OnLocationChangedListener mListener;
	private Marker currentMarker;
	private StationInfoPopupWindow popWindow;
	private RelativeLayout mpop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须要写
		mpop = (RelativeLayout) findViewById(R.id.rent_map_pop);
		init();
	}
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}
	
	/**
	 * 设置地图样式
	 */
	private void setUpMap() {
		// 自定义系统定位蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
//		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
		// 自定义精度范围的圆形边框颜色
		myLocationStyle.strokeColor(Color.BLUE);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);
		// 自定义精度范围的圆形边框宽度
		myLocationStyle.strokeWidth(2);
		// 将自定义的 myLocationStyle 对象添加到地图上
		aMap.setMyLocationStyle(myLocationStyle);
		
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
		// 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
		// aMap.setMyLocationType(AMap.MAP_TYPE_SATELLITE);

		// 设置地图可视缩放大小
		aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
		aMap.getUiSettings().setCompassEnabled(true);// 设置指南针
		aMap.getUiSettings().setScaleControlsEnabled(true);// 设置比例尺

		LatLng latLng = new LatLng(31.23224611592442, 121.47578073062927);
		MarkerOptions otMarkerOptions = new MarkerOptions();
		otMarkerOptions.position(latLng);
		otMarkerOptions.visible(true);//设置可见
		otMarkerOptions.title("上海市").snippet("上海市：31.23224611592442, 121.47578073062927");//里面的内容自定义
		otMarkerOptions.draggable(true);
		//下面这个是标记上面这个经纬度在地图的位置是
		// otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mark));
		
		//下面这个是自定义的标记图标使用方法
		otMarkerOptions.icon(ImageNormal(0));

		aMap.addMarker(otMarkerOptions);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnMapClickListener(this);
	}
	
	/**
	 * 自定义标记物的图片（未选中状态）
	 * @param i
	 * @return
	 */
	private BitmapDescriptor ImageNormal(int i) {
		//这个布局是自定义的，这面的内容同样自动，在poi_view 这个xml文件里有一个有一张图片，有一个TextView
		//被我删除了，这个TextView，有需要的网友可以自己设置，这个TextView里面可以写数字，或者ABCD...更具需求
		//各位自由发挥
		View view = null;
		view = getLayoutInflater().inflate(R.layout.poi_view, null);
		RelativeLayout ly = (RelativeLayout) view.findViewById(R.id.view_mark);

		// TextView tv = (TextView) view.findViewById(R.id.poi_mark_img);
		// tv.setText(i + "");
		// tv.setPadding(0, 0, 0, 25);
		// tv.setBackgroundResource(R.drawable.poi_mark_normal);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
		return bitmap;
	}
	
	/**
	 * 自定义标记物图片（选中状态）
	 * @param i
	 * @return
	 */
	private BitmapDescriptor ImagePress(int i) {
		//使用方法同上
		View view = null;
		view = getLayoutInflater().inflate(R.layout.poi_view, null);
		// TextView tv = (TextView) view.findViewById(R.id.poi_mark_img);
		// tv.setText(i + "");
		// tv.setPadding(0, 0, 0, 25);
		// tv.setBackgroundResource(R.drawable.poi_mark_press);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
		return bitmap;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		if (popWindow != null) {//隐藏popwindow
			popWindow.dismiss();
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用destroy()方法
			// 其中如果间隔时间为-1，则定位只定一次
			mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		currentMarker = marker;
		Toast.makeText(this, "你点击了的是" + marker.getTitle(), 10000).show();
		
		if(popWindow !=null){//先把原来的给隐藏起来
			popWindow.dismiss();
		}
		
		popWindow = new StationInfoPopupWindow(this);
		popWindow.showAsDropDown(mpop);
		
		return false;
	}

	/**
	 * 点击地图其他地方时，隐藏InfoWindow,和popWindow弹出框
	 */
	@Override
	public void onMapClick(LatLng latLng) {
		if (currentMarker != null) {
			currentMarker.hideInfoWindow();//隐藏InfoWindow框
			popWindow.dismiss();
		}
	}

}
