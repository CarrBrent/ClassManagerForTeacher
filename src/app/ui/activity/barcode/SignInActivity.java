package app.ui.activity.barcode;


import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import myclass.manager.teacher.R;
import app.ui.TitleActivity;
import app.ui.activity.barcode.CaptureActivity;
import app.ui.activity.myclass.MyClassActivity;
import app.util.BaseInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SignInActivity extends TitleActivity {
    /** Called when the activity is first created. */
	private TextView resultTextView;
	private String url = "signin.do";
	private HttpUtils http = new HttpUtils();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("课程签到");
		showBackwardView(R.string.button_backward, true);//设置左上角返回箭头生效
        
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        
        scanBarCodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(SignInActivity.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
    }
	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			JSONObject jsonObject;
			String CId = null;
			String SId = null;
			try {
				jsonObject = new JSONObject(bundle.getString("result"));
				CId = jsonObject.getString("CId");
				SId = jsonObject.getString("SId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//通过访问服务器，获取数据
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("CId",CId);
			params.addQueryStringParameter("SId",SId);
			final BaseInfo baseInfo = (BaseInfo)getApplication();
			http.send(HttpRequest.HttpMethod.GET,
					baseInfo.getUrl()+url,
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
					resultTextView.setText("签到成功");
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					resultTextView.setText("签到失败");
				}
			});
			
		}
	}
}