package com.example.mainuddin.pomotodo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainuddin.pomotodo.R;
import com.example.mainuddin.pomotodo.adapters.SingerAdapter;
import com.example.mainuddin.pomotodo.database.DbTaskDhandle;
import com.example.mainuddin.pomotodo.model.Task_Detail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;
import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration;
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView;
import xyz.sangcomz.stickytimelineview.model.SectionInfo;

public class TimeLineFragment extends Fragment {


    View timelineView;
    private Drawable icFinkl, icBuzz, icWannaOne, icGirlsGeneration, icSolo;

    public TimeLineFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        timelineView = inflater.inflate(R.layout.fragment_timeline, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        TimeLineRecyclerView recyclerView = null;
        RelativeLayout layout1 = timelineView.findViewById(R.id.layout1);
        RelativeLayout layout2 = timelineView.findViewById(R.id.layout2);

        layout1.setVisibility(RelativeLayout.GONE);


        if (isDark && layout1.getVisibility() == View.GONE) {
            layout1.setVisibility(RelativeLayout.VISIBLE);
            layout2.setVisibility(RelativeLayout.GONE);
            recyclerView = layout1.findViewById(R.id.recycler_view);

        }
        layout2.setVisibility(RelativeLayout.GONE);

        if (!isDark && layout2.getVisibility() == View.GONE) {
            layout1.setVisibility(RelativeLayout.GONE);
            layout2.setVisibility(RelativeLayout.VISIBLE);
            recyclerView = layout2.findViewById(R.id.recycler_view);

        }

        initDrawable();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false));
        recyclerView.animate();


        DbTaskDhandle mDBHelper = new DbTaskDhandle(getContext());
        List<Task_Detail> singerList = new ArrayList<>();
        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                Task_Detail word = new Task_Detail();

                word.setID(Integer.parseInt(cursor.getString(0)));
                word.setTitle(cursor.getString(1));
                word.setNum_of_promotodo(cursor.getInt(2));
                word.setCompleted_promotodo(cursor.getInt(3));
                word.setIsrepeat(cursor.getInt(4));
                word.setDue_date(cursor.getString(5));
                word.setSTART_TIME(cursor.getString(6));
                word.setEND_TIME(cursor.getString(7));


                //  System.out.println(word.toString());
                singerList.add(word);

            }

            // size = contactList.size();

        } else {

            Toasty.info(getContext(), "Nothing to show.", Toasty.LENGTH_LONG).show();
        }

       /* LocalDate parsedDate = LocalDate.now().minusDays(1); //Parse date from String
        String str = parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        singerList.add(new Task_Detail(0,"read".concat(Integer.toString(5)),0,3,0,str));

        Long max =0L;
        Long min =100000000000L;
        //Use the date format that best suites you
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i <100 ; i+=2) {
            Random r = new Random();
            Long randomLong=(r.nextLong() % (max - min)) + min;
            Date dt =new Date(randomLong);
            System.out.println("Date generated from long: "+spf.format(dt));
            Task_Detail task_detail = new Task_Detail(i,"sleep".concat(Integer.toString(i)),0,r.nextInt(),0,spf.format(dt));

            singerList.add(task_detail);
            singerList.add(new Task_Detail(i++,"read".concat(Integer.toString(i)),0,r.nextInt(),0,spf.format(dt)));
        }
*/
        Collections.sort(singerList, new Comparator<Task_Detail>() {
            @Override
            public int compare(Task_Detail a, Task_Detail b) {
                return b.getDue_date().compareTo(a.getDue_date());
            }
        });

        recyclerView.addItemDecoration(getSectionCallback(singerList));
        recyclerView.setAdapter(new SingerAdapter(singerList, getActivity()));

        return timelineView;

    }


    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<Task_Detail> singerList) {
        return new RecyclerSectionItemDecoration.SectionCallback() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Nullable
            @Override
            public SectionInfo getSectionHeader(int position) {
                LocalDate parsedDate = LocalDate.now(); //Parse date from String
                String str = parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Task_Detail singer = singerList.get(position);
                Drawable dot;


                if (str.equals(singer.getDue_date())) {
                    dot = icSolo;
                } else {
                    dot = icFinkl;
                }

                return new SectionInfo(singer.getDue_date(), "", dot);
            }

            @Override
            public boolean isSection(int position) {
                return !singerList.get(position).getDue_date().equals(singerList.get(position - 1).getDue_date());
            }
        };
    }


    private void initDrawable() {
        icFinkl = AppCompatResources.getDrawable(getContext(), R.drawable.ic_timeline_past);
        icSolo = AppCompatResources.getDrawable(getContext(), R.drawable.ic_timeline_present);
    }
}
