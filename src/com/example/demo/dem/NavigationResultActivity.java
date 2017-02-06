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
	
	private int busMode = RouteSearch.BusDefault;// ����Ĭ��ģʽ
	private int drivingMode = RouteSearch.DrivingDefault;// �ݳ�Ĭ��ģʽ
	private int walkMode = RouteSearch.WalkDefault;// ����Ĭ��ģʽ
	private RouteSearch routeSearch;
	
	private BusRouteResult busRouteResult;// ����ģʽ��ѯ���
	private DriveRouteResult driveRouteResult;// �ݳ�ģʽ��ѯ���
	private WalkRouteResult walkRouteResult;// ����ģʽ��ѯ���
	
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
		mv_map.onCreate(savedInstanceState);// �˷���������д
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
				aMap.clear();// �����ͼ�ϵ����и�����
				BusRouteOverlay routeOverlay = new BusRouteOverlay(NavigationResultActivity.this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos()){
					 //�޸����marker��ʽ 
					@Override
					protected BitmapDescriptor getStartBitmapDescriptor() {
							return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_start);
					}
					 //�޸��յ�marker��ʽ 
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
				aMap.clear();// �����ͼ�ϵ����и�����
				BusRouteOverlay routeOverlay = new BusRouteOverlay(NavigationResultActivity.this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos()){
					 �޸����marker��ʽ 
					@Override
					protected BitmapDescriptor getStartBitmapDescriptor() {
							return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_start);
					}
					 �޸��յ�marker��ʽ 
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
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = mv_map.getMap();
			setUpMap();
		}

		tv_drving.setOnClickListener(this);
		tv_bus.setOnClickListener(this);
		tv_walk.setOnClickListener(this);

		// ���õ�ͼ�������Ŵ�С
		aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
	}
	
	/**
	 * ���õ�ͼ��ʽ
	 */
	private void setUpMap() {
		// �Զ���ϵͳ��λ����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// �Զ��嶨λ����ͼ��
//		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
		// �Զ��徫�ȷ�Χ��Բ�α߿���ɫ
		myLocationStyle.strokeColor(Color.BLUE);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);
		// �Զ��徫�ȷ�Χ��Բ�α߿���
		myLocationStyle.strokeWidth(2);
		// ���Զ���� myLocationStyle ������ӵ���ͼ��
		aMap.setMyLocationStyle(myLocationStyle);
		
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// ���ö�λ������Ϊ��λģʽ����λ��AMap.LOCATION_TYPE_LOCATE�������棨AMap.LOCATION_TYPE_MAP_FOLLOW��
		// ��ͼ������������ת��AMap.LOCATION_TYPE_MAP_ROTATE������ģʽ
		// aMap.setMyLocationType(AMap.MAP_TYPE_SATELLITE);
		aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		// ���õ�ͼ�������Ŵ�С
//		aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
		aMap.getUiSettings().setCompassEnabled(true);// ����ָ����
		aMap.getUiSettings().setScaleControlsEnabled(true);// ���ñ�����
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(currentMarker.isInfoWindowShown()){
                    currentMarker.hideInfoWindow();//���������infowindow���ڵķ���
                }
            }
        });
        setMarker();
	}
	
	public void setMarker(){
		LatLng latLng = new LatLng(latitude, longitude);
		MarkerOptions otMarkerOptions = new MarkerOptions();
		otMarkerOptions.position(latLng);
		otMarkerOptions.visible(true);//���ÿɼ�
		otMarkerOptions.title("�˴����").snippet("��ɽ��̫·�˳�·388��");//����������Զ���
		otMarkerOptions.draggable(true);
		//��������Ǳ�����������γ���ڵ�ͼ��λ����
		otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end));
		aMap.addMarker(otMarkerOptions);
		currentMarker = aMap.addMarker(otMarkerOptions);
		currentMarker.setObject("�˴����");//������Դ洢�û�����
		currentMarker.showInfoWindow();
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 12, 0, 24)));
	}
	
	public void initData(){
		if(fromAndTo!=null){
			DriveRouteQuery driveRouteQuery = new DriveRouteQuery(fromAndTo, drivingMode, null, null, "");// ��һ��������ʾ·���滮�������յ㣬�ڶ���������ʾ�ݳ�ģʽ��������������ʾ;���㣬���ĸ�������ʾ�������򣬵����������ʾ���õ�·
			routeSearch.calculateDriveRouteAsyn(driveRouteQuery);// �첽·���滮�ݳ�ģʽ��ѯ
			BusRouteQuery busRouteQuery = new BusRouteQuery(fromAndTo, busMode, "�Ϻ���", 1);// ��һ��������ʾ·���滮�������յ㣬�ڶ���������ʾ������ѯģʽ��������������ʾ������ѯ�������ţ����ĸ�������ʾ�Ƿ����ҹ�೵��0��ʾ������
			routeSearch.calculateBusRouteAsyn(busRouteQuery);// �첽·���滮����ģʽ��ѯ
			WalkRouteQuery walkRouteQuery = new WalkRouteQuery(fromAndTo, walkMode);
			routeSearch.calculateWalkRouteAsyn(walkRouteQuery);// �첽·���滮����ģʽ��ѯ
		}
	}
	
	public void setAdapter(){
		gallery.setAdapter(new MypagerAdapter(NavigationResultActivity.this, busRouteResult));
	}
	
	@Override
	public void onClick(View v) {
		gallery.setVisibility(View.GONE);
		if (v == tv_bus) {// ����
			aMap.setInfoWindowAdapter(null);// �����Զ���InfoWindow��ʽ
			tv_bus.setTextColor(Color.parseColor("#ffff8800"));
			tv_drving.setTextColor(Color.BLACK);
			tv_walk.setTextColor(Color.BLACK);
			BusPath busPath = busRouteResult.getPaths().get(0);
			aMap.clear();// �����ͼ�ϵ����и�����
			BusRouteOverlay routeOverlay = new BusRouteOverlay(NavigationResultActivity.this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos()){
				 //�޸����marker��ʽ 
				@Override
				protected BitmapDescriptor getStartBitmapDescriptor() {
						return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_start);
				}
				 //�޸��յ�marker��ʽ 
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
		} else if (v == tv_drving) {// �ݳ�
			aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
			tv_drving.setTextColor(Color.parseColor("#ffff8800"));
			tv_bus.setTextColor(Color.BLACK);
			tv_walk.setTextColor(Color.BLACK);
			aMap.clear();// �����ͼ�ϵ����и�����
			setMarker();
			/*if(driveRouteResult!=null){
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// �����ͼ�ϵ����и�����
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(this, aMap, drivePath, driveRouteResult.getStartPos(), driveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			}*/
		} else if (v == tv_walk) {// ����
			aMap.setInfoWindowAdapter(null);// �����Զ���InfoWindow��ʽ
			tv_walk.setTextColor(Color.parseColor("#ffff8800"));
			tv_drving.setTextColor(Color.BLACK);
			tv_bus.setTextColor(Color.BLACK);
			if(walkRouteResult!=null){
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				aMap.clear();// �����ͼ�ϵ����и�����
				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, aMap, walkPath, walkRouteResult.getStartPos(), walkRouteResult.getTargetPos()){
					/* �޸����marker��ʽ */
					@Override
					protected BitmapDescriptor getStartBitmapDescriptor() {
						return BitmapDescriptorFactory.fromResource(R.drawable.icon_around_start);
					}
					/* �޸��յ�marker��ʽ */
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
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ���������ں���ʱ�����removeUpdates()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
			// ����������ʱ��Ϊ-1����λֻ��һ��
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
				tv_bus.setText("����\n"+"Լ"+formated(busRouteResult.getPaths().get(0).getDuration()));//+"\n"+"Լ"+busRouteResult.getPaths().get(0).getCost()+"Ԫ"
				setAdapter();
			} else {
				Toast.makeText(NavigationResultActivity.this, "�Բ���û��������������ݣ�", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(NavigationResultActivity.this, "����ʧ��,�����������ӣ�", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(NavigationResultActivity.this, "key��֤��Ч��", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(NavigationResultActivity.this, "δ֪�������Ժ�����!������Ϊ" + rCode, Toast.LENGTH_SHORT).show();
		}
		
	}
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				driveRouteResult = result;
				tv_drving.setText("�ݳ�\n"+"Լ"+formated(driveRouteResult.getPaths().get(0).getDuration()));//+"\n"+"��"+driveRouteResult.getPaths().get(0).getDistance()+"��"
			} else {
				Toast.makeText(NavigationResultActivity.this, "�Բ���û��������������ݣ�", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(NavigationResultActivity.this, "����ʧ��,�����������ӣ�", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(NavigationResultActivity.this, "key��֤��Ч��", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(NavigationResultActivity.this, "δ֪�������Ժ�����!������Ϊ"+rCode, Toast.LENGTH_SHORT).show();
		}
		
	}
	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				walkRouteResult = result;
				tv_walk.setText("����\n"+"Լ"+formated(walkRouteResult.getPaths().get(0).getDistance()));//+"\n"+walkRouteResult.getPaths().get(0).getDuration()+"��"
			} else {
				Toast.makeText(NavigationResultActivity.this, "�Բ���û��������������ݣ�", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(NavigationResultActivity.this, "����ʧ��,�����������ӣ�", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(NavigationResultActivity.this, "key��֤��Ч��", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(NavigationResultActivity.this, "δ֪�������Ժ�����!������Ϊ" + rCode, Toast.LENGTH_SHORT).show();
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
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 12, 0, 24)));
			}
		}
	}
	
	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mv_map.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mv_map.onPause();
		deactivate();
	}
	
	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mv_map.onSaveInstanceState(outState);
	}
	
	/**
	 * ����������д
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mv_map.onDestroy();
	}
	
	public String formated(float s){
		if(s<3600){//����
			float m=s/60;
			return Math.round(m)+"����";
		}else{//Сʱ
			int h=(int) (s/(60*60));
			float m=s%3600/60;
			return h+"Сʱ"+Math.round(m)+"��";
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
     * �Զ���infowinfow����
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
            		Intent intent = new Intent("android.intent.action.VIEW",android.net.Uri.parse("androidamap://viewMap?sourceApplication=����ѧ��&poiname=�˴����&lat="+latitude+"&lon="+longitude+"&dev=0"));
					intent.setPackage("com.autonavi.minimap");
					startActivity(intent); // ��������
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

            RouteBusLineItem busLine = busStep.getBusLine(); //��ȡ����������Ϣ

            //System.out.println("-------------------"+busLine.toString()); ����1����(�Ļݶ�--ƻ��԰) 05:05-23:15
            if(busLine!=null && !busLine.equals(null)){
                Log.e("����վ����", "--------------"+busLine.getBusLineName());
                BusStationItem arrivalBusStation = busLine.getArrivalBusStation();//����վ
                BusStationItem departureBusStation = busLine.getDepartureBusStation();//����վ

                String busStationName = arrivalBusStation.getBusStationName();//����վ����վ����
                String busStationName2 = departureBusStation.getBusStationName();//����վ����վ����

                Log.e("����",busLine.getBusLineName()+"����"+busStationName);
                //Log.e("վ��","����վ����վ����"+busStationName+"����վ����վ����"+busStationName2);
                sb.append(busLine.getBusLineName()+"����"+busStationName+"--����--");
            }else{
                Log.e("null", "------------busStep.getBusLine().equals(null)---------------------");
            }

        }
		return sb.toString()+"";
    }
}
