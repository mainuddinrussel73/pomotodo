package com.example.mainuddin.pomotodo;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.mainuddin.pomotodo.fragments.PomotodoFragment;
import com.example.mainuddin.pomotodo.model.promotododata;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.suke.widget.SwitchButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class promodetail extends AppCompatActivity {

    public static TextView textView1, textView2, textView3;
    public static FloatingActionButton fab1, fab2, fab3;
    private Animation rotate_forward, rotate_backward;
    public static boolean toogle = false;
    public static CircularProgressBar circularProgressBar;
    public static CircularProgressBar circularProgressBar1, circularProgressBar2, circularProgressBar3, circularProgressBar4;
    public static CircularProgressBar circularProgressBar11, circularProgressBar12, circularProgressBar13, circularProgressBar14;
    public static int curr;
    public static FloatingActionButton fab;
    public static TextView titletask;
    public static ExtendedEditText text_field_boxes2;
    public static SharedPreferences prefs;
    public static Animation shake;
    public static MyBounceInterpolator interpolator;
    public static View view;
    public static View dialogView;
    public static boolean toogleview = false;
    public static Dialog dialog;
    ImageButton button1;
    long tike = 18000;
    // public static FABRevealLayout fabRevealLayout;
    promotododata currenttask;
    boolean isfinish = false;
    Intent serviceIntent;
    AppCompatButton edittext, whitenoise;
    Calendar myCalendar;
    MediaPlayer mediaPlayer;
    TextView textViewss, shownoise;
    private CountDownTimer cdTimer;
    PulsatorLayout pulsator;
    private long total = 1800000;
    private StorageReference mStorageRef;
    ImageButton saveTitle, cancelTitle;

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static void revealShow(boolean b) {


        toogleview = true;
        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (fab.getX() + (fab.getWidth() / 2));
        int cy = (int) (fab.getY()) + fab.getHeight() + 56;


        if (b) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);

            revealAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //dialog.create();
                    view.setVisibility(View.GONE);


                }
            });

            revealAnimator.setDuration(700);
            revealAnimator.start();

        } else {

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);


                }
            });
            anim.setDuration(700);
            anim.start();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        try {
            setContentView(R.layout.activity_promodetail);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        dialogView = View.inflate(this, R.layout.content_promosec, null);
        view = dialogView.findViewById(R.id.secondary_view);
        dialog = new Dialog(this, R.style.MyAlertDialogStyle);
        fab = findViewById(R.id.fab);
        fab.setActivated(true);

        fab.setImageBitmap(textAsBitmap(00 + ":" + 00, 40, Color.WHITE));

        textViewss = findViewById(R.id.viewdate);

        shake = AnimationUtils.loadAnimation(this, R.anim.glowing);
        // fab.setImageBitmap(textAsBitmap("OK", 40, Color.WHITE));

        pulsator = findViewById(R.id.pulsator);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSecond();
            }
        });


        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);


        text_field_boxes2 = findViewById(R.id.text_field_boxes2);


        circularProgressBar1 = findViewById(R.id.circularProgressBar2);
        circularProgressBar1.setProgressMax(tike);
        circularProgressBar2 = findViewById(R.id.circularProgressBar3);
        circularProgressBar2.setProgressMax(tike);
        circularProgressBar3 = findViewById(R.id.circularProgressBar4);
        circularProgressBar3.setProgressMax(tike);
        circularProgressBar4 = findViewById(R.id.circularProgressBar5);
        circularProgressBar4.setProgressMax(tike);


        Intent intent = getIntent();
        curr = intent.getIntExtra("ID", -1);
        currenttask = PomotodoFragment.promotododataList.get(curr);

        //prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        text_field_boxes2.setText(currenttask.getTitle());
        //cancelTitle = findViewById(R.id.textcancel);
        saveTitle = findViewById(R.id.textsave);


        saveTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text_field_boxes2.getText().toString().trim().equals(
                        PomotodoFragment.promotododataList.get(curr).getTitle())) {
                    PomotodoFragment.mDBHelper.updateTitle(currenttask.getTitle(), text_field_boxes2.getText().toString().trim());
                    PomotodoFragment.promotododataList.get(curr).setTitle(text_field_boxes2.getText().toString().trim());

                    Toasty.success(promodetail.this, "Done Update.", Toast.LENGTH_LONG).show();


                } else {

                }
            }
        });


        System.out.println("item   " + PomotodoFragment.promotododataList.get(curr).getTitle() + "<>" + PomotodoFragment.promotododataList.get(curr).getNum_of_promotodo());
        if (currenttask.getNum_of_promotodo() == 1) {
            fab.setActivated(true);
            // circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            System.out.println(currenttask.getNum_of_promotodo());
            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
        } else if (currenttask.getNum_of_promotodo() == 2) {
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
        } else if (currenttask.getNum_of_promotodo() == 3) {
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
        } else if (currenttask.getNum_of_promotodo() == 4) {
            fab.setActivated(true);
            //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
            // circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
            circularProgressBar4.setProgressWithAnimation(tike, (long) 1000);
            //circularProgressBar14.setProgressWithAnimation(tike, (long) 1000);
        } else {
            fab.setActivated(false);
        }

        System.out.println(currenttask.getTitle());

        RelativeLayout relativeLayout = findViewById(R.id.spinerlayout);
        MaterialSpinner spinner = relativeLayout.findViewById(R.id.spinner);
        spinner.setItems(new Integer(1), new Integer(2), new Integer(3), new Integer(4));
        switch (PomotodoFragment.promotododataList.get(curr).getNum_of_promotodo()) {
            case 1:
                spinner.setSelectedIndex(0);
                break;
            case 2:
                spinner.setSelectedIndex(1);
                break;
            case 3:
                spinner.setSelectedIndex(2);
                break;
            case 4:
                spinner.setSelectedIndex(3);
                break;

        }

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<Integer>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Integer item) {
                Snackbar.make(view, "Clicked " + item.intValue(), Snackbar.LENGTH_LONG).show();
                PomotodoFragment.mDBHelper.updateNum(currenttask.getTitle(), item.intValue());
                PomotodoFragment.promotododataList.get(curr).setNum_of_promotodo(item.intValue());

                PomotodoFragment.mDBHelper.updateCompleted(currenttask.getTitle(), 0);
                PomotodoFragment.promotododataList.get(curr).setCompleted_promotodo(0);

                System.out.println("item   " + PomotodoFragment.promotododataList.get(curr).getTitle() + "<>" + PomotodoFragment.promotododataList.get(curr).getNum_of_promotodo());
                if (item.intValue() == 1) {
                    fab.setActivated(true);
                    // circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    System.out.println(item.intValue());
                    circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    //  circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar12.setProgressWithAnimation(0, (long) 1000);
                    //circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                    //circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                } else if (item.intValue() == 2) {
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                    // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    //circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    // circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar13.setProgressWithAnimation(0, (long) 1000);
                    //circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                } else if (item.intValue() == 3) {
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
                    circularProgressBar4.setProgressWithAnimation(0, (long) 1000);

                    //circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgre1ssBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar14.setProgressWithAnimation(0, (long) 1000);
                } else if (item.intValue() == 4) {
                    fab.setActivated(true);
                    //circularProgressBar1.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    circularProgressBar4.setProgressWithAnimation(tike, (long) 1000);

                    //circularProgressBar11.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar2.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar12.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar3.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar13.setProgressWithAnimation(tike, (long) 1000);
                    //circularProgressBar4.setBackgroundProgressBarColor(Color.TRANSPARENT);
                    //circularProgressBar14.setProgressWithAnimation(tike, (long) 1000);
                } else {
                    fab.setActivated(false);
                }
            }


        });


        com.suke.widget.SwitchButton switchButton = findViewById(R.id.spinner1);

        final TextView textView = findViewById(R.id.isrepeat);


        if (!Promotodo_service.ispause) {
            pulsator.start();
        }

        if (currenttask.isIsrepeat() == 1) {
            textView.setText("Repeat");
            switchButton.setChecked(true);
        } else {
            textView.setText("No repeat");
            switchButton.setChecked(false);
        }

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job

                Snackbar.make(view, "Clicked " + currenttask.getTitle() + isChecked, Snackbar.LENGTH_LONG).show();
                if (isChecked == true) {
                    PomotodoFragment.mDBHelper.updateIsrepeat(currenttask.getTitle(), 1);
                    PomotodoFragment.promotododataList.get(curr).setIsrepeat(1);
                } else {
                    PomotodoFragment.mDBHelper.updateIsrepeat(currenttask.getTitle(), 0);
                    PomotodoFragment.promotododataList.get(curr).setIsrepeat(0);
                }

                // PomotodoFragment.promotododataList.get(curr).setNum_of_promotodo(item.intValue());

                if (isChecked == true) textView.setText("Repeat");
                else textView.setText("No repeat");
            }
        });


        textViewss.setText(currenttask.getDue_date());

        myCalendar = Calendar.getInstance();

        edittext = findViewById(R.id.datepick);


        edittext.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


                SharedPreferences prefs = getSharedPreferences("myPrefsKey", MODE_PRIVATE);
                boolean isDark = prefs.getBoolean("isDark", false);
                try {

                    final Dialog alertDialog = new Dialog(promodetail.this);
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setContentView(R.layout.custom_datepick);
                    ImageButton buttonEdit = alertDialog.findViewById(R.id.messageButton);
                    buttonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //As an example, display the message
                            alertDialog.dismiss();

                        }
                    });

                    DatePicker datePicker = alertDialog.findViewById(R.id.date_picker);
                    datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();

                        }
                    });
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.show();


                } catch (Exception e) {
                }
            }
        });


        mStorageRef = FirebaseStorage.getInstance().getReference();
        whitenoise = findViewById(R.id.soundpick);
        shownoise = findViewById(R.id.viewsound);
        shownoise.setText(Promotodo_service.fileName);
        boolean finalIsDark = isDark;
        whitenoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences("myPrefsKey", MODE_PRIVATE);
                boolean isDark = prefs.getBoolean("isDark", false);
                try {

                    String[] types = {"Rain", "Ticking", "Ocean Shore", "Shower", "Train", "Water Fall", "Water", "Storm", "Water Rain", "Ancient Woodlane", "Blue Lagoon",
                            "Cedarwood Spring", "Cracking Log Fire", "Desert Night", "Fire Place", "Lullaby", "Rain Chimes", "Rain On Window", "Rainforest Waterfall",
                            "Thunder Rain", "Tranquil Pond", "Tropical Rain Forest", "Under Water", "Under Water Soft",
                            "Space Smooth", "Space Voyager", "Island Of Sorrow", "Rain Of Sorrow", "The Trappers Remorse",
                            "A Brand New Dawn", "Keyboard", "Owl Forest Night", "None"};

                    //Create a View object yourself through inflater
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.custom_list_pick, null);
                    popupView.setAnimation(AnimationUtils.loadAnimation(promodetail.this, R.anim.anim_in));

                    //Specify the length and width through constants

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

                    //custom_list_row contains textview only
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.custom_list_row, types);
                    ListView listView = popupView.findViewById(R.id.list_white);
                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //pass the view
                            switch (position) {
                                case 0:
                                    Promotodo_service.fileName = "rain";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 1:

                                    Promotodo_service.fileName = "ticking";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 2:
                                    Promotodo_service.fileName = "oceanshore";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 3:
                                    Promotodo_service.fileName = "shower";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 4:
                                    Promotodo_service.fileName = "train";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 5:
                                    Promotodo_service.fileName = "waterfall";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 6:
                                    Promotodo_service.fileName = "water";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 7:
                                    Promotodo_service.fileName = "storm";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 8:
                                    Promotodo_service.fileName = "rainonwater";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 9:
                                    Promotodo_service.fileName = "ancient_woodland";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 10:
                                    Promotodo_service.fileName = "blue_lagoon";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 11:
                                    Promotodo_service.fileName = "ceder_wood_spring";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 12:
                                    Promotodo_service.fileName = "cracking_log_fire";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 13:
                                    Promotodo_service.fileName = "desert_night";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 14:
                                    Promotodo_service.fileName = "fireplace";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 15:
                                    Promotodo_service.fileName = "music_box_lullaby";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 16:
                                    Promotodo_service.fileName = "rain_chimes";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 17:
                                    Promotodo_service.fileName = "rain_on_window";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 18:
                                    Promotodo_service.fileName = "rainforest_waterfall";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 19:
                                    Promotodo_service.fileName = "thunder_rain";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 20:
                                    Promotodo_service.fileName = "tranquil_pond";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 21:
                                    Promotodo_service.fileName = "tropical_rain_forest";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 22:
                                    Promotodo_service.fileName = "underwater";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 23:
                                    Promotodo_service.fileName = "underwater_a";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 24:
                                    Promotodo_service.fileName = "space_smooth";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 25:
                                    Promotodo_service.fileName = "space_voyager";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 26:
                                    Promotodo_service.fileName = "island_of_sorrow";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 27:
                                    Promotodo_service.fileName = "rain_of_sorrow";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 28:
                                    Promotodo_service.fileName = "trappers_remorse";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 29:
                                    Promotodo_service.fileName = "new_dawn";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 30:
                                    Promotodo_service.fileName = "keyboard";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 31:
                                    Promotodo_service.fileName = "owl_forestnight";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;
                                case 32:
                                    Promotodo_service.fileName = "none";
                                    shownoise.setText(Promotodo_service.fileName);
                                    break;

                            }
                            if (!Promotodo_service.fileName.equals("none") || !Promotodo_service.fileName.equals("rain") ||
                                    !Promotodo_service.fileName.equals("ticking") || !Promotodo_service.fileName.equals("oceanshore")) {
                                downloadFile(Promotodo_service.fileName);
                            }

                            ViewGroupOverlay overlay = root.getOverlay();
                            overlay.clear();
                            popupWindow.dismiss();

                        }
                    });


                } catch (Exception e) {

                }


            }
        });

        RelativeLayout additem = findViewById(R.id.pro_detail);
        RelativeLayout relativeLayoutr = dialogView.findViewById(R.id.secondary_view);
        RelativeLayout relativeLayout1 = findViewById(R.id.belowhere);
        RelativeLayout dddd = findViewById(R.id.dddd);

        TextView place = findViewById(R.id.place);
        TextView place1 = findViewById(R.id.place1);
        TextView texts = findViewById(R.id.texts);

        RelativeLayout main_view = findViewById(R.id.maindd);


        //SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        isDark = prefs.getBoolean("isDark", false);
        if (isDark) {
            // mListView.setBackgroundColor(Color.BLACK);
            //relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.list_viewdark));
            additem.setBackgroundColor(ContextCompat.getColor(promodetail.this, R.color.bbb));
            place.setTextColor(Color.parseColor("#cdcacf"));
            place1.setTextColor(Color.parseColor("#cdcacf"));

            texts.setTextColor(Color.WHITE);
            texts.setHintTextColor(Color.rgb(185, 185, 185));
            relativeLayout1.setBackgroundDrawable(ContextCompat.getDrawable(promodetail.this, R.drawable.background_card_dark));
            dddd.setBackgroundDrawable(ContextCompat.getDrawable(promodetail.this, R.drawable.base_rounded));

            shownoise.setTextColor(Color.WHITE);
            main_view.setBackground(ContextCompat.getDrawable(promodetail.this, R.drawable.background_card_dark));
            //fab1.setBackgroundColor(Color.RED);
            spinner.setBackgroundColor(Color.rgb(31, 39, 41));
            spinner.setTextColor(Color.WHITE);
            // spinner1.setBackgroundColor(Color.rgb(64,64,64));
            // edittext.setTextColor(Color.WHITE);
            //edittext.setBackground(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            //edittext.setHintTextColor(Color.WHITE);
            relativeLayoutr.setBackgroundColor(Color.rgb(255, 69, 0));
            textView.setTextColor(Color.WHITE);
            textViewss.setTextColor(Color.WHITE);
            //titletask.setTextColor(Color.WHITE);
            //  text_field_boxes2.setPrefixTextColor(Color.WHITE);
            text_field_boxes2.setHintTextColor(Color.rgb(185, 185, 185));
            // tfb.setBackgroundColor(Color.BLACK);
            // tfb.setTextColor(Color.WHITE);
            // tfb.setHintTextColor(Color.rgb(185,185,185));
        } else {
            place.setTextColor(Color.parseColor("#59565B"));
            place1.setTextColor(Color.parseColor("#59565B"));

            texts.setTextColor(Color.BLACK);
            texts.setHintTextColor(Color.BLACK);
            relativeLayout1.setBackgroundDrawable(ContextCompat.getDrawable(promodetail.this, R.drawable.background_card));
            dddd.setBackgroundDrawable(ContextCompat.getDrawable(promodetail.this, R.drawable.base_rounded_white));

            shownoise.setTextColor(Color.BLACK);
            main_view.setBackground(ContextCompat.getDrawable(promodetail.this, R.drawable.background_card));
            // mListView.setBackgroundColor(Color.WHITE);
            // relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.listview_border));
            additem.setBackgroundColor(ContextCompat.getColor(promodetail.this, R.color.white));
            //fab1.setBackgroundColor(Color.RED);
            spinner.setBackgroundColor(Color.WHITE);
            spinner.setTextColor(Color.BLACK);
            //spinner1.setBackgroundColor(Color.rgb(185,185,185));
            // edittext.setHintTextColor(Color.BLACK);
            //edittext.setBackground(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            text_field_boxes2.setHintTextColor(Color.rgb(64, 64, 64));
            //text_field_boxes2.setPrefixTextColor(Color.BLACK);
            textView.setTextColor(Color.BLACK);
            textViewss.setTextColor(Color.BLACK);
            // edittext.setTextColor(Color.BLACK);
            relativeLayoutr.setBackgroundColor(Color.rgb(255, 69, 0));
            //titletask.setTextColor(Color.BLACK);
            //  tfb.setBackgroundColor(Color.WHITE);
            ///  tfb.setTextColor(Color.BLACK);
            //  tfb.setHintTextColor(Color.BLACK);
        }

    }

    private boolean isFileExists(String filename) {

        File folder1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/White_noise/" + filename + ".mp3");
        return folder1.exists();


    }

    public boolean deleteFile(String filename) {

        File folder1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/White_noise/" + filename + ".mp3");
        return folder1.delete();


    }

    private void downloadFile(final String filenames) {


        File localFile = null;
        try {
            localFile = File.createTempFile(filenames, "mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference reference = mStorageRef.child(filenames.concat(".mp3"));


        reference.getDownloadUrl().addOnSuccessListener(
                new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/White_noise";
                        File dir = new File(file_path);


                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        System.out.println(dir.getPath());
                        if (!isFileExists(filenames)) {

                            downloadFile(promodetail.this, filenames, ".mp3", file_path, uri.toString());
                            Toasty.success(promodetail.this,
                                    "Downloaded the file",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toasty.success(promodetail.this,
                                    "File Exists",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getMessage());
                        Toasty.success(promodetail.this,
                                "Couldn't be downloaded",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void downloadFile(Context context, String fileName, String ext, String destination, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("/White_noise", fileName + ext);
        downloadManager.enqueue(request);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        try {
            textViewss.setText(sdf.format(myCalendar.getTime()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        boolean b = PomotodoFragment.mDBHelper.updateDuedate(currenttask.getTitle(), sdf.format(myCalendar.getTime()));
        if (b) {
            Toasty.success(promodetail.this, "Done", Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            super.onBackPressed(); //replaced
        }
    }

    @SuppressLint("RestrictedApi")
    private void showSecond() {


        toogleview = false;

        dialogView = View.inflate(this, R.layout.content_promosec, null);
        view = dialogView.findViewById(R.id.secondary_view);
        dialog = new Dialog(this, R.style.MyAlertDialogStyle);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            dialog.setContentView(dialogView);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        ImageButton imageView = dialogView.findViewById(R.id.cancel_task);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealShow(false);
            }
        });

        fab1 = dialogView.findViewById(R.id.fab16);
        fab2 = dialogView.findViewById(R.id.fab24);
        fab3 = dialogView.findViewById(R.id.fab3);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);


        button1 = dialogView.findViewById(R.id.cancel_task);
        textView1 = dialogView.findViewById(R.id.procountH);
        textView2 = dialogView.findViewById(R.id.procountM);
        textView3 = dialogView.findViewById(R.id.procountS);

        titletask = dialogView.findViewById(R.id.tasktitle);


        textView1.setText(String.format("%02d", 00));
        textView2.setText(String.format("%02d", 30));
        textView3.setText(String.format("%02d", 00));

        circularProgressBar11 = dialogView.findViewById(R.id.circularProgressBar01);
        circularProgressBar11.setProgressMax(tike);
        circularProgressBar12 = dialogView.findViewById(R.id.circularProgressBar11);
        circularProgressBar12.setProgressMax(tike);
        circularProgressBar13 = dialogView.findViewById(R.id.circularProgressBar12);
        circularProgressBar13.setProgressMax(tike);
        circularProgressBar14 = dialogView.findViewById(R.id.circularProgressBar13);
        circularProgressBar14.setProgressMax(tike);

        circularProgressBar = dialogView.findViewById(R.id.circularProgressBar1);
        circularProgressBar.setProgressMax(total);

        // RelativeLayout additem = findViewById(R.id.pro_detail);
        RelativeLayout relativeLayoutr = dialogView.findViewById(R.id.secondary_view);
        //  RelativeLayout relativeLayout1 = relativeLayout.findViewById(R.id.borderss);

        final SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {
            // mListView.setBackgroundColor(Color.BLACK);
            //relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.list_viewdark));
            // additem.setBackgroundColor(Color.BLACK);
            //fab1.setBackgroundColor(Color.RED);
            // relativeLayoutr.setBackgroundColor(Color.A400red);
            //textView1.setTextColor(Color.WHITE);
            // tfb.setBackgroundColor(Color.BLACK);
            // tfb.setTextColor(Color.WHITE);
            // tfb.setHintTextColor(Color.rgb(185,185,185));
        } else {
            // mListView.setBackgroundColor(Color.WHITE);
            // relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.listview_border));
            //additem.setBackgroundColor(Color.WHITE);
            //textView1.setTextColor(Color.BLACK);
            //edittext.setTextColor(Color.BLACK);
            // relativeLayoutr.setBackgroundColor(Color.rgb(255, 51, 0));
            //titletask.setTextColor(Color.BLACK);
            //  tfb.setBackgroundColor(Color.WHITE);
            ///  tfb.setTextColor(Color.BLACK);
            //  tfb.setHintTextColor(Color.BLACK);
        }


        // System.out.println("item   "+ PomotodoFragment.promotododataList.get(curr).getTitle()+"<>"+PomotodoFragment.promotododataList.get(curr).getNum_of_promotodo());


        final int width = dialogView.getWidth();
        float positionX = fab1.getPivotX();
        float posiitonY = fab1.getPivotY();
        int translationX = (int) ((-(width - fab1.getWidth()) / 2f));


        if (isMyServiceRunning(Promotodo_service.class)) {


            System.out.println("RUnning.......");
            //toogle = true;
            //fab1.setImageResource(R.drawable.ic_pause_black_24dp);
            if (!Promotodo_service.ispause) {
                fab1.setVisibility(View.INVISIBLE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);
            } else {
                fab1.setVisibility(View.VISIBLE);
                int ccc = promodetail.prefs.getInt("CURR", 0);
                promotododata ccco = PomotodoFragment.promotododataList.get(ccc);

                if (ccco.getNum_of_promotodo() <= 0) {
                    fab1.setVisibility(View.INVISIBLE);
                } else {
                    fab1.setVisibility(View.VISIBLE);
                }
                fab2.setVisibility(View.INVISIBLE);
                fab3.setVisibility(View.INVISIBLE);
            }

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //fab1.setImageResource(R.drawable.ic_pause_black_24dp);


                    TranslateAnimation animation = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 94,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0
                    );
                    TranslateAnimation animation2 = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, -94,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0
                    );
                    animation.setDuration(200);
                    animation2.setDuration(200);
                    fab2.startAnimation(animation2);
                    fab3.startAnimation(animation);


                    Promotodo_service.resume(promodetail.this);

                    pulsator.start();

                    fab1.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    fab3.setVisibility(View.VISIBLE);

                    // startCountDownTimer();

                    //fab1.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    //cdTimer.cancel();


                }
            });
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Promotodo_service.pause();
                    pulsator.stop();
                    //stopService(new Intent(promodetail.this, Promotodo_service.class));
                    // mediaPlayer.pause();

                    TranslateAnimation animation = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 144,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0
                    );
                    TranslateAnimation animation2 = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, -144,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0
                    );
                    animation.setDuration(200);
                    animation2.setDuration(200);
                    fab2.startAnimation(animation);
                    fab3.startAnimation(animation2);


                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);
                }
            });

            fab3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Promotodo_service.pause();


                    Promotodo_service.total = 1800000;

                    Promotodo_service.ispause = true;
                    pulsator.stop();
                    TranslateAnimation animation = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 144,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0
                    );
                    TranslateAnimation animation2 = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, -144,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0
                    );
                    animation.setDuration(200);
                    animation2.setDuration(200);
                    fab2.startAnimation(animation);
                    fab3.startAnimation(animation2);


                    stopService(new Intent(promodetail.this, Promotodo_service.class));
                    // mediaPlayer.pause();


                    Intent myIntent = new Intent(promodetail.this, promodetail.class);
                    //String s = view.findViewById(R.id.subtitle).toString();
                    //String s = (String) parent.getI;
                    myIntent.putExtra("ID", curr);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);

                    // revealShow(true);


                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            if (!Promotodo_service.ispause) {
                fab1.setVisibility(View.INVISIBLE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);
            } else {
                fab1.setVisibility(View.VISIBLE);

                if (currenttask.getNum_of_promotodo() <= 0) {
                    fab1.setVisibility(View.INVISIBLE);
                } else {
                    fab1.setVisibility(View.VISIBLE);
                }
                fab2.setVisibility(View.INVISIBLE);
                fab3.setVisibility(View.INVISIBLE);
            }

            System.out.println("Not rUnning.......");

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    SharedPreferences.Editor editor;
                    editor = prefs.edit();
                    editor.putInt("CURR", curr);
                    editor.commit();

                    TranslateAnimation animation = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 94,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0
                    );
                    TranslateAnimation animation2 = new TranslateAnimation(
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, -94,
                            Animation.ABSOLUTE, 0,
                            Animation.ABSOLUTE, 0
                    );
                    animation.setDuration(200);
                    animation2.setDuration(200);
                    fab2.startAnimation(animation2);
                    fab3.startAnimation(animation);


                    pulsator.start();
                    startService();
                    Promotodo_service.ispause = false;
                    // revealShow(true);

                    fab1.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    fab3.setVisibility(View.VISIBLE);
                    // startCountDownTimer();


                }
            });

            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Promotodo_service.pause();
                    pulsator.stop();
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);

                }
            });


        }


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
    }

    public void startService() {


        Intent resultBroadCastIntent = new Intent("android.intent.action.BOOT_COMPLETED");
        resultBroadCastIntent.setAction("GET_TIME");
        sendBroadcast(resultBroadCastIntent);
        startService(new Intent(this, Promotodo_service.class));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}

class MyBounceInterpolator implements android.view.animation.Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    MyBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}

