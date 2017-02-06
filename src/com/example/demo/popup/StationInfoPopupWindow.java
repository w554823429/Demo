package com.example.demo.popup;

import com.example.demo.R;
import com.example.demo.RouteActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;

public class StationInfoPopupWindow implements OnClickListener {
	private Context context;
	private PopupWindow popupWindow;
    //到这里去按钮
	private Button goBtn;

	public StationInfoPopupWindow(final Context context) {
		this.context = context;

		View view = LayoutInflater.from(context).inflate(R.layout.view_map_popup_window, null);

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources()));
		goBtn = (Button) view.findViewById(R.id.go_to_hotel_btn);
		goBtn.setOnClickListener(this);
	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		// 保证尺寸是根据屏幕像素密度来的
		popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(false);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(false);
		// 设置动画
//		popupWindow.setAnimationStyle(R.style.PopupWindowAnimStyle);
		// 刷新状态
		popupWindow.update();
	}

	public void setDismissListener(OnDismissListener onDismissListener) {
		popupWindow.setOnDismissListener(onDismissListener);
	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	// 是否显示
	public boolean isShowing() {
		return popupWindow.isShowing();
	}

	@Override
	public void onClick(View v) {
		if (v == goBtn) {
                        //这里跳转到路径规划界面
			Intent intent = new Intent(context, RouteActivity.class);
			context.startActivity(intent);

		}
	}
}
