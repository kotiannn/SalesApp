package com.mahathisol.salesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.mahathisol.salesapp.R;
import com.mahathisol.salesapp.app.AppController;
import com.mahathisol.salesapp.utils.SharedPrefKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {

    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;
    @BindView(R.id.img_logo)
    ImageView imgLogo;

    private boolean animationStarted = false;
    private SharedPreferences mSecurePrefs;
    String customer_id="";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        mSecurePrefs =  getSharedPref();
        customer_id = mSecurePrefs.getString(SharedPrefKey.CUSTOMER_ID,"");



        mSecurePrefs
                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(
                            SharedPreferences sharedPreferences, String key) {
                        //updateEncValueDisplay();
                       // Toast.makeText(getApplicationContext(), "onSharedPreferenceChanged", Toast.LENGTH_SHORT).show();
                    }
                });
//        setTheme(R.style.SplashTheme);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus || animationStarted) {
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }

    private void animate() {

        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ViewCompat.animate(imgLogo)
                .translationY(-250)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).setListener(new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(View view) {

                if (!customer_id.equalsIgnoreCase("")) {
                    intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                    //Apply splash exit (fade out) and activity_rowingfcbug entry (fade in) animation transitions.
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
//                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
//                    startActivity(intent);

                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //Apply splash exit (fade out) and activity_rowingfcbug entry (fade in) animation transitions.
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }

                finish();
            }
        }).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(50).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(1000);
            } else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }




    }


    private SharedPreferences getSharedPref(){
        if(mSecurePrefs==null){
            mSecurePrefs = AppController.getInstance().getSharedPreferences();
        }
        return mSecurePrefs;
    }

}
