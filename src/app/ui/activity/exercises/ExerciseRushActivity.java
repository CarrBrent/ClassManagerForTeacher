package app.ui.activity.exercises;

import org.json.JSONException;
import org.json.JSONObject;

import myclass.manager.teacher.R;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.ui.TitleActivity;
import app.util.BaseInfo;

public class ExerciseRushActivity extends TitleActivity implements OnClickListener{
	private Button rerush;
	private Button start;
	private Button submit;
	private HttpUtils http = new HttpUtils();

	private BaseInfo baseInfo;
	private String rerushString = "restartResponder.do";
	private String startString = "startResponder.do";
	private String submitString = "endResponder.do";
	private String findResponderStuString = "findResponderStu.do";

	private String rerushurl;
	private String findResponderUrl;
	private String starturl;
	private String submiturl;

	private String seName;
	private String seId;
	private int rdid;
	private TextView tvName;
	private TextView tvSId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_rush);
		showBackwardView(R.string.button_backward, true);

		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(100);
		//获取URL
		baseInfo = (BaseInfo)getApplication();
		starturl = baseInfo.getUrl()+startString;
		submiturl = baseInfo.getUrl()+submitString;
		rerushurl = baseInfo.getUrl()+rerushString;
		findResponderUrl = baseInfo.getUrl()+findResponderStuString;

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		seName = bundle.getString("seName");
		seId= Integer.toString(bundle.getInt("seId"));
		setTitle(seName);

		tvName = (TextView)this.findViewById(R.id.name);
		tvSId = (TextView)this.findViewById(R.id.sId);

		rerush = (Button)this.findViewById(R.id.rerush);
		start = (Button)this.findViewById(R.id.rush);
		submit = (Button)this.findViewById(R.id.submit);
		rerush.setOnClickListener(this);
		start.setOnClickListener(this);
		submit.setOnClickListener(this);

	}
	public void setRdid(int rdid) {
		this.rdid = rdid;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.rerush:
			rerush();
			break;
		case R.id.rush:
			start();
			break;
		case R.id.submit:
			submit();
			break;
		default:
			break;
		}

	}
	public void findResponder(int rdid) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("rdid",Integer.toString(rdid));
		http.send(HttpRequest.HttpMethod.GET,
				findResponderUrl,
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
					int sId = (Integer) jsonObject.get("sId");
					String name = (String) jsonObject.get("name");
					tvName.setText(name);
					tvSId.setText(Integer.toString(sId));

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
	public void start() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId",seId);
		http.send(HttpRequest.HttpMethod.GET,
				starturl,
				params,
				new RequestCallBack<String>() {

			@Override
			public void onStart() {
			}
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				Toast.makeText(ExerciseRushActivity.this, "等待抢答",1).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(responseInfo.result);
					setRdid((Integer)jsonObject.get("rdid"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				findResponder(rdid);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}
	public void rerush() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("rdid",Integer.toString(rdid));
		http.send(HttpRequest.HttpMethod.GET,
				rerushurl,
				params,
				new RequestCallBack<String>() {

			@Override
			public void onStart() {
			}
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				Toast.makeText(ExerciseRushActivity.this, "等待抢答",1).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(responseInfo.result);
					setRdid((Integer)jsonObject.get("rdid"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				findResponder(rdid);

			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}
	public void submit() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("rdid",Integer.toString(rdid));
		http.send(HttpRequest.HttpMethod.GET,
				submiturl,
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
				Toast.makeText(ExerciseRushActivity.this, "提交成功",1).show();

			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}



}
