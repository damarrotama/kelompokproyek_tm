package com.ghifa.mobile.kelompokproyek2.component;
/** USED */
/**
 * Created by ghifa on 07/10/16.
 */
import android.content.Context;


public class ControllerUrl {

    public String TAG_SUKSES        = "sukses";

    private String host = "";
    private String URL = "";
    public int IS_LOGIN = 1;

    public String TAG_IS_LOGIN = "is_login";


    public ControllerUrl(Context context) {

        setURL();
    }
    void setURL(){
        this.URL = "http://192.168.1.87/paw/lowker/admpanel/api/";
    }

    public String getURLLogin(){
        return this.URL + "get-user.php";
    }
    public String getURLPengumuman(){
        return this.URL + "get-pengumuman.php";
    }

}
