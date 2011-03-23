package com.yarin.android.Examples_09_03;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
//import com.yarin.android.Examples_09_04.R;

public class Activity01 extends MapActivity
{
	private MapView	 mMapView;
	private MapController mMapController; 
	private GeoPoint mGeoPoint;
	private int num_site=0;
	List<PinOverlay> PinOverlays=new ArrayList<PinOverlay>();
	private SharedPreferences sites;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
		//添加Overlay，用于显示标注信息
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
        List<Overlay> list = mMapView.getOverlays();
        list.add(myLocationOverlay);
	}
	protected boolean isRouteDisplayed()
	{
		return false;
	}
	
	
	public void DisplayToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
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
		
		@Override
		public void draw(Canvas canvas, MapView mapView,boolean shadow)
		{
			super.draw(canvas, mapView, shadow);
			Paint paint = new Paint();
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 0, 0);
			paint.setStyle(Paint.Style.STROKE);
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
			Point PinCoords=new Point(0,0);
			mMapView.getProjection().toPixels(GeoPinCoords,PinCoords);
			
			canvas.drawBitmap(bmp, PinCoords.x, PinCoords.y, paint);
			//canvas.drawText("天府广场", myScreenCoords.x, myScreenCoords.y, paint);
		//	return true;
		}
	}
		
	class MyLocationOverlay extends Overlay
	{
		@Override
		public boolean onTouchEvent(MotionEvent event,MapView mv)
		{
			//TextView Touch_LocationText = (TextView)findViewById(R.id.TextView01);
			int iAction=event.getAction();
			if(iAction==MotionEvent.ACTION_CANCEL||
				iAction==MotionEvent.ACTION_DOWN||
				iAction==MotionEvent.ACTION_MOVE)
			{
				return false;
			}
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
			int i;
			for(i=0;i<num_site;i++)
			{
				if(Double.toString(Touch_latitude).equals(sites.getString("sitelat" +
						Integer.toString(i), ""))&&
						Double.toString(Touch_longitude).equals(sites.getString("sitelong" +
								Integer.toString(i), ""))) break;
			}
			if(i>=num_site)
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
				if(PinOverlays.get(k).GeoPinCoords.equals(Touch_GeoPoint)) 
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
			return true;
		}
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
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.home);
			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText("天府广场", myScreenCoords.x, myScreenCoords.y, paint);
			return true;
		}
	}
}
