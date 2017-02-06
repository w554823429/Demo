package com.example.demo.tagView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.view.View;

public abstract class TagAdapter<T> {
	
	private List<T> mTagDatas;
    private OnDataChangedListener mOnDataChangedListener;
    private HashSet<Integer> mCheckedPosList = new HashSet<Integer>();
    
    public TagAdapter(List<T> datas){
        mTagDatas = datas;
    }

    public TagAdapter(T[] datas){
        mTagDatas = new ArrayList<T>(Arrays.asList(datas));
    }
    
    public static interface OnDataChangedListener{
        void onChanged();
    }

    public void setOnDataChangedListener(OnDataChangedListener listener){
        mOnDataChangedListener = listener;
    }
    
    public void setSelectedList(int... poses)
    {
        Set<Integer> set = new HashSet<Integer>();
        for (int pos : poses)
        {
            set.add(pos);
        }
        setSelectedList(set);
    }

    public void setSelectedList(Set<Integer> set)
    {
        mCheckedPosList.clear();
        if (set != null)
            mCheckedPosList.addAll(set);
        notifyDataChanged();
    }

    public HashSet<Integer> getPreCheckedList()
    {
        return mCheckedPosList;
    }

    public int getCount(){
        return mTagDatas == null ? 0 : mTagDatas.size();
    }

    public void notifyDataChanged(){
        mOnDataChangedListener.onChanged();
    }

    public T getItem(int position){
        return mTagDatas.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);
    
    public boolean setSelected(int position, T t){
//    	if(t.equals([position])){
    	Iterator<Integer> it = mCheckedPosList.iterator();
    	String srtString = "";
    	while (it.hasNext()) {
    		srtString=mTagDatas.get(it.next())+",";
    	}
    	if(srtString.contains((CharSequence)t)){
    		return true;
    	}else{
    		return false;
    	}
    }

}
