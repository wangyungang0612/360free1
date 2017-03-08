package com.example.isweixin;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.example.isweixin.tc1.CallLogsContentObserver;
import com.example.isweixin.tc1.ChatActivity;
import com.example.isweixin.tc1.ContactsContentObserver;
import com.example.isweixin.tc1.SmssContentObserver;
import com.example.isweixin.tc3.Utils;
import com.example.isweixin.tc3.Utils.CallLogs;
import com.example.isweixin.tc3.Utils.Person_Sms;
import com.example.isweixin.tc3.Utils.Persons;
import com.example.isweixin.tc4.AddContactsActivity;
import com.example.isweixin.use.LocationModeSourceActivity;
import com.example.tctc1002.AlphabetScrollBar;
import com.example.tctc1002.pop;

public class MainActivity extends Activity implements OnViewChangeListener,
		OnClickListener {
	private MyScrollLayout mScrollLayout;
	private LinearLayout[] mImageViews;
	private int mViewCount;
	private int mCurSel;
	private TextView liaotian;
	private TextView faxian;
	private TextView tongxunlu;
	private TextView usecenter;
	private boolean isOpen = false;
	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow; // 弹出框
	SelectAddPopupWindow menuWindow2; // 弹出框

	private ListView m_contactslist;

	public static ContactsAdapter m_contactsadapter;
	// 搜索过滤联系人EditText
	private EditText m_FilterEditText;
	// 没有匹配联系人时显示的TextView
	private TextView m_listEmptyText;

	private Button m_AddContactBtn;

	private Button m_RemoveSameContactBtn;
	// 最上面的layout
	private FrameLayout m_topcontactslayout;
	// 联系人内容观察者
	private ContactsContentObserver ContactsCO;

	private String ChooseContactName;

	private String ChooseContactNumber;

	private String ChooseContactID; // 联系人列表选中的名字、号码、id
	// 加载对话框
	ProgressDialog m_dialogLoading;
	// 字母列视图View
	private AlphabetScrollBar m_asb;
	// 显示选中的字母
	private TextView m_letterNotice;

	private ListView m_smsslist;

	public static SmsAdapter m_smsadapter;

	private SmssContentObserver SmssCO; // 短信息内容观察者

	private ListView m_calllogslist;

	public static CallLogsAdapter m_calllogsadapter;

	private CallLogsContentObserver CallLogsCO;// 通话记录内容观察者

	private EditText EditNum; // 号码编辑框

	private Button BtnBackSpace; // 退格按键
	private LinearLayout jianpan;
	private Button button, button2, button3, bohao_btn, button_shouqi,
			bohao_btn4, button_q;

	private pop menuWindow1;
	TelephonyManager tManager;

	// ************************下面为主函数***************************//
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();

		// 声明Utils里面的函數
		Utils.init(this);
		Utils.getContacts();
		Utils.getCallLogs();
		Utils.getSmss();

		tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		jianpan = (LinearLayout) findViewById(R.id.shuzijianpan);
		button = (Button) findViewById(R.id.button188); // 地图定位按钮
		button2 = (Button) findViewById(R.id.button189);
		button3 = (Button) findViewById(R.id.button3);
		bohao_btn = (Button) findViewById(R.id.bohao_btn);
		EditNum = (EditText) findViewById(R.id.shuzi_edit); // 号码编辑框
		button_shouqi = (Button) findViewById(R.id.button_shouqi);
		bohao_btn4 = (Button) findViewById(R.id.bohao_btn4);

		// 拿到数字键盘的按键
		findViewById(R.id.shuzi1).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi2).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi3).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi4).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi5).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi6).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi7).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi8).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi9).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi0).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi_xing).setOnClickListener(new BtnClick2());
		findViewById(R.id.shuzi_jing).setOnClickListener(new BtnClick2());
		findViewById(R.id.dadianhua).setOnClickListener(new BtnClick2());

		BtnBackSpace = (Button) findViewById(R.id.shuzi_shanchubtn);
		BtnBackSpace.setOnClickListener(new BtnClick2());

		m_listEmptyText = (TextView) findViewById(R.id.nocontacts_notice);
		m_AddContactBtn = (Button) findViewById(R.id.add_contacts);// 新增联系人按钮
		m_topcontactslayout = (FrameLayout) findViewById(R.id.top_contacts_layout);
		m_FilterEditText = (EditText) findViewById(R.id.pb_search_edit);// 初始化搜索编辑框,设置文本改变时的监听器

		// 注册更新联系人的广播,用来实时更新联系人listview
		IntentFilter filter = new IntentFilter();
		filter.addAction("huahua.action.UpdataContactsView");
		MainActivity.this.registerReceiver(mReceiver, filter);
		// 注册更新短信的广播，用来实时更新短信listview
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("huahua.action.UpdataSmssView");
		MainActivity.this.registerReceiver(mReceiver2, filter2);
		// 注册更新通话记录的广播,用来实时更新通话记录listview
		IntentFilter filter3 = new IntentFilter();
		filter3.addAction("huahua.action.UpdataCallLogsView");// 自己改了filter為filter3
		MainActivity.this.registerReceiver(mReceiver3, filter3);

		IntentFilter filter4 = new IntentFilter();
		filter4.addAction("huahua.action.deleteCallLogsView");// 
		MainActivity.this.registerReceiver(mReceiver4, filter4);

		PhoneStateListener pStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// TODO Auto-generated method stub
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:// 空闲状态不做处理
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:// 接起电话不做处理
					break;
				case TelephonyManager.CALL_STATE_RINGING:// 正在响铃
					// 如果来电号码在黑名单中则自动挂断
					if ((incomingNumber).equals("15695555683")) {
						try {
							// 获取android.os.ServiceManager类的对象的getService()方法
							Method method = Class.forName(
									"android.os.ServiceManager").getMethod(
									"getService", String.class);
							// 获取远程TELEPHONY_SERVICE的IBinder对象的代理
							IBinder binder = (IBinder) method.invoke(null,
									new Object[] { TELEPHONY_SERVICE });
							// 将IBinder对象的代理转换为ITelephony对象
							ITelephony telephony = ITelephony.Stub
									.asInterface(binder);
							// 挂断电话
							telephony.endCall();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}
		};

		// 为TelephonyManager添加监听器
		tManager.listen(pStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		bohao_btn.setOnClickListener(new OnClickListener() {
			// 设置数字键盘的可见和不可见
			public void onClick(View arg0) {

				if (jianpan.getVisibility() != 0) {
					jianpan.setVisibility(View.VISIBLE);
				} else {

					jianpan.setVisibility(View.GONE);
				}
			}
		});

		button_shouqi.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				jianpan.setVisibility(View.GONE); // 完成收起数字键盘
			}
		});

		button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,
						LocationModeSourceActivity.class);
				startActivity(intent); // 跳转到定位activity

			}
		});

		bohao_btn4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 实例化SelectPicPopupWindow
				menuWindow1 = new pop(MainActivity.this, itemsOnClick);
				// 显示窗口
				int xoff = menuWindow1.getWidth() / 2 - v.getWidth();

				menuWindow1.showAtLocation(v, Gravity.CENTER, 0, 0); // 设置layout在Popu
				// //CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL

			}
		});

		ContactsCO = new ContactsContentObserver(new Handler());
		MainActivity.this.getContentResolver().registerContentObserver(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, false,
				ContactsCO);

		// 得到字母列的对象,并设置触摸响应监听器
		m_asb = (AlphabetScrollBar) findViewById(R.id.alphabetscrollbar);
		m_asb.setOnTouchBarListener(new ScrollBarListener());
		m_letterNotice = (TextView) findViewById(R.id.pb_letter_notice);
		m_asb.setTextView(m_letterNotice);

		// 得到联系人列表,并设置适配器
		m_contactslist = (ListView) findViewById(R.id.pb_listvew);
		m_contactsadapter = new ContactsAdapter(MainActivity.this,
				Utils.persons);
		m_contactslist.setAdapter(m_contactsadapter);
		// 联系人列表长按监听
		m_contactslist
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {

						Vibrator vib = (Vibrator) MainActivity.this
								.getSystemService(Service.VIBRATOR_SERVICE);
						vib.vibrate(50); // 手机振动服务
						// 用来判断是否是过滤后的联系人
						if (m_topcontactslayout.getVisibility() == View.VISIBLE) {
							ChooseContactName = Utils.persons.get(arg2).Name; // 判断出选择的名字
							ChooseContactNumber = Utils.persons.get(arg2).Number;// 判断出选择的号码
							ChooseContactID = Utils.persons.get(arg2).ID;// 判断出选择的id
						} else {
							ChooseContactName = Utils.filterpersons.get(arg2).Name;// 判断出选择的名字(过滤后的)
							ChooseContactNumber = Utils.filterpersons.get(arg2).Number;// 判断出选择的号码(过滤后的)
							ChooseContactID = Utils.filterpersons.get(arg2).ID;// 判断出选择的id(过滤后的)
						}

						final Person_Sms personsms = new Person_Sms(); // Utils里面的Person_Sms
						personsms.Name = ChooseContactName; // 选中的联系人名字(用来发短信)
						personsms.Number = ChooseContactNumber; // 选中的联系人号码(用来发短信)
						for (int i = 0; i < Utils.persons_sms.size(); i++) {
							if (personsms.Name.equals(Utils.persons_sms.get(i).Name)) {
								personsms.person_smss = Utils.persons_sms
										.get(i).person_smss;
								break;
							}
						}

						AlertDialog ListDialog = new AlertDialog.Builder(
								MainActivity.this)
								.setTitle(ChooseContactName)
								.setItems(
										new String[] { "拨号", "短信", "删除联系人",
												"编辑联系人", "查看详细信息" },
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												if (which == 0) {
													Intent intent = new Intent(
															Intent.ACTION_CALL,
															Uri.parse("tel://"
																	+ ChooseContactNumber));
													startActivity(intent);
												} else if (which == 1) {
													Intent intent = new Intent(
															MainActivity.this,
															ChatActivity.class);
													Bundle mBundle = new Bundle();
													mBundle.putSerializable(
															"chatperson",
															personsms);
													intent.putExtras(mBundle);
													startActivity(intent);
												} else if (which == 2) {
													AlertDialog DeleteDialog = new AlertDialog.Builder(
															MainActivity.this)
															.setTitle("删除")
															.setMessage(
																	"删除联系人"
																			+ ChooseContactName
																			+ "?")
															.setPositiveButton(
																	"确定",
																	new DialogInterface.OnClickListener() {

																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			// 删除联系人操作,放在线程中处理
																			new DeleteContactTask()
																					.execute();
																		}
																	})
															.setNegativeButton(
																	"取消",
																	new DialogInterface.OnClickListener() {

																		public void onClick(
																				DialogInterface dialog,
																				int which) {

																		}
																	}).create();
													DeleteDialog.show();
												} else if (which == 3) { // 编辑联系人操作
													Bundle bundle = new Bundle();
													bundle.putInt("tpye", 1);
													bundle.putString("id",
															ChooseContactID);
													bundle.putString("name",
															ChooseContactName);
													bundle.putString("number",
															ChooseContactNumber);

													Intent intent = new Intent(
															MainActivity.this,
															AddContactsActivity.class);
													intent.putExtra("person",
															bundle);
													startActivity(intent);
												} else if (which == 4) { //
													Bundle bundle = new Bundle();
													bundle.putInt("tpye", 1);
													bundle.putString("id",
															ChooseContactID);
													bundle.putString("name",
															ChooseContactName);
													bundle.putString("number",
															ChooseContactNumber);

													Intent intent = new Intent(
															MainActivity.this,
															Contacts_listActivity.class);
													intent.putExtra("person",
															bundle);
													startActivity(intent);
												}
											}
										}).create();

						ListDialog.show();

						return false;
					}
				});

		m_AddContactBtn.setOnClickListener(new BtnClick());

		// 根据输入的名字来过滤联系人
		m_FilterEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (!"".equals(s.toString().trim())) {
					// 根据编辑框的关键字来过滤联系人并更新联系列表
					Utils.filterContacts(s.toString().trim());
					m_asb.setVisibility(View.GONE);
					m_topcontactslayout.setVisibility(View.GONE);
				} else {
					m_topcontactslayout.setVisibility(View.VISIBLE);
					m_asb.setVisibility(View.VISIBLE);
					m_contactsadapter.updateListView(Utils.persons);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {

			}
		});

		SmssCO = new SmssContentObserver(new Handler()); // 短消息的内容观察者
		MainActivity.this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), false, SmssCO);

		m_smsslist = (ListView) findViewById(R.id.sms_list);// 放短信列表的listview
		m_smsadapter = new SmsAdapter(MainActivity.this, Utils.persons_sms);
		m_smsslist.setAdapter(m_smsadapter);
		m_smsslist.setOnItemClickListener(new OnItemClickListener() {
			// 短信列表点击监听
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(MainActivity.this,
						ChatActivity.class); // 跳转到聊天界面

				Bundle mBundle = new Bundle();

				mBundle.putSerializable("chatperson",
						Utils.persons_sms.get(arg2)); // 放上要传送的值：要聊天的人的名字
				intent.putExtras(mBundle);
				startActivity(intent);

			}
		});

		// 通话内容观察者
		CallLogsCO = new CallLogsContentObserver(new Handler());
		MainActivity.this.getContentResolver().registerContentObserver(
				CallLog.Calls.CONTENT_URI, false, CallLogsCO);

		// 通话记录列表
		m_calllogslist = (ListView) findViewById(R.id.calllogs_list);
		m_calllogsadapter = new CallLogsAdapter(MainActivity.this,
				Utils.calllogs); //
		m_calllogslist.setAdapter(m_calllogsadapter);

		if (android.os.Build.VERSION.SDK_INT <= 10) {
			EditNum.setInputType(InputType.TYPE_NULL); // edittext不显示软键盘,要显示光标
		} else {
			MainActivity.this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setSoftInputShownOnFocus;
				setSoftInputShownOnFocus = cls.getMethod(
						"setShowSoftInputOnFocus", boolean.class);
				setSoftInputShownOnFocus.setAccessible(true);
				setSoftInputShownOnFocus.invoke(EditNum, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		BtnBackSpace.setOnLongClickListener(new OnLongClickListener() {
			// 设置退格键长按清空输入电话号码
			public boolean onLongClick(View v) {
				Vibrator vib = (Vibrator) MainActivity.this
						.getSystemService(Service.VIBRATOR_SERVICE);
				vib.vibrate(50); // 触发手机震动

				EditNum.setText("");
				return false;
			}
		});
	}

	private class BtnClick2 implements View.OnClickListener {

		public void onClick(View v) {
			if (v.getId() == R.id.shuzi1) {
				keyPressed(KeyEvent.KEYCODE_1);
			} else if (v.getId() == R.id.shuzi2) {
				keyPressed(KeyEvent.KEYCODE_2);
			} else if (v.getId() == R.id.shuzi3) {
				keyPressed(KeyEvent.KEYCODE_3);
			} else if (v.getId() == R.id.shuzi4) {
				keyPressed(KeyEvent.KEYCODE_4);
			} else if (v.getId() == R.id.shuzi5) {
				keyPressed(KeyEvent.KEYCODE_5);
			} else if (v.getId() == R.id.shuzi6) {
				keyPressed(KeyEvent.KEYCODE_6);
			} else if (v.getId() == R.id.shuzi7) {
				keyPressed(KeyEvent.KEYCODE_7);
			} else if (v.getId() == R.id.shuzi8) {
				keyPressed(KeyEvent.KEYCODE_8);
			} else if (v.getId() == R.id.shuzi9) {
				keyPressed(KeyEvent.KEYCODE_9);
			} else if (v.getId() == R.id.shuzi0) {
				keyPressed(KeyEvent.KEYCODE_0);
			} else if (v.getId() == R.id.shuzi_xing) {
				keyPressed(KeyEvent.KEYCODE_STAR);
			} else if (v.getId() == R.id.shuzi_jing) {
				keyPressed(KeyEvent.KEYCODE_POUND);
			} else if (v.getId() == R.id.dadianhua) {
				if (EditNum.length() != 0) {
					Intent intent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel://" + EditNum.getText().toString()));
					startActivity(intent);
				} else {
					Toast.makeText(MainActivity.this, "请输入号码",
							Toast.LENGTH_SHORT).show();
				}
			} else if (v.getId() == R.id.shuzi_shanchubtn) {
				keyPressed(KeyEvent.KEYCODE_DEL);
			}

		}
	}

	private void keyPressed(int keyCode) {
		KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
		EditNum.onKeyDown(keyCode, event);
	}

	// *********************以上为对数字键盘实行监听********************//

	BroadcastReceiver mReceiver3 = new BroadcastReceiver() {
		// 此广播用来更新通话记录列表
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("huahua.action.UpdataCallLogsView")) {
				m_calllogsadapter.updateListView(Utils.calllogs);
			}
		}
	};
	BroadcastReceiver mReceiver4 = new BroadcastReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("huahua.action.deleteCallLogsView")) {
				Utils.deleteCallLogs();
			}
		}
	};

	public class CallLogsAdapter extends BaseAdapter {
		private LayoutInflater m_inflater;
		private ArrayList<CallLogs> calllogs;
		private Context context;

		public CallLogsAdapter(Context context, ArrayList<CallLogs> calllogs) {
			this.m_inflater = LayoutInflater.from(context);
			this.calllogs = calllogs;
			this.context = context;
		}

		// *******************以下为动态更新通话记录的办法******************//

		// 当通话记录列表数据要完成动态更新时,用此方法来更新列表
		public void updateListView(ArrayList<CallLogs> calllogs) {
			this.calllogs = calllogs;
			notifyDataSetChanged();
		}

		public int getCount() {

			return calllogs.size();
		}

		public Object getItem(int arg0) {
			return calllogs.get(arg0);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = m_inflater.inflate(R.layout.calllogs_list_item,
						null);
			}

			if (calllogs.isEmpty()) {
				return convertView;
			}

			// 通话记录的姓名
			TextView name = (TextView) convertView
					.findViewById(R.id.calllog_name);

			name.setText(calllogs.get(position).Name);

			if (calllogs.get(position).Name == null) {
				name.setText("未知号码");
			} else {
				name.setText(calllogs.get(position).Name);
			}

			// 通话记录的电话状态
			TextView Type = (TextView) convertView
					.findViewById(R.id.calllog_type);
			if (calllogs.get(position).Type == CallLog.Calls.INCOMING_TYPE) {
				Type.setText("已接来电");
				Type.setTextColor(Color.rgb(0, 0, 255));
			} else if (calllogs.get(position).Type == CallLog.Calls.OUTGOING_TYPE) {
				Type.setText("拨出号码");
				Type.setTextColor(Color.rgb(0, 150, 0));
			} else if (calllogs.get(position).Type == CallLog.Calls.MISSED_TYPE) {
				Type.setText("未接来电");
				Type.setTextColor(Color.rgb(255, 0, 0));
			}

			// 通话记录的号码
			TextView number = (TextView) convertView
					.findViewById(R.id.calllog_number);
			number.setText(calllogs.get(position).Number);

			// 通话记录的日期
			TextView data = (TextView) convertView
					.findViewById(R.id.calllog_data);

			Date date2 = new Date(Long.parseLong(calllogs.get(position).Data));
			SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd");
			String time = sfd.format(date2);
			data.setText(time);

			Button dialBtn = (Button) convertView
					.findViewById(R.id.calllog_dial);
			dialBtn.setTag(calllogs.get(position).Number);
			dialBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel://" + (String) arg0.getTag()));
					startActivity(intent);
				}
			});

			return convertView;
		}

	}

	public void onDestroy3() {
		MainActivity.this.unregisterReceiver(mReceiver3);
		super.onDestroy();
	}

	// ****************************************************//

	// *******************以下为动态更新短信记录的办法******************//

	BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
		// 此广播用来动态更新短信记录
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("huahua.action.UpdataSmssView")) {
				m_smsadapter.updateListView(Utils.persons_sms);
			}
		}
	};

	public class SmsAdapter extends BaseAdapter {// 短信适配器，拿到短信的数据
		private LayoutInflater m_inflater;
		private ArrayList<Person_Sms> persons_sms;
		private Context context;

		public SmsAdapter(Context context, ArrayList<Person_Sms> persons_sms) {
			this.m_inflater = LayoutInflater.from(context);
			this.persons_sms = persons_sms;
			this.context = context;
		}

		public void updateListView(ArrayList<Person_Sms> persons_sms) {
			this.persons_sms = persons_sms;
			notifyDataSetChanged();
		}

		public int getCount() {

			return persons_sms.size();
		}

		public Object getItem(int arg0) {

			return persons_sms.get(arg0);
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = m_inflater.inflate(R.layout.sms_list_item, null);
			}

			TextView sms_name = (TextView) convertView
					.findViewById(R.id.sms_name);
			sms_name.setText(persons_sms.get(position).Name + "("
					+ persons_sms.get(position).person_smss.size() + ")");

			TextView sms_content = (TextView) convertView
					.findViewById(R.id.sms_content);
			sms_content
					.setText(persons_sms.get(position).person_smss.get(0).SMSContent);

			TextView sms_data = (TextView) convertView
					.findViewById(R.id.sms_date);
			Date date = new Date(
					persons_sms.get(position).person_smss.get(0).SMSDate);
			SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd");
			String time = sfd.format(date);
			sms_data.setText(time);

			return convertView;
		}

	}

	public void onDestroy2() {
		MainActivity.this.unregisterReceiver(mReceiver2);
		super.onDestroy();
	}

	// ********************** 删除联系人的线程 *********************//
	class DeleteContactTask extends AsyncTask<Void, Integer, Void> {

		protected Void doInBackground(Void... params) {
			Utils.DeleteContact(ChooseContactID);
			return null;
		}

		protected void onPostExecute(Void result) {
			if (m_dialogLoading != null) {
				m_dialogLoading.dismiss();
			}
		}

		protected void onPreExecute() {
			m_dialogLoading = new ProgressDialog(MainActivity.this);
			m_dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
			m_dialogLoading.setMessage("正在删除");
			m_dialogLoading.setCancelable(false);
			m_dialogLoading.show();
		}

		protected void onProgressUpdate(Integer... values) {
			Log.i("huahua", "onProgressUpdate");
		}

	}

	// *****************用来接收更新联系人列表的广播接收者*************//
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("huahua.action.UpdataContactsView")) {
				if (m_topcontactslayout.getVisibility() == View.VISIBLE) {
					m_contactsadapter.updateListView(Utils.persons);
				} else {
					// 如果没有匹配的联系人
					if (Utils.filterpersons.isEmpty()) {
						m_contactslist.setEmptyView(m_listEmptyText);
					}

					m_contactsadapter.updateListView(Utils.filterpersons);
				}

			}
		}
	};

	private class BtnClick implements View.OnClickListener {
		// 增加联系人的按钮
		public void onClick(View v) {
			if (v == m_AddContactBtn) {
				Bundle bundle = new Bundle(); // 放上要传的值
				bundle.putInt("tpye", 0);
				bundle.putString("name", "");
				bundle.putString("number", "");
				Intent intent = new Intent(MainActivity.this,
						AddContactsActivity.class);// 跳转到增加联系人activity
				intent.putExtra("person", bundle);
				startActivity(intent);
			}
		}
	}

	public class ContactsAdapter extends BaseAdapter {
		private LayoutInflater m_inflater;
		private ArrayList<Persons> persons;
		private Context context;

		public ContactsAdapter(Context context, ArrayList<Persons> persons) {
			this.m_inflater = LayoutInflater.from(context);
			this.persons = persons;
			this.context = context;
		}

		// 当联系人列表数据发生变化时,用此方法来更新列表
		public void updateListView(ArrayList<Persons> persons) {
			this.persons = persons;
			notifyDataSetChanged();
		}

		public int getCount() {

			return persons.size();
		}

		public Object getItem(int position) {

			return persons.get(position);
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = m_inflater.inflate(R.layout.contacts_list_item,
						null);
			}

			if (persons.isEmpty()) {

				return convertView;
			}

			TextView name = (TextView) convertView
					.findViewById(R.id.contacts_name);
			name.setText(persons.get(position).Name);

			TextView number = (TextView) convertView
					.findViewById(R.id.contacts_number);
			number.setText(persons.get(position).Number);

			// 字母提示textview的显示
			TextView letterTag = (TextView) convertView
					.findViewById(R.id.pb_item_LetterTag);
			// 获得当前姓名的拼音首字母
			String firstLetter = persons.get(position).PY.substring(0, 1)
					.toUpperCase();

			// 如果是第1个联系人 那么letterTag始终要显示
			if (position == 0) {
				letterTag.setVisibility(View.VISIBLE);
				letterTag.setText(firstLetter);
			} else {
				// 获得上一个姓名的拼音首字母
				String firstLetterPre = persons.get(position - 1).PY.substring(
						0, 1).toUpperCase();
				// 比较一下两者是否相同
				if (firstLetter.equals(firstLetterPre)) {
					letterTag.setVisibility(View.GONE);
				} else {
					letterTag.setVisibility(View.VISIBLE);
					letterTag.setText(firstLetter);
				}
			}

			return convertView;
		}

	}

	// 字母列触摸的监听器
	private class ScrollBarListener implements
			AlphabetScrollBar.OnTouchBarListener {

		@Override
		public void onTouch(String letter) {

			// 触摸字母列时,将联系人列表更新到首字母出现的位置
			for (int i = 0; i < Utils.persons.size(); i++) {
				if (Utils.persons.get(i).PY.substring(0, 1)
						.compareToIgnoreCase(letter) == 0) {
					m_contactslist.setSelection(i);
					break;
				}
			}
		}
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			if (v.getId() == R.id.button_q) {
				Toast.makeText(MainActivity.this, "XIAOPENG",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("a", "b");
				intent.setAction("huahua.action.deleteCallLogsView");
				sendBroadcast(intent);
			} else if (v.getId() == R.id.button_w) {
				Intent intent2 = new Intent();
				intent2.setAction("huahua.action.notification");
				Log.i("qwer", "123");
				Log.i("qwer", "123");
				Log.i("qwer", "123");
				sendBroadcast(intent2);
			}

			// menuWindow.dismiss();
		}
	};

	private void init() {
		liaotian = (TextView) findViewById(R.id.liaotian);
		faxian = (TextView) findViewById(R.id.faxian);
		tongxunlu = (TextView) findViewById(R.id.tongxunlu);
		usecenter = (TextView) findViewById(R.id.usecenter);

		mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lllayout);
		mViewCount = mScrollLayout.getChildCount();
		mImageViews = new LinearLayout[mViewCount];
		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (LinearLayout) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}
		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);

	}

	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mCurSel = index;

		if (index == 0) {
			liaotian.setTextColor(0xff228B22);
			faxian.setTextColor(Color.BLACK);
			tongxunlu.setTextColor(Color.BLACK);
			usecenter.setTextColor(Color.BLACK);

		} else if (index == 1) {
			liaotian.setTextColor(Color.BLACK);
			faxian.setTextColor(0xff228B22);
			tongxunlu.setTextColor(Color.BLACK);
			usecenter.setTextColor(Color.BLACK);

		} else if (index == 2) {
			liaotian.setTextColor(Color.BLACK);
			faxian.setTextColor(Color.BLACK);
			tongxunlu.setTextColor(0xff228B22);
			usecenter.setTextColor(Color.BLACK);

		} else {
			liaotian.setTextColor(Color.BLACK);
			faxian.setTextColor(Color.BLACK);
			tongxunlu.setTextColor(Color.BLACK);
			usecenter.setTextColor(0xff228B22);
		}
	}

	public void OnViewChange(int view) {

		setCurPoint(view);
	}

	public void onClick(View v) {

		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}

}
