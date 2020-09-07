package com.example.mainuddin.pomotodo.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azan.Azan;
import com.azan.AzanTimes;
import com.azan.Method;
import com.azan.Time;
import com.azan.astrologicalCalc.Location;
import com.azan.astrologicalCalc.SimpleDate;
import com.example.mainuddin.pomotodo.R;
import com.example.mainuddin.pomotodo.Utils.GridDividerDecoration;
import com.example.mainuddin.pomotodo.Utils.LocationTrack;
import com.example.mainuddin.pomotodo.Utils.NewMovieDialogFragment;
import com.example.mainuddin.pomotodo.adapters.BaseMovieAdapter;
import com.example.mainuddin.pomotodo.adapters.MovieAdapterByGenre;
import com.example.mainuddin.pomotodo.daily_service;
import com.example.mainuddin.pomotodo.model.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeFragment extends Fragment implements BaseMovieAdapter.OnItemClickListener, NewMovieDialogFragment.DialogListener {

    private View homeView;
    public static final int PICKFILE_RESULT_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static Activity mActivity;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    protected double latitude, longitude;
    private List<String> permissionsToRequest;
    private List<String> permissionsRejected = new ArrayList();
    private List<String> permissions = new ArrayList();
    LocationTrack locationTrack;

    private List<Movie> mMovieList;
    private Comparator<Movie> movieComparator;
    private RecyclerView recyclerView;
    private GridDividerDecoration gridDividerDecoration;
    private BaseMovieAdapter mSectionedRecyclerAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions((ArrayList) permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {

                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission();
            }
        } else {

        }
        AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intenta = new Intent(getContext(), daily_service.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(getContext(), 0, intenta, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 58);

        if (Build.VERSION.SDK_INT >= 23) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), pIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);
        } else {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
        }

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = day + "/" + (month + 1) + "/" + year;


        locationTrack = new LocationTrack(getContext());


        if (locationTrack.canGetLocation()) {


            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();

            Toast.makeText(getContext(), "Longitude:" + longitude + "\nLatitude:" + latitude, Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }

        TextView faz = homeView.findViewById(R.id.faz);
        TextView zhr = homeView.findViewById(R.id.zoh);
        TextView asr = homeView.findViewById(R.id.asr);
        TextView mag = homeView.findViewById(R.id.mag);
        TextView esa = homeView.findViewById(R.id.esa);


        SimpleDate simpleDate = new SimpleDate(new GregorianCalendar());
        System.out.println(latitude + ",,,,,," + longitude);
        Location locationa = new Location(latitude, longitude, 6.0, 0);
        Azan azan = new Azan(locationa, Method.Companion.getEGYPT_SURVEY());
        AzanTimes prayerTimes = azan.getPrayerTimes(simpleDate);
        System.out.println(prayerTimes.assr());
        Time imsaak = azan.getImsaak(simpleDate);

        System.out.println(imsaak.toString());
        faz.setText("Fazr : " + prayerTimes.fajr().toString());
        zhr.setText("Zhr : " + prayerTimes.thuhr().toString());
        asr.setText("Asr' : " + prayerTimes.assr().toString());
        mag.setText("Maghreb : " + prayerTimes.maghrib().toString());
        esa.setText("Esa : " + prayerTimes.ishaa().toString());


        recyclerView = homeView.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        gridDividerDecoration = new GridDividerDecoration(getContext());
        recyclerView.addItemDecoration(gridDividerDecoration);

        mMovieList = new ArrayList<>(20);


        if (day % 2 == 0) {
            mMovieList.add(new Movie("ইংরেজি(কম্প্রিহেন্সনস)১ ঘণ্টা", "সকাল"));
            mMovieList.add(new Movie("ডাইজেস্ট(বাংলা + আন্তর্জাতিক) ১.৫ ঘণ্টা", "সকাল"));
            mMovieList.add(new Movie("মানসিক দক্ষতা ১ ঘণ্টা", "সকাল"));

            mMovieList.add(new Movie("কারেন্ট অ্যাফেয়ার্স ০.৫ ঘণ্টা", "বিকাল"));
            mMovieList.add(new Movie("সিএসসি জব ১ ঘণ্টা", "বিকাল"));

            mMovieList.add(new Movie("বাংলাদেশ লিখিত ১ ঘণ্টা", "রাত"));
            mMovieList.add(new Movie("বিজ্ঞান লিখিত  ১ ঘণ্টা", "রাত"));
            mMovieList.add(new Movie("গণিত লিখিত ১ ঘণ্টা", "রাত"));

        } else {
            mMovieList.add(new Movie("ইংরেজি(ট্রান্সলেশন)১ ঘণ্টা", "সকাল"));
            mMovieList.add(new Movie("ডাইজেস্ট(বাংলাদেশ + (বিজ্ঞান / আইসিটি + ভূগোল)) ১.৫ ঘণ্টা", "সকাল"));
            mMovieList.add(new Movie("মানসিক দক্ষতা ১ ঘণ্টা", "সকাল"));

            mMovieList.add(new Movie("পেপার ০.৫ ঘণ্টা", "বিকাল"));
            mMovieList.add(new Movie("সিএসসি জব ১ ঘণ্টা", "বিকাল"));

            mMovieList.add(new Movie("বাংলাদেশ লিখিত ১ ঘণ্টা", "রাত"));
            mMovieList.add(new Movie("বিজ্ঞান লিখিত  ১ ঘণ্টা", "রাত"));
            mMovieList.add(new Movie("গণিত লিখিত ১ ঘণ্টা", "রাত"));

        }


        this.movieComparator = new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getGroup().compareTo(o2.getGroup());
            }
        };

        Collections.sort(mMovieList, movieComparator);
        mSectionedRecyclerAdapter = new MovieAdapterByGenre(mMovieList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSectionedRecyclerAdapter.setGridLayoutManager(gridLayoutManager);


        mSectionedRecyclerAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(mSectionedRecyclerAdapter);

        FloatingActionButton button = homeView.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                onFabClick(v);
            }
        });

        return homeView;
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            Toasty.info(getContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onMovieCreated(Movie movie) {
        for (int i = 0; i < mMovieList.size(); i++) {
            if (movieComparator.compare(mMovieList.get(i), movie) >= 0) {
                mMovieList.add(i, movie);
                mSectionedRecyclerAdapter.notifyItemInsertedAtPosition(i);
                return;
            }
        }
        mMovieList.add(mMovieList.size(), movie);//
        mSectionedRecyclerAdapter.notifyItemInsertedAtPosition(mMovieList.size() - 1);
    }

    @Override
    public void onItemClicked(Movie movie) {
        final int index = mMovieList.indexOf(movie);
        //mMovieList.remove(movie);
        //mSectionedRecyclerAdapter.notifyItemRemovedAtPosition(index);
    }

    @Override
    public void onSubheaderClicked(int position) {
        if (mSectionedRecyclerAdapter.isSectionExpanded(mSectionedRecyclerAdapter.getSectionIndex(position))) {
            mSectionedRecyclerAdapter.collapseSection(mSectionedRecyclerAdapter.getSectionIndex(position));
        } else {
            mSectionedRecyclerAdapter.expandSection(mSectionedRecyclerAdapter.getSectionIndex(position));
        }
    }

    public void onFabClick(View v) {
        NewMovieDialogFragment newMovieDialogFragment = new NewMovieDialogFragment();
        newMovieDialogFragment.setTargetFragment(this, 1);
        newMovieDialogFragment.show(getFragmentManager(), "newMovie");
    }

}
