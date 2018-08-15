package com.klaus3d3.xDripwatchface.data;

import android.graphics.Bitmap;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Xdrip {

    public String JSONstr;
    public String strike="";
    public String color="WHITE";
    public String sgv="--";
    public String delta="--";
    public String updatetime="--";

    public String phonebattery="--";
    public String sgv_graph="";
    public Long timestamp=Long.valueOf(1);


    public Xdrip(String parmStr1) {
        this.JSONstr = parmStr1;



        if(parmStr1!=null && !parmStr1.equals("")){
            try {
                // Extract data from JSON
                JSONObject json_data = new JSONObject(parmStr1);
                this.sgv = json_data.getString("sgv");
                this.delta = json_data.getString("delta");
                this.timestamp=Long.valueOf(json_data.getString("updatetime"));
                this.updatetime = json_data.getString("timeago");;
                this.sgv_graph=json_data.getString("sgv_graph");
                this.phonebattery=json_data.getString("phonebattery");
                this.color=json_data.getString("color");
                if(Boolean.valueOf(json_data.getString("strike")))
                this.strike=new String(new char[this.sgv.length()]).replace("\0", "─");
                else this.strike="";
            }catch (Exception e) {
                // Nothing
            }
        }
    }

    public static String Epoch2DateString(long epoch, String formatString) {
        java.util.Date updatedate = new Date(epoch);
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updatedate);
    }

    public String toString() {
        // Default onDataUpdate value
        return String.format("[Xdrip data info] Sting 1:%s", this.JSONstr);
    }
}