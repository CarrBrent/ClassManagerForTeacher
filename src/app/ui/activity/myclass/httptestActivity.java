package app.ui.activity.myclass;



import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import myclass.manager.teacher.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class httptestActivity extends Activity
{
	private Button mSendReqBtn = null;// 发送请求的按钮
	private TextView text = null;// 用于显示结果，用载入html字符串的方式显示响应结果，而不是使用WebView自己的方式加载URL

	HttpUtils http = new HttpUtils();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.httptest);

		mSendReqBtn = (Button) findViewById(R.id.submit);
		text = (TextView) findViewById(R.id.textview);
		mSendReqBtn.setOnClickListener(mSendClickListener);

	}


	private OnClickListener mSendClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("username", "jaishil");

			http.send(HttpRequest.HttpMethod.GET,
					"http://219.216.64.170:8080/spring/helloworld.do",
					params,
					new RequestCallBack<String>() {

				@Override
				public void onStart() {
					text.setText("conn...");
				}

				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					text.setText(current + "/" + total);
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					text.setText("response:" + responseInfo.result);
				}


				@Override
				public void onFailure(HttpException error, String msg) {
					text.setText(msg);
				}
			});

		}
	};


}