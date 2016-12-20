package app.ui.activity.myclass;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import myclass.manager.teacher.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.ui.TitleActivity;
import app.ui.activity.barcode.BarCodeActivity;
import app.ui.activity.barcode.CaptureActivity;
import app.ui.activity.setting.LoginActivity;

public class ShowBarCodeActivity extends TitleActivity implements OnClickListener{
	/* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
	private Button showstudents;
	private ImageView qrImgImageView;
	private int seId;
	private String seName;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        showBackwardView(R.string.button_backward, true);
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        seId = bundle.getInt("seId");
        seName = bundle.getString("seName");
        setTitle(seName);

        showstudents = (Button) this.findViewById(R.id.showstudents);
        showstudents.setOnClickListener(this);
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
        try {
			//String contentString = qrStrEditText.getText().toString();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("seId",seId);
			jsonObject.put("seName",seName);
			
				String contentString = jsonObject.toString();
			if (!contentString.equals("")) {
				//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（600*600）
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 600);
				qrImgImageView.setImageBitmap(qrCodeBitmap);
			}else {
				Toast.makeText(ShowBarCodeActivity.this, "请输入二维码内容", Toast.LENGTH_SHORT).show();
			}
			
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);//实现父类的onClick方法这样才可使使左上角的返回按钮生效
        switch (v.getId()) {
		case R.id.showstudents:
			Intent newIntent = new Intent(ShowBarCodeActivity.this,SeminarSAtudentsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("seId",seId);
			bundle.putString("seName",seName);
			newIntent.putExtras(bundle);
			startActivityForResult(newIntent, 0);
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
