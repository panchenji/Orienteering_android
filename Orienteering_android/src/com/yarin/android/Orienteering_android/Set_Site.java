package com.yarin.android.Orienteering_android;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


import android.widget.EditText;
//import com.yarin.android.Examples_09_04.R;

public class Set_Site extends MapActivity
{
	private MapView	 mMapView;
	private MapController mMapController; 
	private GeoPoint mGeoPoint,mGeoPoint_search;
	private int num_site=0;
	List<PinOverlay> PinOverlays=new ArrayList<PinOverlay>();
	private SharedPreferences sites;
	private boolean pin=false;
    private static final int Pin_Start=Menu.FIRST; 
    private static final int Pin_End=Menu.FIRST+1;
    private static final int My_Location=Menu.FIRST+2;
    
    Location location;
    LocationManager locationManager;
    Criteria criteria =new Criteria();
    String provider=null;
    
	//Geocoder gc=new Geocoder(this,Locale.CHINA);
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main2);
		
        
        String context=Context.LOCATION_SERVICE;
        locationManager=(LocationManager)getSystemService(context);
        
		mMapView = (MapView) findViewById(R.id.MapView01);
		//����Ϊ��ͨģʽ
		//mMapView.setTraffic(true);
		//����Ϊ����ģʽ
		mMapView.setSatellite(true); 
		//����Ϊ�־�ģʽ
		//mMapView.setStreetView(false);
		//ȡ��MapController����(����MapView)
		mMapController = mMapView.getController(); 
		mMapView.setEnabled(true);
		mMapView.setClickable(true);
		//���õ�ͼ֧������
		mMapView.setBuiltInZoomControls(true); 
		//�������Ϊ�ɶ�
		mGeoPoint = new GeoPoint((int) (30.659259 * 1000000), (int) (104.065762 * 1000000));
		//��λ���ɶ�
		mMapController.animateTo(mGeoPoint); 
		//���ñ���(1-21)
		mMapController.setZoom(12); 
		sites=getSharedPreferences("sites",0);
		
		
		//����Criteria�������̣�����Ϣ
        
        //����Ҫ��
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //ȡ��Ч����õ�criteria
        while(provider==null){
        provider=locationManager.getBestProvider(criteria, true);
        }
        //�õ�������ص���Ϣ
        location=locationManager.getLastKnownLocation(provider);
        //��������
       // updateWithNewLocation(location);
        //ע��һ�������Եĸ��£�3000ms����һ��
		//locationListener����������λ��Ϣ�ĸı�
        //locationManager.requestLocationUpdates(provider, 3000, 0,locationListener);
		//���Overlay��������ʾ��ע��Ϣ
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
        List<Overlay> list = mMapView.getOverlays();
        list.add(myLocationOverlay);
        
	}
	
	//����ʵ��GPSλ�õĸ���
    private void UpdateMyLocation() 
    {
    	 location=locationManager.getLastKnownLocation(provider);
    	 mGeoPoint = new GeoPoint((int) (location.getLatitude() * 1000000), (int) (location.getLongitude() * 1000000));
    	 mMapController.animateTo(mGeoPoint); //ת���µ�λ��
    }
	protected boolean isRouteDisplayed()
	{
		return false;
	}
	
	//�������ܣ����ؼ�:�������˵�   ��������������������
	public boolean onKeyDown(int KeyCode,KeyEvent event)
	{
		switch(KeyCode)
		{
		
		case KeyEvent.KEYCODE_BACK:
			Intent intent =new Intent();
			intent.setClass(Set_Site.this, Main.class);
			
			startActivity(intent);
			Set_Site.this.finish();
			break;
		
		case KeyEvent.KEYCODE_SEARCH:
			final LayoutInflater factory = LayoutInflater.from(Set_Site.this);
			//�õ��Զ���Ի���
		    final View DialogView = factory.inflate(R.layout.dialog, null);
            //�����Ի���
            AlertDialog dlg = new AlertDialog.Builder(Set_Site.this)
            .setTitle("����")
            .setView(DialogView)//�����Զ���Ի������ʽ
            .setPositiveButton("ȷ��", //����"ȷ��"��ť
            new DialogInterface.OnClickListener() //�����¼�����
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
                	//������ɺ󣬵����ȷ������ʼ����
                
                                          	//System.out.println('k');
                	String search_content="1600 Pennsylvania Ave, Washington DC";
                    //search_content=((EditText)DialogView.findViewById(R.id.username)).getText().toString();
                	change_center(search_content);
                	
                    
                    
                }
            })
            .setNegativeButton("ȡ��", //���á�ȡ������ť
            new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                	dialog.cancel();
                }
            })
            .create();//����
            dlg.show();//��ʾ
		}

           return super.onKeyDown(KeyCode, event);
		
		}
	
	//ʵ��������,��ͼ���ĵ��Ƶ������ص�
	public void change_center(String search_content)
	{
		Geocoder gc= new Geocoder(this, Locale.getDefault());
    	
        System.out.println(search_content+"tmdnmb");
      //  double lat=0.0;
       // double lng=0.0;
        try{
        	List<Address> addresses = gc.getFromLocationName(search_content, 5);
        	//���ݵ���λ��ȡ�þ�γ������
        	//String add = "";
        	
        	if (addresses.size() > 0) {
        		mGeoPoint_search = new GeoPoint((int) (addresses.get(0).getLatitude() * 1000000),
        				(int) (( addresses.get(0)).getLongitude() * 1000000));
        		//��λ���ɶ�
        		mMapController.animateTo(mGeoPoint_search); 
        		mMapView.invalidate();
        	}
        	else{
        		DisplayToast("Cannot find the address");
        		
        	}
    	
        }catch(Exception e){DisplayToast("error:"+e);};
		
	}
	
	//�˵���
    public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, Pin_Start, Menu.NONE, "���");
		menu.add(0,Pin_End,Menu.NONE,"�رձ��");
		menu.add(0, My_Location, Menu.NONE, "�ҵ�λ��");
		
		
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case (Pin_Start):
				//�������
				pin=true;
				return true;

			case (Pin_End):
				//ȡ�����
			    pin=false;
			
				return true;
			case (My_Location):
				//�ҵ�λ��
				UpdateMyLocation();
		}
		return true;
	}
	public void DisplayToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	//����Pin��Overlay,ÿ��¼һ��Pin�򴴽�һ��PinOverlay������ʾPin
	class PinOverlay extends Overlay
	{
		GeoPoint GeoPinCoords;
		public PinOverlay(GeoPoint GeoPinCoords){
			this.GeoPinCoords=GeoPinCoords;
		}
		/*@Override
		public boolean onTouchEvent(MotionEvent event,MapView mv)
		{
			int iAction=event.getAction();
			if(iAction==MotionEvent.ACTION_CANCEL||
				iAction==MotionEvent.ACTION_DOWN||
				iAction==MotionEvent.ACTION_MOVE)
			{
				return false;
			}
			int x=(int)event.getX();
			int y=(int)event.getY();
			List<Overlay> overlays=mv.getOverlays();
			if(x==this.PinCoords.x&&y==this.PinCoords.y)
			{
				overlays.remove(this);
			}
	            
			return true;
		}*/
		//public remove
		
		
		//��дPinOverlay��draw����
		@Override
		public void draw(Canvas canvas, MapView mapView,boolean shadow)
		{
			super.draw(canvas, mapView, shadow);
			//һЩPaint������
			Paint paint = new Paint();
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColorFilter(new LightingColorFilter(Color.WHITE, 0x80000000));
			Bitmap bmparrow = BitmapFactory.decodeResource(getResources(), R.drawable.push_pin);
			//paint.setAlpha(00);
			Point PinCoords=new Point(0,0);
			mMapView.getProjection().toPixels(GeoPinCoords,PinCoords);
			
			//��ʾPin
			canvas.drawBitmap(bmparrow, PinCoords.x-bmparrow.getWidth(), PinCoords.y-bmparrow.getHeight(), paint);
			//canvas.drawText("�츮�㳡", myScreenCoords.x, myScreenCoords.y, paint);
		//	return true;
		}
	}
		
	
	//������ʾ�ҵ�λ�õ�Overlay,�ҵ�λ����һ������ʾ
	class MyLocationOverlay extends Overlay
	{
		//��дMyLocationOverlay��onTouchOverlay��������pin==trueʱΪ
		//���״̬�����Ե����ͼ�ϵ�λ������¼Site,��pin==falseʱ��Ϊ��ͨ
		//���״̬
		@Override
		public boolean onTouchEvent(MotionEvent event,MapView mv)
		{
			//TextView Touch_LocationText = (TextView)findViewById(R.id.TextView01);
			super.onTouchEvent(event, mv);
			int iAction=event.getAction();
			if(iAction==MotionEvent.ACTION_CANCEL||
				iAction==MotionEvent.ACTION_DOWN||
				iAction==MotionEvent.ACTION_MOVE)
			{
				return false;
			}
			if(pin==true){
			//DisplayToast("aaaaaaa");
			int x=(int)event.getX();
			int y=(int)event.getY();
			Point Touch_point=new Point(x,y);
			//Touch_point.set(x, y);
			
			//DisplayToast("choose ("+Integer.toString(x)+","+Integer.toString(y)+")");
			GeoPoint Touch_GeoPoint;
			//Location Touch_Location;
			Touch_GeoPoint=mMapView.getProjection().fromPixels(x, y);
			
			double Touch_latitude = ((double)Touch_GeoPoint.getLatitudeE6())/1000000;
			double Touch_longitude = ((double)Touch_GeoPoint.getLongitudeE6())/1000000;
		//	Touch_Location.setLatitude(Touch_latitude);
		//	Touch_Location.setLongitude(Touch_longitude);
			//Touch_LocationText.setText("you choose xxxxxxx");
			SharedPreferences sites=getSharedPreferences("site",0);
			SharedPreferences.Editor editor=sites.edit();
            double lat_abs=0.000000;
            double long_abs=0.000000;
			int i;
			for(i=0;i<num_site;i++)
			{
				lat_abs=Touch_latitude-Double.parseDouble(sites.getString("sitelat" +
						Integer.toString(i), ""));
				long_abs=Touch_longitude-Double.parseDouble(sites.getString("sitelong" +
						Integer.toString(i), ""));
				
				if((Math.abs(lat_abs)<=1E-2)&&(Math.abs(long_abs)<=1E-2)) 
				{
					Touch_latitude=Double.parseDouble(sites.getString("sitelat" +
							Integer.toString(i), ""));
					Touch_longitude=Double.parseDouble(sites.getString("sitelong" +
							Integer.toString(i), ""));
				
					break;
				}
			}
			if(i==num_site)
			{
			editor.putString("sitelat"+Integer.toString(num_site), Double.toString(Touch_latitude));
			editor.putString("sitelong"+Integer.toString(num_site), Double.toString(Touch_longitude));
			num_site++;
			
			DisplayToast("You choose point"+
					Double.toString(Touch_latitude)+","+
					Double.toString(Touch_longitude));
			
			List<Overlay> overlays=mv.getOverlays();
			PinOverlay pin=new PinOverlay(Touch_GeoPoint);
			overlays.add(pin);
			PinOverlays.add(pin);
			
			
			}
			else
			{
				List<Overlay> overlays=mv.getOverlays();
				for(int k=0;k<num_site;k++)
				if(
						//PinOverlays.get(k).GeoPinCoords.equals(Touch_GeoPoint)
						Touch_latitude==((double)PinOverlays.get(k).GeoPinCoords.getLatitudeE6())/1000000
						&&Touch_longitude==((double)PinOverlays.get(k).GeoPinCoords.getLongitudeE6())/1000000
						) 
				{
					overlays.remove(PinOverlays.get(k));
					PinOverlays.remove(k);
					while(k<num_site-1)
					{
						editor.putString("sitelat"+Integer.toString(k), 
								sites.getString("sitelat"+Integer.toString(k+1), ""));
						editor.putString("sitelong"+Integer.toString(k), 
								sites.getString("sitelong"+Integer.toString(k+1), ""));
						k++;
					}
					num_site--;
					break;
				}
				
				DisplayToast("ɾ��������������");
			}
			editor.commit();
			}
			return true;
		}
		
		//��дdraw����������ʾ�ҵ�λ��
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
		{
			super.draw(canvas, mapView, shadow);
			Paint paint = new Paint();
			Point myScreenCoords = new Point();
			// ����γ��ת����ʵ����Ļ����
			mapView.getProjection().toPixels(mGeoPoint, myScreenCoords);
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
			//Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.home);
			//canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText("Here", myScreenCoords.x, myScreenCoords.y, paint);
			return true;
		}
	}
}
