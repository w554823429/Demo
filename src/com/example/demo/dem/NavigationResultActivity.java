package com.example.demo.dem;

import java.util.ArrayList;
import java.util.List;
















import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.example.demo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

public class NavigationResultActivity extends FragmentActivity implements OnClickListener,LocationSource,AMapLocationListener,OnRouteSearchListener,InfoWindowAdapter {
	private AMap aMap;
	
	private double latitude;
	private double longitude;
	private MapView mv_map;
	private TextView tv_drving,tv_bus,tv_walk;
	
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
	
	private LatLonPoint startPoint;
	private LatLonPoint endPoint;
	private RouteSearch.FromAndTo fromAndTo;
	
	private Marker currentMarker;
	
	private Gallery gallery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		latitude=getIntent().getDoubleExtra("latitude", 31.3366002);
		longitude=getIntent().getDoubleExtra("longitude", 121.3894558);
		setContentView(R.layout.activity_result);
		mv_map=(MapView) findViewById(R.id.mv_map);
		mv_map.onCreate(savedInstanceState);// 此方法必须重写
		tv_drving=(TextView) findViewById(R.id.tv_drving);
		tv_bus=(TextView) findViewById(R.id.tv_bus);
		tv_walk=(TextView) findViewById(R.id.tv_walk);
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
		gallery=(Gallery) findViewById(R.id.gallery);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				BusPath busPath = busRouteResult.getPaths().get(position);
				aMap.clear();// 清理地图上的所有覆盖物
				BusRouteOverlay routeOverlay = new BusRouteOverlay(NavigationResultActivity.this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos()){
					 //修改起点marker样式 
					@Override
					protected BitmapDescriptor getStartBitmapDescriptor() {
							return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_start);
					}
					 //修改终点marker样式 
					@Override
					protected BitmapDescriptor getEndBitmapDescriptor() {
						return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_end);
					}
				};
				routeOverlay.removeFromMap();
				routeOverlay.addToMap();
				routeOverlay.zoomToSpan();
				routeOverlay.setNodeIconVisibility(false);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		/*vp_view.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int postion) {
				BusPath busPath = busRouteResult.getPaths().get(postion);
				aMap.clear();// 清理地图上的所有覆盖物
				BusRouteOverlay routeOverlay = new BusRouteOverlay(NavigationResultActivity.this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos()){
					 修改起点marker样式 
					@Override
					protected BitmapDescriptor getStartBitmapDescriptor() {
							return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_start);
					}
					 修改终点marker样式 
					@Override
					protected BitmapDescriptor getEndBitmapDescriptor() {
						return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_end);
					}
				};
				routeOverlay.removeFromMap();
				routeOverlay.addToMap();
				routeOverlay.zoomToSpan();
				routeOverlay.setNodeIconVisibility(false);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});*/
		init();
	}
	
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mv_map.getMap();
			setUpMap();
		}

		tv_drving.setOnClickListener(this);
		tv_bus.setOnClickListener(this);
		tv_walk.setOnClickListener(this);

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
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		// 设置地图可视缩放大小
//		aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
		aMap.getUiSettings().setCompassEnabled(true);// 设置指南针
		aMap.getUiSettings().setScaleControlsEnabled(true);// 设置比例尺
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(currentMarker.isInfoWindowShown()){
                    currentMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
                }
            }
        });
        setMarker();
	}
	
	public void setMarker(){
		LatLng latLng = new LatLng(latitude, longitude);
		MarkerOptions otMarkerOptions = new MarkerOptions();
		otMarkerOptions.position(latLng);
		otMarkerOptions.visible(true);//设置可见
		otMarkerOptions.title("顾村基地").snippet("宝山沪太路顾陈路388号");//里面的内容自定义
		otMarkerOptions.draggable(true);
		//下面这个是标记上面这个经纬度在地图的位置是
		otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end));
		aMap.addMarker(otMarkerOptions);
		currentMarker = aMap.addMarker(otMarkerOptions);
		currentMarker.setObject("顾村基地");//这里可以存储用户数据
		currentMarker.showInfoWindow();
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 12, 0, 24)));
	}
	
	public void initData(){
		if(fromAndTo!=null){
			DriveRouteQuery driveRouteQuery = new DriveRouteQuery(fromAndTo, drivingMode, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			routeSearch.calculateDriveRouteAsyn(driveRouteQuery);// 异步路径规划驾车模式查询
			BusRouteQuery busRouteQuery = new BusRouteQuery(fromAndTo, busMode, "上海市", 1);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			routeSearch.calculateBusRouteAsyn(busRouteQuery);// 异步路径规划公交模式查询
			WalkRouteQuery walkRouteQuery = new WalkRouteQuery(fromAndTo, walkMode);
			routeSearch.calculateWalkRouteAsyn(walkRouteQuery);// 异步路径规划步行模式查询
		}
	}
	
	public void setAdapter(){
		gallery.setAdapter(new MypagerAdapter(NavigationResultActivity.this, busRouteResult));
	}
	
	@Override
	public void onClick(View v) {
		gallery.setVisibility(View.GONE);
		if (v == tv_bus) {// 公交
			aMap.setInfoWindowAdapter(null);// 设置自定义InfoWindow样式
			tv_bus.setTextColor(Color.parseColor("#ffff8800"));
			tv_drving.setTextColor(Color.BLACK);
			tv_walk.setTextColor(Color.BLACK);
			BusPath busPath = busRouteResult.getPaths().get(0);
			aMap.clear();// 清理地图上的所有覆盖物
			BusRouteOverlay routeOverlay = new BusRouteOverlay(NavigationResultActivity.this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos()){
				 //修改起点marker样式 
				@Override
				protected BitmapDescriptor getStartBitmapDescriptor() {
						return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_start);
				}
				 //修改终点marker样式 
				@Override
				protected BitmapDescriptor getEndBitmapDescriptor() {
					return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_end);
				}
			};
			routeOverlay.removeFromMap();
			routeOverlay.addToMap();
			routeOverlay.zoomToSpan();
			routeOverlay.setNodeIconVisibility(false);
			gallery.setVisibility(View.VISIBLE);
		} else if (v == tv_drving) {// 驾车
			aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			tv_drving.setTextColor(Color.parseColor("#ffff8800"));
			tv_bus.setTextColor(Color.BLACK);
			tv_walk.setTextColor(Color.BLACK);
			aMap.clear();// 清理地图上的所有覆盖物
			setMarker();
			/*if(driveRouteResult!=null){
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(this, aMap, drivePath, driveRouteResult.getStartPos(), driveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			}*/
		} else if (v == tv_walk) {// 步行
			aMap.setInfoWindowAdapter(null);// 设置自定义InfoWindow样式
			tv_walk.setTextColor(Color.parseColor("#ffff8800"));
			tv_drving.setTextColor(Color.BLACK);
			tv_bus.setTextColor(Color.BLACK);
			if(walkRouteResult!=null){
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, aMap, walkPath, walkRouteResult.getStartPos(), walkRouteResult.getTargetPos()){
					/* 修改起点marker样式 */
					@Override
					protected BitmapDescriptor getStartBitmapDescriptor() {
						return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_start);
					}
					/* 修改终点marker样式 */
					@Override
					protected BitmapDescriptor getEndBitmapDescriptor() {
						return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_end);
					}
				};
				walkRouteOverlay.removeFromMap();
				walkRouteOverlay.addToMap();
				walkRouteOverlay.zoomToSpan();
				walkRouteOverlay.setNodeIconVisibility(false);
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
	
	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				busRouteResult = result;
				tv_bus.setText("公交\n"+"约"+formated(busRouteResult.getPaths().get(0).getDuration()));//+"\n"+"约"+busRouteResult.getPaths().get(0).getCost()+"元"
				setAdapter();
			} else {
				Toast.makeText(NavigationResultActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(NavigationResultActivity.this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(NavigationResultActivity.this, "key验证无效！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(NavigationResultActivity.this, "未知错误，请稍后重试!错误码为" + rCode, Toast.LENGTH_SHORT).show();
		}
		
	}
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				driveRouteResult = result;
				tv_drving.setText("驾车\n"+"约"+formated(driveRouteResult.getPaths().get(0).getDuration()));//+"\n"+"共"+driveRouteResult.getPaths().get(0).getDistance()+"米"
			} else {
				Toast.makeText(NavigationResultActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(NavigationResultActivity.this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(NavigationResultActivity.this, "key验证无效！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(NavigationResultActivity.this, "未知错误，请稍后重试!错误码为"+rCode, Toast.LENGTH_SHORT).show();
		}
		
	}
	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				walkRouteResult = result;
				tv_walk.setText("步行\n"+"约"+formated(walkRouteResult.getPaths().get(0).getDistance()));//+"\n"+walkRouteResult.getPaths().get(0).getDuration()+"米"
			} else {
				Toast.makeText(NavigationResultActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(NavigationResultActivity.this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(NavigationResultActivity.this, "key验证无效！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(NavigationResultActivity.this, "未知错误，请稍后重试!错误码为" + rCode, Toast.LENGTH_SHORT).show();
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
				startPoint = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
				endPoint = new LatLonPoint(latitude, longitude);
				fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
				initData();
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 12, 0, 24)));
			}
		}
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mv_map.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mv_map.onPause();
		deactivate();
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mv_map.onSaveInstanceState(outState);
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mv_map.onDestroy();
	}
	
	public String formated(float s){
		if(s<3600){//分钟
			float m=s/60;
			return Math.round(m)+"分钟";
		}else{//小时
			int h=(int) (s/(60*60));
			float m=s%3600/60;
			return h+"小时"+Math.round(m)+"分";
		}
	}
	@Override
	public View getInfoContents(Marker marker) {
		View infoContent = getLayoutInflater().inflate(
                R.layout.layout_custom_window, null);
        render(marker, infoContent);
        return infoContent;
	}
	@Override
	public View getInfoWindow(Marker marker) {
		 View infoWindow = getLayoutInflater().inflate(
	                R.layout.layout_custom_window, null);
	        render(marker, infoWindow);
	        return infoWindow;
	}
	
	/**
     * 自定义infowinfow窗口
     */
    public void render(final Marker marker, View view) {
        String title = marker.getSnippet();
        TextView titleUi = (TextView) view.findViewById(R.id.tv_title);
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                    titleText.length(), 0);
            titleUi.setTextSize(14);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }
        Button btn_navigation = (Button) view.findViewById(R.id.btn_navigation);
        btn_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	String[] startStr=new String[]{amapLocation.getLatitude()+"",amapLocation.getLongitude()+""};
            	String[] endStr=new String[]{latitude+"",longitude+""};
            	if(NavigationDialog.isInstallByread("com.autonavi.minimap")){
            		Intent intent = new Intent("android.intent.action.VIEW",android.net.Uri.parse("androidamap://viewMap?sourceApplication=闭门学车&poiname=顾村基地&lat="+latitude+"&lon="+longitude+"&dev=0"));
					intent.setPackage("com.autonavi.minimap");
					startActivity(intent); // 启动调用
            	}else{
            		NavigationDialog.CancelDialog(false,NavigationResultActivity.this,startStr,endStr);
            	}
            }
        });
    }
    
    
    
    public void showWind(View view){
    	boolean flag=false;
    	String[] startStr=new String[]{amapLocation.getLatitude()+"",amapLocation.getLongitude()+""};
    	String[] endStr=new String[]{latitude+"",longitude+""};
    	if(NavigationDialog.isInstallByread("com.autonavi.minimap")){
    		flag=true;
    	}
    	NavigationDialog.CancelDialog(flag,NavigationResultActivity.this,startStr,endStr);
    }
    
    class MypagerAdapter extends BaseAdapter{

    	private BusRouteResult busRouteResult;
    	private Context context;
    	
		public MypagerAdapter(Context context,BusRouteResult busRouteResult) {
			this.busRouteResult=busRouteResult;
			this.context=context;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView=LayoutInflater.from(context).inflate(R.layout.fragment_busroute, null);
			TextView tv_desc=(TextView) convertView.findViewById(R.id.tv_desc);
			tv_desc.setText(fromatRoute(busRouteResult.getPaths().get(position))+"");
			return convertView;
			/*TextView tv_desc=new  TextView(context);
			tv_desc.setText(fromatRoute(busRouteResult.getPaths().get(position))+"");
            return tv_desc;*/
		}

		@Override
		public int getCount() {
			return busRouteResult.getPaths().size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
    	
    }
    
    public String fromatRoute(BusPath busPath){
    	List<BusStep> steps=busPath.getSteps();
		StringBuffer sb=new StringBuffer();
		sb.delete(0,sb.length());
		for(int i=0;i<steps.size();i++){
            BusStep busStep = steps.get(i);

            RouteBusLineItem busLine = busStep.getBusLine(); //获取公交导航信息

            //System.out.println("-------------------"+busLine.toString()); 地铁1号线(四惠东--苹果园) 05:05-23:15
            if(busLine!=null && !busLine.equals(null)){
                Log.e("公交站名称", "--------------"+busLine.getBusLineName());
                BusStationItem arrivalBusStation = busLine.getArrivalBusStation();//到达站
                BusStationItem departureBusStation = busLine.getDepartureBusStation();//出发站

                String busStationName = arrivalBusStation.getBusStationName();//到达站公交站名称
                String busStationName2 = departureBusStation.getBusStationName();//出发站公交站名称

                Log.e("公交",busLine.getBusLineName()+"到达"+busStationName);
                //Log.e("站名","到达站公交站名称"+busStationName+"出发站公交站名称"+busStationName2);
                sb.append(busLine.getBusLineName()+"到达"+busStationName+"--换乘--");
            }else{
                Log.e("null", "------------busStep.getBusLine().equals(null)---------------------");
            }

        }
		return sb.toString()+"";
    }
}
