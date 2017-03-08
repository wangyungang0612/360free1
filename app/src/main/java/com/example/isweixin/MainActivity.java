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
	// �Զ���ĵ�������
	SelectPicPopupWindow menuWindow; // ������
	SelectAddPopupWindow menuWindow2; // ������

	private ListView m_contactslist;

	public static ContactsAdapter m_contactsadapter;
	// ����������ϵ��EditText
	private EditText m_FilterEditText;
	// û��ƥ����ϵ��ʱ��ʾ��TextView
	private TextView m_listEmptyText;

	private Button m_AddContactBtn;

	private Button m_RemoveSameContactBtn;
	// �������layout
	private FrameLayout m_topcontactslayout;
	// ��ϵ�����ݹ۲���
	private ContactsContentObserver ContactsCO;

	private String ChooseContactName;

	private String ChooseContactNumber;

	private String ChooseContactID; // ��ϵ���б�ѡ�е����֡����롢id
	// ���ضԻ���
	ProgressDialog m_dialogLoading;
	// ��ĸ����ͼView
	private AlphabetScrollBar m_asb;
	// ��ʾѡ�е���ĸ
	private TextView m_letterNotice;

	private ListView m_smsslist;

	public static SmsAdapter m_smsadapter;

	private SmssContentObserver SmssCO; // ����Ϣ���ݹ۲���

	private ListView m_calllogslist;

	public static CallLogsAdapter m_calllogsadapter;

	private CallLogsContentObserver CallLogsCO;// ͨ����¼���ݹ۲���

	private EditText EditNum; // ����༭��

	private Button BtnBackSpace; // �˸񰴼�
	private LinearLayout jianpan;
	private Button button, button2, button3, bohao_btn, button_shouqi,
			bohao_btn4, button_q;

	private pop menuWindow1;
	TelephonyManager tManager;

	// ************************����Ϊ������***************************//
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();

		// ����Utils����ĺ���
		Utils.init(this);
		Utils.getContacts();
		Utils.getCallLogs();
		Utils.getSmss();

		tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		jianpan = (LinearLayout) findViewById(R.id.shuzijianpan);
		button = (Button) findViewById(R.id.button188); // ��ͼ��λ��ť
		button2 = (Button) findViewById(R.id.button189);
		button3 = (Button) findViewById(R.id.button3);
		bohao_btn = (Button) findViewById(R.id.bohao_btn);
		EditNum = (EditText) findViewById(R.id.shuzi_edit); // ����༭��
		button_shouqi = (Button) findViewById(R.id.button_shouqi);
		bohao_btn4 = (Button) findViewById(R.id.bohao_btn4);

		// �õ����ּ��̵İ���
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
		m_AddContactBtn = (Button) findViewById(R.id.add_contacts);// ������ϵ�˰�ť
		m_topcontactslayout = (FrameLayout) findViewById(R.id.top_contacts_layout);
		m_FilterEditText = (EditText) findViewById(R.id.pb_search_edit);// ��ʼ�������༭��,�����ı��ı�ʱ�ļ�����

		// ע�������ϵ�˵Ĺ㲥,����ʵʱ������ϵ��listview
		IntentFilter filter = new IntentFilter();
		filter.addAction("huahua.action.UpdataContactsView");
		MainActivity.this.registerReceiver(mReceiver, filter);
		// ע����¶��ŵĹ㲥������ʵʱ���¶���listview
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("huahua.action.UpdataSmssView");
		MainActivity.this.registerReceiver(mReceiver2, filter2);
		// ע�����ͨ����¼�Ĺ㲥,����ʵʱ����ͨ����¼listview
		IntentFilter filter3 = new IntentFilter();
		filter3.addAction("huahua.action.UpdataCallLogsView");// �Լ�����filter��filter3
		MainActivity.this.registerReceiver(mReceiver3, filter3);

		IntentFilter filter4 = new IntentFilter();
		filter4.addAction("huahua.action.deleteCallLogsView");// 
		MainActivity.this.registerReceiver(mReceiver4, filter4);

		PhoneStateListener pStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// TODO Auto-generated method stub
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:// ����״̬��������
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:// ����绰��������
					break;
				case TelephonyManager.CALL_STATE_RINGING:// ��������
					// �����������ں����������Զ��Ҷ�
					if ((incomingNumber).equals("15695555683")) {
						try {
							// ��ȡandroid.os.ServiceManager��Ķ����getService()����
							Method method = Class.forName(
									"android.os.ServiceManager").getMethod(
									"getService", String.class);
							// ��ȡԶ��TELEPHONY_SERVICE��IBinder����Ĵ���
							IBinder binder = (IBinder) method.invoke(null,
									new Object[] { TELEPHONY_SERVICE });
							// ��IBinder����Ĵ���ת��ΪITelephony����
							ITelephony telephony = ITelephony.Stub
									.asInterface(binder);
							// �Ҷϵ绰
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

		// ΪTelephonyManager��Ӽ�����
		tManager.listen(pStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		bohao_btn.setOnClickListener(new OnClickListener() {
			// �������ּ��̵Ŀɼ��Ͳ��ɼ�
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
				jianpan.setVisibility(View.GONE); // ����������ּ���
			}
		});

		button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,
						LocationModeSourceActivity.class);
				startActivity(intent); // ��ת����λactivity

			}
		});

		bohao_btn4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// ʵ����SelectPicPopupWindow
				menuWindow1 = new pop(MainActivity.this, itemsOnClick);
				// ��ʾ����
				int xoff = menuWindow1.getWidth() / 2 - v.getWidth();

				menuWindow1.showAtLocation(v, Gravity.CENTER, 0, 0); // ����layout��Popu
				// //CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL

			}
		});

		ContactsCO = new ContactsContentObserver(new Handler());
		MainActivity.this.getContentResolver().registerContentObserver(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, false,
				ContactsCO);

		// �õ���ĸ�еĶ���,�����ô�����Ӧ������
		m_asb = (AlphabetScrollBar) findViewById(R.id.alphabetscrollbar);
		m_asb.setOnTouchBarListener(new ScrollBarListener());
		m_letterNotice = (TextView) findViewById(R.id.pb_letter_notice);
		m_asb.setTextView(m_letterNotice);

		// �õ���ϵ���б�,������������
		m_contactslist = (ListView) findViewById(R.id.pb_listvew);
		m_contactsadapter = new ContactsAdapter(MainActivity.this,
				Utils.persons);
		m_contactslist.setAdapter(m_contactsadapter);
		// ��ϵ���б�������
		m_contactslist
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {

						Vibrator vib = (Vibrator) MainActivity.this
								.getSystemService(Service.VIBRATOR_SERVICE);
						vib.vibrate(50); // �ֻ��񶯷���
						// �����ж��Ƿ��ǹ��˺����ϵ��
						if (m_topcontactslayout.getVisibility() == View.VISIBLE) {
							ChooseContactName = Utils.persons.get(arg2).Name; // �жϳ�ѡ�������
							ChooseContactNumber = Utils.persons.get(arg2).Number;// �жϳ�ѡ��ĺ���
							ChooseContactID = Utils.persons.get(arg2).ID;// �жϳ�ѡ���id
						} else {
							ChooseContactName = Utils.filterpersons.get(arg2).Name;// �жϳ�ѡ�������(���˺��)
							ChooseContactNumber = Utils.filterpersons.get(arg2).Number;// �жϳ�ѡ��ĺ���(���˺��)
							ChooseContactID = Utils.filterpersons.get(arg2).ID;// �жϳ�ѡ���id(���˺��)
						}

						final Person_Sms personsms = new Person_Sms(); // Utils�����Person_Sms
						personsms.Name = ChooseContactName; // ѡ�е���ϵ������(����������)
						personsms.Number = ChooseContactNumber; // ѡ�е���ϵ�˺���(����������)
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
										new String[] { "����", "����", "ɾ����ϵ��",
												"�༭��ϵ��", "�鿴��ϸ��Ϣ" },
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
															.setTitle("ɾ��")
															.setMessage(
																	"ɾ����ϵ��"
																			+ ChooseContactName
																			+ "?")
															.setPositiveButton(
																	"ȷ��",
																	new DialogInterface.OnClickListener() {

																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			// ɾ����ϵ�˲���,�����߳��д���
																			new DeleteContactTask()
																					.execute();
																		}
																	})
															.setNegativeButton(
																	"ȡ��",
																	new DialogInterface.OnClickListener() {

																		public void onClick(
																				DialogInterface dialog,
																				int which) {

																		}
																	}).create();
													DeleteDialog.show();
												} else if (which == 3) { // �༭��ϵ�˲���
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

		// ���������������������ϵ��
		m_FilterEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (!"".equals(s.toString().trim())) {
					// ���ݱ༭��Ĺؼ�����������ϵ�˲�������ϵ�б�
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

		SmssCO = new SmssContentObserver(new Handler()); // ����Ϣ�����ݹ۲���
		MainActivity.this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), false, SmssCO);

		m_smsslist = (ListView) findViewById(R.id.sms_list);// �Ŷ����б��listview
		m_smsadapter = new SmsAdapter(MainActivity.this, Utils.persons_sms);
		m_smsslist.setAdapter(m_smsadapter);
		m_smsslist.setOnItemClickListener(new OnItemClickListener() {
			// �����б�������
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(MainActivity.this,
						ChatActivity.class); // ��ת���������

				Bundle mBundle = new Bundle();

				mBundle.putSerializable("chatperson",
						Utils.persons_sms.get(arg2)); // ����Ҫ���͵�ֵ��Ҫ������˵�����
				intent.putExtras(mBundle);
				startActivity(intent);

			}
		});

		// ͨ�����ݹ۲���
		CallLogsCO = new CallLogsContentObserver(new Handler());
		MainActivity.this.getContentResolver().registerContentObserver(
				CallLog.Calls.CONTENT_URI, false, CallLogsCO);

		// ͨ����¼�б�
		m_calllogslist = (ListView) findViewById(R.id.calllogs_list);
		m_calllogsadapter = new CallLogsAdapter(MainActivity.this,
				Utils.calllogs); //
		m_calllogslist.setAdapter(m_calllogsadapter);

		if (android.os.Build.VERSION.SDK_INT <= 10) {
			EditNum.setInputType(InputType.TYPE_NULL); // edittext����ʾ�����,Ҫ��ʾ���
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
			// �����˸�������������绰����
			public boolean onLongClick(View v) {
				Vibrator vib = (Vibrator) MainActivity.this
						.getSystemService(Service.VIBRATOR_SERVICE);
				vib.vibrate(50); // �����ֻ���

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
					Toast.makeText(MainActivity.this, "���������",
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

	// *********************����Ϊ�����ּ���ʵ�м���********************//

	BroadcastReceiver mReceiver3 = new BroadcastReceiver() {
		// �˹㲥��������ͨ����¼�б�
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

		// *******************����Ϊ��̬����ͨ����¼�İ취******************//

		// ��ͨ����¼�б�����Ҫ��ɶ�̬����ʱ,�ô˷����������б�
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

			// ͨ����¼������
			TextView name = (TextView) convertView
					.findViewById(R.id.calllog_name);

			name.setText(calllogs.get(position).Name);

			if (calllogs.get(position).Name == null) {
				name.setText("δ֪����");
			} else {
				name.setText(calllogs.get(position).Name);
			}

			// ͨ����¼�ĵ绰״̬
			TextView Type = (TextView) convertView
					.findViewById(R.id.calllog_type);
			if (calllogs.get(position).Type == CallLog.Calls.INCOMING_TYPE) {
				Type.setText("�ѽ�����");
				Type.setTextColor(Color.rgb(0, 0, 255));
			} else if (calllogs.get(position).Type == CallLog.Calls.OUTGOING_TYPE) {
				Type.setText("��������");
				Type.setTextColor(Color.rgb(0, 150, 0));
			} else if (calllogs.get(position).Type == CallLog.Calls.MISSED_TYPE) {
				Type.setText("δ������");
				Type.setTextColor(Color.rgb(255, 0, 0));
			}

			// ͨ����¼�ĺ���
			TextView number = (TextView) convertView
					.findViewById(R.id.calllog_number);
			number.setText(calllogs.get(position).Number);

			// ͨ����¼������
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

	// *******************����Ϊ��̬���¶��ż�¼�İ취******************//

	BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
		// �˹㲥������̬���¶��ż�¼
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("huahua.action.UpdataSmssView")) {
				m_smsadapter.updateListView(Utils.persons_sms);
			}
		}
	};

	public class SmsAdapter extends BaseAdapter {// �������������õ����ŵ�����
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

	// ********************** ɾ����ϵ�˵��߳� *********************//
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
			m_dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���÷��ΪԲ�ν�����
			m_dialogLoading.setMessage("����ɾ��");
			m_dialogLoading.setCancelable(false);
			m_dialogLoading.show();
		}

		protected void onProgressUpdate(Integer... values) {
			Log.i("huahua", "onProgressUpdate");
		}

	}

	// *****************�������ո�����ϵ���б�Ĺ㲥������*************//
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("huahua.action.UpdataContactsView")) {
				if (m_topcontactslayout.getVisibility() == View.VISIBLE) {
					m_contactsadapter.updateListView(Utils.persons);
				} else {
					// ���û��ƥ�����ϵ��
					if (Utils.filterpersons.isEmpty()) {
						m_contactslist.setEmptyView(m_listEmptyText);
					}

					m_contactsadapter.updateListView(Utils.filterpersons);
				}

			}
		}
	};

	private class BtnClick implements View.OnClickListener {
		// ������ϵ�˵İ�ť
		public void onClick(View v) {
			if (v == m_AddContactBtn) {
				Bundle bundle = new Bundle(); // ����Ҫ����ֵ
				bundle.putInt("tpye", 0);
				bundle.putString("name", "");
				bundle.putString("number", "");
				Intent intent = new Intent(MainActivity.this,
						AddContactsActivity.class);// ��ת��������ϵ��activity
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

		// ����ϵ���б����ݷ����仯ʱ,�ô˷����������б�
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

			// ��ĸ��ʾtextview����ʾ
			TextView letterTag = (TextView) convertView
					.findViewById(R.id.pb_item_LetterTag);
			// ��õ�ǰ������ƴ������ĸ
			String firstLetter = persons.get(position).PY.substring(0, 1)
					.toUpperCase();

			// ����ǵ�1����ϵ�� ��ôletterTagʼ��Ҫ��ʾ
			if (position == 0) {
				letterTag.setVisibility(View.VISIBLE);
				letterTag.setText(firstLetter);
			} else {
				// �����һ��������ƴ������ĸ
				String firstLetterPre = persons.get(position - 1).PY.substring(
						0, 1).toUpperCase();
				// �Ƚ�һ�������Ƿ���ͬ
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

	// ��ĸ�д����ļ�����
	private class ScrollBarListener implements
			AlphabetScrollBar.OnTouchBarListener {

		@Override
		public void onTouch(String letter) {

			// ������ĸ��ʱ,����ϵ���б���µ�����ĸ���ֵ�λ��
			for (int i = 0; i < Utils.persons.size(); i++) {
				if (Utils.persons.get(i).PY.substring(0, 1)
						.compareToIgnoreCase(letter) == 0) {
					m_contactslist.setSelection(i);
					break;
				}
			}
		}
	}

	// Ϊ��������ʵ�ּ�����
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
