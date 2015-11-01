package com.scp.abc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner bTypeOfCrime;
    Button bSendReportSMS, bSendReportInternet;
    EditText etCrimeDetails;
    TextView tvLocation;
    double latitude, longitude;
    String[] items = new String[]{"Theft", "Rape", "Accident", "Murder"};
    String street, imei;
    String Type=items[0];
    String GoogleGeoCodingAPI="AIzaSyBuYPePPAf_rmP5lsd4D0HR2JUAj87oZQo";
    SharedPreferences preferences = null;
    Toolbar toolbar;
    int pid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        drawer();
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
    }

    private void getIMEI() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
    }

    private void sendReport() throws IOException {
        bSendReportInternet = (Button) findViewById(R.id.bSendReportInternet);
        bSendReportSMS = (Button) findViewById(R.id.bSendReportSMS);
        System.out.println();
        bSendReportSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textMessage = Type + "," + etCrimeDetails.getText().toString() + "," + latitude + "," + longitude + "," + imei;
                System.out.println(textMessage);
                SmsManager.getDefault().sendTextMessage("+919248066655", null, textMessage, null, null);
                Toast.makeText(MainActivity.this, "Sending text Message", Toast.LENGTH_SHORT).show();
            }
        });
        bSendReportInternet = (Button) findViewById(R.id.bSendReportInternet);
        bSendReportInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Sending Message", Toast.LENGTH_SHORT).show();
                sendData();
            }
        });
    }

    private void sendData() {

        String k="1234";
        try {
            k = sendDataToServer("http://e414646b.ngrok.io//hack/AndroidConnectingToPhpMySQL/android/create_product.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(k);
        pid=Integer.parseInt(String.valueOf(k.charAt(4)))*10+Integer.parseInt(String.valueOf(k.charAt(5)));
        //Toast.makeText(MainActivity.this,"Your complain has been registered. Your complaint no. is " + pid,Toast.LENGTH_LONG).show();
        showDialog();
        System.out.println(pid);
        saveData();
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Response");
        builder.setMessage("Your complain has been registered. Your complaint no. is " + pid);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Type",Type);
        editor.putString("detail", etCrimeDetails.getText().toString());
        editor.putString("street", street);
        System.out.println(street);
        editor.putString("imei", imei);
        editor.putInt("pid", pid);
        editor.apply();
    }

    private String sendDataToServer(String url) throws IOException{
        HttpRequest req = new HttpRequest(url);
        HashMap<String, String>params=new HashMap<>();
        params.put("type", Type);
        params.put("imei", imei);
        params.put("detail", etCrimeDetails.getText().toString());
        params.put("street", street);
        String q="1234";
        try {
            q=req.preparePost().withData(params).sendAndReadJSON().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return q;
    }


    private void geoLocation() {
        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            //Toast.makeText(MainActivity.this,latitude+" "+longitude,Toast.LENGTH_LONG).show();
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+ Double.toString(latitude)+","+Double.toString(longitude)+"&key="+GoogleGeoCodingAPI;
            String loc = "abc";
            HttpRequest httpRequest = null;
            try {
                httpRequest = new HttpRequest(url);
                loc = httpRequest.prepare().sendAndReadString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(loc);
                JSONArray geoData = jsonObject.getJSONArray("results");
                int n = geoData.length();
                if(n==0)
                    loc = "Surat, Gujarat";
                for(int i =0;i<n;i++){
                    loc=geoData.getJSONObject(i).getString("formatted_address");
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            street = loc;
            tvLocation = (TextView) findViewById(R.id.tvLocation);
            tvLocation.setText(loc);
        }else{
            gps.showSettingsAlert();
        }
    }

    private void crimeDetails() {
        etCrimeDetails = (EditText) findViewById(R.id.etCrimeDetails);
    }

    private void dropDownMenu(){
        bTypeOfCrime = (Spinner) findViewById(R.id.bTypeCrime);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        bTypeOfCrime.setAdapter(adapter);
        bTypeOfCrime.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Type = items[position].toLowerCase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void drawer() {
        toolbar = (Toolbar) findViewById(R.id.tb);
        toolbar.setTitle("Apraadh");
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Register Crime");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Check Status");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("Exit");
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawerbackground).build();



        Drawer result = new DrawerBuilder().withToolbar(toolbar).withAccountHeader(headerResult).withActivity(MainActivity.this).addDrawerItems(item1, item2, item3).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                switch (i) {
                    case 1:
                        return false;
                    case 2:
                        checkStatus();
                        break;
                    case 3:
                        finish();
                        break;
                }
                return false;
            }
        }).build();

    }

    private void checkStatus() {
        Intent intent = new Intent(MainActivity.this,checkStatus.class);
        String typePass = preferences.getString("Type","null");
        String detailPass = preferences.getString("detail","null");
        String imeiPass = preferences.getString("imei","null");
        int pidPass = preferences.getInt("pid", -1);
        intent.putExtra("Type",typePass);
        intent.putExtra("detail",detailPass);
        intent.putExtra("street",street);
        intent.putExtra("imei",imeiPass);
        intent.putExtra("pid",pidPass);
        startActivity(intent);

    }
}
