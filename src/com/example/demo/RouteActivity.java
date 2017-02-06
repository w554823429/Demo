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
	 * ������ť���ݳ���ť�����а�ť
	 */
	private Button transitBtn, drivingBtn, walkBtn;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		mapView = (MapView) findViewById(R.id.route_map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		transitBtn = (Button) findViewById(R.id.imagebtn_roadsearch_tab_transit);
		drivingBtn = (Button) findViewById(R.id.imagebtn_roadsearch_tab_driving);
		walkBtn = (Button) findViewById(R.id.imagebtn_roadsearch_tab_walk);

		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
		init();
	}
	
	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}

		transitBtn.setOnClickListener(this);
		drivingBtn.setOnClickListener(this);
		walkBtn.setOnClickListener(this);

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

		// ���õ�ͼ�������Ŵ�С
		aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
		aMap.getUiSettings().setCompassEnabled(true);// ����ָ����
		aMap.getUiSettings().setScaleControlsEnabled(true);// ���ñ�����

		LatLng latLng = new LatLng(31.23224611592442, 121.47578073062927);
		MarkerOptions otMarkerOptions = new MarkerOptions();
		otMarkerOptions.position(latLng);
		otMarkerOptions.visible(true);//���ÿɼ�
		otMarkerOptions.title("�Ϻ���").snippet("�Ϻ��У�31.23224611592442, 121.47578073062927");//����������Զ���
		otMarkerOptions.draggable(true);
		//��������Ǳ�����������γ���ڵ�ͼ��λ����
		// otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mark));
		
		aMap.addMarker(otMarkerOptions);
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
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
		if (v == transitBtn) {// ����
			BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, "�ߺ���", 1);// ��һ��������ʾ·���滮�������յ㣬�ڶ���������ʾ������ѯģʽ��������������ʾ������ѯ�������ţ����ĸ�������ʾ�Ƿ����ҹ�೵��0��ʾ������
			routeSearch.calculateBusRouteAsyn(query);// �첽·���滮����ģʽ��ѯ
		} else if (v == drivingBtn) {// �ݳ�
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode, null, null, "");// ��һ��������ʾ·���滮�������յ㣬�ڶ���������ʾ�ݳ�ģʽ��������������ʾ;���㣬���ĸ�������ʾ�������򣬵����������ʾ���õ�·
			routeSearch.calculateDriveRouteAsyn(query);// �첽·���滮�ݳ�ģʽ��ѯ
		} else if (v == walkBtn) {// ����
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo, walkMode);
			routeSearch.calculateWalkRouteAsyn(query);// �첽·���滮����ģʽ��ѯ
		}
	}
	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(0);
				aMap.clear();// �����ͼ�ϵ����и�����
				BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos());
				routeOverlay.removeFromMap();
				routeOverlay.addToMap();
				routeOverlay.zoomToSpan();
				transitBtn.setText("Լ"+Math.round(busPath.getDuration() / 60)+"����"+"\n"+"Լ"+busPath.getCost()+"Ԫ");
			} else {
				Toast.makeText(RouteActivity.this, "�Բ���û��������������ݣ�", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(RouteActivity.this, "����ʧ��,�����������ӣ�", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(RouteActivity.this, "key��֤��Ч��", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(RouteActivity.this, "δ֪�������Ժ�����!������Ϊ" + rCode, Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// �����ͼ�ϵ����и�����
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
				drivingBtn.setText("Լ"+Math.round(drivePath.getDuration()/60)+"����"+"\n"+"��"+drivePath.getDistance()+"��");
			} else {
				Toast.makeText(RouteActivity.this, "�Բ���û��������������ݣ�", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(RouteActivity.this, "����ʧ��,�����������ӣ�", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(RouteActivity.this, "key��֤��Ч��", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(RouteActivity.this, "δ֪�������Ժ�����!������Ϊ"+rCode, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
				walkRouteResult = result;
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				aMap.clear();// �����ͼ�ϵ����и�����
				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, aMap, walkPath, walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
				walkRouteOverlay.removeFromMap();
				walkRouteOverlay.addToMap();
				walkRouteOverlay.zoomToSpan();
				List<WalkStep> walkSteps = walkPath.getSteps();
				walkBtn.setText("Լ"+Math.round(walkPath.getDistance() / 60)+"����"+"\n"+walkPath.getDuration()+"��");
			} else {
				Toast.makeText(RouteActivity.this, "�Բ���û��������������ݣ�", Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(RouteActivity.this, "����ʧ��,�����������ӣ�", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(RouteActivity.this, "key��֤��Ч��", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(RouteActivity.this, "δ֪�������Ժ�����!������Ϊ" + rCode, Toast.LENGTH_SHORT).show();
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
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
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
}
