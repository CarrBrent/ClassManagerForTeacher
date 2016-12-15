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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import app.ui.TitleActivity;
import app.util.BaseInfo;

public class SeminarActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
	private ListView listView;
	private String url = "findseminarbycid.do";
	private HttpUtils http = new HttpUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminar);
        setTitle("研讨课");
        showBackwardView(R.string.button_backward, true);
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String CId = Integer.toString(bundle.getInt("CId"));
		//通过访问服务器，获取数据
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("CId",CId);
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
				int seId = (Integer) map.get("seId");
				int cId = (Integer) map.get("cId");
				String seName = (String) map.get("seName");
				String seTheme = (String) map.get("seTheme");
				String seTime = map.get("seTime").toString();
				int seUp = (Integer) map.get("seUp");
				int seDown = (Integer) map.get("seDown");
				Intent intent = new Intent();
				intent.setClass(SeminarActivity.this, SeminarDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("seId",seId);
				bundle.putInt("cId",cId);
				bundle.putString("seName",seName);
				bundle.putString("seTheme",seTheme);
				bundle.putString("seTime",seTime);
				bundle.putInt("seUp",seUp);
				bundle.putInt("seDown",seDown);
				intent.putExtras(bundle);
				startActivity(intent);

			}

		});
        
        
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
				List<Map<String, Object>> seminars = getMaps("seminars", responseInfo.result);

				SimpleAdapter adapter = new SimpleAdapter(SeminarActivity.this,seminars,R.layout.activity_seminar_items,
						new String[]{"seName"},
						new int[]{R.id.SeName});
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
