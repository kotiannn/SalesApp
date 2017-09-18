package com.mahathisol.salesapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.pojos.UploadCompany;
import com.mahathisol.salesapp.pojos.UploadCustomer;
import com.mahathisol.salesapp.pojos.UploadPaymentMode;
import com.mahathisol.salesapp.pojos.UploadProduct;
import com.mahathisol.salesapp.pojos.UploadProductCategory;
import com.mahathisol.salesapp.pojos.UploadProductCompany;
import com.mahathisol.salesapp.pojos.UploadProductType;
import com.mahathisol.salesapp.pojos.UploadVaraint;
import com.mahathisol.salesapp.pojos.User;
import com.mahathisol.salesapp.utils.ApiEndPoint;
import com.mahathisol.salesapp.utils.SharedPrefKey;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper myDb;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String customer_id = "";
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_forgot)
    TextView linkForgot;
    UploadVaraint.Variantlist addr;
    UploadProductType.Prtypelist prlst;
    UploadCustomer.Custlist culist;
    UploadProductCategory.Prcat prcat;
    UploadProduct.Prlist product;
    UploadProductCompany.Prcomplist prcomp;
    UploadCompany.Comapnyli comp;
    UploadPaymentMode.Paymentmodeli pay;
    int tripstatus = 0;
    String tripno = "";
    private ProgressDialog progressDialog;


    private SharedPreferences mSecurePrefs;


    private String device_token = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // _emailText.setText("raju@g.com");
        myDb = new DatabaseHelper(this);
        mSecurePrefs = getSharedPref();

        device_token = mSecurePrefs.getString(SharedPrefKey.DEVICE_TOKEN_KEY, "");
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        // mSecurePrefs= AppController.getInstance().getSharedPreferences1000();

        mSecurePrefs
                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(
                            SharedPreferences sharedPreferences, String key) {
                        //updateEncValueDisplay();
                        // Toast.makeText(getApplicationContext(), "onSharedPreferenceChanged"+key, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @OnClick(R.id.btn_login)
    public void logIn() {
        login();
        downloadCustomer();
        downloadVariant();
        downloadProductType();
        downloadProductCompany();
        downloadProduct();
        downloadProductCategory();
        downloadCompany();
        downloadPaymentMode();
    }
    public void downloadPaymentMode() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PAYMENT)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadPaymentMode>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadPaymentMode>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadPaymentMode adel) {

                        List<UploadPaymentMode.Paymentmodeli> custdatalist = adel.getPaymentmodeli();


                        if (!custdatalist.isEmpty()) {

                            int lengthData = adel.getPaymentmodeli().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                pay = custdatalist.get(i);
                                myDb.saveToPaymentMode(pay.getId(), pay.getDescription(),pay.getActive());
                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    public void downloadVariant() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_VARIANT)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadVaraint>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadVaraint>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadVaraint adel) {

                        List<UploadVaraint.Variantlist> custdatalist = adel.getVariantlist();


                        if (!custdatalist.isEmpty()) {

                            int lengthData = adel.getVariantlist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                addr = custdatalist.get(i);
                                saveToVariant(addr.getId(), addr.getDescription());
                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadProductType() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PRODUCT_TYPE)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadProductType>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadProductType>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadProductType adel) {

                        List<UploadProductType.Prtypelist> prdatalist = adel.getPrtypelist();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getPrtypelist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                prlst = prdatalist.get(i);
                                saveToProductType(prlst.getId(), prlst.getDescription(), prlst.getProdcategoryid());
                            }


                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadProductCompany() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PRODUCT_COMPANY)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadProductCompany>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadProductCompany>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadProductCompany adel) {

                        List<UploadProductCompany.Prcomplist> prdatalist = adel.getPrcomplist();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getPrcomplist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                prcomp = prdatalist.get(i);
                                saveToProductCompany(prcomp.getId(), prcomp.getName());
                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadProduct() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PRODUCT)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadProduct>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadProduct>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadProduct adel) {

                        List<UploadProduct.Prlist> prdatalist = adel.getPrlist();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getPrlist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                product = prdatalist.get(i);
                                saveToProduct(product.getId(), product.getProductcode(), product.getPrCompanyId(), product.getPrTypeId(), product.getPrVariantId(), product.getCgstRate(), product.getSgstRate(), product.getIgstRate(), product.getBarcodeno(), product.getHsncode(), product.getMrpRate(), product.getRetailRate());

                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadProductCategory() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_PRODUCT_CATEGORY)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadProductCategory>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadProductCategory>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadProductCategory adel) {

                        List<UploadProductCategory.Prcat> prdatalist = adel.getPrcat();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getPrcat().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                prcat = prdatalist.get(i);
                                saveToProductCategory(prcat.getId(), prcat.getDescription());
                            }
                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void downloadCompany() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_COMPANY)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadCompany>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadCompany>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadCompany adel) {

                        List<UploadCompany.Comapnyli> prdatalist = adel.getComapnyli();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getComapnyli().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                comp = prdatalist.get(i);
                                saveToCompany(comp.getId(),comp.getName(),comp.getMobile(),comp.getEmailid(),comp.getAddress1(),
                                        comp.getAddress2(),comp.getAddress3(),comp.getCity(),comp.getStateid(),comp.getPincode(),comp.getGstinno(),
                                        comp.getInvoiceno(),comp.getTaxaccross(),comp.getCgst(),comp.getSgst(),comp.getIgst(),comp.getInvoicectr(),
                                        comp.getTaxincludedamt(),comp.getReportfooter(),comp.getShowbankdetails(),comp.getAccholdername(),
                                        comp.getAccno(),comp.getBankname(),comp.getBankbranch(),comp.getIfsccode(),comp.getInheaderwithtax(),
                                        comp.getInheaderwithouttax(),comp.getPassword(),comp.getLogincount(),comp.getTripnoAI(),comp.getTripnoCounter(),comp.getCompanylogo());
                            }

                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    public void downloadCustomer() {

        AndroidNetworking.get(ApiEndPoint.BASE_URL + ApiEndPoint.GET_CUSTOMER)
                // AndroidNetworking.get("http://www.json-generator.com/api/json/get/ceMXczcMpu?indent=2")

                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsOkHttpResponseAndParsed(new TypeToken<UploadCustomer>() {
                }, new OkHttpResponseAndParsedRequestListener<UploadCustomer>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UploadCustomer adel) {

                        List<UploadCustomer.Custlist> prdatalist = adel.getCustlist();


                        if (!prdatalist.isEmpty()) {
                            int lengthData = adel.getCustlist().size();
                            for (int i = 0; i <= lengthData - 1; i++) {
                                culist = prdatalist.get(i);
                                saveToCustomer(culist.getId(), culist.getName(), culist.getMobile(),culist.getEmailid(),culist.getAddress1(),culist.getAddress2(),
                                        culist.getAddress3(),culist.getCity(),culist.getStateid(),culist.getPincode(),culist.getGstinno(),
                                        culist.getActive(),culist.getRetailOrMRP());

                            }
                            int maxcid = myDb.getMAXid("Customer");

                            int count = myDb.getCount("Counters");
                            int ctr = maxcid+1;
                            if(count > 0){
                                myDb.updateCounter(ctr,"customerId","Counters");
                            }else {
                                myDb.insertCustomerIdCOunter(ctr,"customerId");

                            }
                            myDb.updateCounter(1,"invoiceId","Counters");
                        } else {
//                            progress.showEmpty(emptyDrawable,
//                                    "Trip Product is empty",
//                                    "Please wait");
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        //progressActivity.showContent();

//                        progress.showError(errorDrawable, "No Connection",
//                                "We could not establish a connection with our servers. Try again when you are connected to the internet.",
//                                "Try Again", errorClickListener);

                        //Toast.makeText(getApplicationContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void saveToVariant(String id, String desc) {

        //myDb.delete("Variant");

        myDb.addVariant(id, desc);

    }

    private void saveToProductType(String id, String desc, String catid) {

        // myDb.delete("ProductType");

        myDb.addProductType(id, desc, catid);
    }

    private void saveToProductCompany(String id, String desc) {

        //  myDb.delete("ProductCompany");

        myDb.addProductCompany(id, desc);
    }

    private void saveToProduct(String id, String prcode, String pcompid, String ptypeid, String varintid, String cgstRate, String sgstRate, String igstRate, String barcodeno, String hsncode, String mrp, String retail) {

        // myDb.delete("Product");


        myDb.addProduct(id, prcode, pcompid, ptypeid, varintid, cgstRate, sgstRate, igstRate, barcodeno, hsncode, mrp, retail);
    }

    private void saveToProductCategory(String id, String desc) {

        //  myDb.delete("ProductCategory");

        myDb.addProductCategory(id, desc);
    }

    private void saveToCompany(String id, String name, String mobile, String emailid, String address1, String address2, String address3, String city, String stateid, String pincode, String gstinno, String invoiceno, String taxaccross, String cgst, String sgst, String igst, String invoicectr, String taxincludedamt, String reportfooter, String showbankdetails, String accholdername, String accno, String bankname, String bankbranch, String ifsccode, String inheaderwithtax, String inheaderwithouttax, String password, String logincount, String tripnoAI, String tripnoCounter,String complogo) {

        // myDb.delete("Company");

        myDb.addCompany(id,name,mobile,emailid,address1,address2,address3,city,stateid,pincode,gstinno,invoiceno,taxaccross,cgst,sgst,igst,invoicectr,taxincludedamt,reportfooter,showbankdetails,accholdername,accno,bankname,bankbranch,ifsccode,inheaderwithtax,inheaderwithouttax,password,logincount,tripnoAI,tripnoCounter,complogo);
    }
    private void saveToCustomer(String id, String name, String mobile, String emailid, String address1, String address2, String address3, String city, String stateid, String pincode, String gstinno, String active, String retailOrMRP) {
        //   int sid = myDb.getCount();
        // Toast.makeText(getApplicationContext(),"hello"+sid, Toast.LENGTH_LONG).show();
        // myDb.delete("Customer");

        myDb.addCustomer(id,name,mobile,emailid,address1,address2,address3,city,stateid,pincode,gstinno,active,retailOrMRP);
    }

    //
    @OnClick(R.id.link_forgot)
    public void link_forgot() {
        Intent intent = new Intent(getApplicationContext(), ForgotpasswordActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        //finish();
        // overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    // _signupLink.setOnClickListener(new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // Start the Signup activity
//            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//            startActivityForResult(intent, REQUEST_SIGNUP);
//        }
//    });
//}
    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        makeJSONObjectRequest(email, password);
        //testserver();


//        new Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    //Social login


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                //this.finish();
                //  Toast.makeText(this, "successful signup logic ", Toast.LENGTH_SHORT).show();

            }
        }


    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        _loginButton.setEnabled(true);
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        //   Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
//|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (email.isEmpty()) {
            _emailText.setError("enter valid email address or mobile number");
            valid = false;


        } else {
            _emailText.setError(null);
        }
//        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//
//        } else if( !Patterns.PHONE.matcher(email).matches() || email.length() > 10){
//            _emailText.setError("enter a valid mobile number");
//            valid = false;
//
//        }else {
//
//            _emailText.setError(null);
//        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");

            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    //networkcall
    private void makeJSONObjectRequest(String username, String passsword) {

//        Toast.makeText(getBaseContext(), "makeJSONObjectRequest", Toast.LENGTH_LONG).show();
//        Log.d(TAG,username+passsword+device_token+ApiEndPoint.DEVICE_TYPE);

        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.LOGIN_POSTMETHOD_NAME)
                // AndroidNetworking.get(ApiEndPoint.BASE_URL+ApiEndPoint.LOGIN_GETMETHOD_NAME)


                //AndroidNetworking.post("http://www.json-generator.com/api/json/get/bYRTegwKYy?indent=2")
                .addHeaders("Content-Type", "application/json; charset=utf-8")
                .addUrlEncodeFormBodyParameter("txtemailid", username)
                .addUrlEncodeFormBodyParameter("txtpassword", passsword)
                // .addQueryParameter("txtemailid", "vendor1@g.com")
                // .addQueryParameter("txtpassword", "vendor1")
                .setTag(ApiEndPoint.LOGIN_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<User>() {
                }, new OkHttpResponseAndParsedRequestListener<User>() {
                    @Override
                    public void onResponse(Response okHttpResponse, User user) {


                        System.out.println(okHttpResponse.toString());
                        System.out.println(user.getValue().toString());
                        if (user == null || user.getValue().isEmpty()) {

                            _passwordText.setError("enter a valid password");
                            _emailText.setError("enter a valid email address or mobile number");
                            progressDialog.dismiss();
                            onLoginFailed();
                            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

//                            Intent intent = new Intent(CustomerLoginActivity.this, VendorActivity.class);
//                            startActivity(intent);
//                            //Apply splash exit (fade out) and main entry (fade in) animation transitions.
//                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//
//                            finish();
                        } else {
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();


                            //  mSecurePrefs.putString("customer_id", customer_id);

                            System.out.println("CUSTMORE_ID" + user.getValue().get(0).getId());

                            mSecurePrefs.edit().putString(SharedPrefKey.CUSTOMER_ID, user.getValue().get(0).getId()).apply();
                            mSecurePrefs.edit().putString(SharedPrefKey.CUSTOMER_NAME, user.getValue().get(0).getName()).apply();


                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                            finish();

                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error

                        Toast.makeText(getApplicationContext(), "Server Not Responding", Toast.LENGTH_SHORT).show();
                        _loginButton.setEnabled(true);
                    }
                });

    }


}



