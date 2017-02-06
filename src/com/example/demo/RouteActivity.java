package com.example.demo;

import java.sql.DriverPropertyInfo;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RouteBusWalkItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RouteActivity extends Activity implements LocationSource, AMapLocationListener,OnClickListener, OnRouteSearchListener{
	private AMap aMap;
	private MapView mapView;
	
	/**
	 * 公交按钮，驾车按钮，步行按钮
	 */
	private Button transitBtn, drivingBtn, walkBtn;

	private int busMode = RouteSearch.BusDefault;// 公交默认模式
	private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
	private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
	private RouteSearch routeSearch;

	private BusRouteResult busRouteResult;// 公交模式查询结果
	private DriveRouteResult driveRouteResult;// 驾车模式查询结果
	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	
	private LocationManagerProxy mAMapLocationManager;
	private OnLocationChangedListener mListener;
	private AMapLocation amapLocation=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		mapView = (MapView) findViewById(R.id.route_map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		transitBtn = (Button) findViewById(R.id.imagebtn_roadsearch_tab_transit);
		drivingBtn = (Button) findViewById(R.id.imagebtn_roadsearch_tab_driving);
		walkBtn = (Button) findViewById(R.id.imagebtn_roadsearch_tab_walk);

		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
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

		transitBtn.setOnClickListener(this);
		drivingBtn.setOnClickListener(this);
		walkBtn.setOnClickListener(this);

		// 设置地图可视缩放大小
		aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
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
		
		aMap.addMarker(otMarkerOptions);
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
	}
	
	@Override
	public void onClick(View v) {
		LatLonPoint startPoint = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
		LatLonPoint endPoint = new LatLonPoint(31.23224611592442, 121.47578073062927);
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
		if (v == transitBtn) {// 公交
			BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, "芜湖市", 1);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
		} else if (v == drivingBtn) {// 驾车
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		} else if (v == walkBtn) {// 步行
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo, walkMode);
			routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		}
	}
	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos());
				routeOverlay.removeFromMap();
				routeOverlay.addToMap();
				routeOverlay.zoomToSpan();
				transitBtn.setText("约"+Math.round(busPath.getDuration() / 60)+"分钟"+"\n"+"约"+busPath.getCost()+"元");
			} else {
				Toast.makeText(RouteActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(RouteActivity.this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(RouteActivity.this, "key验证无效！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(RouteActivity.this, "未知错误，请稍后重试!错误码为" + rCode, Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(this, aMap, drivePath, driveRouteResult.getStartPos(), driveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
				List<DriveStep> driveSteps = drivePath.getSteps();
				/*long time=0;
				for (DriveStep driveStep : driveSteps) {
	                if (driveStep.getRoad() != null) {
	                	DriverPropertyInfo walkPath = driveStep.getPolyline();
	                	time+=walkPath.getDuration();
	                }
	            }
				drivingBtn.setText(Math.round(time / 60));*/
				drivingBtn.setText("约"+Math.round(drivePath.getDuration()/60)+"分钟"+"\n"+"共"+drivePath.getDistance()+"米");
			} else {
				Toast.makeText(RouteActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(RouteActivity.this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(RouteActivity.this, "key验证无效！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(RouteActivity.this, "未知错误，请稍后重试!错误码为"+rCode, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				walkRouteResult = result;
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, aMap, walkPath, walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
				walkRouteOverlay.removeFromMap();
				walkRouteOverlay.addToMap();
				walkRouteOverlay.zoomToSpan();
				List<WalkStep> walkSteps = walkPath.getSteps();
				walkBtn.setText("约"+Math.round(walkPath.getDistance() / 60)+"分钟"+"\n"+walkPath.getDuration()+"米");
			} else {
				Toast.makeText(RouteActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(RouteActivity.this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(RouteActivity.this, "key验证无效！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(RouteActivity.this, "未知错误，请稍后重试!错误码为" + rCode, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation.getAMapException().getErrorCode() == 0) {
				this.amapLocation=amapLocation;
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			}
		}
	}

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

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}
}
