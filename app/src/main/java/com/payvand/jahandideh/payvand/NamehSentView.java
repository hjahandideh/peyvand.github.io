package com.payvand.jahandideh.payvand;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.payvand.jahandideh.payvand.Config.TAG_EGHDAM;
import static com.payvand.jahandideh.payvand.Config.TAG_Lname;
import static com.payvand.jahandideh.payvand.Config.TAG_MANAMEH;
import static com.payvand.jahandideh.payvand.Config.TAG_MNAMEH;
import static com.payvand.jahandideh.payvand.Config.TAG_NAME;
import static com.payvand.jahandideh.payvand.Config.TAG_TERSAL;


public class NamehSentView extends AppCompatActivity {

    private TextView mnameh;
    private TextView manameh;
    private TextView ersal;
    private TextView ersall;
    private TextView tersall;
    private TextView eghdam;
    String SetData;
    String recive;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nameh_sent_view);
        Bundle b = getIntent().getExtras();
        SetData = b.getString("id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mnameh = (TextView) findViewById(R.id.textmnamehs);
        manameh = (TextView) findViewById(R.id.textmanamehs);
        ersal = (TextView) findViewById(R.id.textersals);
        ersall = (TextView) findViewById(R.id.textView19s);
        tersall = (TextView) findViewById(R.id.txttersals);
        eghdam = (TextView) findViewById(R.id.textView14);
        getData();

            eghdam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent viewActivity = new Intent(NamehSentView.this, EghdamUpdate.class);
                        Bundle bd=new Bundle();
                        bd.putString("id",SetData);
                        viewActivity.putExtras(bd);
                        startActivity(viewActivity);
                        finish();
                        eghdam.setText("اقدامات انجام شد");

                }
            });


    }

    private void registerUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.NAMEH_EDIT_EGHDAM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", SetData);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getData() {
        final ProgressDialog loading = ProgressDialog.show(this, "در حال دریافت اطلاعات...", "لطفا منتظر بمانید", false, false);
        final StringRequest jsonobjectRequest = new StringRequest(Request.Method.POST, Config.NAMEH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            JSONArray jsonArray = jo.getJSONArray(Config.TAG_NAMEHS);
                            loading.dismiss();
                            parseData(jsonArray);
                        } catch (JSONException e) {
                            Log.i("matis", "error in nameh jsonobject()-->" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(NamehSentView.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", SetData);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonobjectRequest);
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                mnameh.setText(json.getString(TAG_MNAMEH));
                manameh.setText(json.getString(TAG_MANAMEH));
                ersal.setText(json.getString(TAG_NAME));
                ersall.setText(json.getString(TAG_Lname));

                String de=json.getString(TAG_TERSAL);
                String[] dated=de.split("-");
                Roozh roozh =new Roozh();
                int year=Integer.parseInt(dated[0]);
                int month=Integer.parseInt(dated[1]);
                int day=Integer.parseInt(dated[2]);
                roozh.GregorianToPersian(year,month,day);
                tersall.setTypeface(NamehSentAdapter.face);
                tersall.setText(roozh.toString());
                eghdam.setText(json.getString(TAG_EGHDAM));
            } catch (JSONException e) {
                Log.i("matis", "error in nameh paredata parsedata()-->" + recive + e.toString());
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.overridePendingTransition(R.anim.slide_l, R.anim.slide_r);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}