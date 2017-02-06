package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.example.demo.bean.AnwerInfo;
import com.example.demo.fragment.ReadFragment;
import com.example.demo.view.ReaderViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ViewpagerActivity extends FragmentActivity {
    private ReaderViewPager readerViewPager;
    private List<AnwerInfo.DataBean.SubDataBean> datas;
    private ImageView shadowView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager);
		AnwerInfo anwerInfo =  getAnwer();

        datas = anwerInfo.getData().getData();
        
        initReadViewPager();
        
        Button bt_pre = (Button) findViewById(R.id.bt_pre);
        Button bt_next = (Button) findViewById(R.id.bt_next);
        
        bt_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 int currentItem = readerViewPager.getCurrentItem();
                currentItem = currentItem-1;
                if (currentItem>datas.size()-1){
                    currentItem=datas.size()-1;
                }
                readerViewPager.setCurrentItem(currentItem,true);
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = readerViewPager.getCurrentItem();
                currentItem = currentItem+1;
                if (currentItem<0){
                    currentItem=0;
                }
                readerViewPager.setCurrentItem(currentItem,true);
            }
        });
	}
	
	private void initReadViewPager() {
        shadowView = (ImageView) findViewById(R.id.iv_shadowView);
        readerViewPager = (ReaderViewPager) findViewById(R.id.readerViewPager);


        readerViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                AnwerInfo.DataBean.SubDataBean subDataBean = datas.get(position);

                return ReadFragment.newInstance(subDataBean);
            }

            @Override
            public int getCount() {
                return datas.size();
            }
        });


        readerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                shadowView.setTranslationX(readerViewPager.getWidth()-positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
	
	private AnwerInfo getAnwer() {

        try {
            InputStream in = getAssets().open("anwer.json");
            AnwerInfo anwerInfo = JSON.parseObject(inputStream2String(in), AnwerInfo.class);

            return anwerInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("data.size=", e.toString());
        }

        return null;
    }
	public String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

}
