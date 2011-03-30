package com.yarin.android.Orienteering_android;





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("gps","VERBOSE");
        Log.d("gps","DEBUG");
        Log.i("gps","INFO");
        Log.w("gps","WARN");
        Log.e("gps","ERROR");
        setContentView(R.layout.main);
      //取得GridView对象
		GridView gridview = (GridView) findViewById(R.id.gridview);
		//添加元素给gridview
		gridview.setAdapter(new ImageAdapter(this));

		// 设置Gallery的背景
		gridview.setBackgroundResource(R.drawable.bg0);

		//事件监听
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				switch(position) 
				{
				case 0:
					//当选择第一个图标时进入Site设置
					Intent intent =new Intent();
					intent.setClass(Main.this, Set_Site.class);
					
					startActivity(intent);
					Main.this.finish();
					break;
					
				case 1:
					//当选择第一个图标时进入定向越野打卡
					Intent intent2 =new Intent();
					intent2.setClass(Main.this, Check.class);
					startActivity(intent2);
					Main.this.finish();
					break;
				}
				
				//Toast.makeText(Main.this, "你选择了" + (position + 1) + " 号图片", Toast.LENGTH_SHORT).show();
			}
			
		});
		/*
		public boolean onCreateOptionsMenu(Menu menu)
		{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menu, menu);
			return true;
		}

		//处理menu的事件
		public boolean onOptionsItemSelected(MenuItem item)
		{
			//得到当前选中的MenuItem的ID,
			int item_id = item.getItemId();

			switch (item_id)
			{
				case 0:
				case 1:
					//新建一个Intent对象 
					Intent intent = new Intent();
					// 指定intent要启动的类 
					intent.setClass(Activity02.this, Activity01.class);
					// 启动一个新的Activity 
					startActivity(intent);
					// 关闭当前的Activity 
					Activity02.this.finish();
					break;
			}
			return true;
		}*/
    }
}