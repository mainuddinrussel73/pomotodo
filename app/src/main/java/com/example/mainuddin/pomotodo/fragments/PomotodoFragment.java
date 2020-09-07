package com.example.mainuddin.pomotodo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.mainuddin.pomotodo.R;
import com.example.mainuddin.pomotodo.Utils.Utils;
import com.example.mainuddin.pomotodo.database.DBproHandle;
import com.example.mainuddin.pomotodo.model.promotododata;
import com.example.mainuddin.pomotodo.promodetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import static com.yydcdut.sdlv.Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;

public class PomotodoFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener,
        SlideAndDragListView.OnDragDropListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {

    public BaseAdapter mAdapter = new BaseAdapter() {


        public View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Object o = v.getTag();
                if (o != null && o instanceof Integer) {
                    mListView.startDrag(((Integer) o).intValue());

                }
                return false;
            }
        };
        CustomViewHolder cvh;
        private View.OnClickListener mOnTouchListener1 = new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(final View v) {
                Object o = v.getTag();
                if (o != null && o instanceof Integer) {
                    if (((CheckBox) v).isChecked()) {

                        DBproHandle mDBHelper = new DBproHandle(v.getContext());
                        int k = promotododataList.get(((Integer) o).intValue() - mListView.getHeaderViewsCount()).getISREPEAT();
                        String s = promotododataList.get(((Integer) o).intValue() - mListView.getHeaderViewsCount()).getTitle();
                        int n = promotododataList.get(((Integer) o).intValue() - mListView.getHeaderViewsCount()).getNum_of_promotodo();

                        String d = promotododataList.get(((Integer) o).intValue() - mListView.getHeaderViewsCount()).getDue_date();

                        System.out.println(d);
                        promotododata item = (promotododata) mAdapter.getItem(((Integer) o).intValue());
                        //mDBHelper = new DBproHandle(PomotodoFragment.this);
                        int i = mDBHelper.deleteData(item.getTitle());
                        if (i == 1) {
                            Toasty.success(v.getContext(), "Task Completed!", Toasty.LENGTH_LONG).show();
                            //wait 1 second
                            v.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    ((CheckBox) v).toggle();
                                }
                            }, 1000);


                            promotododataList.remove(((Integer) o).intValue() - mListView.getHeaderViewsCount());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toasty.error(v.getContext(), "Task not Completed!", Toasty.LENGTH_LONG).show();
                        }

                        if (k == 1) {
                            try {
                                LocalDate parsedDate = LocalDate.parse(d); //Parse date from String
                                LocalDate addedDate = parsedDate.plusDays(1);
                                String str = addedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                boolean b = mDBHelper.insertIsreat(s, k, n, str);

                                if (b == true) {
                                    Toasty.success(v.getContext(), "Repeated.", Toast.LENGTH_SHORT).show();
                                    item.setDue_date(str);
                                    item.setNum_of_promotodo(0);
                                    promotododataList.add(promotododataList.size(), item);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    Toasty.error(v.getContext(), "opps.", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }

                        }


                    }
                }

            }


        };

        @Override
        public int getCount() {
            return promotododataList.size();
        }

        @Override
        public Object getItem(int position) {
            return promotododataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return promotododataList.get(position).hashCode();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int position, View rowView, ViewGroup parent) {

            promotododata item = (promotododata) this.getItem(position);

            if (rowView == null) {
                cvh = new CustomViewHolder();
                rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, null);
                //cvh = new CustomViewHolder();
                // rowView = inflater.inflate(R.layout.item_custom, null,true);
                cvh.imgLogo = rowView.findViewById(R.id.img_item_edit);
                cvh.imgLogo3 = rowView.findViewById(R.id.loopp);
                cvh.txtName = rowView.findViewById(R.id.txt_item_edit);
                cvh.date = rowView.findViewById(R.id.dates);
                cvh.imgLogo2 = rowView.findViewById(R.id.img_item_edit2);
                cvh.circularProgressBar = rowView.findViewById(R.id.circularProgressBar);
                cvh.circularProgressBar.setProgressMax(tike);
                cvh.circularProgressBar1 = rowView.findViewById(R.id.circularProgressBar1);
                cvh.circularProgressBar1.setProgressMax(tike);
                cvh.circularProgressBar2 = rowView.findViewById(R.id.circularProgressBar2);
                cvh.circularProgressBar2.setProgressMax(tike);
                cvh.circularProgressBar3 = rowView.findViewById(R.id.circularProgressBar3);
                cvh.circularProgressBar3.setProgressMax(tike);
                cvh.imgLogo2.setOnTouchListener(mOnTouchListener);
                cvh.imgLogo.setOnClickListener(mOnTouchListener1);
                rowView.setTag(cvh);
            } else {
                cvh = (CustomViewHolder) rowView.getTag();
            }

            cvh.txtName.setText(item.getTitle());
            cvh.date.setText(item.getDue_date());
            Drawable d;
            SharedPreferences prefs = getActivity().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("isDark", false);
            cvh.imgLogo.setButtonDrawable(getActivity().getResources().getDrawable(
                    R.drawable.radio_btn));
            RelativeLayout listitm = rowView.findViewById(R.id.layout_item_edit);
            if (isDark) {
                cvh.imgLogo2.setImageDrawable(Utils.getDrawable(rowView.getContext(), R.drawable.ic_keyboard_arrow_left_white_24dp));
                cvh.txtName.setTextColor(getResources().getColor(R.color.link));
                listitm.setBackgroundDrawable(ContextCompat.getDrawable(rowView.getContext(), R.drawable.background_card_dark));
                LocalDate parsedDate = LocalDate.now(); //Parse date from String
                LocalDate parsedDate1 = LocalDate.parse(item.getDue_date()); //Parse date from String
                if (parsedDate.isAfter(parsedDate1)) {
                    cvh.date.setTextColor(Color.RED);
                } else {

                    cvh.date.setTextColor(Color.WHITE);
                }

                //cvh.imgLogo.setB(Color.WHITE);
                if (item.isIsrepeat() == 1) {

                    d = VectorDrawableCompat.create(getResources(), R.drawable.ic_loop_white_24dp, null);
                    //d = DrawableCompat.wrap(d);
                    // System.out.println("jkjkjkk");
                    //DrawableCompat.setTint(d, Color.BLACK);
                    //cvh.imgLogo3.setImageDrawable(d);
                } else {
                    d = VectorDrawableCompat.create(getResources(), R.drawable.ic_loop_black_24dp, null);
                    // d = DrawableCompat.wrap(d);
                    // DrawableCompat.setTint(d, Color.WHITE);

                }
            } else {
                cvh.imgLogo2.setImageDrawable(Utils.getDrawable(rowView.getContext(), R.drawable.ic_keyboard_arrow_left_black_24dp));
                cvh.txtName.setTextColor(getResources().getColor(R.color.link));
                listitm.setBackgroundDrawable(ContextCompat.getDrawable(rowView.getContext(), R.drawable.background_card));
                LocalDate parsedDate = LocalDate.now(); //Parse date from String
                LocalDate parsedDate1 = LocalDate.parse(item.getDue_date()); //Parse date from String
                if (parsedDate.isAfter(parsedDate1)) {
                    cvh.date.setTextColor(Color.RED);
                } else {

                    cvh.date.setTextColor(Color.BLACK);
                }


                // cvh.imgLogo.setBackgroundColor(Color.BLACK);
                if (item.isIsrepeat() == 1) {

                    d = VectorDrawableCompat.create(getResources(), R.drawable.ic_loop_black_24dp, null);
                    //d = DrawableCompat.wrap(d);
                    // System.out.println("jkjkjkk");
                    //DrawableCompat.setTint(d, Color.BLACK);
                    //cvh.imgLogo3.setImageDrawable(d);
                } else {
                    d = VectorDrawableCompat.create(getResources(), R.drawable.ic_loop_white_24dp, null);
                    // d = DrawableCompat.wrap(d);
                    // DrawableCompat.setTint(d, Color.WHITE);

                }

            }
            cvh.imgLogo3.setImageDrawable(d);

            // this.notifyDataSetChanged();
            // cvh.imgLogo.setImageDrawable(Utils.getDrawable(context, R.drawable.ic_timer_white_24dp));

            cvh.imgLogo2.setTag(Integer.parseInt(Integer.toString(position)));
            cvh.imgLogo.setTag(Integer.parseInt(Integer.toString(position)));
            cvh.circularProgressBar.setProgressBarColor(Color.RED);
            cvh.circularProgressBar1.setProgressBarColor(Color.RED);
            cvh.circularProgressBar2.setProgressBarColor(Color.RED);
            cvh.circularProgressBar3.setProgressBarColor(Color.RED);
            if (promotododataList.get(position).getNum_of_promotodo() == 1) {
                cvh.circularProgressBar.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar1.setProgressWithAnimation(0, (long) 1000);
                cvh.circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
            } else if (promotododataList.get(position).getNum_of_promotodo() == 2) {
                cvh.circularProgressBar.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
            } else if (promotododataList.get(position).getNum_of_promotodo() == 3) {
                cvh.circularProgressBar.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
            } else if (promotododataList.get(position).getNum_of_promotodo() == 4) {
                cvh.circularProgressBar.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar1.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar2.setProgressWithAnimation(tike, (long) 1000);
                cvh.circularProgressBar3.setProgressWithAnimation(tike, (long) 1000);
            } else {

            }


            return rowView;
        }

        class CustomViewHolder {
            public CheckBox imgLogo;
            public TextView txtName;
            public TextView date;
            public ImageView imgLogo2;
            public ImageView imgLogo3;
            public CircularProgressBar circularProgressBar;
            public CircularProgressBar circularProgressBar1;
            public CircularProgressBar circularProgressBar2;
            public CircularProgressBar circularProgressBar3;

        }

    };
    int start = 0;
    public static List<promotododata> promotododataList = new ArrayList<>();
    public static SlideAndDragListView mListView;
    public static DBproHandle mDBHelper;
    long tike = 18000;
    String titles = "  ";
    ExtendedEditText tfb;
    TextView totaltask, totalhour, remain;
    ImageButton button;
    promotododata currenttask;
    private Menu mMenu;
    private Toast mToast;
    private promotododata mDraggedEntity;
    public static SharedPreferences prefs;

    private View promoView;

    public PomotodoFragment() {
        // Required empty public constructor
    }

    public static PomotodoFragment newInstance(String param1, String param2) {
        PomotodoFragment fragment = new PomotodoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        promoView = inflater.inflate(R.layout.fragment_pomotodo, container, false);

        prefs = getContext().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        FloatingActionButton fab = promoView.findViewById(R.id.fab);


        tfb = promoView.findViewById(R.id.text_field_boxes2);
        button = promoView.findViewById(R.id.dark_button);


        totaltask = promoView.findViewById(R.id.totaltask);
        totalhour = promoView.findViewById(R.id.totalhour);
        remain = promoView.findViewById(R.id.taskremain);
        mDBHelper = new DBproHandle(getContext());

        initList();
        initMenu();
        try {
            initUiAndListener();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        totaltask.setText(String.valueOf(promotododataList.size()));

        float tasktime = 0;
        for (int i = 0; i < promotododataList.size(); i++) {
            tasktime += promotododataList.get(i).getNum_of_promotodo();
        }
        totalhour.setText((tasktime * 0.5) + "h");


        SharedPreferences prefs1 = getActivity().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        remain.setText((prefs1.getInt("t", 0) * 0.5) + "h");
        remain.setTextColor(Color.RED);

     /*  DBDaily dbDaily = new DBDaily(this);
        //dbDaily.deleteAll();
        String []months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
        String []days = {"MON", "TUR", "WED", "THU", "FRI","SAT","SUN"};

        Calendar calendar = Calendar.getInstance();




        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH)+1;
        int currentWEEK = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        // currentDay-=1;

        dbDaily.insertAll1("WED",String.valueOf(45),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(11));
         dbDaily.insertAll1("THU",String.valueOf(45),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(3));
        dbDaily.insertAll1("FRI",String.valueOf(45),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(1));
        dbDaily.insertAll1("SAT",String.valueOf(45),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(1));


        dbDaily.insertAll1("SUN",String.valueOf(46),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(0));
        dbDaily.insertAll1("MON",String.valueOf(46),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(1));
        dbDaily.insertAll1("TUR",String.valueOf(46),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(0));
        dbDaily.insertAll1("WED",String.valueOf(46),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(1));
        dbDaily.insertAll1("THU",String.valueOf(46),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(0));
        dbDaily.insertAll1("FRI",String.valueOf(46),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(1));
        dbDaily.insertAll1("SAT",String.valueOf(46),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(5));

        dbDaily.insertAll1("SUN",String.valueOf(47),months[currentMonth-1],String.valueOf(currentYear),Integer.toString(0));

*/
        mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //DatabaseHelper mDBHelper = new DatabaseHelper(PomotodoFragment.this);;
                // SQLiteDatabase mDb;

                if (tfb.getText().toString().isEmpty() || tfb.getText().toString().trim().length() <= 0) {
                    Toasty.error(getContext(), "No input.", Toast.LENGTH_SHORT).show();
                } else {
                    String id = "-1";
                    //SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    SQLiteDatabase db1 = mDBHelper.getReadableDatabase();
                    try {
                        Cursor re = db1.rawQuery("SELECT * FROM promotodo_table WHERE TITLE = ?; ", new String[]{tfb.getText().toString()});
                        if (re.moveToFirst()) {
                            do {
                                //System.out.println(re.getString(0));
                                id = re.getString(0);
                            } while (re.moveToNext());
                        }

                        re.close();
                        // System.out.println(re.getString(0));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    if (!id.equals("-1")) {
                        Toasty.warning(getContext(), "Data already exist", Toasty.LENGTH_LONG).show();
                        //.setEnabled(false);
                    } else {
                        //done.setEnabled(true);
                        LocalDate parsedDate = LocalDate.now(); //Parse date from String

                        String str = parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        boolean b = mDBHelper.insertIsreat(tfb.getText().toString(), 0, 0, str);
                        if (b == true) {

                            PomotodoFragment nextFrag = new PomotodoFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, nextFrag, "findThisFragment")
                                    .addToBackStack(null)
                                    .commit();

                            Toasty.success(getContext(), "Done.", Toast.LENGTH_SHORT).show();
                            // Intent myIntent = new Intent(view.getContext(), PomotodoFragment.class);
                            // myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //startActivityForResult(myIntent, 0);
                        } else {
                            Toasty.error(getContext(), "opps.", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            }
        });

        final RelativeLayout additem = promoView.findViewById(R.id.promotodo_activity);
        RelativeLayout relativeLayout = promoView.findViewById(R.id.bottom);
        RelativeLayout relativeLayout1 = relativeLayout.findViewById(R.id.borderss);
        LinearLayout linearLayout = promoView.findViewById(R.id.textlayout);

        SharedPreferences prefs = getActivity().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        boolean isDark = prefs.getBoolean("isDark", false);


        if (isDark) {

            mListView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bbb));
            relativeLayout1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bbb));
            additem.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bbb));
            relativeLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_rounded));
            linearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_card_dark));

            tfb.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.soft_dark));
            tfb.setTextColor(Color.WHITE);
            tfb.setHintTextColor(Color.rgb(185, 185, 185));

        } else {

            mListView.setBackgroundColor(Color.parseColor("#e6e6e6"));
            relativeLayout1.setBackgroundColor(Color.parseColor("#e6e6e6"));
            linearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_card));
            additem.setBackgroundColor(Color.parseColor("#e6e6e6"));
            relativeLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.base_rounded_white));
            tfb.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.soft_light));
            tfb.setTextColor(Color.BLACK);
            tfb.setHintTextColor(Color.BLACK);

        }

        return promoView;
    }


    public void initList() {

        promotododataList.clear();

        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                promotododata word = new promotododata();
                word.setID(Integer.parseInt(cursor.getString(0) + 1));
                word.setTitle(cursor.getString(1));
                word.setNum_of_promotodo(cursor.getInt(2));
                word.setCompleted_promotodo(cursor.getInt(3));
                word.setIsrepeat(cursor.getInt(4));
                word.setDue_date(cursor.getString(5));

                promotododataList.add(word);
                //    System.out.println(word.NUM);


            }


        } else {

            Toasty.info(getContext(), "Nothing to show.", Toasty.LENGTH_LONG).show();
        }

        // mAdapter = new proadapter(promotododataList,this);


    }

    public void initMenu() {
        mMenu = new Menu(true);
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width) * 2)
                .setBackground(Utils.getDrawable(getContext(), R.drawable.btn_left0))
                .setText("One")
                .setTextColor(Color.GRAY)
                .setTextSize(14)
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width))
                .setBackground(Utils.getDrawable(getContext(), R.drawable.btn_left1))
                .setText("Two")
                .setTextColor(Color.BLACK)
                .setTextSize(14)
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width) + 30)
                .setBackground(Utils.getDrawable(getContext(), R.drawable.btn_right0))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.drawable.ic_cancel))
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(Utils.getDrawable(getContext(), R.drawable.btn_right1))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.drawable.ic_timer))
                .build());
    }

    public void initUiAndListener() {
        mListView = promoView.findViewById(R.id.lv_edit);
        mListView.setMenu(mMenu);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
        mListView.setOnDragDropListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnSlideListener(this);
        mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        mListView.setOnItemLongClickListener(this);
        //mListView.setOnItemScrollBackListener((SlideAndDragListView.OnItemScrollBackListener) this);
    }

    @Override
    public void onDragViewStart(int beginPosition) {
        mDraggedEntity = promotododataList.get(beginPosition);
        start = beginPosition;
        try {
            int ccc = prefs.getInt("CURR", 0);
            titles = promotododataList.get(ccc).getTitle();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //toast("onDragViewStart   beginPosition--->" + beginPosition);
    }

    @Override
    public void onDragDropViewMoved(int fromPosition, int toPosition) {

        promotododata applicationInfo = promotododataList.remove(fromPosition);
        //f_promotodo(),applicationInfo.getDue_date());
        promotododataList.add(toPosition, applicationInfo);
        // toast("onDragDropViewMoved   fromPosition--->" + fromPosition + "  toPosition-->" + toPosition);
    }

    @Override
    public void onDragViewDown(int finalPosition) {


        promotododataList.set(finalPosition, mDraggedEntity);


        mDBHelper.deleteAll();
        for (int i = 0; i < promotododataList.size(); i++) {
            mDBHelper.insertIsreat(promotododataList.get(i).getTitle(), promotododataList.get(i).isIsrepeat(),
                    promotododataList.get(i).getNum_of_promotodo(), promotododataList.get(i).getDue_date());
        }


        // promotododata ccco= PomotodoFragment.promotododataList.get(ccc);;

        for (int i = 0; i < promotododataList.size(); i++) {
            // System.out.println(titles);
            if (titles.equals(promotododataList.get(i).getTitle())) {


                SharedPreferences.Editor editor;
                editor = prefs.edit();
                editor.putInt("CURR", i);
                editor.commit();
                break;

            }
        }

        //toast("onDragViewDown   finalPosition--->" + finalPosition);
    }

    @Override
    public void onSlideOpen(View view, View parentView, int position, int direction) {
        //toast("onSlideOpen   position--->" + position + "  direction--->" + direction);
        //cvh.findViewById().imgLogo2.setImageDrawable(Utils.getDrawable(PomotodoFragment.this, R.drawable.ic_keyboard_arrow_left_black_24dp));
    }

    @Override
    public void onSlideClose(View view, View parentView, int position, int direction) {
        //toast("onSlideClose   position--->" + position + "  direction--->" + direction);
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        //toast("onMenuItemClick   itemPosition--->" + itemPosition + "  buttonPosition-->" + buttonPosition + "  direction-->" + direction);
        switch (direction) {
            case MenuItem.DIRECTION_LEFT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_NOTHING;
                    case 1:
                        return Menu.ITEM_SCROLL_BACK;
                }
                break;
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 1:
                        Intent myIntent = new Intent(v.getContext(), promodetail.class);
                        //String s = view.findViewById(R.id.subtitle).toString();
                        //String s = (String) parent.getI;
                        myIntent.putExtra("ID", itemPosition);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        return Menu.ITEM_SCROLL_BACK;
                    case 0:
                        return ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemDeleteAnimationFinished(View view, int position) {
        //mDBHelper = new DBproHandle(PomotodoFragment.this);
        int k = promotododataList.get(position - mListView.getHeaderViewsCount()).getISREPEAT();
        String s = promotododataList.get(position - mListView.getHeaderViewsCount()).getTitle();
        int n = promotododataList.get(position - mListView.getHeaderViewsCount()).getNum_of_promotodo();
        String d = promotododataList.get(position - mListView.getHeaderViewsCount()).getDue_date();

        System.out.println(k + "isrepeat");
        promotododata promotododata = promotododataList.get(position - mListView.getHeaderViewsCount());
        int i = mDBHelper.deleteData(promotododataList.get(position - mListView.getHeaderViewsCount()).getTitle());
        promotododataList.remove(position - mListView.getHeaderViewsCount());
        if (k == 1) {
            LocalDate parsedDate = LocalDate.parse(d); //Parse date from String
            LocalDate addedDate = parsedDate.plusDays(1);
            String str = addedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            boolean b = mDBHelper.insertIsreat(s, k, n, str);
            if (b == true) {
                Toasty.success(getContext(), "Repeated.", Toast.LENGTH_SHORT).show();
                Toasty.success(view.getContext(), "Repeated.", Toast.LENGTH_SHORT).show();
                promotododata.setDue_date(str);
                promotododata.setNum_of_promotodo(0);
                promotododataList.add(promotododataList.size(), promotododata);
                mAdapter.notifyDataSetChanged();
            } else {
                Toasty.error(getContext(), "opps.", Toast.LENGTH_SHORT).show();
            }
        }
        //mAdapter = new proadapter(promotododataList,this);
        mListView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        if (i == 1) {
            Toasty.success(view.getContext(), "Task Completed!", Toasty.LENGTH_LONG).show();
        } else {
            Toasty.error(view.getContext(), "Task not Completed!", Toasty.LENGTH_LONG).show();
        }
        //toast("onItemDeleteAnimationFinished   position--->" + position);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //toast("onItemClick   position--->" + position);

        Intent myIntent = new Intent(view.getContext(), promodetail.class);
        //String s = view.findViewById(R.id.subtitle).toString();
        //String s = (String) parent.getI;
        myIntent.putExtra("ID", position);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //toast("onItemLongClick   position--->" + position);
        Object o = view.getTag();
        if (o != null && o instanceof Integer) {
            mListView.startDrag(((Integer) o).intValue());

        }
        return false;
        //onDragViewStart(position);
        //return false;
    }

    private void toast(String toast) {
        mToast.setText(toast);
        mToast.show();
    }


}
