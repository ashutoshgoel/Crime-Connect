package com.scp.abc;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by sakshi on 11/1/2015.
 */
public class checkStatus extends Activity{

    String Type, street, imei, url, status, details;
    int pid;
    TextView tvStatus, tvType, tvStreet, tvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkstatus);
        Bundle extras = getIntent().getExtras();
        street = "avd";
        if(extras!=null){
            Type = extras.getString("Type");
            street = extras.getString("street");
            System.out.println(street);
            imei = extras.getString("imei");
            pid = extras.getInt("pid");
            details = extras.getString("detail");
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvType = (TextView) findViewById(R.id.tvType);
        tvStreet = (TextView) findViewById(R.id.tvLocation);
        tvDetails = (TextView) findViewById(R.id.tvDetails);
        tvType.setText(Type);
        tvStreet.setText(street);
        tvDetails.setText(details);
        url = "http://e414646b.ngrok.io//hack/AndroidConnectingToPhpMySQL/android/get_product_details.php?pid="+String.valueOf(pid);
        try {
            HttpRequest req = new HttpRequest(url);
            status = req.prepare().sendAndReadString();
            System.out.println(status);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status = String.valueOf(status.charAt(12));
        switch(Integer.parseInt(status)){
            case 0:
                status="Complain made";
                break;

            case 1:
                status="Complain under process";
                break;

            case 2:
                status="Complain Resolved";
                break;
        }
        tvStatus.setText(status);
    }
}
