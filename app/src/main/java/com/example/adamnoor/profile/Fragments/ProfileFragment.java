package com.example.adamnoor.profile.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.adamnoor.profile.Activities.SplashActivity;
import com.example.adamnoor.profile.Utils.AlertsUtils;
import com.example.adamnoor.profile.Utils.Api;
import com.example.adamnoor.profile.Utils.HTTPMultiPartEntity;
import com.example.adamnoor.profile.Utils.sharePrefrence;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.techease.profile.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    String Name,userId;
    ImageView imageView;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    TextView tvName;
    LinearLayout logout;
    final int CAMERA_CAPTURE = 1;
    final int RESULT_LOAD_IMAGE = 2;
    android.support.v7.app.AlertDialog alertDialog;
    File file;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Name=sharedPreferences.getString("name","");
        userId=sharedPreferences.getString("id","");

        imageView=view.findViewById(R.id.profile_image);
        logout=view.findViewById(R.id.logout);
        tvName=view.findViewById(R.id.tvName);
        tvName.setText(Name);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
                pictureDialog.setTitle("Open");
                String[] pictureDialogItems = {
                        "\tGallery",
                        "\tCamera"};
                pictureDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        galleryIntent();
                                        break;
                                    case 1:
                                        cameraIntent();
                                        break;
                                }
                            }
                        });
                pictureDialog.show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().commit();
                editor.remove("token").commit();
                editor.remove("name").commit();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }

        }).check();
        return view;
    }
    public void cameraIntent() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, CAMERA_CAPTURE);
    }

    public void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && null != data) {
            Uri selectedImageUri = data.getData();
            String imagepath = getPath(selectedImageUri);
            file = new File(imagepath);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE && data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            file = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                file.createNewFile();
                fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(thumbnail);

            if (alertDialog==null)
            {
                alertDialog= AlertsUtils.createProgressDialog(getActivity());
                alertDialog.show();
            }
            ProfileFragment.UploadFileToServer uploadFileToServer=new UploadFileToServer();
            uploadFileToServer.execute();
        }

    }
    @SuppressLint("SetTextI18n")
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));

        if (alertDialog==null)
        {
            alertDialog= AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        ProfileFragment.UploadFileToServer uploadFileToServer=new UploadFileToServer();
        uploadFileToServer.execute();
        return cursor.getString(column_index);

    }
    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Api.uploadImage);
            try {
                HTTPMultiPartEntity entity = new HTTPMultiPartEntity(
                        new HTTPMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) 100) * 100));
                            }
                        });
                // Adding file data to http body
                // Extra parameters if you want to pass to server
                entity.addPart("file", new FileBody(file));
                Looper.prepare();
                entity.addPart("userid", new StringBody(userId));

                Log.d("zmaFile",file.toString());


                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                responseString = EntityUtils.toString(r_entity);


            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.d("zmaClient", e.getCause().toString());

            } catch (IOException e) {
                responseString = e.toString();
                Log.d("zmaIo", responseString);
            }

            Log.d("zma return string", responseString);
            return responseString;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("zmaResss",s);
            if (alertDialog!=null)
                alertDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if (s.contains("true"))
            {
                builder.setMessage("Uploaded successfully!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
            else
            {
                builder.setMessage(s);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                builder.show();
            }

        }
    }


}
