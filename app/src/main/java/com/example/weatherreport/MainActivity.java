package com.example.weatherreport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView tvresult, etcity;
    ImageButton button;
    private final String url="http://api.weatherapi.com/v1/current.json";
    private final String key="c84b7b83ba524c3ab0c41904212508";
    DecimalFormat df=new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        etcity=findViewById(R.id.etcity);
        String text=getIntent().getStringExtra("keyname");
        etcity.setText(text);
        button=findViewById(R.id.logout);

        tvresult=findViewById(R.id.tvfresult);
    }

    public void getWeatherDetails(View view) {
        String tempurl="";
        String city=etcity.getText().toString().trim();
        if(city.equals("")){
            tvresult.setText("City Field Cannot Be Empty");
        }else {
            tempurl=url + "?key=" +key+ "&q="+city+"&aqi=no";
        }
        StringRequest stringRequest=new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               Log.d("response",response);

                String Output="";
                try {
                    JSONObject jsonResponse=new JSONObject(response);

                    JSONObject jsonObjectCurrent=jsonResponse.getJSONObject("current");
                    double tempc=jsonObjectCurrent.getDouble("temp_c");
                    double tempf=jsonObjectCurrent.getDouble("temp_f");
                    JSONObject jsonObjectLocation=jsonResponse.getJSONObject("location");
                    double lat=jsonObjectLocation.getDouble("lat");
                    double lon=jsonObjectLocation.getDouble("lon");
                    tvresult.setTextColor(Color.WHITE);
                    Output+="Weather Report" +"\n"+"\nTemperature in Centigrade : "+df.format(tempc)+"°C"
                            + "\n\nTemperature in Fahrenheit : "+df.format(tempf)+"°F"
                    +"\n\nLatitude : " +df.format(lat)+"\n\nLongitude : "+df.format(lon);
                    tvresult.setText(Output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void logout(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, button);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.menuu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Toast message on menu item clicked

                System.exit(0);
               return true;
           }
        });
        // Showing the popup menu
        popupMenu.show();
    }


}
