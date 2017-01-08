package app.ui.activity.courseselect;

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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import app.ui.TitleActivity;
import app.util.BaseInfo;

public class CourseSelectResultActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private ListView listView;
	private Button grouping;
	private Button submit;
	private String startCourseSelectUrl = "startcourseselect.do";
	private String refreshCourseSelectUrl = "findseminarstudentsnumberbycid.do";
	private HttpUtils http = new HttpUtils();

	private int cId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courseselect_result);
		showBackwardView(R.string.button_backward, true);

		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(1000);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		cId = bundle.getInt("cId");
		setTitle("学生选课");

		grouping = (Button)this.findViewById(R.id.start);
		submit = (Button)this.findViewById(R.id.refresh);
		grouping.setOnClickListener(this);
		submit.setOnClickListener(this);

		listView=(ListView)this.findViewById(R.id.listview); 



	}

	@Override
	public void onClick(View v) {
		final BaseInfo baseInfo = (BaseInfo)getApplication();
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.start:
			//设置访问服务器时需要传递的参数
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("cId",Integer.toString(cId));
			//通过访问服务器，开启选课功能
			StartCourseSelect(baseInfo.getUrl()+startCourseSelectUrl, params);
			//通过访问服务器，获取数据
			GetData(baseInfo.getUrl()+refreshCourseSelectUrl, params);
			break;
		case R.id.refresh:
			//设置访问服务器时需要传递的参数
			RequestParams refreshparams = new RequestParams();
			refreshparams.addQueryStringParameter("cId",Integer.toString(cId));	
			//通过访问服务器，获取数据
			GetData(baseInfo.getUrl()+refreshCourseSelectUrl, refreshparams);
			break;

		default:
			break;
		}

	}


	private void StartCourseSelect(String URL, RequestParams params) {
		// TODO Auto-generated method stub
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
	//从服务器获取数据
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
				//如果访问成功则从返回值responseInfo中获取students
				List<Map<String, Object>> numbers = getMaps("numbers", responseInfo.result);

				SimpleAdapter adapter = new SimpleAdapter(CourseSelectResultActivity.this,numbers,R.layout.activity_courseselect_result_items,
						new String[]{"seName","studentNo"},
						new int[]{R.id.seName,R.id.studentNo});
				listView.setAdapter(adapter);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});

	}
	public static List<Map<String, Object>> getMaps(String key,  
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

}
