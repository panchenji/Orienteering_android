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
      //ȡ��GridView����
		GridView gridview = (GridView) findViewById(R.id.gridview);
		//���Ԫ�ظ�gridview
		gridview.setAdapter(new ImageAdapter(this));

		// ����Gallery�ı���
		gridview.setBackgroundResource(R.drawable.bg0);

		//�¼�����
		gridview.setOnItemClickListener(new OnItemClickListener() {
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
				}
				
				//Toast.makeText(Main.this, "��ѡ����" + (position + 1) + " ��ͼƬ", Toast.LENGTH_SHORT).show();
			}
			
		});
		/*
		public boolean onCreateOptionsMenu(Menu menu)
		{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menu, menu);
			return true;
		}

		//����menu���¼�
		public boolean onOptionsItemSelected(MenuItem item)
		{
			//�õ���ǰѡ�е�MenuItem��ID,
			int item_id = item.getItemId();

			switch (item_id)
			{
				case 0:
				case 1:
					//�½�һ��Intent���� 
					Intent intent = new Intent();
					// ָ��intentҪ�������� 
					intent.setClass(Activity02.this, Activity01.class);
					// ����һ���µ�Activity 
					startActivity(intent);
					// �رյ�ǰ��Activity 
					Activity02.this.finish();
					break;
			}
			return true;
		}*/
    }
}