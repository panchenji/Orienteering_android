package com.yarin.android.Orienteering_android;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Check extends MapActivity 
{
	public MapController mapController;
    public MyLocationOverlay myPosition=new MyLocationOverlay();
    public PastLocationOverlay Current_Position;
    public MapView myMapView;
    //private SharedPreferences GPS_COORDS;
    //private int num_GPS=0;
    private int num_sites=0;
   // Location[] site=new Location[5];
   // site[0]=new Location(-81,30);
   double[] site_lat =new double[100];
    double[] site_long =new double[100];
    //site_lat[0]=10.0000;
    Location location;
    private static final int ZOOM_IN=Menu.FIRST; 
    private static final int ZOOM_OUT=Menu.FIRST+1;
    //**********
    private static final int CHECK=Menu.FIRST+2;
    //private static final int =Menu.FIRST+3;
    //************

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("gps","VERBOSE"); //错误检查
        Log.d("gps","DEBUG");
        Log.i("gps","INFO");
        Log.w("gps","WARN");
        Log.e("gps","ERROR");
        setContentView(R.layout.main3);
        
        /***********
         SharedPreferences sites=getSharedPreferences("site",0);
         //读取文件中的设置的sites
         num_sites=sites.getInt("num_sites", 0);
        //读取文件中site的个数
         for(int k=0;k<num_sites;k++)
         {
        	 site_lat[k]=Double.parseDouble(sites.getString("sitelat" +Integer.toString(k), ""));
        	 site_lat[k]=Double.parseDouble(sites.getString("sitelong" +Integer.toString(k), ""));
         }
         //将sites存入数组中
         //////////////
          */
         num_sites=2;
        site_lat[0]=30.731881;
        site_lat[1]=30.643898;
        //site_lat[2]=27.000000;
        site_long[0]=104.02937;
        site_long[1]=103.993665;
        //site_long[2]=-83.000000;
      	 //取得LocationManager实例
        LocationManager locationManager;
        String context=Context.LOCATION_SERVICE;
        locationManager=(LocationManager)getSystemService(context);
        myMapView=(MapView)findViewById(R.id.MapView01);
        //取得MapController实例，控制地图
        mapController=myMapView.getController();
        //设置显示模式
        myMapView.setSatellite(true);
        myMapView.setStreetView(true);
        //设置缩放控制,这里我们自己实现缩放菜单
        myMapView.displayZoomControls(false);   
        //设置使用MyLocationOverlay来绘图
        mapController.setZoom(17);
        
        List<Overlay> overlays=myMapView.getOverlays();
        overlays.add(myPosition);

        //设置Criteria（服务商）的信息
        Criteria criteria =new Criteria();
        //经度要求
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider=null;
        //取得效果最好的criteria
       // while(provider==null){
        provider=locationManager.getBestProvider(criteria, true);
        //}
        //得到坐标相关的信息
        location=locationManager.getLastKnownLocation(provider);
        //更新坐标
        updateWithNewLocation(location);
        //注册一个周期性的更新，3000ms更新一次
		//locationListener用来监听定位信息的改变
        locationManager.requestLocationUpdates(provider, 3000, 0,locationListener);
 
    }
	
	//设置按键功能，按下返回时回到主菜单
	public boolean onKeyDown(int KeyCode,KeyEvent event)
	{
		switch(KeyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			Intent intent =new Intent();
			intent.setClass(Check.this, Main.class);
			
			startActivity(intent);
			Check.this.finish();
			break;
		}
		 return super.onKeyDown(KeyCode, event);
	}
	
	//更新的GPS地址
    private void updateWithNewLocation(Location location) 
    {
        String latLongString;
        TextView myLocationText = (TextView)findViewById(R.id.TextView01);
        
      // String addressString="没有找到地址\n";
        
        if(location!=null)
        {
            Current_Position=new PastLocationOverlay(location);
            List<Overlay> overlays=myMapView.getOverlays();
            overlays.add(Current_Position);
            

            
            
        	//为绘制标志的类设置坐标
            myPosition.setLocation(location);
            //取得经度和纬度
            Double geoLat=location.getLatitude()*1E6;
            Double geoLng=location.getLongitude()*1E6;
            //将其转换为int型
            GeoPoint point=new GeoPoint(geoLat.intValue(),geoLng.intValue());
            //定位到指定坐标
            mapController.animateTo(point);
            double lat=location.getLatitude();
            double lng=location.getLongitude();
            latLongString="经度："+lat+"\n纬度："+lng;
            
           // double latitude=location.getLatitude();
            //double longitude=location.getLongitude();
            //更具地理环境来确定编码
           // Geocoder gc=new Geocoder(this,Locale.getDefault());
          /*  try
            {
            	//取得地址相关的一些信息\经度、纬度
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
            latLongString="没有找到坐标.\n";
        }
        //显示
        //myLocationText.setText("你当前的坐标如下:\n"+latLongString+"\n"+addressString);
            myLocationText.setText("你当前的坐标如下:\n"+latLongString+"\n");
        }
	
    //检查是否离某site足够近，如果足够近则进行打卡，提示前往下一个site
	public boolean Check(Location location)
	{
		
		for(int k=0;k<num_sites;k++)
		{
			if((Math.pow((location.getLatitude()-site_lat[k]),2)+
					Math.pow((location.getLongitude()-site_long[k]),2))<=2) 
			{
				site_lat[k]=-0.1;
				site_long[k]=-0.1;
				return true;
			}
	
		}
		return false;
		
		//double Site_X=28.054553;
		//double Site_Y=-82.411629;
		//double lat_C=location.getLatitude();
        //double long_C=location.getLongitude();
		//if((Math.pow((lat_C-Site_X),2)+Math.pow((long_C-Site_Y),2))<=100) return true;
		//else return false;
	
	}
	
	
    private final LocationListener locationListener=new LocationListener()
    {
    	//当坐标改变时触发此函数
        public void onLocationChanged(Location location)
        {
        	updateWithNewLocation(location);
        }
        //Provider被disable时触发此函数，比如GPS被关闭 
        public void onProviderDisabled(String provider)
        {
        	updateWithNewLocation(null);
        }
        //Provider被enable时触发此函数，比如GPS被打开
        public void onProviderEnabled(String provider){}
        //Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        public void onStatusChanged(String provider,int status,Bundle extras){}
    };
    protected boolean isRouteDisplayed()
	{
		return false;
	}
    //为应用程序添加菜单
    public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, ZOOM_IN, Menu.NONE, "放大");
		menu.add(0, ZOOM_OUT, Menu.NONE, "缩小");
		menu.add(0,CHECK,Menu.NONE,"确认");
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case (ZOOM_IN):
				//放大
				mapController.zoomIn();
				return true;
			case (ZOOM_OUT):
				//缩小
				mapController.zoomOut();
				return true;
			case (CHECK):
				//打卡
				//Location location=getLocation();
				if (Check(location)) 
					{
					DisplayToast("Check successful!Go to next site~");
					
					
					
					}
				else DisplayToast("Check fail! Get closer.");
				return true;
		}
		return true;
	}
    
    //Toast提示
	public void DisplayToast(String str)
	{
		Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
	}
    
	//用于记录经过路径的Overlay
	class PastLocationOverlay extends Overlay
	{
		Location mLocation;
		public PastLocationOverlay(Location mLocation){
			this.mLocation=mLocation;
		}
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
		{
			super.draw(canvas, mapView, shadow);
			Paint paint = new Paint();
			Point myScreenCoords = new Point();
			// 将经纬度转换成实际屏幕坐标
			GeoPoint tmpGeoPoint = new GeoPoint((int)(mLocation.getLatitude()*1E6),(int)(mLocation.getLongitude()*1E6));
			mapView.getProjection().toPixels(tmpGeoPoint, myScreenCoords);
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
		//	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.home);
		//	Bitmap site_bmp=BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
		
			
	

			canvas.drawText(".", myScreenCoords.x, myScreenCoords.y, paint);
			return true;
		}
		}
	
	
	class MyLocationOverlay extends Overlay
	{
		Location mLocation;
		//在更新坐标时，设置该坐标，一边画图
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
			// 将经纬度转换成实际屏幕坐标
			GeoPoint tmpGeoPoint = new GeoPoint((int)(mLocation.getLatitude()*1E6),(int)(mLocation.getLongitude()*1E6));
			mapView.getProjection().toPixels(tmpGeoPoint, myScreenCoords);
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
			//Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.home);
			Bitmap site_bmp=BitmapFactory.decodeResource(getResources(), R.drawable.push_pin);
			//canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			
			GeoPoint Site_GeoPoint = null;
			Point Site_ScreenCoords=new Point();
			//for(int i=0;i<=1;i++)
			for(int i=0;i<num_sites;i++)
			{
				if(site_lat[i]!=-0.1&&site_long[i]!=-0.1)
				{
				Site_GeoPoint = new GeoPoint((int)(site_lat[i]*1E6),(int)(site_long[i]*1E6));
				//SharedPreferences sites=getSharedPreferences("site",0);
				//Site_GeoPoint = new GeoPoint((int)(sites.getString("sitelat" +
				//		Integer.toString(i), "")),(int)(sites.getString("sitelat" +
				//				Integer.toString(i), "")));
				mapView.getProjection().toPixels(Site_GeoPoint, Site_ScreenCoords);
				canvas.drawBitmap(site_bmp,Site_ScreenCoords.x-site_bmp.getWidth(), Site_ScreenCoords.y-site_bmp.getHeight() , paint);
				canvas.drawText("("+Double.toString(site_lat[i])+","+Double.toString(site_long[i])+")", Site_ScreenCoords.x,Site_ScreenCoords.y, paint);
				}
			}
			canvas.drawText(".", myScreenCoords.x, myScreenCoords.y, paint);
			return true;
		}
	}
	
}
