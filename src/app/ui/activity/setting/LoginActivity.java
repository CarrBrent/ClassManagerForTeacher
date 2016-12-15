package app.ui.activity.setting;

import myclass.manager.teacher.R;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.ui.TitleActivity;
import app.util.BaseInfo;

public class LoginActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private String url = "teacherlogin.do";
	private HttpUtils http = new HttpUtils();
	// 声明控件对象
	private EditText et_name, et_pass;
	private Button mLoginButton;  
	int selectIndex=1;
	int tempSelect=selectIndex;
	boolean isReLogin=false;
	// private String [] coutry_phone_sn_array,coutry_name_array;
	public final static int LOGIN_ENABLE=0x01;    //注册完毕了
	public final static int LOGIN_UNABLE=0x02;    //注册完毕了
	public final static int PASS_ERROR=0x03;      //注册完毕了
	public final static int NAME_ERROR=0x04;      //注册完毕了	
	final Handler UiMangerHandler = new Handler(){   
		@Override    
		public void handleMessage(Message msg) {  
			// TODO Auto-generated method stub
			switch(msg.what){  
			case LOGIN_ENABLE:  
				mLoginButton.setClickable(true);            
				//		    mLoginButton.setText(R.string.login);
				break;
			case LOGIN_UNABLE:
				mLoginButton.setClickable(false);
				break;
			case PASS_ERROR:

				break;
			case NAME_ERROR:
				break;
			}     
			super.handleMessage(msg);
		}   
	};
	private Button bt_username_clear;
	private Button bt_pwd_clear;
	private Button bt_pwd_eye;
	private TextWatcher username_watcher;       
	private TextWatcher password_watcher; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle("登录");
		showBackwardView(R.string.button_backward,true);//设置左上角返回箭头生效

		et_name = (EditText) findViewById(R.id.username);
		et_pass = (EditText) findViewById(R.id.password);

		bt_username_clear = (Button)findViewById(R.id.bt_username_clear);
		bt_pwd_clear = (Button)findViewById(R.id.bt_pwd_clear);
		bt_pwd_eye = (Button)findViewById(R.id.bt_pwd_eye);
		bt_username_clear.setOnClickListener(this);
		bt_pwd_clear.setOnClickListener(this);
		bt_pwd_eye.setOnClickListener(this);
		initWatcher();
		et_name.addTextChangedListener(username_watcher);
		et_pass.addTextChangedListener(password_watcher);

		mLoginButton = (Button) findViewById(R.id.login);
//		mLoginError  = (Button) findViewById(R.id.login_error);
//		mRegister    = (Button) findViewById(R.id.register);
		mLoginButton.setOnClickListener(this);       
//		mLoginError.setOnClickListener(this);     
//		mRegister.setOnClickListener(this); 
	}
	private void initWatcher() {
		username_watcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			public void afterTextChanged(Editable s) {
				et_pass.setText("");
				if(s.toString().length()>0){
					bt_username_clear.setVisibility(View.VISIBLE);
				}else{
					bt_username_clear.setVisibility(View.INVISIBLE);
				}
			}
		};
		password_watcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			public void afterTextChanged(Editable s) {
				if(s.toString().length()>0){
					bt_pwd_clear.setVisibility(View.VISIBLE);
				}else{
					bt_pwd_clear.setVisibility(View.INVISIBLE);
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.login:  //登陆
			login();
			break;
//		case R.id.login_error: //无法登陆(忘记密码了吧)
//			//   Intent login_error_intent=new Intent();
//			//   login_error_intent.setClass(LoginActivity.this, ForgetCodeActivity.class);
//			//   startActivity(login_error_intent);
//			break;
//		case R.id.register:    //注册新的用户
//			//   Intent intent=new Intent();
//			//   intent.setClass(LoginActivity.this, ValidatePhoneNumActivity.class);
//			//   startActivity(intent);
//
//			break;
		default://必须设置default，否则会和onBackward(View backwardView)冲突
			break;
		}

	}
	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
	}
	/**
	 * 登陆
	 */
	private void login() {
		String name = et_name.getText().toString();
		String password = et_pass.getText().toString();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("tAccount",name);
		params.addQueryStringParameter("tPwd",password);
		final BaseInfo baseInfo = (BaseInfo)getApplication();

		http.send(HttpRequest.HttpMethod.GET,
				baseInfo.getUrl()+url,
				params,
				new RequestCallBack<String>() {

			@Override
			public void onStart() {
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();//获取编辑器
				editor.putString("userId", responseInfo.result);
				editor.commit();//提交修改
				Toast.makeText(getApplicationContext(), "登录成功", 1).show();
				
				finish();

			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});

	}

	/**
	 * 监听Back键按下事件,方法2:
	 * 注意:
	 * 返回值表示:是否能完全处理该事件
	 * 在此处返回false,所以会继续传播该事件.
	 * 在具体项目中此处的返回值视情况而定.
	 */ 
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(isReLogin){
				Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
				mHomeIntent.addCategory(Intent.CATEGORY_HOME);
				mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				LoginActivity.this.startActivity(mHomeIntent);
			}else{
				LoginActivity.this.finish();
			}
			return false; 
		}else { 
			return super.onKeyDown(keyCode, event); 
		} 
	}




}
