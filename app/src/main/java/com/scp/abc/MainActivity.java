package com.scp.abc;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {

    Button bTypeCrime, bSendReportSMS, bSendReportInternet;
    EditText etCrimeDetails;
    TextView tvLocation;
    double latitude, longitude;
    String Type, street, imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        dropDownMenu();
        crimeDetails();
        geoLocation();
        getIMEI();
        //TODO optional AADHAR Card number
        try {
            sendReport();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new DrawerBuilder().withActivity(this).build();
    }

    private void getIMEI() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
    }

    private void sendReport() throws IOException {
        bSendReportInternet = (Button) findViewById(R.id.bSendReportInternet);
        bSendReportSMS = (Button) findViewById(R.id.bSendReportSMS);
        final String textMessage = Type + "," + latitude + "," + longitude + "," + etCrimeDetails.getText().toString();
        bSendReportSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager.getDefault().sendTextMessage("Phone Number", null, textMessage, null, null);
                Toast.makeText(MainActivity.this, "Sending text Message", Toast.LENGTH_SHORT).show();
            }
        });

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type",Type);
            jsonObject.put("street",street);
            jsonObject.put("imei",imei);
            jsonObject.put("detail",etCrimeDetails.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        System.out.println(jsonObject.toString());
        String k = sendDataToServer("http://192.168.247.102/hack/AndroidConnectingToPhpMySQL/android/create_product.php", jsonArray.toString());
        System.out.println(k);
    }

    private String sendDataToServer(String url, String data) throws IOException{


        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.MINUTES);
        client.setReadTimeout(5,TimeUnit.MINUTES);
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }


    private void geoLocation() {
        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            String loc = "abc";
            tvLocation = (TextView) findViewById(R.id.tvLocation);
            tvLocation.setText(loc);
            street = loc;
        }else{
            gps.showSettingsAlert();
        }
    }

    private void crimeDetails() {
        etCrimeDetails = (EditText) findViewById(R.id.etCrimeDetails);
        //Send the details
    }


    private void dropDownMenu(){
        bTypeCrime = (Button) findViewById(R.id.bTypeCrime);
        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(MainActivity.this, bTypeCrime);
        droppyBuilder.addMenuItem(new DroppyMenuItem("Theft"))
                .addMenuItem(new DroppyMenuItem("Rape"))
                .addMenuItem(new DroppyMenuItem("Accident"))
                .addMenuItem(new DroppyMenuItem("Murder"))
                .addSeparator();
        final DroppyMenuPopup droppyMenu = droppyBuilder.build();
        bTypeCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppyMenu.show();
            }
        });
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View view, int i) {
                Type = String.valueOf(i);
            }
        });
    }
}
