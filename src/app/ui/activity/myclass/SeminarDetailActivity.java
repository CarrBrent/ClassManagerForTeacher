package app.ui.activity.myclass;

import myclass.manager.teacher.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import app.ui.TitleActivity;

public class SeminarDetailActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
	private Button start;
	private int seId;
	private String seName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminardetail);
        showBackwardView(R.string.button_backward, true);
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        seId = bundle.getInt("seId");
        seName = bundle.getString("seName");
        setTitle(seName);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        
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
			bundle.putString("seName",seName);
			intent.putExtras(bundle);
			startActivity(intent);
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
