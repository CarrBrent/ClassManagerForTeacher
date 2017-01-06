package app.ui.activity.evaluate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import myclass.manager.teacher.R;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import app.ui.TitleActivity;
import app.util.BaseInfo;

public class TeacherEvaluateActivity extends TitleActivity implements OnClickListener {

	private ListView listView;

	// 基本地址：服务器ip地址：端口号/Web项目逻辑地址+目标页面（Servlet）的url-pattern
	private String findstudentsbyseid = "findstudentsbyseid.do";
	private String findstudentsbyseidUrl;
	private BaseInfo baseInfo;

	private int cId;
	private int seId;
	private String seName;

	private HttpUtils http = new HttpUtils();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_teacher);
		showBackwardView(R.string.button_backward, true);//设置左上角返回箭头生效
		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(1000);

		//从intent中获取seName，seId
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		seId = bundle.getInt("seId");
		cId = bundle.getInt("cId");
		seName = bundle.getString("seName");
		setTitle(seName);

		//通过访问服务器，获取数据
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId", Integer.toString(seId) );
		baseInfo = (BaseInfo)getApplication();
		findstudentsbyseidUrl = baseInfo.getUrl()+findstudentsbyseid;
		listView=(ListView)this.findViewById(R.id.listview); 
		GetData(findstudentsbyseidUrl, params);

		//为每个item设置监听器
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				Map map = (Map)listView.getItemAtPosition(arg2);
				int sId = (Integer)map.get("sId");
				String sName = (String)map.get("sName");
				Intent intent = new Intent();
				intent.setClass(TeacherEvaluateActivity.this, TeacherEvaluateDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("cId",cId);
				bundle.putInt("sId",sId);
				bundle.putInt("seId",seId);
				bundle.putString("sName",sName);
				bundle.putString("seName",seName);
				intent.putExtras(bundle);
				startActivity(intent);

			}

		});
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
				List<Map<String, Object>> students = getMaps("students", responseInfo.result);

				SimpleAdapter adapter = new SimpleAdapter(TeacherEvaluateActivity.this,students,R.layout.activity_evaluate_teacher_items,
						new String[]{"sName","sId","status"},
						new int[]{R.id.sName,R.id.sId,R.id.status});
				listView.setAdapter(adapter);
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


}
