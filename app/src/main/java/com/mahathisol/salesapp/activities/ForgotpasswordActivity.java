package com.mahathisol.salesapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.ForgotPassword;
import com.mahathisol.salesapp.utils.ApiEndPoint;
import com.mahathisol.salesapp.utils.SharedPrefKey;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class ForgotpasswordActivity extends AppCompatActivity {


    private static final String TAG = "ForgotpasswordActivity";
    private static final int REQUEST_SIGNUP = 0;
    ProgressDialog progressDialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    @BindView(R.id.progress)
    ProgressActivity progress;
    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    private SharedPreferences mSecurePrefs;

    String customer_id = "";
    Drawable emptyDrawable;
    Drawable errorDrawable;
    Drawable succDrawable;
    String  deliveryboy_id="";

    String emailid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ButterKnife.bind(this);

        toolbar.setTitle("Forgot Password");

        mSecurePrefs = getSharedPref();
        customer_id = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID, "");
        System.out.println("CUSTMORE_ID" + customer_id);
//        linearLyt.setVisibility(View.GONE);
        succDrawable = new IconDrawable(this, Iconify.IconValue.zmdi_cloud_done)
                .colorRes(R.color.colorAccent);
        emptyDrawable = new IconDrawable(this, Iconify.IconValue.zmdi_alert_circle)
                .colorRes(R.color.colorAccent);

        errorDrawable = new IconDrawable(this, Iconify.IconValue.zmdi_wifi_off)
                .colorRes(R.color.colorAccent);



    }


    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (true) {
                progress.showLoading();
                //  callTimeLineFreshRestApi(customer_id);
            }
        }
    };


    @OnClick(R.id.btn_save)
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }
        btnSave.setEnabled(false);

        progressDialog = new ProgressDialog(ForgotpasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progress.showLoading();
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying Email Address...");
        progressDialog.show();
        String email = inputEmail.getText().toString();
        makeJSONObjectRequest(email);



    }


//        // TODO: Implement your own signup logic here.
//
//        new Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);


    public void onSignupSuccess() {
        btnSave.setEnabled(true);
        setResult(RESULT_OK, null);

        progressDialog.dismiss();
        finish();
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "process failed", Toast.LENGTH_LONG).show();

        btnSave.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = inputEmail.getText().toString();

        //  if (!email.equals(emailid)) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please Enter Email Address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        return valid;
    }

    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }

    public void makeJSONObjectRequest(String username) {

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.GET_EMAIL)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")
                .addHeaders("Content-Type", "application/json; charset=utf-8")
                .addUrlEncodeFormBodyParameter("txtemailid", username)

                // .addQueryParameter("txtemailid", "vendor1@g.com")
                // .addQueryParameter("txtpassword", "vendor1")
                .setTag(ApiEndPoint.LOGIN_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<ForgotPassword>() {
                }, new OkHttpResponseAndParsedRequestListener<ForgotPassword>() {
                    @Override
                    public void onResponse(Response okHttpResponse, ForgotPassword user) {


                        System.out.println(okHttpResponse.toString());

                        if (user.getCreate().getType().equalsIgnoreCase("DoesnotExist")) {


                            inputEmail.setError("Please enter registered email address");
                            progress.showContent();
                            progressDialog.dismiss();
                            onSignupFailed();
                            Toast.makeText(getBaseContext(), "Process failed", Toast.LENGTH_LONG).show();

//                            Intent intent = new Intent(DeliveryBoyLoginActivity.this, VendorActivity.class);
//                            startActivity(intent);
//                            //Apply splash exit (fade out) and main entry (fade in) animation transitions.
//                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//
//                            finish();
                        }  else if (user.getCreate().getType().equalsIgnoreCase("Fail")) {
                            progress.showEmpty(succDrawable,
                                    "Server Error",
                                    "Please Try Again");
                        }else if (user.getCreate().getType().equalsIgnoreCase("success")) {
                            progress.showEmpty(succDrawable,
                                    "A Password has been sent to your EmailId",
                                    "");
                            // emailid=user.getEmailId();
                            //   onSignupSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                            new CountDownTimer(1000, 1000) {
                                public void onFinish() {
                                    // When timer is finished

                                    //   EventBus.getDefault().post(new EventADForm());
                                    // mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                                    //this.onFinish();
                                    // Execute your code here
                                }

                                //
                                public void onTick(long millisUntilFinished) {


                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

                        progress.showError(errorDrawable, "No Connection",
                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
                                "Try Again", errorClickListener);
                               progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }


}
