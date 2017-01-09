package app.ui.activity.exercises;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;









import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;
import myclass.manager.teacher.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import app.ui.TitleActivity;
import app.util.BaseInfo;
public class ExerciseVoteActivity extends TitleActivity implements OnClickListener {
	private ColumnChartView mColumnChartView;
	private ColumnChartData mColumnChartData;
	private static final int DEFAULT_DATA = 0;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLabels = true;
	private boolean hasLabelForSelected = false;
	
	private int voteId;
	private int seId;
	private HttpUtils http = new HttpUtils();
	private Button start;
	private Button end;
	private Button refresh;
	private String startVoteUrl="startvote.do";
	private String endVoteUrl="endvote.do";
	private String getVoteData="getvotedata.do";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_vote);
		showBackwardView(R.string.button_backward, true);

		// 设置缓存1秒,1秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(1000);
		Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        seId = bundle.getInt("seId");

		mColumnChartView = (ColumnChartView) findViewById(R.id.column);
		mColumnChartView.setOnValueTouchListener(new ValueTouchListener());
		
		start = (Button) findViewById(R.id.startVote);
		end  = (Button) findViewById(R.id.endVote);
		refresh = (Button) findViewById(R.id.refreshVote);
		
		end.setOnClickListener(this);
		start.setOnClickListener(this);
		refresh.setOnClickListener(this);
		
		
		
	}
	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}
	public void onClick(View v) {
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.startVote:
			startVote();
			break;
		case R.id.endVote:
			endVote();
			break;
		case R.id.refreshVote:
			generateData();
			break;

		default:
			break;
		}

	}
	private void startVote() {
		final BaseInfo baseInfo = (BaseInfo)getApplication();
		RequestParams params = new RequestParams();
		http.send(HttpRequest.HttpMethod.GET,
				baseInfo.getUrl()+startVoteUrl,
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
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(responseInfo.result);
					setVoteId((Integer)jsonObject.get("voteId"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				generateData();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}
	private void endVote() {
		RequestParams params = new RequestParams();
		final BaseInfo baseInfo = (BaseInfo)getApplication();
		params.addQueryStringParameter("vqid", Integer.toString(voteId));
		http.send(HttpRequest.HttpMethod.GET,
				baseInfo.getUrl()+endVoteUrl,
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
				finish();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}
	private void generateData() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("seId",Integer.toString(seId));
		params.addQueryStringParameter("voteId",Integer.toString(voteId));
		final BaseInfo baseInfo = (BaseInfo)getApplication();

		http.send(HttpRequest.HttpMethod.GET,
				baseInfo.getUrl()+getVoteData,
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
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					int a = (Integer) jsonObject.get("A");
					int b = (Integer) jsonObject.get("B");
					int c = (Integer) jsonObject.get("C");
					int d = (Integer) jsonObject.get("D");
					generateDefaultData(a,b,c,d);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}
	private void reset() {
		hasAxes = true;
		hasAxesNames = true;
		hasLabels = false;
		hasLabelForSelected = false;
		mColumnChartView.setValueSelectionEnabled(hasLabelForSelected);
	}
	private void generateDefaultData(int a,int b,int c,int d) {
		int[] data = {a,b,c,d};
		int numSubcolumns = 1;//设置每个柱状图显示的颜色数量(每个柱状图显示多少块)
		int numColumns = 4;   //柱状图的数量
		// Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {
			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				SubcolumnValue value = new SubcolumnValue((float)data[i], ChartUtils.COLOR_ORANGE);//第一个值是数值(值>0 方向朝上，值<0，方向朝下)，第二个值是颜色
				//    SubcolumnValue value = new SubcolumnValue((float) Math.random() * 50f + 5, Color.parseColor("#00000000"));//第一个值是数值，第二个值是颜色
				//    values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
				values.add(value);
			}
			Column column = new Column(values);//一个柱状图的实例
			column.setHasLabels(hasLabels);//设置是否显示每个柱状图的高度，
			column.setHasLabelsOnlyForSelected(hasLabelForSelected);//点击的时候是否显示柱状图的高度，和setHasLabels()和用的时候，setHasLabels()失效
			columns.add(column);
		}
		mColumnChartData = new ColumnChartData(columns);//表格的数据实例
		if (hasAxes) {
			Axis axisX = new Axis();
			//   axisX.setInside(true);//是否显示在里面，默认为false
			AxisValue value1 = new AxisValue(0f);//值是在哪显示 0 是指 第0个柱状图
			value1.setLabel("A");//设置显示的文本，默认为柱状图的位置
			AxisValue value2 = new AxisValue(1.0f);
			value2.setLabel("B");
			AxisValue value3 = new AxisValue(2.0f);
			value3.setLabel("C");
			AxisValue value4 = new AxisValue(3.0f);
			value4.setLabel("D");
			
			List<AxisValue> axisValues = new ArrayList<AxisValue>();
			axisValues.add(value1);
			axisValues.add(value2);
			axisValues.add(value3);
			axisValues.add(value4);
			axisX.setValues(axisValues);
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("选项");//设置X轴的注释
				axisY.setName("投票数量");//设置Y轴的注释
			}
			mColumnChartData.setAxisXBottom(axisX);//设置X轴显示的位置
			mColumnChartData.setAxisYLeft(axisY);//设置Y轴显示的位置
		} else {
			mColumnChartData.setAxisXBottom(null);
			mColumnChartData.setAxisYLeft(null);
		}
		mColumnChartView.setColumnChartData(mColumnChartData);//为View设置数据
	}
	private int getSign() {
		int[] sign = new int[]{-1, 1};
		return sign[Math.round((float) Math.random())];
	}
	
	
	private class ValueTouchListener implements ColumnChartOnValueSelectListener {
		@Override
		public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
			Toast.makeText(ExerciseVoteActivity.this, Float.toString(value.getValue()), Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onValueDeselected() {
		}
	}
}