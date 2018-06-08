package com.example.adamnoor.profile.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adamnoor.profile.Activities.BottomNavigationActivity;
import com.example.adamnoor.profile.Activities.LoginActivity;
import com.example.adamnoor.profile.Utils.AlertsUtils;
import com.example.adamnoor.profile.Utils.Api;
import com.example.adamnoor.profile.Utils.sharePrefrence;
import com.techease.profile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {

    EditText etFirstname,etLastName,etEmail,etPass,etCpass;
    CheckBox checkBox;
    Button btnRegister;
    TextView tvHaveAccount,tvTerms;
    boolean abc=false;
    String strUsername,strFname,strLname,strCpass;
    android.support.v7.app.AlertDialog alertDialog;
    String strEmail,strPass,userId,firstName,lastName,email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_register, container, false);


        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        etEmail=view.findViewById(R.id.etEmailReg);
        tvTerms=view.findViewById(R.id.tvTerms);
        tvHaveAccount=view.findViewById(R.id.tvHaveAccount);
        etFirstname=view.findViewById(R.id.etFirstname);
        etLastName=view.findViewById(R.id.etLastName);
        etPass=view.findViewById(R.id.etPassReg);
        etCpass=view.findViewById(R.id.etConfirm);
        checkBox=view.findViewById(R.id.chkbox);
        btnRegister=view.findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                 abc=true;
                }
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Terms & Conditions");
                alertDialogBuilder.setMessage(" We aim to create a community that shares our love for food and is honest, responsible in their feedback.   There is a vacuum for our province and its rich food . To ensure that the PFD serves its purpose there are certain rules that we expect our members to follow:\n" +
                        "• The app is about your food experiences (be it at home, restaurant, or somewhere else)\n" +
                        "• Be honest in your review of a restaurant.\n" +
                        "• If you are the owner of a restaurant or a catering business, you can post / promote it ONCE a week. When you post please state that you belong to the management or own the restaurant. Any excessive posts within a week marketing your restaurant will result in deletion.\n" +
                        "• HEALTHY CRITICISM is welcome but criticizing a restaurant just for the sake of it is not acceptable and unfair to them.\n" +
                        "• And most importantly, please be COURTEOUS and RESPECTFUL towards estaurants. No personal attacks will be tolerated.\n" +
                        "We hope that this app will become a healthy platform that will help foodies in connecting with each Resturants and benefiting from our experiences.");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    private void check() {
        strEmail=etEmail.getText().toString();
        strPass=etPass.getText().toString();
        strCpass=etCpass.getText().toString();
        strFname=etFirstname.getText().toString();
        strLname=etLastName.getText().toString();

        if (strEmail.equals("") && !strEmail.contains("@") && !strEmail.contains(".com"))
        {
            etEmail.setError("Please enter valid email");
        }
        else
        if (strPass.equals(""))
        {
            etPass.setError("Please enter valid password");
        }
        else
        if (strLname.equals(""))
        {
            etLastName.setError("Please fill this field");
        }
        else
        if (strFname.equals(""))
        {
            etFirstname.setError("Please fill this field");
        }
        else
        if (strFname.equals(""))
        {
            etFirstname.setError("Please fill this field");
        }
        else
           if (abc==false)
        {

            Toast.makeText(getActivity(), "Checked the box first", Toast.LENGTH_SHORT).show();
        }
        else
           {
               if (alertDialog==null)
               {
                   alertDialog= AlertsUtils.createProgressDialog(getActivity());
                   alertDialog.show();
               }
               apiCall();
           }
    }

    private void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zmaReg",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject object=jsonObject.getJSONObject("data");
                    strEmail=object.getString("email");
                    userId=object.getString("id");
                    sharePrefrence.SetSharedPrefrence(getActivity(),"id",userId);
                    firstName=object.getString("firstname");
                    lastName=object.getString("lastname");
                    sharePrefrence.SetSharedPrefrence(getActivity(),"token","login");
                    startActivity(new Intent(getActivity(), BottomNavigationActivity.class));
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zma error", String.valueOf(error.getCause()));

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Network Error");
                } else if (error instanceof AuthFailureError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Email or Password Error");
                } else if (error instanceof ServerError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Server Error");
                } else if (error instanceof NetworkError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Network Error");
                } else if (error instanceof ParseError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Parsing Error");
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",strEmail);
                params.put("password",strPass);
                params.put("lastname",strLname);
                params.put("firstname",strFname);
                return params;
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}
