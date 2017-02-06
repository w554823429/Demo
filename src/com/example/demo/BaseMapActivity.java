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
		mapView.onCreate(savedInstanceState);// ����Ҫд
		mpop = (RelativeLayout) findViewById(R.id.rent_map_pop);
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
		
		//����������Զ���ı��ͼ��ʹ�÷���
		otMarkerOptions.icon(ImageNormal(0));

		aMap.addMarker(otMarkerOptions);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnMapClickListener(this);
	}
	
	/**
	 * �Զ��������ͼƬ��δѡ��״̬��
	 * @param i
	 * @return
	 */
	private BitmapDescriptor ImageNormal(int i) {
		//����������Զ���ģ����������ͬ���Զ�����poi_view ���xml�ļ�����һ����һ��ͼƬ����һ��TextView
		//����ɾ���ˣ����TextView������Ҫ�����ѿ����Լ����ã����TextView�������д���֣�����ABCD...��������
		//��λ���ɷ���
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
	 * �Զ�������ͼƬ��ѡ��״̬��
	 * @param i
	 * @return
	 */
	private BitmapDescriptor ImagePress(int i) {
		//ʹ�÷���ͬ��
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
		if (popWindow != null) {//����popwindow
			popWindow.dismiss();
		}
	}

	/**
	 * ���λ
	 */
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

	/**
	 * ֹͣ��λ
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
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		currentMarker = marker;
		Toast.makeText(this, "�����˵���" + marker.getTitle(), 10000).show();
		
		if(popWindow !=null){//�Ȱ�ԭ���ĸ���������
			popWindow.dismiss();
		}
		
		popWindow = new StationInfoPopupWindow(this);
		popWindow.showAsDropDown(mpop);
		
		return false;
	}

	/**
	 * �����ͼ�����ط�ʱ������InfoWindow,��popWindow������
	 */
	@Override
	public void onMapClick(LatLng latLng) {
		if (currentMarker != null) {
			currentMarker.hideInfoWindow();//����InfoWindow��
			popWindow.dismiss();
		}
	}

}
