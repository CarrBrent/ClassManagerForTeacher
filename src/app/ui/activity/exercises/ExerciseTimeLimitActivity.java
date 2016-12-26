package app.ui.activity.exercises;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import myclass.manager.teacher.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.ui.TitleActivity;
import app.util.BaseInfo;

public class ExerciseTimeLimitActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private int seId;
	private String seName;
	private CountDownTimer timer; 
	private EditText etNumber; 
	private EditText etTime; 
	private TextView tv; 
	private TextView defulttime; 
	private TextView defultnumber; 
	private Button defultStart; 
	private Button start; 
	private Button reset; 
	private String starttimelimitexercise = "starttimelimitexercise.do";
	private String endtimelimitexercise = "endtimelimitexercise.do";
	private String findexerciseinfo = "findexerciseinfo.do";
	private String starturl;
	private String endurl;
	private String findexerciseinfourl;

	private HttpUtils http = new HttpUtils();
	private BaseInfo baseInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_timelimit);
		showBackwardView(R.string.button_backward, true);

		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(1000);


		baseInfo = (BaseInfo)getApplication();
		starturl = baseInfo.getUrl()+starttimelimitexercise;
		endurl = baseInfo.getUrl()+endtimelimitexercise;
		findexerciseinfourl = baseInfo.getUrl()+findexerciseinfo;
		etNumber= (EditText) findViewById(R.id.excerisenumber);
		etTime= (EditText) findViewById(R.id.timer);
		tv = (TextView) findViewById(R.id.lefttime);
		defultnumber = (TextView) findViewById(R.id.defultnumber);
		defulttime = (TextView) findViewById(R.id.defulttime);

		defultStart = (Button) findViewById(R.id.btnDefultStart);
		start = (Button) findViewById(R.id.btnStart);
		reset = (Button) findViewById(R.id.btnReset);
		timer = new CountDownTimer(0,0){
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
			}
		};

		start.setOnClickListener(this);
		reset.setOnClickListener(this);
		defultStart.setOnClickListener(this);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		seId = bundle.getInt("seId");
		seName = bundle.getString("seName");
		setTitle(seName);
		
		findexerciseinfo(Integer.toString(seId));


	}
	@Override
	public void onClick(View v) {
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.btnStart:
			start();
			break;
		case R.id.btnDefultStart:
			defultStart();
			break;
		case R.id.btnReset:
			reset();
			break;

		default:
			break;
		}

	}
	public void findexerciseinfo(String seId) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId",seId);
		http.send(HttpRequest.HttpMethod.GET,
				findexerciseinfourl,
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
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					String num = Integer.toString((Integer) jsonObject.get("number"));
					String time = Integer.toString((Integer) jsonObject.get("time"));
					
					defultnumber.setText(num);
					defulttime.setText(time);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}
	public void start(){
		String time = etTime.getText().toString();
		String number = etNumber.getText().toString();
		if (time.length() != 0 & number.length()!=0) {
			starttimelimitexercise(time,number);
		}else {
			Toast.makeText(ExerciseTimeLimitActivity.this, "请输入答题时间和答题数量",1).show();
		}

	}
	public void defultStart(){
		String time = defulttime.getText().toString();
		String number = defultnumber.getText().toString();
		
		if (time.length() != 0 & number.length()!=0) {
			starttimelimitexercise(time,number);
		}else {
			Toast.makeText(ExerciseTimeLimitActivity.this, "未设置默认答题数量和默认答题时间",1).show();
		}
		
	}
	//开始评价的时候调用该方法，客户端向服务器发送请求，设置开始评价标签，学生端才能进行评价。
	public void starttimelimitexercise(final String time,String number) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId",Integer.toString(seId));
		params.addQueryStringParameter("time",time);
		params.addQueryStringParameter("number",number);
		http.send(HttpRequest.HttpMethod.GET,
				starturl,
				params,
				new RequestCallBack<String>() {

			@Override
			public void onStart() {
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			//访问成功之后执行该方法
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//访问成功之后设置倒计时
				int millisInFuture=Integer.parseInt(time);
				start.setClickable(false);
				timer = new CountDownTimer(millisInFuture*1000, 1000) {

					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub
						tv.setText( millisUntilFinished / 1000 + " S");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						//倒计时结束之后调用，向服务器发送请求，关闭评价功能
						endtimelimitexercise(time);
					}

					private void endtimelimitexercise(String time) {
						// TODO Auto-generated method stub
						RequestParams params = new RequestParams();
						params.addQueryStringParameter("seId",Integer.toString(seId));
						http.send(HttpRequest.HttpMethod.GET,
								endurl,
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
								tv.setText("答题时间到");
							}
							@Override
							public void onFailure(HttpException error, String msg) {
							}
						});
					}
				};
				timer.start();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});

	}

	public void reset() { 
		timer.onFinish();
		timer.cancel();
		start.setClickable(true);
		etTime.setText(null);
		tv.setText(null);
	}



	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
	}

}
