package com.yarin.android.Orienteering_android;


import android.app.AlertDialog;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class Instruction extends TabActivity
{
	//声明TabHost对象
	TabHost mTabHost;
	private TextView tv;
	private TextView tv2;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main4);
		tv=(TextView)this.findViewById(R.id.textview1);
		tv2=(TextView)this.findViewById(R.id.textview2);
		//取得TabHost对象
		mTabHost = getTabHost();
	    
		/* 为TabHost添加标签 */
		//新建一个newTabSpec(newTabSpec)
		//设置其标签和图标(setIndicator)
		//设置内容(setContent)
	    mTabHost.addTab(mTabHost.newTabSpec("Instruction")
	    		.setIndicator("使用说明",getResources().getDrawable(R.drawable.img9))
	    		.setContent(R.id.textview1));
	    mTabHost.addTab(mTabHost.newTabSpec("About us")
	    		.setIndicator("About us",getResources().getDrawable(R.drawable.img10))
	    		.setContent(R.id.textview2));
	   // mTabHost.addTab(mTabHost.newTabSpec("tab_test3")
	    //		.setIndicator("TAB 3",getResources().getDrawable(R.drawable.img3))
	    	//	.setContent(R.id.textview3));
	    
	    //设置TabHost的背景颜色
	    mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    //设置TabHost的背景图片资源
	    //mTabHost.setBackgroundResource(R.drawable.bg0);
	    
	    //设置当前显示哪一个标签
	    mTabHost.setCurrentTab(0);
	    
	   
	    //标签切换事件处理，setOnTabChangedListener 
	    
	    mTabHost.setOnTabChangedListener(new OnTabChangeListener()
	    {
	    	// TODO Auto-generated method stub
            @Override
            public void onTabChanged(String tabId) 
            {
            	
            	
            	if(tabId.equals("Instruction")){
            		
            		String str="使用说明：";
            		tv.setText(str);
            	}
            		if(tabId.equals("About us")){
            			String str1="关于我们：";
            			tv2.setText(str1);
            	}
            	/*
  	    	  	Dialog dialog = new AlertDialog.Builder(Instruction.this)
  	    	  			.setTitle("提示")
  	    	  			.setMessage("当前选中："+tabId+"标签")
  	    	  			.setPositiveButton("确定",
  	    	  			new DialogInterface.OnClickListener() 
  	    	  			{
  	    	  				public void onClick(DialogInterface dialog, int whichButton)
  	    	  				{
  	    	  					dialog.cancel();
  	    	  				}
  	    	  			}).create();//创建按钮
	    	  
  	    	  	dialog.show();*/
            }            
        });
	}
	
	 public boolean onKeyDown(int KeyCode,KeyEvent event)
		{
			switch(KeyCode)
			{
			case KeyEvent.KEYCODE_BACK:
				Intent intent =new Intent();
				intent.setClass(Instruction.this, Main.class);
				
				startActivity(intent);
				Instruction.this.finish();
				break;
			}
			 return super.onKeyDown(KeyCode, event);
		}
}
