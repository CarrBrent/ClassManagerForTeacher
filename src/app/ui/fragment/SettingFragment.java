package app.ui.fragment;

import myclass.manager.teacher.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import app.ui.BaseFragment;
import app.ui.activity.barcode.BarCodeActivity;
import app.ui.activity.myclass.MyClassActivity;
import app.ui.activity.myclass.httptestActivity;
import app.ui.activity.setting.AboutActivity;
import app.ui.activity.setting.LoginActivity;

public class SettingFragment extends BaseFragment implements OnClickListener {


	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_about:
			startActivity(new Intent(getActivity(), AboutActivity.class));
			break;
		case R.id.layout_account:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.layout_notification:
			startActivity(new Intent(getActivity(), httptestActivity.class));
			break;
		case R.id.layout_privacy:
			startActivity(new Intent(getActivity(), BarCodeActivity.class));
			break;
		case R.id.layout_exit:
			SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("teacher", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();//获取编辑器
			editor.remove("tId");
//			editor.putString("userId","null");
			editor.commit();//提交修改

			Toast.makeText(this.getActivity().getApplicationContext(), "已退出登录", 1).show();
			break;
		default:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setting, container, false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.layout_about).setOnClickListener(this);
		view.findViewById(R.id.layout_account).setOnClickListener(this);
		view.findViewById(R.id.layout_notification).setOnClickListener(this);
		view.findViewById(R.id.layout_privacy).setOnClickListener(this);
		view.findViewById(R.id.layout_exit).setOnClickListener(this);
	}

}