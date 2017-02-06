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
    //������ȥ��ť
	private Button goBtn;

	public StationInfoPopupWindow(final Context context) {
		this.context = context;

		View view = LayoutInflater.from(context).inflate(R.layout.view_map_popup_window, null);

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı�����������ģ�
		popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources()));
		goBtn = (Button) view.findViewById(R.id.go_to_hotel_btn);
		goBtn.setOnClickListener(this);
	}

	// ����ʽ ���� pop�˵� parent ���½�
	public void showAsDropDown(View parent) {
		// ��֤�ߴ��Ǹ�����Ļ�����ܶ�����
		popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		// ʹ��ۼ�
		popupWindow.setFocusable(false);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(false);
		// ���ö���
//		popupWindow.setAnimationStyle(R.style.PopupWindowAnimStyle);
		// ˢ��״̬
		popupWindow.update();
	}

	public void setDismissListener(OnDismissListener onDismissListener) {
		popupWindow.setOnDismissListener(onDismissListener);
	}

	// ���ز˵�
	public void dismiss() {
		popupWindow.dismiss();
	}

	// �Ƿ���ʾ
	public boolean isShowing() {
		return popupWindow.isShowing();
	}

	@Override
	public void onClick(View v) {
		if (v == goBtn) {
                        //������ת��·���滮����
			Intent intent = new Intent(context, RouteActivity.class);
			context.startActivity(intent);

		}
	}
}
