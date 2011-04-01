package com.yarin.android.Orienteering_android;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity
	{
		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);

			//���Gallery����
			Gallery g = (Gallery) findViewById(R.id.Gallery01);
			//���ImageAdapter��Gallery����
			g.setAdapter(new ImageAdapter(this));

			//����Gallery�ı���
			g.setBackgroundResource(R.drawable.bg0);
				
			//����Gallery���¼�����
			g.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id)
				{
					switch(position) 
					{
					case 0:
						//��ѡ���һ��ͼ��ʱ����Site����
						Intent intent =new Intent();
						intent.setClass(Main.this, Set_Site.class);
						
						startActivity(intent);
						Main.this.finish();
						break;
						
					case 1:
						//��ѡ���һ��ͼ��ʱ���붨��ԽҰ��
						Intent intent2 =new Intent();
						intent2.setClass(Main.this, Check.class);
						startActivity(intent2);
						Main.this.finish();
						break;
					case 2:
						Intent intent3=new Intent();
						intent3.setClass(Main.this, Instruction.class);
						startActivity(intent3);
						Main.this.finish();
						break;
						
					}
					}
				});
			}
		
	}
	
