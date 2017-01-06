package app.ui.activity.evaluate;

import java.util.List;
import java.util.Map;

import myclass.manager.teacher.R;
import android.content.Context;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter{
	private List<Map<String, Object>> keys;  
    private Integer resource;  
    private Context context;  
    private LayoutInflater inflater;  
      
    public SpinnerAdapter(Context context, List<Map<String, Object>> keys,int resource){  
        this.keys=keys;  
        this.resource=resource;  
        this.context=context;  
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }  
      
    public int getCount() {  
        // TODO Auto-generated method stub  
        return keys.size();  
    }  
  
    public Map<String, Object> getItem(int arg0) {  
        // TODO Auto-generated method stub  
        return keys.get(arg0);  
    }
  
    public long getItemId(int arg0) {  
        // TODO Auto-generated method stub  
        return arg0;  
    }
    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {  
        // TODO Auto-generated method stub  
        if(arg1==null){  
            arg1=inflater.inflate(resource, null);
        }  
        final Map<String, Object> key = keys.get(arg0);
        TextView tv_key_id=(TextView)arg1.findViewById(R.id.tv_key_id);  
        final TextView value=(TextView)arg1.findViewById(R.id.value);
        final TextView tv_key=(TextView)arg1.findViewById(R.id.tv_key);  
        final Spinner key_value=(Spinner)arg1.findViewById(R.id.key_value);  
        tv_key_id.setText(Integer.toString((Integer)key.get("keyId"))); 
        value.setText("优秀"); 
        tv_key.setText((String)key.get("key"));
        
        key_value.setOnItemSelectedListener(new OnItemSelectedListener() {  
            public void onItemSelected(AdapterView<?> key_value_arg0, View key_value_arg1,  
                    int key_value_arg2, long key_value_arg3) {
                // TODO Auto-generated method stub  
            	keys.get(arg0).put("value", key_value.getSelectedItem().toString());
            	//tv_key.setText(key_value.getSelectedItem().toString());
            }  
            public void onNothingSelected(AdapterView<?> arg0) {  
                // TODO Auto-generated method stub  
            }  
        });  
        return arg1;  
    }
}
