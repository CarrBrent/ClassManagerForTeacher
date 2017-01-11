package app.ui.activity.evaluate;

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

public class InGroupEvaluateActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private int seId;
	private String seName;
	private CountDownTimer timer; 
	private EditText et; 
	private TextView tv; 
	private Button start; 
	private Button reset; 
	private String startingroupevaluate = "startingroupevaluate.do";
	private String endingroupevaluate = "endingroupevaluate.do";
	private String starturl;
	private String endurl;

	private HttpUtils http = new HttpUtils();
	private BaseInfo baseInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_ingroup);
		showBackwardView(R.string.button_backward, true);

		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(1000);
		
		
		baseInfo = (BaseInfo)getApplication();
		starturl = baseInfo.getUrl()+startingroupevaluate;
		endurl = baseInfo.getUrl()+endingroupevaluate;
		et = (EditText) findViewById(R.id.timer);
		tv = (TextView) findViewById(R.id.lefttime);
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

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		seId = bundle.getInt("seId");
		seName = bundle.getString("seName");
		setTitle(seName);

	}
	@Override
	public void onClick(View v) {
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.btnStart:
			start();
			break;
		case R.id.btnReset:
			reset();
			break;

		default:
			break;
		}

	}
	public void start(){
		String time = et.getText().toString();
		if (time.length() != 0) {
			startingroupevaluate(time);
		}else {
			Toast.makeText(InGroupEvaluateActivity.this, "请输入评价时间",1).show();;
		}

	}
	//开始评价的时候调用该方法，客户端向服务器发送请求，设置开始评价标签，学生端才能进行评价。
	public void startingroupevaluate(String time) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId",Integer.toString(seId));
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
				final String time = et.getText().toString();
				int millisInFuture=Integer.parseInt(time)*60;
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
						endtingroupevaluate(time);
					}

					private void endtingroupevaluate(String time) {
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
								tv.setText("评价时间到");
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
		et.setText(null);
		tv.setText(null);
	}



	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
	}

}
