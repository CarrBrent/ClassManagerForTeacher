package app.ui.activity.evaluate;

import myclass.manager.teacher.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.ui.TitleActivity;

public class InGroupEvaluateActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private int seId;
	private String seName;
	private CountDownTimer timer; 
	private EditText et; 
	private TextView tv; 
	private Button start; 
	private Button reset; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_ingroup);
		showBackwardView(R.string.button_backward, true);

		et = (EditText) findViewById(R.id.timer);
		tv = (TextView) findViewById(R.id.lefttime);
		start = (Button) findViewById(R.id.btnStart);
		reset = (Button) findViewById(R.id.btnReset);
		timer = new CountDownTimer(0,0){
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
			}
		};

		start.setOnClickListener(this);
		reset.setOnClickListener(this);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		seId = bundle.getInt("seId");
		seName = bundle.getString("seName");
		setTitle(seName);

	}
	@Override
	public void onClick(View v) {
		super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
		switch (v.getId()) {
		case R.id.btnStart:
			start();
			break;
		case R.id.btnReset:
			reset();
			break;

		default:
			break;
		}

	}
	public void start(){
		String time = et.getText().toString();
		if (time.length() != 0) {
			int millisInFuture=Integer.parseInt(time);
			start.setClickable(false);

			timer = new CountDownTimer(millisInFuture*1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
					tv.setText( millisUntilFinished / 1000 + " S");
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					tv.setText("评价时间到"); 
				}
			};
			timer.start();
		}else {
			Toast.makeText(InGroupEvaluateActivity.this, "请输入评价时间",1).show();;
		}
		
	}

	public void reset() { 
		timer.onFinish();
		timer.cancel();
		start.setClickable(true);
		et.setText(null);
		tv.setText(null);
	}



	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
	}

}
