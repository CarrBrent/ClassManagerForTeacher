package app.ui.activity.myclass;

import myclass.manager.teacher.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import app.ui.TitleActivity;
import app.ui.activity.barcode.SignInActivity;
import app.ui.activity.evaluate.EvaluateActivity;
import app.ui.activity.exercises.ExerciseActivity;

public class SeminarDetailActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
	private Button start;
	private int seId;
	private int cId;
	private String seName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminardetail);
        showBackwardView(R.string.button_backward, true);
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        seId = bundle.getInt("seId");
        cId = bundle.getInt("cId");
        seName = bundle.getString("seName");
        setTitle(seName);
        start = (Button) findViewById(R.id.start);
        //为控件添加监听器
        start.setOnClickListener(this);
        findViewById(R.id.layout_1).setOnClickListener(this);
        findViewById(R.id.layout_2).setOnClickListener(this);
        findViewById(R.id.layout_3).setOnClickListener(this);
        findViewById(R.id.layout_4).setOnClickListener(this);
        
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
        switch (v.getId()) {
		case R.id.start:
			Intent intent = new Intent();
			intent.setClass(SeminarDetailActivity.this, ShowBarCodeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("seId",seId);
			bundle.putInt("cId",cId);
			bundle.putString("seName",seName);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.layout_1:
			Intent groupingintent = new Intent();
			groupingintent.setClass(SeminarDetailActivity.this, GroupingActivity.class);
			Bundle groupingbundle = new Bundle();
			
			groupingbundle.putInt("seId",seId);
			groupingbundle.putInt("cId",cId);
			groupingbundle.putString("seName",seName);
			groupingintent.putExtras(groupingbundle);
			startActivity(groupingintent);
			break;
		case R.id.layout_2:
			Intent exerciseintent = new Intent();
			exerciseintent.setClass(SeminarDetailActivity.this, ExerciseActivity.class);
			Bundle exercisebundle = new Bundle();
			exercisebundle.putInt("seId",seId);
			exercisebundle.putInt("cId",cId);
			exercisebundle.putString("seName",seName);
			exerciseintent.putExtras(exercisebundle);
			startActivity(exerciseintent);
			break;
		case R.id.layout_3:
			Intent evaluateintent = new Intent();
			evaluateintent.setClass(SeminarDetailActivity.this, EvaluateActivity.class);
			Bundle evaluatebundle = new Bundle();
			evaluatebundle.putInt("seId",seId);
			evaluatebundle.putInt("cId",cId);
			evaluatebundle.putString("seName",seName);
			evaluateintent.putExtras(evaluatebundle);
			startActivity(evaluateintent);
			break;

		default:
			break;
		}
        
    }
    
    
    @Override
    protected void onBackward(View backwardView) {
        super.onBackward(backwardView);
    }

}
