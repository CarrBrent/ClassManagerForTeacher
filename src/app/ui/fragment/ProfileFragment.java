package app.ui.fragment;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import myclass.manager.teacher.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import app.ui.BaseFragment;
import app.ui.activity.setting.LoginActivity;
import app.util.BaseInfo;

public class ProfileFragment extends BaseFragment implements OnClickListener {
	private String url = "finduserbyid.do";
	private HttpUtils http = new HttpUtils();
	TextView username= null;
	TextView gender = null;
	TextView phone = null;

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		//		switch (v.getId()) {
		//		case R.id.layout_about:
		//			startActivity(new Intent(getActivity(), AboutActivity.class));	
		//		default:
		//			break;
		//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile, container, false);

	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		username = (TextView) view.findViewById(R.id.tv_username);
		gender = (TextView) view.findViewById(R.id.tv_gender);
		phone = (TextView) view.findViewById(R.id.tv_phone);
		//查看文件username 如果没有username的id则登录，否则显示个人的信息
		SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
		String userId = sharedPreferences.getString("userId", "null");

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("SId",userId);
		final BaseInfo baseInfo = (BaseInfo)this.getActivity().getApplication();
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
				username.setText(responseInfo.result);

			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});

		//		view.findViewById(R.id.layout_about).setOnClickListener(this);
	}


}

