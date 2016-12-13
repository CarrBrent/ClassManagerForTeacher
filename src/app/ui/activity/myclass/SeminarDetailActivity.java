package app.ui.activity.myclass;

import myclass.manager.teacher.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import app.ui.TitleActivity;

public class SeminarDetailActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
	TextView tvSeId;
	TextView tvCId;
	TextView tvSeName;
	TextView tvSeTheme;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminardetail);
        showBackwardView(R.string.button_backward, true);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        int SeId = bundle.getInt("SeId");
        int CId = bundle.getInt("CId");
        String SeName = bundle.getString("SeName");
        String SeTheme = bundle.getString("SeTheme");
        setTitle(SeName);
        tvCId = (TextView) findViewById(R.id.CId);
        tvSeId = (TextView) findViewById(R.id.SeId);
        tvSeName = (TextView) findViewById(R.id.SeName);
        tvSeTheme = (TextView) findViewById(R.id.SeTheme);
        
        tvCId.setText(String.valueOf(CId));
        tvSeId.setText(String.valueOf(SeId));
        tvSeName.setText(SeName);
        tvSeTheme.setText(SeTheme);
        
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
        switch (v.getId()) {
		case R.id.layout_score:
			Toast.makeText(getApplicationContext(), "我的成绩", 1).show();
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
