package com.mahathisol.salesapp.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.gson.reflect.TypeToken;
import com.mahathisol.salesapp.pojos.CompanyList;
import com.mahathisol.salesapp.pojos.CreateCustomer;
import com.mahathisol.salesapp.pojos.CustomerList;
import com.mahathisol.salesapp.pojos.InvDetailsGroupByHSN;
import com.mahathisol.salesapp.pojos.InvoiceDetailsList;
import com.mahathisol.salesapp.pojos.InvoiceDetailsPrint;
import com.mahathisol.salesapp.pojos.InvoiceList;
import com.mahathisol.salesapp.pojos.InvoicePrintList;
import com.mahathisol.salesapp.pojos.ProductDetails;
import com.mahathisol.salesapp.pojos.ProductDetailsOnBarcode;
import com.mahathisol.salesapp.pojos.ProductList;
import com.mahathisol.salesapp.pojos.SqliteState;
import com.mahathisol.salesapp.utils.ApiEndPoint;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Belal on 1/27/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    //Constants for Database name, table name, and column names
    public static final String DB_NAME = "HemsFood.db";
    public static final String TABLE_NAME = "State";
    public static final String TABLE_CUSTOMER = "Customer";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SQLID = "sqlid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_EMAIL = "emailid";
    public static final String COLUMN_ADD1 = "address1";
    public static final String COLUMN_ADD2 = "address2";
    public static final String COLUMN_ADD3 = "address3";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_PIN = "pincode";
    public static final String COLUMN_SID = "stateid";
    public static final String COLUMN_GST = "gstinno";
    public static final String COLUMN_RETAILMRP = "retailOrMRP";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_STATUS = "status";
    int status = 0;
    ProgressDialog progressdialog;
    Handler handler = new Handler();
    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    private static final String sql = "CREATE TABLE " + TABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME +
            " VARCHAR, " + COLUMN_STATUS +
            " TINYINT);";
    private static final String  sqlcustomer = "CREATE TABLE " + TABLE_CUSTOMER
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY , " + COLUMN_NAME +
            " VARCHAR, " + COLUMN_MOBILE + " VARCHAR, " + COLUMN_EMAIL + " VARCHAR, " + COLUMN_ADD1 + " VARCHAR, " + COLUMN_ADD2 +
            " VARCHAR, " + COLUMN_ADD3 + " VARCHAR, " + COLUMN_CITY + " VARCHAR, " + COLUMN_PIN +
            " VARCHAR, " + COLUMN_SID + " INTEGER , " + COLUMN_GST + " VARCHAR, " + COLUMN_ACTIVE + " TINYINT, " + COLUMN_RETAILMRP + " VARCHAR, " +  COLUMN_STATUS +
            " TINYINT, FOREIGN KEY (stateid) REFERENCES " +  TABLE_NAME + " (id) ON UPDATE CASCADE);";
    private static final String  sqlTripProduct = "CREATE TABLE TripProduct"
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT,tripId INTEGER, productId INTEGER,openingStock DECIMAL,inwardQty DECIMAL,closingStock DECIMAL,loaddate DATETIME,returnquantity DECIMAL,soldquantity DECIMAL);";
    private static final String  sqlVariant = "CREATE TABLE Variant"
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR );";
    private static final String  sqlProductType = "CREATE TABLE ProductType"
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR, prodcategoryid INTEGER, FOREIGN KEY (prodcategoryid) REFERENCES  ProductCATEGORY (id) ON UPDATE CASCADE );";
    private static final String  sqlPRODUCTcATEGORY = "CREATE TABLE ProductCategory"
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR );";
    private static final String  sqlProductCompany = "CREATE TABLE ProductCompany"
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR );";
    private static final String  sqlProduct = "CREATE TABLE Product"
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, productcode VARCHAR, prCompanyId INTEGER, prTypeId INTEGER, prVariantId INTEGER," +
            "cgstRate DECIMAL, sgstRate DECIMAL, igstRate DECIMAL, retailRate DECIMAL, mrpRate DECIMAL, barcodeno VARCHAR, hsncode VARCHAR, FOREIGN KEY (prCompanyId) REFERENCES  ProductCompany (id) ON UPDATE CASCADE,FOREIGN KEY (prTypeId) REFERENCES  ProductType (id) ON UPDATE CASCADE," +
            "FOREIGN KEY (prVariantId) REFERENCES  Variant (id) ON UPDATE CASCADE);";
    private static final String  sqlTrip = "CREATE TABLE Trip"
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, tripno VARCHAR, driverid INTEGER, salesmanid INTEGER, vehicleid INTEGER," +
            "scheduledstartdate DATETIME, startdatetime DATETIME, enddatetime DATETIME, kmreadingstart VARCHAR,kmreadingend VARCHAR,remarks VARCHAR,status TINYINT,routeid INTEGER);";

    private static final String  sqlCompany ="CREATE TABLE Company"
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR," +
            "mobile VARCHAR, emailId VARCHAR, address1 VARCHAR, address2 VARCHAR,address3 VARCHAR, city VARCHAR, stateid INTEGER, pincode VARCHAR, gstinno VARCHAR," +
            "companylogo VARCHAR, invoiceno TINYINT, taxaccross TINYINT, cgst DECIMAL, sgst DECIMAL, igst DECIMAL, invoicectr INTEGER, TaxIncludedAmt TINYINT, reportfooter VARCHAR , active TINYINT, showbankdetails TINYINT, accHolderName VARCHAR, accNo VARCHAR, bankName VARCHAR, " +
            "bankBranch VARCHAR, ifsccode VARCHAR, inHeaderwithtax VARCHAR, inHeaderwithouttax VARCHAR, passowrd VARCHAR, logincount INTEGER, tripnoAI TINYINT, tripnoCounter INTEGER, " +
            "FOREIGN KEY (stateid) REFERENCES  State (id) ON UPDATE CASCADE);";
    private static final String  sqlinvoice = "CREATE TABLE Invoice ( id INTEGER PRIMARY KEY , invoiceNo VARCHAR, invoiceDate datetime, customerId INTEGER, " +
            " cuName VARCHAR, cuMobile VARCHAR, cuEmailid VARCHAR, cuAddress1 VARCHAR, cuAddress2 VARCHAR, cuAddress3 VARCHAR, cuCity VARCHAR, cuStaeid INTEGER, " +
            " cuGSTINNO VARCHAR, companyId INTEGER, cName VARCHAR, cMobile VARCHAR, cEmailid VARCHAR, cAddress1 VARCHAR, cAddress2 VARCHAR, " +
            " cAddress3 VARCHAR, ccity VARCHAR, cpincode VARCHAR, cstateid INTEGER, cGSTINNo VARCHAR, paymentmodeid INTEGER, amountPaid DECIMAL, instrNum VARCHAR, instrDate DATETIME, remarks VARCHAR, " +
            " FOREIGN KEY (paymentmodeid) REFERENCES PaymentMode (id) ON UPDATE CASCADE, FOREIGN KEY (customerId) REFERENCES Customer (id) ON UPDATE CASCADE, FOREIGN KEY (companyId) REFERENCES Company (id) ON UPDATE CASCADE, " +
            " FOREIGN KEY (cstateid) REFERENCES Company (stateid) ON UPDATE CASCADE, FOREIGN KEY (cuStaeid) REFERENCES Customer (stateid) ON UPDATE CASCADE)";
    private static final String  sqlPaymentMode = "CREATE TABLE PaymentMode"
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR , active TINYINT );";
    private static final String  sqlinvoiceDetails ="CREATE TABLE InvoiceDetails ( id INTEGER PRIMARY KEY AUTOINCREMENT, invoiceId INTEGER, productId INTEGER, pHSNcode VARCHAR, productcode VARCHAR," +
            "  quantity DECIMAL, rate DECIMAL, amount DECIMAL, cGSTRate DECIMAL, cGStAmount DECIMAL, sGSTRate DECIMAL, " +
            " sGSTAmount DECIMAL, iGSTRate DECIMAL, iGSTAmount DECIMAL, active TINYINT, pTypeName VARCHAR, pTypeId INTEGER, pVariantName VARCHAR, pVarintId INTEGER, taxableRate DECIMAL, taxableAmount DECIMAL, " +
            "  FOREIGN KEY (invoiceId) REFERENCES Invoice(id) ON UPDATE CASCADE, FOREIGN KEY (productId) REFERENCES Product(id) ON UPDATE CASCADE, FOREIGN KEY (pTypeId) REFERENCES Product (prTypeId) ON UPDATE CASCADE, FOREIGN KEY (pVarintId) REFERENCES Product (prVariantId) ON UPDATE CASCADE)";
    private static final String  sqlCounter = "CREATE TABLE Counters"
            + "( customerId INTEGER , invoiceId INTEGER);";
    private static final String  sqlTripGPS = "CREATE TABLE TripGPS"
            + "( id INTEGER PRIMARY KEY AUTOINCREMENT, tripid INTEGER, lattitude VARCHAR, longitude VARCHAR, dateTime DATETIME, status TINYINT, salesmenId INTEGER,  FOREIGN KEY (tripid) REFERENCES Trip (id) ON UPDATE CASCADE);";
    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sql);
        db.execSQL("INSERT INTO " + TABLE_NAME+ "(name) VALUES ('Andhra Pradesh'),('Andaman and Nicobar Islands'),('Arunachal Pradesh')," +
                "('Assam'),('Bihar'),('Chandigarh'),('Chhattisgarh'),('Dadar and Nagar Haveli'),('Daman and Diu')," +
                "('Delhi'),('Goa'),('Gujarat'),('Haryana'),('Himachal Pradesh')," +
                "('Jammu and Kashmir'),('Jharkhand'),('Karnataka'),('Kerala'),('Lakshadweep')," +
                "('Madhya Pradesh'),('Maharashtra'),('Manipur'),('Meghalaya'),('Mizoram')," +
                "('Nagaland'),('Odisha'),('Pondicherry'),('Punjab')," +
                "('Rajasthan'),('Sikkim'),('Tamil Nadu'),('Telangana'),('Tripura')," +
                "('Uttar Pradesh'),('Uttarakhand'),('West Bangal')");
        db.execSQL(sqlTripGPS);
       db.execSQL(sqlcustomer);
        db.execSQL(sqlTripProduct);
        db.execSQL(sqlTrip);
        db.execSQL(sqlVariant);
        db.execSQL(sqlProductType);
        db.execSQL(sqlPRODUCTcATEGORY);
        db.execSQL(sqlProductCompany);
        db.execSQL(sqlProduct);
        db.execSQL(sqlCompany);
        db.execSQL(sqlinvoice);
        db.execSQL(sqlinvoiceDetails);
        db.execSQL(sqlPaymentMode);
        db.execSQL(sqlCounter);
        Log.d(TAG, "New table inserted into sqlite: ");
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        String sql = "DROP TABLE IF EXISTS State";
//        db.execSQL(sql);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        onCreate(db);
    }

    /*
    * This method is taking two arguments
    * first one is the name that is to be saved
    * second one is the status
    * 0 means the name is synced with the server
    * 1 means the name is not synced with the server
    * */
    public boolean addName(int id,String name, String txtphoneNo, String txtemailId, String txtaddress1, String txtaddress2, String txtaddress3, String txtpin, String txtcity, String txtstateid, String txtgstinno, String txtactive,String retailmrp, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_MOBILE, txtphoneNo);
        contentValues.put(COLUMN_EMAIL, txtemailId);
        contentValues.put(COLUMN_ADD1, txtaddress1);
        contentValues.put(COLUMN_ADD2, txtaddress2);
        contentValues.put(COLUMN_ADD3, txtaddress3);
        contentValues.put(COLUMN_PIN, txtpin);
        contentValues.put(COLUMN_CITY, txtcity);
        contentValues.put(COLUMN_SID, txtstateid);
        contentValues.put(COLUMN_GST, txtgstinno);
        contentValues.put(COLUMN_ACTIVE, txtactive);
        contentValues.put(COLUMN_RETAILMRP, retailmrp);
        contentValues.put(COLUMN_STATUS, status);
        long Id= db.insert(TABLE_CUSTOMER, null, contentValues);
       // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "New customer inserted into sqlite: "+Id);
        db.close();
        return true;

    }

    public boolean addTripProduct(String Id, String tripId, String productid, String openingstock, String inwardqty, String closingstock, String loaddae, String returnqty,String soldquant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", Id);
        contentValues.put("tripId", tripId);
        contentValues.put("productId", productid);
        contentValues.put("openingStock", openingstock);
        contentValues.put("inwardQty", inwardqty);
        contentValues.put("closingStock", closingstock);
        contentValues.put("loaddate", loaddae);
        contentValues.put("returnquantity", returnqty);
        contentValues.put("soldquantity", soldquant);
        long id= db.insert("TripProduct", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "TripProduct inserted into sqlite: "+id);
        db.close();
        return true;

    }
    public boolean addTrip(String id, String tripno, String driverid, String salesmanid, String vehicleid, String scheduledstartdate, String startdatetime, String enddatetime, String kmreadingstart, String kmreadingend, String remarks, String status, String routeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("tripno", tripno);
        contentValues.put("driverid", driverid);
        contentValues.put("salesmanid", salesmanid);
        contentValues.put("vehicleid", vehicleid);
        contentValues.put("scheduledstartdate", scheduledstartdate);
        contentValues.put("startdatetime", startdatetime);
        contentValues.put("enddatetime", enddatetime);
        contentValues.put("kmreadingstart", kmreadingstart);
        contentValues.put("kmreadingend", kmreadingend);
        contentValues.put("remarks", remarks);
        contentValues.put("status", status);
        contentValues.put("routeid", routeid);
        long Id = db.insert("Trip", null, contentValues);

        db.close(); // Closing database connection

        Log.d(TAG, "Trip inserted into sqlite: "+Id);
        db.close();
        return true;

    }
    public boolean addVariant(String Id, String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", Id);
        contentValues.put("description", desc);


        long id= db.insert("Variant", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "Variant inserted into sqlite: "+id);
        db.close();
        return true;

    }

    public boolean saveToPaymentMode(String Id, String desc,String active){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", Id);
        contentValues.put("description", desc);
        contentValues.put("active", active);

        long id= db.insert("PaymentMode", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "PaymentMode inserted into sqlite: "+id);
        db.close();
        return true;

    }
    public boolean addProductType(String Id, String desc,String cid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", Id);
        contentValues.put("description", desc);
        contentValues.put("prodcategoryid", cid);

        long id= db.insert("ProductType", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "ProductType inserted into sqlite: "+id);
        db.close();
        return true;

    }
    public boolean addProductCompany(String Id, String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", Id);
        contentValues.put("name", desc);


        long id= db.insert("ProductCompany", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "ProductCompany inserted into sqlite: "+id);
        db.close();
        return true;

    }
    public boolean addProductCategory(String Id, String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", Id);
        contentValues.put("description", desc);


        long id= db.insert("ProductCategory", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "ProductCategory inserted into sqlite: "+id);
        db.close();
        return true;

    }


    public boolean addProduct(String id, String prcode, String pcompid, String ptypeid, String varintid, String cgstRate, String sgstRate, String igstRate, String barcodeno, String hsncode, String mrp, String retail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("productcode", prcode);
        contentValues.put("prCompanyId", pcompid);
        contentValues.put("prTypeId", ptypeid);
        contentValues.put("prVariantId", varintid);
        contentValues.put("cgstRate", cgstRate);
        contentValues.put("sgstRate", sgstRate);
        contentValues.put("igstRate", igstRate);
        contentValues.put("retailRate", retail);
        contentValues.put("mrpRate",mrp );
        contentValues.put("barcodeno", barcodeno);
        contentValues.put("hsncode", hsncode);
      long Id= db.insert("Product", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "Product inserted into sqlite: "+Id);
        db.close();
        return true;

    }
    public boolean addCompany(String id, String name, String mobile, String emailid, String address1, String address2, String address3, String city, String stateid, String pincode, String gstinno, String invoiceno, String taxaccross, String cgst, String sgst, String igst, String invoicectr, String taxincludedamt, String reportfooter, String showbankdetails, String accholdername, String accno, String bankname, String bankbranch, String ifsccode, String inheaderwithtax, String inheaderwithouttax, String password, String logincount, String tripnoAI, String tripnoCounter,String complogo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("mobile", mobile);
        contentValues.put("emailId", emailid);
        contentValues.put("address1", address1);
        contentValues.put("address2", address2);
        contentValues.put("address3", address3);
        contentValues.put("city", city);
        contentValues.put("stateid", stateid);
        contentValues.put("pincode",pincode );
        contentValues.put("gstinno", gstinno);
        contentValues.put("companylogo", complogo);
        contentValues.put("invoiceno", invoiceno);
        contentValues.put("taxaccross", taxaccross);
        contentValues.put("cgst", cgst);
        contentValues.put("sgst", sgst);
        contentValues.put("igst", igst);
        contentValues.put("invoicectr", invoicectr);
        contentValues.put("TaxIncludedAmt", taxincludedamt);
        contentValues.put("reportfooter", reportfooter);

        contentValues.put("showbankdetails",showbankdetails );
        contentValues.put("accHolderName", accholdername);
        contentValues.put("accNo", accno);
        contentValues.put("bankName", bankname);
        contentValues.put("bankBranch", bankbranch);
        contentValues.put("ifsccode", ifsccode);
        contentValues.put("inHeaderwithtax", inheaderwithtax);
        contentValues.put("inHeaderwithouttax",inheaderwithouttax );
        contentValues.put("logincount", logincount);
        contentValues.put("passowrd", password);
        contentValues.put("tripnoAI", tripnoAI);
        contentValues.put("tripnoCounter", tripnoCounter);
        long Id= db.insert("Company", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "Company inserted into sqlite: "+Id);
        db.close();
        return true;

    }
    public boolean addCustomer(String id, String name, String mobile, String emailid, String address1, String address2, String address3, String city, String stateid, String pincode, String gstinno, String active, String retailOrMRP){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("mobile", mobile);
        contentValues.put("emailId", emailid);
        contentValues.put("address1", address1);
        contentValues.put("address2", address2);
        contentValues.put("address3", address3);
        contentValues.put("city", city);
        contentValues.put("stateid", stateid);
        contentValues.put("pincode",pincode );
        contentValues.put("gstinno", gstinno);
        contentValues.put("active", active);
        contentValues.put("retailOrMRP", retailOrMRP);
        contentValues.put("status", "1");
        long Id= db.insert("Customer", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "Customer inserted into sqlite: "+Id);
        db.close();
        return true;

    }

    public boolean insertToTripGSPS(String tripId, double longitude, double lattitude, String dateToStr,String SalesmenId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("tripid", tripId);
        contentValues.put("lattitude", lattitude);
        contentValues.put("longitude", longitude);
        contentValues.put("dateTime", dateToStr);
        contentValues.put("salesmenId", SalesmenId);
        contentValues.put("status", "0");
        long Id= db.insert("TripGPS", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "TripGPS inserted into sqlite: "+Id);
        db.close();
        return true;

    }
    public List<String> getBindLabels(String tbl,String pr_categoryid,String tripid){
        List<String> labels = new ArrayList<String>();

        // Select All Query
     //   String selectQuery = "SELECT * FROM " + tbl + " where prodcategoryid = " + pr_categoryid + " ORDER BY description ASC ";

        String selectQuery = "SELECT * FROM " + tbl + " where prodcategoryid = " + pr_categoryid + " and ProductType.id in (select prTypeId from Product where id in (select productId from TripProduct where tripId = '"+ tripid +"' ))";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "New  "+labels);
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getBarcode(String tbl,String fieldname,String id){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        //   String selectQuery = "SELECT * FROM " + tbl + " where prodcategoryid = " + pr_categoryid + " ORDER BY description ASC ";

        String selectQuery = "SELECT " + fieldname + " FROM " + tbl + " where id = " + id ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "New  "+labels);
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<String> getCategoryOnCompId(String compid,String tbl){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT(SELECT id FROM ProductCategory WHERE ProductType.prodcategoryid=ProductCategory.id)AS id,(SELECT description FROM ProductCategory WHERE ProductType.prodcategoryid=ProductCategory.id)AS description FROM Product,ProductType\n" +
                "WHERE Product.prTypeId=ProductType.id AND prCompanyId=" + compid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "New  "+labels);
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getCategoryOnBarcode(String pid,String varid,String catid){
        List<String> labels = new ArrayList<String>();

        //   String selectQuery = "SELECT ProductCategory.description FROM ProductCategory,Product,ProductType WHERE Product.prTypeId= '"+ pid + "' AND Product.prVariantId= '" + varid + "' AND Product.prTypeId = ProductType.id AND ProductType.prodcategoryid = ProductCategory.id";


        String selectQuery = "SELECT description FROM ProductCategory where id = " + catid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "New  "+labels);
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<String> getAllLabels(String tbl){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + tbl;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "New  "+labels);
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<String> getAllCustomers(String tbl){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + tbl + " ORDER BY Customer.name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "New  "+labels);
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getInvoiceNo(String tbl){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + tbl;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "New  "+labels);
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<String> getVariantFilter(String tbl,String pid,String tripid){
        List<String> labels = new ArrayList<String>();

        // Select All Query
      // String selectQuery = "SELECT * FROM " + tbl + " where Variant.id in (select prVariantId from Product where id in (select productId from TripProduct where tripId = '"+ tripid +"' ))" ;
      String selectQuery = "SELECT * FROM " + tbl + " where id in (select prVariantId from Product where prTypeId = " + pid + ")" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "New  "+labels);
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public boolean addInvoiceDetails(String txtinvoiceId, String txtproductId, String txtpHSNcode, String txtproductcode, String txtquantity, String txtrate, String txtamount,
                                     String txtcGSTRate, String txtcGStAmount, String txtsGSTRate, String txtsGSTAmount, String txtiGSTRate, String txtiGSTAmount, String txtactive,
                                     String txtpTypeName, String txtpTypeId, String txtpVariantName, String txtpVarintId, String taxablerate, String taxableamt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("invoiceId", txtinvoiceId);
        contentValues.put("productId", txtproductId);
        contentValues.put("pHSNcode", txtpHSNcode);
        contentValues.put("productcode", txtproductcode);
        contentValues.put("quantity", txtquantity);
        contentValues.put("rate", txtrate);
        contentValues.put("amount", txtamount);
        contentValues.put("cGSTRate", txtcGSTRate);
        contentValues.put("cGStAmount", txtcGStAmount);
        contentValues.put("sGSTRate", txtsGSTRate);
        contentValues.put("sGSTAmount", txtsGSTAmount);
        contentValues.put("iGSTRate", txtiGSTRate);
        contentValues.put("iGSTAmount", txtiGSTAmount);
        contentValues.put("active", txtactive);
        contentValues.put("pTypeName", txtpTypeName);
        contentValues.put("pTypeId", txtpTypeId);
        contentValues.put("pVariantName", txtpVariantName);
        contentValues.put("pVarintId", txtpVarintId);
        contentValues.put("taxableRate", taxablerate);
        contentValues.put("taxableAmount", taxableamt);
       long Id = db.insert("InvoiceDetails", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "New invoice details inserted into sqlite: "+txtinvoiceId);
        db.close();
        return true;

    }



    public boolean addInvoice(int id,String txtinvoiceNo, String txtinvoiceDate, String txtcustomerId, String txtcuName, String txtcuMobile, String txtcuEmailid, String txtcuAddress1,
                              String txtcuAddress2, String txtcuAddress3, String txtcuCity, int txtcuStaeid, String txtcuGSTINNO,String txtcompanyId,String txtcName,
                              String txtcMobile,String txtcEmailid, String txtcAddress1, String txtcAddress2, String txtcAddress3,String txtccity,String txtcpincode,String txtcstateid,String txtcGSTINNo,
                             String payid,String amtpaid,String instrnum,String instrdate,String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("invoiceNo", txtinvoiceNo);
        contentValues.put("invoiceDate", txtinvoiceDate);
        contentValues.put("customerId", txtcustomerId);
        contentValues.put("cuName", txtcuName);
        contentValues.put("cuMobile", txtcuMobile);
        contentValues.put("cuEmailid", txtcuEmailid);
        contentValues.put("cuAddress1", txtcuAddress1);
        contentValues.put("cuAddress2", txtcuAddress2);
        contentValues.put("cuAddress3", txtcuAddress3);
        contentValues.put("cuCity", txtcuCity);
        contentValues.put("cuStaeid", txtcuStaeid);
        contentValues.put("cuGSTINNO", txtcuGSTINNO);
        contentValues.put("companyId", txtcompanyId);

        contentValues.put("cName", txtcName);
        contentValues.put("cMobile", txtcMobile);
        contentValues.put("cEmailid", txtcEmailid);
        contentValues.put("cAddress1", txtcAddress1);
        contentValues.put("cAddress2", txtcAddress2);
        contentValues.put("cAddress3", txtcAddress3);
        contentValues.put("ccity", txtccity);
        contentValues.put("cpincode", txtcpincode);
        contentValues.put("cstateid", txtcstateid);
        contentValues.put("cGSTINNo", txtcGSTINNo);
        contentValues.put("paymentmodeid", payid);
        contentValues.put("amountPaid", amtpaid);
        contentValues.put("instrNum", instrnum);
        contentValues.put("instrDate", instrdate);
        contentValues.put("remarks", remarks);

        long Id= db.insert("invoice", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "New invoice inserted into sqlite: "+Id);
        db.close();
        return true;

    }
    public int getCounter(String fieldname)
    {

        String sql = "SELECT MAX("+fieldname+") FROM Counters";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String ID = cursor.getString(0);
        updateCounter(Integer.valueOf(ID)+1,fieldname,"Counters");
        return Integer.valueOf(ID);
    }
    public int getCount(String tbl)
    {
        String sql = "SELECT count(*) FROM " + tbl;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String ID = cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public void deletetbl(String tbl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tbl,null, null);
    }
    public boolean updateCounter(int ctr,String fieldname,String tbl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(fieldname, ctr);


        db.update(tbl, contentValues, null, null);
        db.close();
        return true;
    }
    public boolean insertCustomerIdCOunter(int cid,String fieldname){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(fieldname, cid);


        long Id= db.insert("Counters", null, contentValues);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "Counters inserted into sqlite: "+Id);
        db.close();
        return true;

    }
    public String[] getYourData(String cid) {

        final String TABLE_NAME = "Customer";

        String selectQuery = "SELECT mobile, city, (select state.name from State where State.id=Customer.stateid) FROM "+ TABLE_NAME+"  WHERE Id="+cid;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String[] data      = null;

        if (cursor.moveToFirst()) {
            do {
                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
    public ArrayList<CustomerList> getAllCustomerDetails(String cust_id) {

        ArrayList<CustomerList> customerLists = new ArrayList<CustomerList>();
        String query;


        query = "select * from "  + TABLE_CUSTOMER + " where id ="+cust_id+" order by " + COLUMN_ID +  " DESC ";
        Log.d(TAG, "New  "+query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            CustomerList emp = new CustomerList();

            int id = res.getInt(0);
            String name = res.getString(1);
            String mobile = res.getString(2);
            String emailid = res.getString(3);
            String address1 = res.getString(4);
            String address2 = res.getString(5);
            String address3 = res.getString(6);
            String city = res.getString(7);
            String pincode = res.getString(8);
            int stateid = res.getInt(9);
            String gstinno = res.getString(10);
            String active = res.getString(11);
            String retailmrp = res.getString(12);
            String status = res.getString(13);
            emp.setAll(id,name,mobile,emailid,address1,address2,address3,city,pincode,stateid,gstinno,active,retailmrp,status);
            customerLists.add(emp);

            res.moveToNext();
        }
        return customerLists;

    }
    public ArrayList<ProductDetails> getProductDetails(String pr_companyid, String pr_typeid, String varnt_id) {

        ArrayList<ProductDetails> productDetailses = new ArrayList<ProductDetails>();
        String query;


        query = "select * , (SELECT description FROM ProductType WHERE ProductType.id = Product.prTypeId)AS ptypename,(SELECT description FROM Variant WHERE Variant.id = Product.prVariantId)AS pvaraint from Product where prCompanyId =" + pr_companyid + " and  prTypeId = " + pr_typeid + " and prVariantId = " + varnt_id;
        Log.d(TAG, "New  "+query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            ProductDetails emp = new ProductDetails();

            String id = res.getString(0);
            String productcode = res.getString(1);
            String prCompanyId = res.getString(2);
            String prTypeId = res.getString(3);
            String prVariantId = res.getString(4);
            String cgstRate = res.getString(5);
            String sgstRate = res.getString(6);
            String igstRate = res.getString(7);
            String retailRate = res.getString(8);
            String mrpRate = res.getString(9);
            String barcodeno = res.getString(10);
            String hsncode = res.getString(11);
            String typename = res.getString(12);
            String variantname = res.getString(13);
            emp.ProductDetails(id,productcode,prCompanyId,prTypeId,prVariantId,cgstRate,sgstRate,igstRate,retailRate,mrpRate,barcodeno,hsncode,typename,variantname);
            productDetailses.add(emp);

            res.moveToNext();
        }
        return productDetailses;

    }

    public ArrayList<InvoiceList> getInvoicCustomerDetails(String invoiceno) {

        ArrayList<InvoiceList> invoiceLists = new ArrayList<InvoiceList>();
        String query;


        query = "select * from Invoice where invoiceNo = '" + invoiceno + "'" ;
        Log.d(TAG, "New  "+query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            InvoiceList emp = new InvoiceList();

            String id = res.getString(0);
            String invno = res.getString(1);
            String invoicedate = res.getString(2);
            String cid = res.getString(3);
            String cname = res.getString(4);
            String cmobile = res.getString(5);
            String cemailid = res.getString(6);
            String ccity = res.getString(10);
            String cgstinno = res.getString(12);

            emp.Invoice(id,invno,invoicedate,cid,cname,cmobile,cemailid,ccity,cgstinno);
            invoiceLists.add(emp);

            res.moveToNext();
        }
        return invoiceLists;

    }
    public ArrayList<ProductDetailsOnBarcode> getProductDetailsOnBarcode(String barcode) {

        ArrayList<ProductDetailsOnBarcode> productDetailses = new ArrayList<ProductDetailsOnBarcode>();
        String query;


        query = "SELECT * , (SELECT description FROM ProductType WHERE ProductType.id = Product.prTypeId)AS ptypename,(SELECT description FROM Variant WHERE Variant.id = Product.prVariantId)AS pvaraint,(SELECT ProductCategory.id FROM ProductCategory,ProductType WHERE ProductType.prodcategoryid = ProductCategory.id AND ProductType.id = Product.prTypeId)AS pcatid FROM Product where barcodeno = " + barcode ;
        Log.d(TAG, "New  "+query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            ProductDetailsOnBarcode emp = new ProductDetailsOnBarcode();

            String id = res.getString(0);
            String productcode = res.getString(1);
            String prCompanyId = res.getString(2);
            String prTypeId = res.getString(3);
            String prVariantId = res.getString(4);
            String cgstRate = res.getString(5);
            String sgstRate = res.getString(6);
            String igstRate = res.getString(7);
            String retailRate = res.getString(8);
            String mrpRate = res.getString(9);
            String barcodeno = res.getString(10);
            String hsncode = res.getString(11);
            String typename = res.getString(12);
            String variantname = res.getString(13);
            String pcatid = res.getString(14);
            emp.ProductDetails(id,productcode,prCompanyId,prTypeId,prVariantId,cgstRate,sgstRate,igstRate,retailRate,mrpRate,barcodeno,hsncode,typename,variantname,pcatid);
            productDetailses.add(emp);

            res.moveToNext();
        }
        return productDetailses;

    }

    public int getMAXid(String tbl)
    {
        String selectQuery = "SELECT MAX(id) FROM " + tbl;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String ID=cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public int getINvoiceId(String label,String tbl)
    {
        String selectQuery = "SELECT id FROM " + tbl + "  WHERE invoiceNo =\""+label+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String ID=cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public int getId(String label,String tbl)
    {
        String selectQuery = "SELECT id FROM " + tbl + "  WHERE name =\""+label+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String ID=cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public int getPaymentId(String label,String tbl)
    {
        String selectQuery = "SELECT id FROM " + tbl + "  WHERE description =\""+label+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String ID=cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public int getLastInvoiceId(String tbl)
    {
        String selectQuery = "SELECT MAX(id) FROM " + tbl;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String ID=cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public ArrayList<CompanyList> getCompanyDetails() {

        ArrayList<CompanyList> customerLists = new ArrayList<CompanyList>();
        String query;


        query = "select * from Company";
        Log.d(TAG, "Newff"+query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            CompanyList emps = new CompanyList();

            String id = res.getString(0);
            String name = res.getString(1);
            String mobile = res.getString(2);
            String emailid = res.getString(3);
            String address1 = res.getString(4);
            String address2 = res.getString(5);
            String address3 = res.getString(6);
            String city = res.getString(7);
            String stateid = res.getString(8);
            String pincode = res.getString(9);
            String gstinno = res.getString(10);
            String companylogo = res.getString(11);
            String invoiceno = res.getString(12);
            String taxaccross = res.getString(13);

            String cgst = res.getString(14);
            String sgst = res.getString(15);
            String igst = res.getString(16);
            String invoicectr = res.getString(17);
            String taxincludedamt = res.getString(18);
            String reportfooter = res.getString(19);
            String showbankdetails = res.getString(20);
            String accholdername = res.getString(21);
            String accno = res.getString(22);
            String bankname = res.getString(23);
            String bankbranch = res.getString(24);
            String ifsccode = res.getString(25);
            String inheaderwithtax = res.getString(26);
            String inheaderwithouttax = res.getString(27);
            String password = res.getString(28);
            String logincount = res.getString(29);
            String tripnoAI = res.getString(30);
            String tripnoCounter = res.getString(31);
            emps.Comapnyli(id, name, mobile, emailid, address1, address2, address3,city, stateid, pincode, gstinno, companylogo,invoiceno,taxaccross, cgst, sgst, igst,  invoicectr,  taxincludedamt,  reportfooter,  showbankdetails,  accholdername,  accno,  bankname,  bankbranch,  ifsccode,  inheaderwithtax,  inheaderwithouttax,  password,  logincount,  tripnoAI,  tripnoCounter);

            customerLists.add(emps);

            res.moveToNext();
        }
        return customerLists;

    }
    public int getIdProduct(String label,String tbl)
    {
        String selectQuery = "SELECT id FROM " + tbl + "  WHERE description =\""+label+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String ID=cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public String getProduct(int id,String tbl)
    {
        String selectQuery = "SELECT description FROM " + tbl + " WHERE id =\""+id+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String ID=cursor.getString(0);
        return ID;
    }
    public String getCustomerState(int id,String tbl)
    {
        String selectQuery = "SELECT name FROM " + tbl + " WHERE id =\""+id+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String ID=cursor.getString(0);
        return ID;
    }
    public String getInstock(String pid,String tripid)
    {
        String ID = "";
        String selectQuery = "SELECT (openingStock+inwardQty-soldquantity) As closingStock FROM TripProduct WHERE productId =\""+pid+"\"  and tripId =\""+tripid+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null && cursor.moveToFirst()) {
            ID = cursor.getString(0);

        }
        return ID;
    }
    public String getINVOICENOONID(String id)
    {
        String ID = "";
        String selectQuery = "SELECT invoiceNo from Invoice where id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null && cursor.moveToFirst()) {
            ID = cursor.getString(0);

        }
        return ID;
    }
    public String getSoldQuant(String pid,String tripid)
    {
        String ID = "";
        String selectQuery = "SELECT soldquantity FROM TripProduct WHERE productId =\""+pid+"\"  and tripId =\""+tripid+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null && cursor.moveToFirst()) {
            ID = cursor.getString(0);

        }
        return ID;
    }
    public String getState(String pid)
    {
        String ID = "";
        String selectQuery = "SELECT name FROM State WHERE id =\""+pid+"\" ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null && cursor.moveToFirst()) {
            ID = cursor.getString(0);

        }
        return ID;
    }
    public List<SqliteState> getAllStates(){

        List <SqliteState> states = new ArrayList < SqliteState > ();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                states.add ( new SqliteState ( cursor.getString(0) , cursor.getString(1) ) );

            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return states;

    }
  //  String invno
    public ArrayList<InvoicePrintList> getCompanyCustomerPrint(String invno) {

        ArrayList<InvoicePrintList> invoiceLists = new ArrayList<InvoicePrintList>();
        String query;


        query = "SELECT * From Invoice WHERE Invoice.id = '" + invno + "' " ;

        Log.d(TAG, "query: "+query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            InvoicePrintList emp = new InvoicePrintList();

            String id = res.getString(0);
            String invoiceno = res.getString(1);
            String invdate = res.getString(2);
            String cid = res.getString(3);
             String cname = res.getString(4);
            String cmobile = res.getString(5);
            String cemailid = res.getString(6);
            String cadd1 = res.getString(7);
            String cadd2 = res.getString(8);
            String cadd3 = res.getString(9);
            String ccity = res.getString(10);
            String cstate = res.getString(11);
            String cgstinno = res.getString(12);
            String coid = res.getString(13);
            String coname = res.getString(14);
            String comobile = res.getString(15);
            String coemailid = res.getString(16);
            String coadd1 = res.getString(17);
            String coadd2 = res.getString(18);
            String coadd3 = res.getString(19);
            String cocity = res.getString(20);
            String copin = res.getString(21);
            String costate = res.getString(22);
            String cogstin = res.getString(23);

            emp.setAll(id,invoiceno,invdate,cid,cname,cmobile,cemailid,cadd1,cadd2,cadd3,ccity,cstate,cgstinno,coid,coname,comobile,coemailid,coadd1,coadd2,coadd3,cocity,copin,costate,cogstin);
            invoiceLists.add(emp);
            Log.d(TAG, "Invoice list from sqlite: "+id);
            res.moveToNext();
        }

        return invoiceLists;

    }

    public ArrayList<InvoiceDetailsPrint> getInvoiceDetailsToPrint(String invno) {

        ArrayList<InvoiceDetailsPrint> invoicePrintLists = new ArrayList<InvoiceDetailsPrint>();
        String query;

     //   query = "SELECT id,invoiceId,productId,pHSNcode,productcode,quantity,rate,amount,cGSTRate,cGStAmount,sGSTRate,sGSTAmount,iGSTRate,iGSTAmount,active,pTypeName,pTypeId,pVariantName,pVarintId From InvoiceDetails WHERE invoiceId = '" + invno + "' " ;
        query = "SELECT * From InvoiceDetails WHERE invoiceId = '" + invno + "' " ;

        Log.d(TAG, "query: "+query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            InvoiceDetailsPrint emp = new InvoiceDetailsPrint();

            String id = res.getString(0);
            String invoiceid = res.getString(1);
            String productId = res.getString(2);
            String pHSNcode = res.getString(3);
            String productcode = res.getString(4);
            String quantity = res.getString(5);
            String rate = res.getString(6);
            String amount = res.getString(7);
            String cGSTRate = res.getString(8);
            String cGStAmount = res.getString(9);
            String sGSTRate = res.getString(10);
            String sGSTAmount = res.getString(11);
            String iGSTRate = res.getString(12);
            String iGSTAmount = res.getString(13);
            String active = res.getString(14);
            String pTypeName = res.getString(15);
            String pTypeId = res.getString(16);
            String pVariantName = res.getString(17);
            String pVarintId = res.getString(18);
            String taxablerate = res.getString(19);
            String taxableamt = res.getString(20);
            emp.setAll(id,invoiceid,productId,pHSNcode,productcode,quantity,rate,amount,cGSTRate,cGStAmount,sGSTRate,sGSTAmount,iGSTRate,iGSTAmount,active,pTypeName,pTypeId,pVariantName,pVarintId,taxablerate,taxableamt);
            invoicePrintLists.add(emp);
            Log.d(TAG, "Invoice list from sqlite: "+id);
            res.moveToNext();
        }

        return invoicePrintLists;

    }


    public ArrayList<InvDetailsGroupByHSN> gethsncgst(String invno) {

        ArrayList<InvDetailsGroupByHSN> invoicePrintLists = new ArrayList<InvDetailsGroupByHSN>();
        String query;

        //   query = "SELECT id,invoiceId,productId,pHSNcode,productcode,quantity,rate,amount,cGSTRate,cGStAmount,sGSTRate,sGSTAmount,iGSTRate,iGSTAmount,active,pTypeName,pTypeId,pVariantName,pVarintId From InvoiceDetails WHERE invoiceId = '" + invno + "' " ;
        query ="SELECT pHSNCode,SUM(taxableRate*quantity) AS tax,cGSTRate,SUM(cGStAmount) As cGStAmount ,sGSTRate,SUM(sGSTAmount) As sGSTAmount,SUM(cGStAmount+sGSTAmount)AS total FROM InvoiceDetails WHERE invoiceId = '" + invno + "' GROUP BY pHSNCode " ;
     //   (amount/((1+(cGSTRate+sGSTRate))/100))
        Log.d(TAG, "query: "+query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            InvDetailsGroupByHSN emp = new InvDetailsGroupByHSN();

            String pHSNCode = res.getString(0);
            String tax = res.getString(1);
            String cGSTRate = res.getString(2);
            String cGSTAmount = res.getString(3);
            String sGSTRate = res.getString(4);
            String sGSTAmount = res.getString(5);
            String total = res.getString(6);

           emp.setAll(pHSNCode,tax,cGSTRate,cGSTAmount,sGSTRate,sGSTAmount,total);
            invoicePrintLists.add(emp);
            Log.d(TAG, "HSN list from sqlite: "+pHSNCode);
            res.moveToNext();
        }

        return invoicePrintLists;

    }
    public ArrayList<InvoiceDetailsList> getAllInvoiceDetails(String invno) {

        ArrayList<InvoiceDetailsList> invoiceLists = new ArrayList<InvoiceDetailsList>();
        String query;


        query = "SELECT * ,Product.id as prid ,ProductCategory.description FROM InvoiceDetails,Product,Invoice,ProductCategory,ProductType WHERE ProductCategory.id = ProductType.prodcategoryid AND ProductType.id = Product.prTypeId AND InvoiceDetails.productId = Product.id AND Invoice.id = InvoiceDetails.invoiceId AND Invoice.invoiceNo = '" + invno + "' " ;

        Log.d(TAG, "query: "+query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            InvoiceDetailsList emp = new InvoiceDetailsList();

            String id = res.getString(0);
            String invoiceid = res.getString(1);
            String productId = res.getString(2);
            String pHSNcode = res.getString(3);
            String productcode = res.getString(4);
            String quantity = res.getString(5);
            String rate = res.getString(6);
            String amount = res.getString(7);
            String cGSTRate = res.getString(8);
            String cGStAmount = res.getString(9);
            String sGSTRate = res.getString(10);
            String sGSTAmount = res.getString(11);
            String iGSTRate = res.getString(12);
            String iGSTAmount = res.getString(13);
            String active = res.getString(14);
            String pTypeName = res.getString(15);
            String pTypeId = res.getString(16);
            String pVariantName = res.getString(17);
            String pVarintId = res.getString(18);
            String taxrate = res.getString(19);
            String taxamt = res.getString(20);
            String pid = res.getString(21);
            String pcode = res.getString(22);
            String prcomid = res.getString(23);
            String prtypeid = res.getString(24);
            String prvarid = res.getString(25);
            String cgst = res.getString(26);
            String sgst = res.getString(27);
            String igst = res.getString(28);
            String retail = res.getString(29);
            String mrp = res.getString(30);

            String hsn = res.getString(32);
            String inid = res.getString(33);
            String invoiceno = res.getString(34);
            String indate = res.getString(35);
            String cuid = res.getString(36);
            String cuname = res.getString(37);
            String cumobile = res.getString(38);

            String cucicity = res.getString(43);
            String cugstinno = res.getString(45);
            String prcatid = res.getString(62);
            String prid = res.getString(67);
            String prcatname = res.getString(68);
            emp.SetAll(id,invoiceid,productId,pHSNcode,productcode,quantity,rate,amount,cGSTRate,cGStAmount,sGSTRate,sGSTAmount,iGSTRate,iGSTAmount,active,pTypeName,pTypeId,pVariantName,pVarintId,cgst,sgst,igst,invoiceno,indate,cuname,cumobile,cucicity,cugstinno,prid,prcatid,prcatname);
            invoiceLists.add(emp);
            Log.d(TAG, "Invoice list from sqlite: "+prcatid);
            res.moveToNext();
        }

        return invoiceLists;

    }
    public ArrayList<InvoiceDetailsList> getAllInvoiceForEdit(String invid) {

        ArrayList<InvoiceDetailsList> invoiceLists = new ArrayList<InvoiceDetailsList>();
        String query;


        query = "select *,(SELECT invoiceNo FROM Invoice WHERE Invoice.id = InvoiceDetails.invoiceId) AS invoiceno from InvoiceDetails where InvoiceDetails.invoiceId = " +invid;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            InvoiceDetailsList emp = new InvoiceDetailsList();

            String id = res.getString(0);
            String invoiceid = res.getString(1);
            String productId = res.getString(2);
            String pHSNcode = res.getString(3);
            String productcode = res.getString(4);
            String quantity = res.getString(5);
            String rate = res.getString(6);
            String amount = res.getString(7);
            String cGSTRate = res.getString(8);
            String cGStAmount = res.getString(9);
            String sGSTRate = res.getString(10);
            String sGSTAmount = res.getString(11);
            String iGSTRate = res.getString(12);
            String iGSTAmount = res.getString(13);
            String active = res.getString(14);
            String pTypeName = res.getString(15);
            String pTypeId = res.getString(16);
            String pVariantName = res.getString(17);
            String pVarintId = res.getString(18);
            String invoiceno = res.getString(19);
          //  emp.SetAll(id,invoiceid,productId,pHSNcode,productcode,quantity,rate,amount,cGSTRate,cGStAmount,sGSTRate,sGSTAmount,iGSTRate,iGSTAmount,active,pTypeName,pTypeId,pVariantName,pVarintId,invoiceno);
            invoiceLists.add(emp);
            Log.d(TAG, "Invoice list from sqlite: "+id);
            res.moveToNext();
        }

        return invoiceLists;

    }
    public ArrayList<CustomerList> getAllCustomers() {

        ArrayList<CustomerList> customerLists = new ArrayList<CustomerList>();
        String query;


        query = "select * from "  + TABLE_CUSTOMER + " order by " + COLUMN_ID +  " DESC ";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            CustomerList emp = new CustomerList();

            int id = res.getInt(0);
            String name = res.getString(1);
            String mobile = res.getString(2);
            String emailid = res.getString(3);
            String address1 = res.getString(4);
            String address2 = res.getString(5);
            String address3 = res.getString(6);
            String city = res.getString(7);
            String pincode = res.getString(8);
            int stateid = res.getInt(9);
            String gstinno = res.getString(10);
            String active = res.getString(11);
            String retailmrp = res.getString(12);
            String status = res.getString(13);
            emp.setAll(id,name,mobile,emailid,address1,address2,address3,city,pincode,stateid,gstinno,active,retailmrp,status);
            customerLists.add(emp);
            Log.d(TAG, "Customer list from sqlite: "+id);
            res.moveToNext();
        }

        return customerLists;

    }
    public List<String> getVaraint(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM Variant";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    /*
    * This method taking two arguments
    * first one is the id of the name for which
    * we have to update the sync status
    * and the second one is the status that will be changed
    * */
    public boolean updateTripGPSStatus(String id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("TripGPS", contentValues, "id = " + id, null);
        db.close();
        return true;
    }
    public boolean updateNameStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_CUSTOMER, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }
    public boolean updateTripProduct(String pid, String tripid, String quant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("soldquantity", quant);
        db.update("TripProduct", contentValues, "productId = " + pid + " and tripId = " + tripid , null);
        db.close();
        return true;
    }
    public boolean updateInvoiceId(String newid, String oldid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", newid);
        db.update("Invoice", contentValues, "id = '" + oldid + "'" , null);
        int maxcid = getMAXid("Invoice");

        int count = getCount("Counters");
        if(count > 0){
            updateCounter(maxcid+1,"invoiceId","Counters");
        }else {
            insertCustomerIdCOunter(maxcid + 1,"invoiceId");

        }
        db = this.getWritableDatabase();
        ContentValues invde = new ContentValues();
        invde.put("invoiceId", newid);
        db.update("InvoiceDetails", invde, "invoiceId = '" + oldid + "'" , null);


      //  updEmple.uploadInvoiceDetails(oldid,newid);
      uploadInvoiceDetails(oldid,newid);
      //  db.close();
        return true;

    }
    public boolean updateCustomer(String id, String name,String phone,String email,String txtaddress1, String txtaddress2, String txtaddress3, String txtpin, String txtcity, String txtstateid, String txtgstinno, String txtactive, String mrp,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_MOBILE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_ADD1, txtaddress1);
        contentValues.put(COLUMN_ADD2, txtaddress2);
        contentValues.put(COLUMN_ADD3, txtaddress3);
        contentValues.put(COLUMN_PIN, txtpin);
        contentValues.put(COLUMN_CITY, txtcity);
        contentValues.put(COLUMN_SID, txtstateid);
        contentValues.put(COLUMN_GST, txtgstinno);
        contentValues.put(COLUMN_ACTIVE, txtactive);
        contentValues.put(COLUMN_RETAILMRP, mrp);
        contentValues.put(COLUMN_STATUS, status);

        db.update(TABLE_CUSTOMER, contentValues, COLUMN_ID + "=" + id, null);
        db.close();

        return true;
    }
    public boolean updateInvoiceDetails(String inid,String id, String ghsncode, String productcode, String s, String toString, String string, String gcgstrate, String gcgstamount, String gsgstrate, String gsgstamount, String gigstrate, String gigstamt, String active, String ptypename, String pr_typeid, String pvaraint, String pr_variantid,String TR,String taxamt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productId", id);
        contentValues.put("pHSNcode", ghsncode);
        contentValues.put("productcode", productcode);
        contentValues.put("quantity", s);
        contentValues.put("rate", toString);
        contentValues.put("amount", string);
        contentValues.put("cGSTRate", gcgstrate);
        contentValues.put("cGStAmount", gcgstamount);
        contentValues.put("sGSTRate", gsgstrate);
        contentValues.put("sGSTAmount", gsgstamount);
        contentValues.put("iGSTRate", gigstrate);
        contentValues.put("iGSTAmount", gigstamt);
        contentValues.put("pTypeName", ptypename);
        contentValues.put("pTypeId", pr_typeid);
        contentValues.put("pVariantName", pvaraint);
        contentValues.put("pVarintId", pr_variantid);
        contentValues.put("taxableRate", TR);
        contentValues.put("taxableAmount", taxamt);


        db.update("InvoiceDetails", contentValues, "id = " + inid, null);
        db.close();

        return true;
    }

    public boolean updateCustomerId(String id, int oldid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);

        long ty = db.update(TABLE_CUSTOMER, contentValues, COLUMN_ID + "=" + oldid, null);
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("customerId", id);
        long custid = db.update("Invoice", content, COLUMN_ID + "=" + oldid, null);
        db.close();
     //   Log.d(TAG, "New customer updated into sqlite invoice: "+custid);
        // long id = db.insert(TABLE_CUSTOMER, null, contentValues);
        return true;
    }
    /*
    * this method will give us all the name stored in sqlite
    * */

    public int checkRecord(String ptypeid,String pvarid,String invoiceid)
    {
        String sql = "SELECT count(*) FROM InvoiceDetails WHERE pTypeId = '" + ptypeid + "' and pVarintId = '" + pvarid + "' and invoiceId = " + invoiceid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String ID = cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public int checkRecordForEdit(String ptypeid,String pvarid,String id,String invid)
    {
        String sql = "SELECT count(*) FROM InvoiceDetails WHERE pTypeId = '" + ptypeid + "' and pVarintId = '" + pvarid + "' and invoiceId = '" + invid + "' and id <> " + id ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String ID = cursor.getString(0);
        return Integer.valueOf(ID);
    }
    public void deletein(long _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("InvoiceDetails"," id = " + _id, null);
    }
    public boolean DeleteTableContent(String tbl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ tbl);
return true;
    }
    /*

    * this method is for getting all the unsynced name
    * so that we can sync it with database
    * */
    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public Cursor getUnsyncedTripProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM TripProduct";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public Cursor getAllInvoice() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM Invoice";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public Cursor getAllTripGPS() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM TripGPS where status = 0 ";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public void  uploadInvoiceDetails(String oldid,String newid) {

        Cursor cursor = getAllInvoiceDetailsOnId(oldid,newid);
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveToInvoiceDetails(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("invoiceId")),
                        cursor.getString(cursor.getColumnIndex("productId")),
                        cursor.getString(cursor.getColumnIndex("pHSNcode")),
                        cursor.getString(cursor.getColumnIndex("productcode")),
                        cursor.getString(cursor.getColumnIndex("quantity")),
                        cursor.getString(cursor.getColumnIndex("rate")),
                        cursor.getString(cursor.getColumnIndex("amount")),
                        cursor.getString(cursor.getColumnIndex("cGSTRate")),
                        cursor.getString(cursor.getColumnIndex("cGStAmount")),
                        cursor.getString(cursor.getColumnIndex("sGSTRate")),
                        cursor.getString(cursor.getColumnIndex("sGSTAmount")),
                        cursor.getString(cursor.getColumnIndex("iGSTRate")),
                        cursor.getString(cursor.getColumnIndex("iGSTAmount")),
                        cursor.getString(cursor.getColumnIndex("active")),
                        cursor.getString(cursor.getColumnIndex("pTypeName")),
                        cursor.getString(cursor.getColumnIndex("pTypeId")),
                        cursor.getString(cursor.getColumnIndex("pVariantName")),
                        cursor.getString(cursor.getColumnIndex("pVarintId")),
                        cursor.getString(cursor.getColumnIndex("taxableRate")),
                        cursor.getString(cursor.getColumnIndex("taxableAmount"))
                );
            } while (cursor.moveToNext());
        }
    }

    public void saveToInvoiceDetails(String id, String invoiceId, String productId, String pHSNcode, String productcode, String quantity, String rate, String amount, String cGSTRate, String cGStAmount, String sGSTRate, String sGSTAmount, String iGSTRate, String iGSTAmount, String active, String pTypeName, String pTypeId, String pVariantName, String pVarintId, String txRate, String txAmt){
        AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.INSERT_INVOICEDETAILS)

                // .addQueryParameter("id", id)
                //   .addUrlEncodeFormBodyParameter("txtid", id)
                .addUrlEncodeFormBodyParameter("txtinvoiceId", invoiceId)

                .addUrlEncodeFormBodyParameter("txtproductId", productId)

                .addUrlEncodeFormBodyParameter("txtpHSNcode", pHSNcode)
                // .addUrlEncodeFormBodyParameter("txtproductcode", productcode)
                .addUrlEncodeFormBodyParameter("txtquantity", quantity)
                .addUrlEncodeFormBodyParameter("txtrate", rate)
                .addUrlEncodeFormBodyParameter("txtamount", amount)
                .addUrlEncodeFormBodyParameter("txtcGSTRate", cGSTRate)
                .addUrlEncodeFormBodyParameter("txtcGSTAmount", cGStAmount)
                .addUrlEncodeFormBodyParameter("txtsGSTRate", sGSTRate)
                .addUrlEncodeFormBodyParameter("txtsGSTAmount", sGSTAmount)
                .addUrlEncodeFormBodyParameter("txtiGSTRate", iGSTRate)
                .addUrlEncodeFormBodyParameter("txtiGSTAmount", iGSTAmount)
                .addUrlEncodeFormBodyParameter("txtactive", active)
                .addUrlEncodeFormBodyParameter("txtpTypeName", pTypeName)
                .addUrlEncodeFormBodyParameter("txtpTypeId", pTypeId)
                .addUrlEncodeFormBodyParameter("txtpVariantName", pVariantName)
                .addUrlEncodeFormBodyParameter("txtpVarintId", pVarintId)
                .addUrlEncodeFormBodyParameter("txttaxableRate", txRate)
                .addUrlEncodeFormBodyParameter("txttaxableAmount", txAmt)


                .setTag(ApiEndPoint.TIME_LINE_REQUEST_TAG)
                .setPriority(Priority.HIGH)
                .build()

                .getAsOkHttpResponseAndParsed(new TypeToken<CreateCustomer>() {
                }, new OkHttpResponseAndParsedRequestListener<CreateCustomer>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CreateCustomer cal) {


                        if (!cal.getCreate().getType().equalsIgnoreCase("success")) {
//                            progress.showEmpty(emptyDrawable,
//                                    "Routes not Available",
//                                    "Please Assign Routes");
//
                        } else {
//                            HomeActivity updEmple = new HomeActivity();
//                            updEmple.
                           // (HomeActivity).ShowProgressDialog();
                            //  Toast.makeText(getApplicationContext(),"invoicedetails",Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error


                    }
                });
    }
    public Cursor getAllInvoiceDetailsOnId(String oldid,String newid) {

        SQLiteDatabase db  = this.getReadableDatabase();
        String sql = "SELECT * FROM InvoiceDetails where invoiceId = " +newid;
        Cursor c = db.rawQuery(sql, null);

        return c;
    }
    public void ShowProgressDialog()
    {
        status = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(status < 100){

                    status +=1;

                    try{
                        Thread.sleep(200);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            progressdialog.setProgress(status);

                            if(status == 100){

                                progressdialog.dismiss();

                            }
                        }
                    });
                }
            }
        }).start();

    }
    public ArrayList<ProductList> getAllProducts(String tripid) {

        ArrayList<ProductList> productLists = new ArrayList<ProductList>();
        String query;
        query = "SELECT ProductType.description AS prtype,Variant.description AS prvariant,inwardQty,closingStock,openingStock,returnquantity,soldquantity FROM TripProduct,ProductType,Variant,Product WHERE TripProduct.productId=Product.Id AND Product.prTypeId=ProductType.id AND Variant.id=Product.prVariantId  and  TripProduct.tripId = " + tripid;
        //query = "SELECT 'channa' AS prtype,'20gm' AS prvariant,inwardQty,closingStock,openingStock FROM TripProduct where tripId = "+tripid;

        Log.d(TAG,query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            ProductList emp = new ProductList();
            String prtype = res.getString(0);
            String prvariant = res.getString(1);
            String inwardQty = res.getString(2);
            String closingStock = res.getString(3);
            String openingStock = res.getString(4);
            String returnqty = res.getString(5);
            String sold = res.getString(6);
            emp.setall(prtype,prvariant,inwardQty,closingStock,openingStock,returnqty,sold);
            productLists.add(emp);
            Log.d(TAG, "Trip Product  from sqlite: ");
            res.moveToNext();
        }

        return productLists;

    }
    public boolean updatePaymentDetails(String inid,String paymentmodeid, String amountPaid, String instrNum, String instrDate, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("paymentmodeid", paymentmodeid);
        contentValues.put("amountPaid", amountPaid);
        contentValues.put("instrNum", instrNum);
        contentValues.put("instrDate", instrDate);
        contentValues.put("remarks", remarks);
        db.update("Invoice", contentValues, "invoiceNo = '" + inid + "' ", null);
        db.close();
        return true;
    }
}
