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
	//����TabHost����
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
		//ȡ��TabHost����
		mTabHost = getTabHost();
	    
		/* ΪTabHost��ӱ�ǩ */
		//�½�һ��newTabSpec(newTabSpec)
		//�������ǩ��ͼ��(setIndicator)
		//��������(setContent)
	    mTabHost.addTab(mTabHost.newTabSpec("Instruction")
	    		.setIndicator("ʹ��˵��",getResources().getDrawable(R.drawable.img9))
	    		.setContent(R.id.textview1));
	    mTabHost.addTab(mTabHost.newTabSpec("About us")
	    		.setIndicator("About us",getResources().getDrawable(R.drawable.img10))
	    		.setContent(R.id.textview2));
	   // mTabHost.addTab(mTabHost.newTabSpec("tab_test3")
	    //		.setIndicator("TAB 3",getResources().getDrawable(R.drawable.img3))
	    	//	.setContent(R.id.textview3));
	    
	    //����TabHost�ı�����ɫ
	    mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    //����TabHost�ı���ͼƬ��Դ
	    //mTabHost.setBackgroundResource(R.drawable.bg0);
	    
	    //���õ�ǰ��ʾ��һ����ǩ
	    mTabHost.setCurrentTab(0);
	    
	   
	    //��ǩ�л��¼�����setOnTabChangedListener 
	    
	    mTabHost.setOnTabChangedListener(new OnTabChangeListener()
	    {
	    	// TODO Auto-generated method stub
            @Override
            public void onTabChanged(String tabId) 
            {
            	
            	
            	if(tabId.equals("Instruction")){
            		
            		String str="ʹ��˵����";
            		tv.setText(str);
            	}
            		if(tabId.equals("About us")){
            			String str1="�������ǣ�";
            			tv2.setText(str1);
            	}
            	/*
  	    	  	Dialog dialog = new AlertDialog.Builder(Instruction.this)
  	    	  			.setTitle("��ʾ")
  	    	  			.setMessage("��ǰѡ�У�"+tabId+"��ǩ")
  	    	  			.setPositiveButton("ȷ��",
  	    	  			new DialogInterface.OnClickListener() 
  	    	  			{
  	    	  				public void onClick(DialogInterface dialog, int whichButton)
  	    	  				{
  	    	  					dialog.cancel();
  	    	  				}
  	    	  			}).create();//������ť
	    	  
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
