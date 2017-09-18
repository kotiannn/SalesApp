package com.mahathisol.salesapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.fragments.AddPaymentFragment;
import com.mahathisol.salesapp.pojos.InvDetailsGroupByHSN;
import com.mahathisol.salesapp.pojos.InvoiceDetailsPrint;
import com.mahathisol.salesapp.pojos.InvoicePrintList;
import com.mahathisol.salesapp.utils.SharedPrefKey;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BloothPrinterActivity extends AppCompatActivity {
    // Debugging
    private static final String TAG = "BloothPrinterActivity";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static int revBytes = 0;
    public static boolean isHex = false;

    public static final int REFRESH = 8;
    private DatabaseHelper db;
  List<InvoicePrintList> invoicePrintList = new ArrayList<>();
    List<InvoiceDetailsPrint> invoiceDetailsPrintList = new ArrayList<>();
    List<InvDetailsGroupByHSN> invDetailsGroupByHSNs = new ArrayList<>();
    InvoiceDetailsPrint addr;
    String invoiceno = "";
    String invoiceid = "";
    @BindView(R.id.btn_connect_bluetooth_device)
    Button btnConnectBluetoothDevice;

    @BindView(R.id.btn_print)
    Button btnPrint;
//    @BindView(R.id.btn_bt_inquiry)
//    Button btnBtInquiry;
    @BindView(R.id.btn_quit)
    Button btnQuit;
    @BindView(R.id.progress)
    ProgressActivity progress;
    private SharedPreferences mSecurePrefs;
    // Layout Views
    private TextView mTitle;
    String SGSTDisplaystr="";
    String TotalAmtstr="";
    String CGSTDisplaystr="";
    String roundoffstr="";
String invno = "";
    String invdate = "";
    String cname = "";
    String cmobile = "";
    String cemail = "";
    String cadd1 = "";
    String cadd2 = "";
    String cadd3 = "";String ccity = "";
    String cstate = "";String cgstinno = "";
    String coname = "";String comobile = "";
    String coemail = "";String coadd1 = "";
    String coadd2 = "";String coadd3 = "";
    String cocity = "";
    String copin = "";String costate = "";
    String cogstinno = "";

    String phsnode = "";String ptype = "";
    String pvar = "";String quantity = "";
    String rate = "";String amount = "";
    String taxblerate = ""; String taxableamt = "";
    String cgstrate = "";String cgstamt = "";
    String sgstrate = "";String sgstamt = "";
    String igstrate = "";
    String igstamt = "";String productcode = "";
    String taxable = "";String hsntotal = "";
    String amountt;
    double TotalAmt ;
    double grandtotal = 0 ;
    double SGSTDisplay ;
    String  SGSTstr ="" ;
    String  CGSTstr ="" ;
    double CGSTDisplay ;
    double RoundOff ;
    double TotalTaxable;
    double TotalHSNAmt;
    String gtotal ="";
    /**
     * Called when the activity is first created.
     */


    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_blurtooth_print);
        ButterKnife.bind(this);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        // Set up the custom title
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);
        mTitle = (TextView) findViewById(R.id.title_right_text);
        db = new DatabaseHelper(this);
        mSecurePrefs = getSharedPref();
        invoiceno = mSecurePrefs.getString(SharedPrefKey.INVOICE_DETAILS_NO, "");
        invoiceid = mSecurePrefs.getString(SharedPrefKey.INVOICEID_TO_PRINT, "");
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 界面初始化
        InitUIControl();
        getContentsCompCustToPrint();

    }
    private SharedPreferences getSharedPref() {
        if (mSecurePrefs == null) {
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }
    public void getContentsCompCustToPrint(){
   invoicePrintList = db.getCompanyCustomerPrint(invoiceid);
        if (invoicePrintList.isEmpty()) {
//            progress.showEmpty(emptyDrawable,
//                    "No Product Found",
//                    "");
        }else{

            invno = invoicePrintList.get(0).getInvoiceNo();
            String indate = invoicePrintList.get(0).getInvoiceDate();
            invdate =  getFormatedDate(indate ,"yyyy-MM-dd","dd-MM-yyyy");
            cname = invoicePrintList.get(0).getCuName();
            cemail = invoicePrintList.get(0).getCuEmailid();
            cmobile = invoicePrintList.get(0).getCuMobile();
            cadd1 = invoicePrintList.get(0).getCuAddress1();
            cadd2 = invoicePrintList.get(0).getCuAddress2();
            cadd3 = invoicePrintList.get(0).getCuAddress3();
            ccity = invoicePrintList.get(0).getCuCity();
            String custate = invoicePrintList.get(0).getCuStaeid();
            cstate = db.getState(custate);

            cgstinno = invoicePrintList.get(0).getCuGSTINNO();
            coname = invoicePrintList.get(0).getcName();
            coemail = invoicePrintList.get(0).getcEmailid();
            comobile = invoicePrintList.get(0).getcMobile();
            coadd1 = invoicePrintList.get(0).getcAddress1();
            coadd2 = invoicePrintList.get(0).getcAddress2();
            coadd3 = invoicePrintList.get(0).getcAddress3();
            cocity = invoicePrintList.get(0).getCcity();
            copin = invoicePrintList.get(0).getCpincode();
            String comstate = invoicePrintList.get(0).getCstateid();

            costate = db.getState(comstate);
            cogstinno = invoicePrintList.get(0).getcGSTINNo();

        }

    }
    public static String getFormatedDate(String strDate, String sourceFormate,
                                         String destinyFormate) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(sourceFormate);
        Date date = null;
        try {
            date = df.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        df = new SimpleDateFormat(destinyFormate);
        return df.format(date);

    }
    private void InitUIControl() {

        btnQuit.setOnClickListener(mBtnQuitOnClickListener);

        btnConnectBluetoothDevice.setOnClickListener(mBtnConnetBluetoothDeviceOnClickListener);

        btnPrint.setOnClickListener(mBtnPrintOnClickListener);
       //  btnBtInquiry.setOnClickListener(mBtnInquiryOnClickListener);
       // edtPrintContent = (EditText) findViewById(R.id.edt_print_content);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothPrintDriver.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothPrintDriver(this, mHandler);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    @SuppressLint("NewApi")
    private void ensureDiscoverable() {
        if (D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintDriver.STATE_CONNECTED:
                            mTitle.setText(R.string.title_connected_to);
                            mTitle.append(mConnectedDeviceName);
                            //setTitle(R.string.title_connected_to);
                            //setTitle(mConnectedDeviceName);
                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:
                            mTitle.setText(R.string.title_connecting);
                            //setTitle(R.string.title_connecting);
                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
                        case BluetoothPrintDriver.STATE_NONE:
                            mTitle.setText(R.string.title_not_connected);
                            //setTitle(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String ErrorMsg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    float Voltage = 0;
                    if (D)
                        Log.i(TAG, "readBuf[0]:" + readBuf[0] + "  readBuf[1]:" + readBuf[1] + "  readBuf[2]:" + readBuf[2]);
                    if (readBuf[2] == 0)
                        ErrorMsg = "NO ERROR!         ";
                    else {
                        if ((readBuf[2] & 0x02) != 0)
                            ErrorMsg = "ERROR: No printer connected!";
                        if ((readBuf[2] & 0x04) != 0)
                            ErrorMsg = "ERROR: No paper!  ";
                        if ((readBuf[2] & 0x08) != 0)
                            ErrorMsg = "ERROR: Voltage is too low!  ";
                        if ((readBuf[2] & 0x40) != 0)
                            ErrorMsg = "ERROR: Printer Over Heat!  ";
                    }
                    Voltage = (float) ((readBuf[0] * 256 + readBuf[1]) / 10.0);
                    //if(D) Log.i(TAG, "Voltage: "+Voltage);
                    DisplayToast(ErrorMsg + "                                        " + "Battery voltage：" + Voltage + " V");
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    //显示消息
    public void showMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }//showMessage

    // 显示Toast
    public void DisplayToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        //设置toast显示的位置
        toast.setGravity(Gravity.TOP, 0, 100);
        //显示该Toast
        toast.show();
    }//DisplayToast

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    //Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    OnClickListener mBtnQuitOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Stop the Bluetooth chat services
            if (mChatService != null) mChatService.stop();
            finish();
        }
    };

    OnClickListener mBtnConnetBluetoothDeviceOnClickListener = new OnClickListener() {
        Intent serverIntent = null;

        public void onClick(View arg0) {
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(BloothPrinterActivity.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }
    };
    public String Rpad(String str,int len)

    {
        StringBuilder sb = new StringBuilder();


        for (int toPrepend = len - str.trim().length(); toPrepend > 0; toPrepend--) {
            sb.append(' ');
        }

        //   sb.append(str);
        String result = sb.toString();
        result= str.trim() + result;
        return result;
    }
    public String Cpad(String str,int len)

    {
        StringBuilder sb = new StringBuilder();

        for (int toPrepend = (len-str.trim().length())/2 ; toPrepend > 0; toPrepend--) {
            sb.append(' ');
        }


        sb.append(str.trim());
        String result = sb.toString();
        return result;
    }
    public String Lpad(String str,int len)

    {
        StringBuilder sb = new StringBuilder();

        for (int toPrepend = len - str.trim().length(); toPrepend > 0; toPrepend--) {
            sb.append(' ');
        }


        sb.append(str.trim());
        String result = sb.toString();
        return result;
    }
    OnClickListener mBtnPrintOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
         //   new AddPaymentFragment().show(getSupportFragmentManager(), AddPaymentFragment.TAG);



           if (BluetoothPrintDriver.IsNoConnection()) {
               return;
            }








//            BluetoothPrintDriver.Begin();

//            String tmpContent = edtPrintContent.getText().toString();
//            BluetoothPrintDriver.BT_Write(tmpContent);
//            BluetoothPrintDriver.BT_Write("\r");


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.LF();
            DecimalFormat formater = new DecimalFormat("#0.00");
            BluetoothPrintDriver.SetAlignMode((byte)0);//左对齐
            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
            BluetoothPrintDriver.BT_Write("Invoice No. : " + Rpad(invno,8) + Lpad("Date : " + invdate,19));
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.SetAlignMode((byte)1);//居中
            BluetoothPrintDriver.SetLineSpacing((byte)50);
            BluetoothPrintDriver.SetFontEnlarge((byte)0x01);//倍高，倍宽
            BluetoothPrintDriver.BT_Write(coname);
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.SetAlignMode((byte)0);//左对齐
            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
            BluetoothPrintDriver.BT_Write( Cpad(coadd1 + " " + coadd2,48) );
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write(Cpad(coadd3 + " " + cocity + " - " + copin + " " + costate,48));
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write( "E-Mail: " + coemail);
             BluetoothPrintDriver.LF();
            String empspaces=Rpad(" ",18);
            comobile = Rpad("Mobile:" + comobile,20);
            cogstinno = Rpad("GSTIN/UIN: " + cogstinno,28);
            BluetoothPrintDriver.BT_Write(  comobile +  cogstinno);
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.SetAlignMode((byte)1);//居中
            BluetoothPrintDriver.SetLineSpacing((byte)50);
            BluetoothPrintDriver.SetFontEnlarge((byte)0x11);//倍高，倍宽
            BluetoothPrintDriver.BT_Write("TAX INVOICE");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.SetAlignMode((byte)0);//左对齐
            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
            BluetoothPrintDriver.BT_Write("Customer: " + cname);
            BluetoothPrintDriver.LF();
            String empspacess=Rpad(" ",18);
            cmobile = Rpad("Mobile:" + cmobile,20);
            cgstinno = Rpad("GSTIN/UIN: " + cgstinno,28);

            BluetoothPrintDriver.BT_Write(  cmobile +  cgstinno);
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write(Cpad(cadd1 + " " + cadd2,48));
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write(Cpad(cadd3 + " " + ccity + "  " + cstate,48));
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.BT_Write("------------------------------------------------");
            BluetoothPrintDriver.LF();
           // BluetoothPrintDriver.BT_Write("-123456789-123456789-123456789-123456789-123456789-123456789-123");
         //   BluetoothPrintDriver.BT_Write("-123456789-123456789-123456789-123456789-12345678");
            BluetoothPrintDriver.BT_Write("SLNo          Product               HSN Code    ");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write("     GST Rate    Qty      Rate      Amount(Rs.) ");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write("------------------------------------------------");
            BluetoothPrintDriver.LF();
            invDetailsGroupByHSNs = db.gethsncgst(invoiceid);
            if (invDetailsGroupByHSNs.isEmpty()) {
//            progress.showEmpty(emptyDrawable,
//                    "No Product Found",
//                    "");
            }else{
                int lengthData = invDetailsGroupByHSNs.size();

                double amttotal = 0;
                double cAmt = 0;
                double sAmt = 0;
                double taxtotal = 0;
                double cGSTAmt;
                double sGSTAmt;

                for (int i = 0; i <= lengthData - 1; i++) {
                    phsnode = invDetailsGroupByHSNs.get(i).getpHSNCode();
                    amountt = invDetailsGroupByHSNs.get(i).getTax();
                    cgstrate = invDetailsGroupByHSNs.get(i).getcGSTRate();
                    cgstamt = invDetailsGroupByHSNs.get(i).getcGSTAmount();
                    sgstrate = invDetailsGroupByHSNs.get(i).getsGSTRate();
                    sgstamt = invDetailsGroupByHSNs.get(i).getsGSTAmount();
                    hsntotal = invDetailsGroupByHSNs.get(i).getTotal();
                    double cgst;
                    double sgst;
                    double amt;
                    double totalcs;
                    double hsntotalamt;
                    cGSTAmt = Double.parseDouble(cgstamt);
                    sGSTAmt = Double.parseDouble(sgstamt);
                    cAmt   = cAmt + cGSTAmt;
                    sAmt = sAmt + sGSTAmt;

                    CGSTDisplay =cAmt;
                    SGSTDisplay = sAmt;
                    amt = Double.parseDouble(amountt);
                    String txamt = formater.format(amt);
                    taxtotal   = taxtotal + amt;
                    TotalTaxable = taxtotal;
                    amt = Double.parseDouble(amountt);
                    SGSTstr =  formater.format(SGSTDisplay);

                    CGSTstr =  formater.format(CGSTDisplay);
                    String tax = formater.format(amt);
                    taxable = tax;
                    double taxt = Double.parseDouble(tax);

                    hsntotalamt = Double.parseDouble(hsntotal);
                    amttotal = amttotal + hsntotalamt;
                    TotalHSNAmt = amttotal;
                     }

            }
            invoiceDetailsPrintList = db.getInvoiceDetailsToPrint(invoiceid);
            if (invoiceDetailsPrintList.isEmpty()) {
//            progress.showEmpty(emptyDrawable,
//                    "No Product Found",
//                    "");
            }else{
                int lengthData = invoiceDetailsPrintList.size();
                double total = 0;
                double amt;
                double cAmt = 0;
                double sAmt = 0;
                double cgstam;
                double sgstam;
                double cGSTAmt;
                double sGSTAmt;
                double taxtotal = 0;
String slno="";

                String spaces="";
                for (int i = 0; i <= lengthData - 1; i++) {
                    addr = invoiceDetailsPrintList.get(i);
                    phsnode = invoiceDetailsPrintList.get(i).getpHSNcode();
                    ptype = invoiceDetailsPrintList.get(i).getpTypeName();
                    pvar = invoiceDetailsPrintList.get(i).getpVariantName();
                    quantity = invoiceDetailsPrintList.get(i).getQuantity();
                    rate = invoiceDetailsPrintList.get(i).getRate();
                    amount = invoiceDetailsPrintList.get(i).getAmount();
                    taxblerate = invoiceDetailsPrintList.get(i).getTaxableRate();
                    taxableamt = invoiceDetailsPrintList.get(i).getTaxableAmount();
                    productcode = invoiceDetailsPrintList.get(i).getProductcode();
                    cgstrate = invoiceDetailsPrintList.get(i).getcGSTRate();
                    cgstamt = invoiceDetailsPrintList.get(i).getcGStAmount();
                    sgstrate = invoiceDetailsPrintList.get(i).getsGSTRate();
                    sgstamt = invoiceDetailsPrintList.get(i).getsGSTAmount();
                    igstrate = invoiceDetailsPrintList.get(i).getiGSTRate();
                    igstamt = invoiceDetailsPrintList.get(i).getiGSTAmount();
                  amt = Double.parseDouble(taxableamt);

                grandtotal = grandtotal + Double.parseDouble(amount);
                   gtotal = formater.format(grandtotal);
                    ptype = Rpad(ptype + " - " + pvar,32);
                    phsnode = Rpad(phsnode,10);

                    igstrate = Cpad(igstrate+"%",6);

                    quantity = Cpad(quantity,15);
                double rte = Double.parseDouble(taxblerate);
                    taxblerate = Lpad(formater.format(rte),11);
                    double amtt = Double.parseDouble(quantity)*rte;
                    taxableamt = Lpad(formater.format(amtt),11);
                    total   = total + amtt;
                    TotalAmt = total;
                    slno=Rpad(String.valueOf((i+1)),6);
                    spaces="      ";
                    BluetoothPrintDriver.BT_Write(slno  + "" +   ptype +  phsnode);
                    BluetoothPrintDriver.LF();
                    BluetoothPrintDriver.BT_Write(spaces +""+ igstrate + quantity +  taxblerate +  taxableamt );
                    BluetoothPrintDriver.LF();
                }

//                float tax = Float.parseFloat(taxable);
                double total1 =  TotalTaxable+SGSTDisplay+CGSTDisplay;
                double roundoff = grandtotal - total1;
                String rnd = formater.format(roundoff);
                RoundOff = Double.valueOf(rnd);

                //padding the three

                 TotalAmtstr = Lpad(String.valueOf(formater.format(TotalAmt)),48);
                String sg =  Lpad("SGST",40);
                String cg = Lpad("CGST",40);
                String rf =  Lpad("Round Off",40);
                SGSTDisplaystr =  cg + Lpad(String.valueOf(SGSTstr),8);
                CGSTDisplaystr =  sg + Lpad(String.valueOf(CGSTstr),8);
                roundoffstr =  rf + Lpad(String.valueOf(RoundOff),8);
            }

            BluetoothPrintDriver.BT_Write(TotalAmtstr);
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.SetBold((byte)0x01);//粗体
            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);
            BluetoothPrintDriver.BT_Write(SGSTDisplaystr);
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write(CGSTDisplaystr);
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write(roundoffstr);
            BluetoothPrintDriver.LF();
               BluetoothPrintDriver.SetBold((byte)0x01);//粗体
            BluetoothPrintDriver.BT_Write("------------------------------------------------");
            BluetoothPrintDriver.LF();
           // grandtotal = TotalAmt + SGSTDisplay + CGSTDisplay + RoundOff;

            BluetoothPrintDriver.SetFontEnlarge((byte)0x01);
            String totalstr= Rpad("Total :",32);
            totalstr += Lpad(" Rs."+ gtotal + "/-",16);
            BluetoothPrintDriver.BT_Write(totalstr);
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);
            BluetoothPrintDriver.SetBold((byte)0x01);//粗体
            String part2 = "";
            String string = String.valueOf(gtotal);
            String[] parts = string.split("\\.");
            String part1 = parts[0];
             part2 = parts[1];
            String return_val_total = "";
            if(part2.equals("00")) {
                return_val_total = EnglishNumberToWords.convert(Long.parseLong(part1)) +" Only";
            }
            else {
                return_val_total = EnglishNumberToWords.convert(Long.parseLong(part1)) + " and " + EnglishNumberToWords.convert(Long.parseLong(part2)) + " paisa Only";
            }
//        //    String return_val_in_english =   EnglishNumberToWords.convert(Long.parseLong(TotalAmt));
            BluetoothPrintDriver.BT_Write("Total Amount in Words : ");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write(" INR " + return_val_total);
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.SetBold((byte)0x00);//粗体
            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);
            BluetoothPrintDriver.BT_Write("------------------------------------------------");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.SetBold((byte)0x00);//粗体
            BluetoothPrintDriver.BT_Write("HSN     Taxable  CGST   CGST  SGST   SGST  Total");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write("Code      Amt    Rate   Amt   Rate   Amt        ");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);
            BluetoothPrintDriver.BT_Write("------------------------------------------------");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.SetBold((byte)0x00);//粗体
            invDetailsGroupByHSNs = db.gethsncgst(invoiceid);
            if (invDetailsGroupByHSNs.isEmpty()) {
//            progress.showEmpty(emptyDrawable,
//                    "No Product Found",
//                    "");
            }else{
                int lengthData = invDetailsGroupByHSNs.size();
                double taxtotal = 0;
                double amttotal = 0;

                for (int i = 0; i <= lengthData - 1; i++) {
                    phsnode = invDetailsGroupByHSNs.get(i).getpHSNCode();
                    amountt = invDetailsGroupByHSNs.get(i).getTax();
                    cgstrate = invDetailsGroupByHSNs.get(i).getcGSTRate();
                    cgstamt = invDetailsGroupByHSNs.get(i).getcGSTAmount();
                    sgstrate = invDetailsGroupByHSNs.get(i).getsGSTRate();
                    sgstamt = invDetailsGroupByHSNs.get(i).getsGSTAmount();
                    hsntotal = invDetailsGroupByHSNs.get(i).getTotal();
                    double cgst;
                    double sgst;
                    double amt;
                    double totalcs;
                    double hsntotalamt;
                    amt = Double.parseDouble(amountt);
                    String txamt = formater.format(amt);
                    taxtotal   = taxtotal + amt;
                    TotalTaxable = taxtotal;
                    String tax = formater.format(amt);
                    taxable = tax;
//                    taxable = tax;
//                    double taxt = Double.parseDouble(tax);
//                    taxtotal   = taxtotal + taxt;
                   // TotalTaxable = taxtotal;
                    hsntotalamt = Double.parseDouble(hsntotal);

                    amttotal = amttotal + hsntotalamt;
                    TotalHSNAmt = amttotal;
            BluetoothPrintDriver.BT_Write(Lpad(phsnode,9) + Lpad(txamt,8)  + Lpad(cgstrate,4)+"%"  +Lpad(cgstamt,6) +Lpad(sgstrate,4) +"%"+ Lpad(sgstamt,6) +Lpad(hsntotal,7) );
            BluetoothPrintDriver.LF();

                }

                }
            BluetoothPrintDriver.SetBold((byte)0x01);//粗体
            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);
            BluetoothPrintDriver.BT_Write("------------------------------------------------");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write("Total" + Lpad(formater.format(TotalTaxable),12) + Lpad(CGSTstr,11) + Lpad(SGSTstr,11) + Lpad(formater.format(TotalHSNAmt),7));
            BluetoothPrintDriver.LF();

            String par_2 = "";
            String strng = formater.format(TotalHSNAmt);
            String[] par1 = strng.split("\\.");
            String part_1 = par1[0];
            par_2 = par1[1];
            String return_val_taxable = "";
            if(par_2.equals("00")) {
                return_val_taxable = EnglishNumberToWords.convert(Long.parseLong(part_1)) +" Only";
            }
            else {
                return_val_taxable = EnglishNumberToWords.convert(Long.parseLong(part_1)) + " and " + EnglishNumberToWords.convert(Long.parseLong(par_2)) + " paisa Only";
            }
            BluetoothPrintDriver.BT_Write("Total Tax in Words :");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.BT_Write(" INR " + return_val_taxable);
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.SetBold((byte)0x00);//粗体
            BluetoothPrintDriver.SetFontEnlarge((byte)0x00);
            BluetoothPrintDriver.BT_Write("------------------------------------------------");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.BT_Write("");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.BT_Write("");
          //  mSecurePrefs.edit().putString(SharedPrefKey.INVOICEID_TO_PRINT, "").apply();
            new AddPaymentFragment().show(getSupportFragmentManager(), AddPaymentFragment.TAG);
//            BluetoothPrintDriver.BT_Write("For "+coname);
//            BluetoothPrintDriver.LF();
//            BluetoothPrintDriver.LF();
//            BluetoothPrintDriver.LF();
//            BluetoothPrintDriver.BT_Write("                          Authorised Signatory");






    };


//    OnClickListener mBtnInquiryOnClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            //Log.i(TAG, "inquiry btn");
//            if (BluetoothPrintDriver.IsNoConnection()) {
//                return;
//            }
//            BluetoothPrintDriver.Begin();
//            BluetoothPrintDriver.StatusInquiry();    // 查询错误状态与电池电压
//            /*
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
//			BluetoothPrintDriver.BT_Write("CODEPAGE TEST:\r\n");
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
//			byte[] cmd=new byte[16];
//			byte i;
//			for(i = 0;i<16;i++)
//				cmd[i]=(byte) (0x80+i);
//			BluetoothPrintDriver.BT_Write(cmd);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			for(i = 0;i<16;i++)
//				cmd[i]=(byte) (0x90+i);
//			BluetoothPrintDriver.BT_Write(cmd);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			for(i = 0;i<16;i++)
//				cmd[i]=(byte) (0xA0+i);
//			BluetoothPrintDriver.BT_Write(cmd);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			for(i = 0;i<16;i++)
//				cmd[i]=(byte) (0xB0+i);
//			BluetoothPrintDriver.BT_Write(cmd);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			for(i = 0;i<16;i++)
//				cmd[i]=(byte) (0xC0+i);
//			BluetoothPrintDriver.BT_Write(cmd);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			for(i = 0;i<16;i++)
//				cmd[i]=(byte) (0xD0+i);
//			BluetoothPrintDriver.BT_Write(cmd);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			for(i = 0;i<16;i++)
//				cmd[i]=(byte) (0xE0+i);
//			BluetoothPrintDriver.BT_Write(cmd);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			for(i = 0;i<16;i++)
//				cmd[i]=(byte) (0xF0+i);
//			BluetoothPrintDriver.BT_Write(cmd);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
//			BluetoothPrintDriver.BT_Write("TEXT TEST:\r\n");
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
//			String tmpString = BloothPrinterActivity.this.getResources().getString(R.string.print_text_content);
//			BluetoothPrintDriver.BT_Write(tmpString);
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
//			BluetoothPrintDriver.BT_Write("BARCODE TEST:\r\n");
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
//			BluetoothPrintDriver.AddCodePrint(BluetoothPrintDriver.CODE39, "123456");
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
//			BluetoothPrintDriver.BT_Write("QRCODE TEST:\r\n");
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
//			BluetoothPrintDriver.AddQRCodePrint();
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
//			BluetoothPrintDriver.BT_Write("IMAGE TEST:\r\n");
//			BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
//			BluetoothPrintDriver.printImage();
//			BluetoothPrintDriver.LF();
//			BluetoothPrintDriver.CR();
//			*/
//        }
   };

}