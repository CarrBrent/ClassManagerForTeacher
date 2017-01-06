package app.ui.activity.evaluate;

import myclass.manager.teacher.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import app.ui.TitleActivity;
import app.ui.activity.barcode.SignInActivity;
import app.ui.activity.myclass.GroupingActivity;

public class EvaluateActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
	private int seId;
	private int cId;
	private String seName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        showBackwardView(R.string.button_backward, true);
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        seId = bundle.getInt("seId");
        cId = bundle.getInt("cId");
        seName = bundle.getString("seName");
        setTitle(seName);
        //为控件添加监听器
        findViewById(R.id.layout_1).setOnClickListener(this);
        findViewById(R.id.layout_2).setOnClickListener(this);
        findViewById(R.id.layout_3).setOnClickListener(this);
        
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
        switch (v.getId()) {
		case R.id.layout_1:
			Intent ingroupintent = new Intent();
			ingroupintent.setClass(EvaluateActivity.this, InGroupEvaluateActivity.class);
			Bundle ingroupbundle = new Bundle();
			ingroupbundle.putInt("seId",seId);
			ingroupbundle.putInt("cId",cId);
			ingroupbundle.putString("seName",seName);
			ingroupintent.putExtras(ingroupbundle);
			startActivity(ingroupintent);
			break;
		case R.id.layout_2:
			Intent outgroupintent = new Intent();
			outgroupintent.setClass(EvaluateActivity.this, OutGroupEvaluateActivity.class);
			Bundle outgroupbundle = new Bundle();
			outgroupbundle.putInt("seId",seId);
			outgroupbundle.putInt("cId",cId);
			outgroupbundle.putString("seName",seName);
			outgroupintent.putExtras(outgroupbundle);
			startActivity(outgroupintent);
			break;
		case R.id.layout_3:
			Intent teacherintent = new Intent();
			teacherintent.setClass(EvaluateActivity.this, TeacherEvaluateActivity.class);
			Bundle teacherbundle = new Bundle();
			teacherbundle.putInt("seId",seId);
			teacherbundle.putInt("cId",cId);
			teacherbundle.putString("seName",seName);
			teacherintent.putExtras(teacherbundle);
			startActivity(teacherintent);
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
