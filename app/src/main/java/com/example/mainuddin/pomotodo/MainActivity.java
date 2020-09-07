package com.example.mainuddin.pomotodo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.developer.kalert.KAlertDialog;
import com.example.mainuddin.pomotodo.fragments.DailyDetailFragment;
import com.example.mainuddin.pomotodo.fragments.HomeFragment;
import com.example.mainuddin.pomotodo.fragments.PomotodoFragment;
import com.example.mainuddin.pomotodo.fragments.TimeLineFragment;
import com.example.mainuddin.pomotodo.fragments.TreeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public boolean isDark;
    public SharedPreferences prefs;
    public static KAlertDialog kAlertDialog;
    View v;

    @Override
    public void onBackPressed() {


        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            super.onBackPressed(); //replaced
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        ImageButton backup, darkmode;

        backup = findViewById(R.id.backu);
        darkmode = findViewById(R.id.darkmode);


        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, pro_backup.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        darkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDark) {


                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.custom_dialogue, null);
                    popupView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_in));

                    final ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
                    Drawable dim = new ColorDrawable(Color.BLACK);
                    dim.setBounds(0, 0, root.getWidth(), root.getHeight());
                    dim.setAlpha((int) (255 * 0.5f));

                    ViewGroupOverlay overlay = root.getOverlay();
                    overlay.add(dim);

                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.MATCH_PARENT;


                    //Make Inactive Items Outside Of PopupWindow
                    boolean focusable = true;

                    //Create a window with our parameters
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    //Set the location of the window on the screen
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    popupWindow.setAnimationStyle(R.style.MyA);

                    //Initialize the elements of our window, install the handler


                    ImageButton buttonEdit = popupView.findViewById(R.id.messageButton);
                    buttonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //As an example, display the message
                            ViewGroupOverlay overlay = root.getOverlay();
                            overlay.clear();
                            popupWindow.dismiss();

                        }
                    });

                    AppCompatButton ok, cancel;
                    ok = popupView.findViewById(R.id.ok);
                    cancel = popupView.findViewById(R.id.cancel);
                    TextView textView;
                    textView = popupView.findViewById(R.id.title);

                    textView.setText("Disable Darkmode ? ");


                    LinearLayout relativeLayout = popupView.findViewById(R.id.nnmnm);
                    TextView textView22 = popupView.findViewById(R.id.textView1);
                    if (isDark) {
                        //relativeLayout.setBackgroundColor(Color.BLACK);
                        //textView22.setTextColor(Color.WHITE);


                    } else {
                        //relativeLayout.setBackgroundColor(Color.WHITE);
                        //textView22.setTextColor(Color.BLACK);
                    }


                    //Handler for clicking on the inactive zone of the window

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            //Close the window when clicked
                            //popupWindow.dismiss();

                            return true;
                        }
                    });


                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ViewGroupOverlay overlay = root.getOverlay();
                            overlay.clear();
                            popupWindow.dismiss();
                        }
                    });

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isDark = false;
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("isDark", isDark);
                            editor.commit();
                            // linearLayout.setBackgroundColor(Color.WHITE);
                            // linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.card_background));
                            // list.setAdapter(adapter);

                            Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(myIntent, 0);

                            Toasty.success(getApplicationContext(), "Disabled", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else if (!isDark) {

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.custom_dialogue, null);
                    popupView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_in));

                    final ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
                    Drawable dim = new ColorDrawable(Color.BLACK);
                    dim.setBounds(0, 0, root.getWidth(), root.getHeight());
                    dim.setAlpha((int) (255 * 0.5f));

                    ViewGroupOverlay overlay = root.getOverlay();
                    overlay.add(dim);

                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.MATCH_PARENT;


                    //Make Inactive Items Outside Of PopupWindow
                    boolean focusable = true;

                    //Create a window with our parameters
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    //Set the location of the window on the screen
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    popupWindow.setAnimationStyle(R.style.MyA);

                    //Initialize the elements of our window, install the handler


                    ImageButton buttonEdit = popupView.findViewById(R.id.messageButton);
                    buttonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //As an example, display the message
                            ViewGroupOverlay overlay = root.getOverlay();
                            overlay.clear();
                            popupWindow.dismiss();

                        }
                    });

                    AppCompatButton ok, cancel;
                    ok = popupView.findViewById(R.id.ok);
                    cancel = popupView.findViewById(R.id.cancel);
                    TextView textView;
                    textView = popupView.findViewById(R.id.title);

                    textView.setText("Enable Darkmode ? ");


                    LinearLayout relativeLayout = popupView.findViewById(R.id.nnmnm);
                    TextView textView22 = popupView.findViewById(R.id.textView1);
                    if (isDark) {
                        //relativeLayout.setBackgroundColor(Color.BLACK);
                        //textView22.setTextColor(Color.WHITE);


                    } else {
                        //relativeLayout.setBackgroundColor(Color.WHITE);
                        //textView22.setTextColor(Color.BLACK);
                    }


                    //Handler for clicking on the inactive zone of the window

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            //Close the window when clicked
                            //popupWindow.dismiss();

                            return true;
                        }
                    });


                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ViewGroupOverlay overlay = root.getOverlay();
                            overlay.clear();
                            popupWindow.dismiss();
                        }
                    });

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isDark = true;
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("isDark", isDark);
                            editor.commit();
                            // linearLayout.setBackgroundColor(Color.BLACK);


                            Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(myIntent, 0);

                            Toasty.success(getApplicationContext(), "Enabled", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

        v = getCurrentFocus();
        isDark = prefs.getBoolean("isDark", false);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isDark", isDark);
        editor.commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportActionBar().setTitle("Home");
        loadFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_pomotodo:
                    getSupportActionBar().setTitle("Pomotodo");
                    fragment = new PomotodoFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_timeline:
                    getSupportActionBar().setTitle("Timeline");
                    fragment = new TimeLineFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_daily:
                    getSupportActionBar().setTitle("Daily Details");
                    fragment = new DailyDetailFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_tree:
                    getSupportActionBar().setTitle("Tree Grow");
                    fragment = new TreeFragment();
                    loadFragment(fragment);
                    return true;


            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
