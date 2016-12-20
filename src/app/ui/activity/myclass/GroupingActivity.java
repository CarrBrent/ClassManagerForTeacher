package app.ui.activity.myclass;

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

public class GroupingActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private ListView listView;
	private Button grouping;
	private Button submit;
	private String groupingUrl = "grouping.do";
	private String submitUrl = "groupsubmit.do";
	private HttpUtils http = new HttpUtils();

	private String seName;
	private String seId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouping);
		showBackwardView(R.string.button_backward, true);

		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(1000);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		seName = bundle.getString("seName");
		seId= Integer.toString(bundle.getInt("seId"));
		setTitle(seName);

		grouping = (Button)this.findViewById(R.id.grouping);
		submit = (Button)this.findViewById(R.id.submit);
		grouping.setOnClickListener(this);
		submit.setOnClickListener(this);

		listView=(ListView)this.findViewById(R.id.listview); 



	}

	@Override
	public void onClick(View v) {
		final BaseInfo baseInfo = (BaseInfo)getApplication();
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.grouping:
			//设置访问服务器时需要传递的参数
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("seId",seId);
			//通过访问服务器，获取数据
			GetData(baseInfo.getUrl()+groupingUrl, params);
			break;
		case R.id.submit:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("分组信息提交之后将不可修改，确认要提交分组信息吗？")
			       .setCancelable(false)
			       .setPositiveButton("确认", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	 //设置访问服务器时需要传递的参数
			   			RequestParams submitParams = new RequestParams();
			   			submitParams.addQueryStringParameter("seId",seId);
			   			//通过访问服务器，获取数据
			   			Submit(baseInfo.getUrl()+submitUrl, submitParams);
			   			grouping.setEnabled(false);
			           }
			       })
			       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
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
				List<Map<String, Object>> groups = getMaps("groups", responseInfo.result);

				SimpleAdapter adapter = new SimpleAdapter(GroupingActivity.this,groups,R.layout.activity_grouping_items,
						new String[]{"grName","stNames"},
						new int[]{R.id.grName,R.id.stNames});
				listView.setAdapter(adapter);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});

	}
	//向服务器提交分组结果
	private void Submit(String URL, RequestParams params){
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
				System.out.println("Submit On Success");
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
