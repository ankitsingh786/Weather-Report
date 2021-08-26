package com.example.weatherreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView pintextresult,states;
    Button register , check;
    TextInputEditText pin,firstname,mobile,address,dob;

    String[] courses = { "Male\n", "Female"};
    private final String url="http://www.postalpincode.in/api/pincode/";

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        firstname=findViewById(R.id.first_name);
        register=findViewById(R.id.register);
        pin=findViewById(R.id.pincode);
        states=findViewById(R.id.state);
        check=findViewById(R.id.check_btn);
        dob=findViewById(R.id.dobs);
        mobile=findViewById(R.id.phone);
        address=findViewById(R.id.add_1);
        pintextresult=findViewById(R.id.pinresult);

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.first_name
        , RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.phone
        ,"[5-9]{1}[0-9]{9}",R.string.invalid_number);
        awesomeValidation.addValidation(this,R.id.add_1
                ,RegexTemplate.NOT_EMPTY,R.string.invalid_address);
        awesomeValidation.addValidation(this,R.id.pincode
                ,RegexTemplate.NOT_EMPTY,R.string.invalid_pincode);
        awesomeValidation.addValidation(this,R.id.dobs
                ,RegexTemplate.NOT_EMPTY,R.string.invalid_dob);
        awesomeValidation.addValidation(this,R.id.gender
                , RegexTemplate.NOT_EMPTY,R.string.invalid_gender);

        check.setEnabled(false); // set button disable initially
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabe1();

            }

            private void updateLabe1() {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dob.setText(sdf.format(myCalendar.getTime()));

            }
        };
        dob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        String state="";
        state+=""+"District : "+"\n\nState : ";
        states.setTextColor(Color.WHITE);
        states.setText(state);
        pin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (s.toString().length()>5) {
                    check.setEnabled(true);
                } else {
                    check.setEnabled(false);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        AutoCompleteTextView spino = findViewById(R.id.gender);
        spino.setOnItemSelectedListener(this);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                courses);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);



        spino.setAdapter(ad);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (awesomeValidation.validate()){
                    String text=pintextresult.getText().toString();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    intent.putExtra("keyname",text);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Something Missing",Toast.LENGTH_SHORT).show();
                }

            }
        });




    }




    public void getPinDetail(View view)
    {

        states.setVisibility(View.VISIBLE);

        String tempurl="";
        String pincodes=pin.getText().toString().trim();
        if(pin.equals("")){
            pintextresult.setText("Pincode Field Cannot Be Empty");
        }else {
            tempurl=url +pincodes;
        }
        StringRequest stringRequest=new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response",response);

                String output="";
                try {
                    JSONObject jsonResponse=new JSONObject(response);

                    JSONArray jsonArray=jsonResponse.getJSONArray("PostOffice");
                    JSONObject obj = jsonArray.getJSONObject(0);
                    String district = obj.getString("District");
                    String state = obj.getString("State");
                    pintextresult.setTextColor(Color.WHITE);
                    output+=""+(district)
                            + "\n\n "+(state);

                    pintextresult.setText(output);
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




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}