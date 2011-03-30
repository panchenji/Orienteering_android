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
		//设置为交通模式
		//mMapView.setTraffic(true);
		//设置为卫星模式
		mMapView.setSatellite(true); 
		//设置为街景模式
		//mMapView.setStreetView(false);
		//取得MapController对象(控制MapView)
		mMapController = mMapView.getController(); 
		mMapView.setEnabled(true);
		mMapView.setClickable(true);
		//设置地图支持缩放
		mMapView.setBuiltInZoomControls(true); 
		//设置起点为成都
		mGeoPoint = new GeoPoint((int) (30.659259 * 1000000), (int) (104.065762 * 1000000));
		//定位到成都
		mMapController.animateTo(mGeoPoint); 
		//设置倍数(1-21)
		mMapController.setZoom(12); 
		sites=getSharedPreferences("sites",0);
		
		
		//设置Criteria（服务商）的信息
        
        //经度要求
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //取得效果最好的criteria
        while(provider==null){
        provider=locationManager.getBestProvider(criteria, true);
        }
        //得到坐标相关的信息
        location=locationManager.getLastKnownLocation(provider);
        //更新坐标
       // updateWithNewLocation(location);
        //注册一个周期性的更新，3000ms更新一次
		//locationListener用来监听定位信息的改变
        //locationManager.requestLocationUpdates(provider, 3000, 0,locationListener);
		//添加Overlay，用于显示标注信息
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
        List<Overlay> list = mMapView.getOverlays();
        list.add(myLocationOverlay);
        
	}
	
	//用于实现GPS位置的更新
    private void UpdateMyLocation() 
    {
    	 location=locationManager.getLastKnownLocation(provider);
    	 mGeoPoint = new GeoPoint((int) (location.getLatitude() * 1000000), (int) (location.getLongitude() * 1000000));
    	 mMapController.animateTo(mGeoPoint); //转向新的位置
    }
	protected boolean isRouteDisplayed()
	{
		return false;
	}
	
	//按键功能，返回键:返回主菜单   搜索键：开启搜索窗口
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
			//得到自定义对话框
		    final View DialogView = factory.inflate(R.layout.dialog, null);
            //创建对话框
            AlertDialog dlg = new AlertDialog.Builder(Set_Site.this)
            .setTitle("搜索")
            .setView(DialogView)//设置自定义对话框的样式
            .setPositiveButton("确定", //设置"确定"按钮
            new DialogInterface.OnClickListener() //设置事件监听
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
                	//输入完成后，点击“确定”开始搜索
                
                                          	//System.out.println('k');
                	String search_content="1600 Pennsylvania Ave, Washington DC";
                    //search_content=((EditText)DialogView.findViewById(R.id.username)).getText().toString();
                	change_center(search_content);
                	
                    
                    
                }
            })
            .setNegativeButton("取消", //设置“取消”按钮
            new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                	dialog.cancel();
                }
            })
            .create();//创建
            dlg.show();//显示
		}

           return super.onKeyDown(KeyCode, event);
		
		}
	
	//实现搜索后,地图中心点移到搜索地点
	public void change_center(String search_content)
	{
		Geocoder gc= new Geocoder(this, Locale.getDefault());
    	
        System.out.println(search_content+"tmdnmb");
      //  double lat=0.0;
       // double lng=0.0;
        try{
        	List<Address> addresses = gc.getFromLocationName(search_content, 5);
        	//根据地理位置取得经纬度坐标
        	//String add = "";
        	
        	if (addresses.size() > 0) {
        		mGeoPoint_search = new GeoPoint((int) (addresses.get(0).getLatitude() * 1000000),
        				(int) (( addresses.get(0)).getLongitude() * 1000000));
        		//定位到成都
        		mMapController.animateTo(mGeoPoint_search); 
        		mMapView.invalidate();
        	}
        	else{
        		DisplayToast("Cannot find the address");
        		
        	}
    	
        }catch(Exception e){DisplayToast("error:"+e);};
		
	}
	
	//菜单栏
    public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, Pin_Start, Menu.NONE, "标记");
		menu.add(0,Pin_End,Menu.NONE,"关闭标记");
		menu.add(0, My_Location, Menu.NONE, "我的位置");
		
		
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case (Pin_Start):
				//开启标记
				pin=true;
				return true;

			case (Pin_End):
				//取消标记
			    pin=false;
			
				return true;
			case (My_Location):
				//我的位置
				UpdateMyLocation();
		}
		return true;
	}
	public void DisplayToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	//用于Pin的Overlay,每记录一个Pin则创建一个PinOverlay用来显示Pin
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
		
		
		//重写PinOverlay的draw函数
		@Override
		public void draw(Canvas canvas, MapView mapView,boolean shadow)
		{
			super.draw(canvas, mapView, shadow);
			//一些Paint的设置
			Paint paint = new Paint();
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColorFilter(new LightingColorFilter(Color.WHITE, 0x80000000));
			Bitmap bmparrow = BitmapFactory.decodeResource(getResources(), R.drawable.push_pin);
			//paint.setAlpha(00);
			Point PinCoords=new Point(0,0);
			mMapView.getProjection().toPixels(GeoPinCoords,PinCoords);
			
			//显示Pin
			canvas.drawBitmap(bmparrow, PinCoords.x-bmparrow.getWidth(), PinCoords.y-bmparrow.getHeight(), paint);
			//canvas.drawText("天府广场", myScreenCoords.x, myScreenCoords.y, paint);
		//	return true;
		}
	}
		
	
	//用于显示我的位置的Overlay,我的位置有一个红点表示
	class MyLocationOverlay extends Overlay
	{
		//重写MyLocationOverlay的onTouchOverlay函数，当pin==true时为
		//标记状态，可以点击地图上的位置来记录Site,当pin==false时，为普通
		//浏览状态
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
				
				DisplayToast("删除。。。。。。");
			}
			editor.commit();
			}
			return true;
		}
		
		//重写draw函数用于显示我的位置
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
		{
			super.draw(canvas, mapView, shadow);
			Paint paint = new Paint();
			Point myScreenCoords = new Point();
			// 将经纬度转换成实际屏幕坐标
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
