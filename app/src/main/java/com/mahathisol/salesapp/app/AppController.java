package com.mahathisol.salesapp.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.interfaces.ConnectionQualityChangeListener;
import com.securepreferences.SecurePreferences;
import com.tozny.crypto.android.AesCbcWithIntegrity;

import java.security.GeneralSecurityException;

import hugo.weaving.DebugLog;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

//	private RequestQueue mRequestQueue;
//	private ImageLoader mImageLoader;

	private static AppController mInstance;


	private SecurePreferences mSecurePrefs;
	private SecurePreferences mUserPrefs;


	//private Tracker mTracker;

	@Override
	public void onCreate() {
		super.onCreate();
		//Fabric.with(this, new Crashlytics());


		mInstance = this;
//
//		Crashlytics.setBool(CRASHLYTICS_KEY_CRASHES, areCrashesEnabled());
//		InstanceID instanceID = InstanceID.getInstance(this);
//		String gcmDefaultSenderId = getString( getResources().getIdentifier("gcm_defaultSenderId", "string", this.getPackageName()) );
//		String token = instanceID.getToken( gcmDefaultSenderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

//		try {
//			new EasyGcm.Builder().context(this).senderId(R.string.gcm_defaultSenderId).enableLog(true).build();
//		} catch (PlayServiceNotAvailableException e) {
//			e.printStackTrace();
//		}



//		EasyGcm.setTokenListener(new ITokenListener() {
//			@Override
//			public void onReceived(String token) {
//				Log.d(TAG, token);
//				getSharedPreferences().edit().putString(SharedPrefKey.DEVICE_TOKEN_KEY, token).apply();
////             .commit();
//
//
//			}
//		});

//		EasyGcm.setPushListener(new IPushListener() {
//			@Override
//			public void onReceive(Push push) {
//				EasyNotification.show(1,EasyNotification.generate(null,"Title",R.drawable.logo,"Dummy Text","SummaryText",true));
//			}
//		});

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
               // .addInterceptor(new GzipRequestInterceptor())
				.addInterceptor(interceptor)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
		AndroidNetworking.initialize(getApplicationContext());
		AndroidNetworking.enableLogging(com.androidnetworking.interceptors.HttpLoggingInterceptor.Level.BODY);
	//	AndroidNetworking.enableLogging();
		AndroidNetworking.setConnectionQualityChangeListener(new ConnectionQualityChangeListener() {
			@Override
			public void onChange(ConnectionQuality currentConnectionQuality, int currentBandwidth) {
				Log.d(TAG, "onChange: currentConnectionQuality : " + currentConnectionQuality + " currentBandwidth : " + currentBandwidth);
			}
		});

//		FLog.setMinimumLoggingLevel(FLog.VERBOSE);
//		//Fresco.initialize(this);
//		DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
//				.setBaseDirectoryPath(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),"gbin"))
//				.setBaseDirectoryName("garbage_bin")
//				.setMaxCacheSize(300*1024*1024)//300MB
//				.build();
//		ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
//				.setMainDiskCacheConfig(diskCacheConfig)
//				.build();
//		//Fresco.initialize(this);
//		Fresco.initialize(this, imagePipelineConfig);
//		FacebookSdk.sdkInitialize(this);


		//Log.d(TAG,"return"+getSharedPreferences().getString(SharedPrefKey.DEVICE_TOKEN_KEY,null));
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}



	// ..............GOOGLE ANALYTICS Integration.............
//	/**
//	 * Gets the default {@link Tracker} for this {@link Application}.
//	 * @return tracker
//	 */
//	synchronized public Tracker getDefaultTracker() {
//		if (mTracker == null) {
//			//GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//
//			///mTracker = analytics.newTracker(R.xml.global_tracker);
//		}
//		return mTracker;
//	}



	/**
	 * Single point for the app to get the secure prefs object
	 * @return
	 */
	@DebugLog
	public SharedPreferences getSharedPreferences() {
		if(mSecurePrefs==null){
			mSecurePrefs = new SecurePreferences(this, "", "my_prefs.xml");
			SecurePreferences.setLoggingEnabled(true);
		}
		return mSecurePrefs;
	}


	/**
	 * This is just an example of how you might want to create your own key with less iterations 1,000 rather than default 10,000. This makes it quicker but less secure.
	 * @return
	 */
	@DebugLog
	public SharedPreferences getSharedPreferences1000() {
		try {
			AesCbcWithIntegrity.SecretKeys myKey = AesCbcWithIntegrity.generateKeyFromPassword(Build.SERIAL,AesCbcWithIntegrity.generateSalt(),1000);
			return new SecurePreferences(this, myKey, "my_prefs_1000.xml");
		} catch (GeneralSecurityException e) {
			Log.e(TAG, "Failed to create custom key for SecurePreferences", e);
		}
		return null;
	}

	@DebugLog
	public SharedPreferences getDefaultSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(this);
	}


	@DebugLog
	public SecurePreferences getUserPinBasedSharedPreferences(String password){
		if(mUserPrefs==null) {
			mUserPrefs = new SecurePreferences(this, password, "user_prefs.xml");
		}
		return mUserPrefs;
	}

	@DebugLog
	public boolean changeUserPrefPassword(String newPassword){
		if(mUserPrefs!=null){
			try {
				mUserPrefs.handlePasswordChange(newPassword, this);
				return true;
			} catch (GeneralSecurityException e) {
				Log.e(TAG, "Error during password change", e);
			}
		}
		return false;
	}


	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		//MultiDex.install(this);
	}




}
