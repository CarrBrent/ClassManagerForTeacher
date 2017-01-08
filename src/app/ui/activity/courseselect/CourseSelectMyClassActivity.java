package app.ui.activity.courseselect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;



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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import app.ui.TitleActivity;
import app.ui.activity.setting.LoginActivity;
import app.util.BaseInfo;

public class CourseSelectMyClassActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	private ListView listView;


	// 基本地址：服务器ip地址：端口号/Web项目逻辑地址+目标页面（Servlet）的url-pattern
	private String url = "findcoursebytid.do";
	private HttpUtils http = new HttpUtils();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courseselect_myclass);
		setTitle("学生选课");
		showBackwardView(R.string.button_backward, true);//设置左上角返回箭头生效

		//通过访问服务器，获取数据
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("TId", "1");//教师Id暂时是写死的
		final BaseInfo baseInfo = (BaseInfo)getApplication();
		listView=(ListView)this.findViewById(R.id.listview); 
		GetData(baseInfo.getUrl()+url, params);

		//为每个item设置监听器
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Map map = (Map)listView.getItemAtPosition(arg2);
				int cid = (Integer)map.get("cid");
				Intent intent = new Intent();
				intent.setClass(CourseSelectMyClassActivity.this, CourseSelectResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("cId",cid);
				intent.putExtras(bundle);
				startActivity(intent);

			}

		});



//		String string= "{\"classes\":[{\"classname\":\"数据仓库\"},{\"classname\":\"机器学习\"},{\"classname\":\"数据结构\"},{\"classname\":\"软件重构技术\"}]}";
//		List<Map<String, Object>> myclass = getMaps("classes", string);
//
//
//		//将数据适配到相应的ListView
//		final SimpleAdapter adapter = new SimpleAdapter(this,myclass,R.layout.activity_myclass_items,
//				new String[]{"classname"},
//				new int[]{R.id.classname});
//		listView=(ListView)this.findViewById(R.id.listview);
//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				Map map = (Map)listView.getItemAtPosition(arg2);
//				String classname = (String) map.get("classname");
//				Intent intent = new Intent();
//				intent.setClass(MyClassActivity.this, SelectedClassActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("classname",classname);
//				intent.putExtras(bundle);
//				startActivity(intent);
//
//
//			}
//
//		});

	}

	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
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
				List<Map<String, Object>> myclass = getMaps("classes", responseInfo.result);

				SimpleAdapter adapter = new SimpleAdapter(CourseSelectMyClassActivity.this,myclass,R.layout.activity_myclass_items,
						new String[]{"cname"},
						new int[]{R.id.CName});
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
