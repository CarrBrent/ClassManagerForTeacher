package app.util;

import android.app.Application;  

public class BaseInfo extends Application{  
    private String url;  
      
    public String getUrl(){  
        return this.url;  
    }  
    public void setUrl(String url){  
        this.url= url;  
    }  
    @Override  
    public void onCreate(){  
        //url = "http://219.216.65.246:8080/spring/";
    	url = "http://192.168.56.1:8080/IB/"; 
        super.onCreate();  
    }  
}  