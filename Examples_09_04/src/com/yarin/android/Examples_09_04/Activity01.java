package com.yarin.android.Examples_09_04;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Activity01 extends MapActivity 
{
	public MapController mapController;
    public MyLocationOverlay myPosition;
    public MapView myMapView;
   // Location[] site=new Location[5];
   // site[0]=new Location(-81,30);
   double[] site_lat =new double[2];
    double[] site_long =new double[2];
    //site_lat[0]=10.0000;
    Location location;
    private static final int ZOOM_IN=Menu.FIRST; 
    //private static final int ZOOM_OUT=Menu.FIRST+1;
    //**********
    private static final int CHECK=Menu.FIRST+1;
    //private static final int =Menu.FIRST+3;
    //************

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("gps","VERBOSE");
        Log.d("gps","DEBUG");
        Log.i("gps","INFO");
        Log.w("gps","WARN");
        Log.e("gps","ERROR");
        setContentView(R.layout.main);
        
        
        site_lat[0]=28.054583;
        site_lat[1]=28.054983;
      //  site_lat[2]=27.000000;
        site_long[0]=-82.411429;
        site_long[1]=-82.411829;
       // site_long[2]=-83.000000;
      	 //ȡ��LocationManagerʵ��
        LocationManager locationManager;
        String context=Context.LOCATION_SERVICE;
        locationManager=(LocationManager)getSystemService(context);
        myMapView=(MapView)findViewById(R.id.MapView01);
        //ȡ��MapControllerʵ�������Ƶ�ͼ
        mapController=myMapView.getController();
        //������ʾģʽ
        myMapView.setSatellite(true);
        myMapView.setStreetView(true);
        //�������ſ���,���������Լ�ʵ�����Ų˵�
        myMapView.displayZoomControls(false);   
        //����ʹ��MyLocationOverlay����ͼ
        mapController.setZoom(17);
        myPosition=new MyLocationOverlay();
        List<Overlay> overlays=myMapView.getOverlays();
        overlays.add(myPosition);

        //����Criteria�������̣�����Ϣ
        Criteria criteria =new Criteria();
        //����Ҫ��
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //ȡ��Ч����õ�criteria
        String provider=locationManager.getBestProvider(criteria, true);
        //�õ�������ص���Ϣ
        location=locationManager.getLastKnownLocation(provider);
        //��������
        updateWithNewLocation(location);
        //ע��һ�������Եĸ��£�3000ms����һ��
		//locationListener����������λ��Ϣ�ĸı�
        locationManager.requestLocationUpdates(provider, 3000, 0,locationListener);
 
    }

    private void updateWithNewLocation(Location location) 
    {
        String latLongString;
        TextView myLocationText = (TextView)findViewById(R.id.TextView01);
        
      // String addressString="û���ҵ���ַ\n";
        
        if(location!=null)
        {
        	//Ϊ���Ʊ�־������������
            myPosition.setLocation(location);
            //ȡ�þ��Ⱥ�γ��
            Double geoLat=location.getLatitude()*1E6;
            Double geoLng=location.getLongitude()*1E6;
            //����ת��Ϊint��
            GeoPoint point=new GeoPoint(geoLat.intValue(),geoLng.intValue());
            //��λ��ָ������
            mapController.animateTo(point);
            double lat=location.getLatitude();
            double lng=location.getLongitude();
            latLongString="���ȣ�"+lat+"\nγ�ȣ�"+lng;
            
           // double latitude=location.getLatitude();
            //double longitude=location.getLongitude();
            //���ߵ�������ȷ������
           // Geocoder gc=new Geocoder(this,Locale.getDefault());
          /*  try
            {
            	//ȡ�õ�ַ��ص�һЩ��Ϣ\���ȡ�γ��
                List<Address> addresses=gc.getFromLocation(latitude, longitude,1);
                StringBuilder sb=new StringBuilder();
                if(addresses.size()>0)
                {
                    Address address=addresses.get(0);
                    for(int i=0;i<address.getMaxAddressLineIndex();i++)
                        sb.append(address.getAddressLine(i)).append("\n");
                        
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                        addressString=sb.toString();
                }
            }catch(IOException e){}*/
        }
        else
        {
            latLongString="û���ҵ�����.\n";
        }
        //��ʾ
        //myLocationText.setText("�㵱ǰ����������:\n"+latLongString+"\n"+addressString);
            myLocationText.setText("�㵱ǰ����������:\n"+latLongString+"\n");
        }
	
	public boolean Check(Location location)
	{
		double Site_X=28.054553;
		double Site_Y=-82.411629;
		double lat_C=location.getLatitude();
        double long_C=location.getLongitude();
		if((Math.pow((lat_C-Site_X),2)+Math.pow((long_C-Site_Y),2))<=100) return true;
		else return false;
	
	}
	
    private final LocationListener locationListener=new LocationListener()
    {
    	//������ı�ʱ�����˺���
        public void onLocationChanged(Location location)
        {
        	updateWithNewLocation(location);
        }
        //Provider��disableʱ�����˺���������GPS���ر� 
        public void onProviderDisabled(String provider)
        {
        	updateWithNewLocation(null);
        }
        //Provider��enableʱ�����˺���������GPS����
        public void onProviderEnabled(String provider){}
        //Provider��ת̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
        public void onStatusChanged(String provider,int status,Bundle extras){}
    };
    protected boolean isRouteDisplayed()
	{
		return false;
	}
    //ΪӦ�ó�����Ӳ˵�
    public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, ZOOM_IN, Menu.NONE, "�Ŵ�");
		//menu.add(0, ZOOM_OUT, Menu.NONE, "��С");
		menu.add(0,CHECK,Menu.NONE,"ȷ��");
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case (ZOOM_IN):
				//�Ŵ�
				mapController.zoomOut();
				return true;
			//case (ZOOM_OUT):
				//��С
			//	mapController.zoomOut();
				//return true;
			case (CHECK):
				//��
				//Location location=getLocation();
				if (Check(location)) DisplayToast("Check successful!Go to next site~");
				else DisplayToast("Check fail! Get closer.");
				return true;
		}
		return true;
	}
    
	public void DisplayToast(String str)
	{
		Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
	}
    
	class MyLocationOverlay extends Overlay
	{
		Location mLocation;
		//�ڸ�������ʱ�����ø����꣬һ�߻�ͼ
		public void setLocation(Location location)
		{
			mLocation = location;
		}
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
		{
			super.draw(canvas, mapView, shadow);
			Paint paint = new Paint();
			Point myScreenCoords = new Point();
			// ����γ��ת����ʵ����Ļ����
			GeoPoint tmpGeoPoint = new GeoPoint((int)(mLocation.getLatitude()*1E6),(int)(mLocation.getLongitude()*1E6));
			mapView.getProjection().toPixels(tmpGeoPoint, myScreenCoords);
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.home);
			Bitmap site_bmp=BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			
			GeoPoint Site_GeoPoint;
			Point Site_ScreenCoords=new Point();
			for(int i=0;i<=1;i++)
			{	
				Site_GeoPoint = new GeoPoint((int)(site_lat[i]*1E6),(int)(site_long[i]*1E6));
				mapView.getProjection().toPixels(Site_GeoPoint, Site_ScreenCoords);
				canvas.drawBitmap(site_bmp,Site_ScreenCoords.x, Site_ScreenCoords.y , paint);
			}
			canvas.drawText("Here am I", myScreenCoords.x, myScreenCoords.y, paint);
			return true;
		}
	}
	
}
