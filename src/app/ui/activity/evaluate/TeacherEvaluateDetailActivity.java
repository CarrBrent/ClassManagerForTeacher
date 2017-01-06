package app.ui.activity.evaluate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
	private String teacherevaluatesubmit = "teacherevaluatesubmit.do";
	private String teacherevaluatesubmiturl;
	private String findteacherevaluatekeys = "findteacherevaluatekeys.do";
	private String findteacherevaluatekeysUrl;
	private ListView listView;

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
		submit = (Button) findViewById(R.id.submit);
		listView=(ListView)this.findViewById(R.id.listview); 

		//通过访问服务器，获取数据
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("cId", "12" );//暂时写死，之后需要改成每个页面之间传递数据时把cId也传递过来
		params.addQueryStringParameter("eeName", "教师评价" );
		baseInfo = (BaseInfo)getApplication();
		findteacherevaluatekeysUrl = baseInfo.getUrl()+findteacherevaluatekeys;
		listView=(ListView)this.findViewById(R.id.listview); 
		GetData(findteacherevaluatekeysUrl, params);

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
	private void GetData(String URL, RequestParams params){
		http.send(HttpRequest.HttpMethod.GET,
				URL,
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
				List<Map<String, Object>> keys = getMaps("keys", responseInfo.result);

				SimpleAdapter adapter = new SimpleAdapter(TeacherEvaluateDetailActivity.this,keys,R.layout.activity_evaluate_teacher_detail_items,
						new String[]{"key"},
						new int[]{R.id.tv_key_point});
				listView.setAdapter(adapter);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});

	}
	public void submit() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId",Integer.toString(seId));
		params.addQueryStringParameter("sId",Integer.toString(sId));
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
	private static List<Map<String, Object>> getMaps(String key,  
			String jsonString) {  
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  

		try {  
			JSONObject jsonObject = new JSONObject(jsonString);  
			JSONArray mapsArray = jsonObject.getJSONArray(key);  
			for (int i = 0; i < mapsArray.length(); i++) {  
				JSONObject jsonObject2 = mapsArray.getJSONObject(i);  
				Map<String, Object> map = new HashMap<String, Object>();  
				// 查看Map中的键值对的key值  
				Iterator<String> iterator = jsonObject2.keys();  

				while (iterator.hasNext()) {  
					String json_key = iterator.next();  
					Object json_value = jsonObject2.get(json_key);  
					if(json_value==null){  
						//当键值对中的value为空值时，将value置为空字符串；  
						json_value="";  
					}  
					map.put(json_key, json_value);  
				}  
				list.add(map);  
			}  
		} catch (Exception e) {  
			// TODO: handle exception  
		}  
		return list;  
	}  




	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
	}

}
