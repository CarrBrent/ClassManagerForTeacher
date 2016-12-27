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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import app.ui.TitleActivity;
import app.util.BaseInfo;

public class TeacherEvaluateDetailActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private int seId;
	private int sId;
	private String seName;
	private String sName;
	private TextView tvSName; 
	private TextView tvSId; 
	private Button submit; 
	private Spinner spKeypoint;
	private Spinner spPositivity;
	private Spinner spCommunicate;
	private String teacherevaluatesubmit = "teacherevaluatesubmit.do";
	private String teacherevaluatesubmiturl;

	private HttpUtils http = new HttpUtils();
	private BaseInfo baseInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_teacher_detail);
		showBackwardView(R.string.button_backward, true);

		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(1000);


		baseInfo = (BaseInfo)getApplication();
		teacherevaluatesubmiturl = baseInfo.getUrl()+teacherevaluatesubmit;
		tvSName = (TextView) findViewById(R.id.sname);
		tvSId = (TextView) findViewById(R.id.sId);
		spKeypoint = (Spinner)findViewById(R.id.key_point);
		spPositivity = (Spinner)findViewById(R.id.positivity);
		spCommunicate = (Spinner)findViewById(R.id.communicate);

		submit = (Button) findViewById(R.id.submit);

		submit.setOnClickListener(this);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		seId = bundle.getInt("seId");
		sId = bundle.getInt("sId");
		seName = bundle.getString("seName");
		sName = bundle.getString("sName");
		setTitle(seName);
		tvSId.setText(Integer.toString(sId));
		tvSName.setText(sName);

	}
	@Override
	public void onClick(View v) {
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.submit:
			submit();
			break;

		default:
			break;
		}

	}
	public void submit() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId",Integer.toString(seId));
		params.addQueryStringParameter("sId",Integer.toString(sId));
		params.addQueryStringParameter("keypoint",(String)spKeypoint.getSelectedItem());
		params.addQueryStringParameter("communicate",(String)spCommunicate.getSelectedItem());
		params.addQueryStringParameter("positivity",(String)spPositivity.getSelectedItem());
		http.send(HttpRequest.HttpMethod.GET,
				teacherevaluatesubmiturl,
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
				Toast.makeText(TeacherEvaluateDetailActivity.this, "评价完成", 1).show();
				finish();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}




	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
	}

}
