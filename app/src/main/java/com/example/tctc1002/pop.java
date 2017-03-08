package com.example.tctc1002;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.isweixin.R;

public class pop extends PopupWindow {

	private Button btn_q, btn_w, btn_cancel;
	private View mMenuView;

	public pop(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.activity_pop_windows, null);
		btn_q = (Button) mMenuView.findViewById(R.id.button_q);
		btn_w = (Button) mMenuView.findViewById(R.id.button_w);
		btn_cancel = (Button) mMenuView.findViewById(R.id.button_s);
		// ȡ����ť
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// ���ٵ�����
				dismiss();
			}
		});
		btn_q.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(pop.this, "123", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		// ���ð�ť����
		btn_q.setOnClickListener(itemsOnClick);
		btn_w.setOnClickListener(itemsOnClick);
		// ����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.FILL_PARENT);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// ����SelectPicPopupWindow�������嶯��Ч��
//		this.setAnimationStyle(R.style.AnimBottom);
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// ����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		// mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}