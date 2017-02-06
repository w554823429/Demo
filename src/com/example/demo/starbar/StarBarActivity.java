package com.example.demo.starbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.amap.api.location.c;
import com.example.demo.R;
import com.example.demo.starbar.StarBar.OnStarChangeListener;
import com.example.demo.tagView.FlowLayout;
import com.example.demo.tagView.TagAdapter;
import com.example.demo.tagView.TagFlowLayout;
import com.example.demo.tagView.TagFlowLayout.OnTagClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class StarBarActivity extends Activity {
	
	private TextView tv_info;
	private TagFlowLayout id_flowlayout;
	
	private String[] mVals = new String[]
            {"车干净", "教练人好", "比较准时", "基地好找", "近地铁"};
	private String[] mVals2 = new String[]
            {"教练邋遢", "态度恶劣", "迟到", "喜欢抽烟"};
	
	private TagAdapter<String> adapter1=null;
	private TagAdapter<String> adapter2=null;
	
	private Set<Integer> selectPosSet=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starbar);
		final LayoutInflater mInflater = LayoutInflater.from(this);
		StarBar starBar=(StarBar) findViewById(R.id.starBar);
		tv_info=(TextView) findViewById(R.id.tv_info);
		id_flowlayout=(TagFlowLayout) findViewById(R.id.id_flowlayout);
		starBar.setStarMark(0.5f);
		starBar.setIntegerMark(true);
		starBar.setOnStarChangeListener(new OnStarChangeListener() {
			
			@Override
			public void onStarChange(float mark) {
				tv_info.setText(mark+"");
				if(mark>2.0){
					if(adapter1==null){
						adapter1=new TagAdapter<String>(mVals){

							@Override
							public View getView(FlowLayout parent, int position,
									String t) {
								TextView tv = (TextView) mInflater.inflate(R.layout.tv,
										id_flowlayout, false);
				                tv.setText(t);
								return tv;
							}};
					}
					id_flowlayout.setAdapter(adapter1);
					if(selectPosSet!=null){
						if(adapter2!=null){
							selectPosSet.clear();
						}
						adapter1.setSelectedList(selectPosSet);
					}
					adapter2=null;
//					adapter1.notifyDataChanged();
				}else{
					if(adapter2==null){
						adapter2=new TagAdapter<String>(mVals2){
							@Override
							public View getView(FlowLayout parent, int position,
									String t) {
								TextView tv = (TextView) mInflater.inflate(R.layout.tv,
										id_flowlayout, false);
				                tv.setText(t);
								return tv;
							}};
					}
					id_flowlayout.setAdapter(adapter2);
					if(selectPosSet!=null){
						if(adapter1!=null){
							selectPosSet.clear();
						}
						adapter2.setSelectedList(selectPosSet);
					}
					adapter1=null;
//					adapter2.notifyDataChanged();
				}
			}
		});
		starBar.setOnTouchListener(null);
		id_flowlayout.setOnTagClickListener(new OnTagClickListener() {
			
			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		id_flowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener(){
            @Override
            public void onSelected(Set<Integer> selectPos){
            	selectPosSet=selectPos;
            	tv_info.setText("choose:" + selectPosSet.toString());
            }
        });
	}
	
}
