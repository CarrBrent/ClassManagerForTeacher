package app.ui.activity.myclass;

import java.util.ArrayList;
import java.util.Date;
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
import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import app.ui.TitleActivity;
import app.util.BaseInfo;

public class SeminarStudentsActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private ListView listView;
	private TextView signInNo;
	private String url;
	private String findsigninstudentsbyseid = "findsigninstudentsbyseid.do";
	private HttpUtils http = new HttpUtils();
    private Button refresh;
	private String seName;
	private String seId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seminar_students);

		showBackwardView(R.string.button_backward, true);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		seName = bundle.getString("seName");
		seId= Integer.toString(bundle.getInt("seId"));
		setTitle(seName);
		
		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(100);


		
		final BaseInfo baseInfo = (BaseInfo)getApplication();
		url = baseInfo.getUrl()+findsigninstudentsbyseid;

		refresh =(Button)this.findViewById(R.id.refresh);
		listView=(ListView)this.findViewById(R.id.listview); 
		signInNo=(TextView)this.findViewById(R.id.signInNo);
		refresh.setOnClickListener(this);
		//通过访问服务器，获取数据
		GetData();

	}
	@Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
		case R.id.refresh:
			GetData();
			break;

		default:
			break;
		}
        
    }


	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
	}
	//从服务器获取数据
	private void GetData(){
		//设置访问服务器时需要传递的参数
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId",seId);
		http.send(HttpRequest.HttpMethod.GET,
				url,
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
				List<Map<String, Object>> students = getMaps("students", responseInfo.result);

				SimpleAdapter adapter = new SimpleAdapter(SeminarStudentsActivity.this,students,R.layout.activity_seminar_students_items,
						new String[]{"sname","sid"},
						new int[]{R.id.sName,R.id.sId});
				listView.setAdapter(adapter);
				signInNo.setText(Integer.toString(students.size()));
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
