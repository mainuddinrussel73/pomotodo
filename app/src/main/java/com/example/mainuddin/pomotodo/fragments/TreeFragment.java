package com.example.mainuddin.pomotodo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mainuddin.pomotodo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeFragment extends Fragment {

    View treeView;
    private int collected = 0;
    private int coll = 0;
    private int screenWidth = 300;
    private int screenHeight = 300;
    boolean l1 = false, l2 = false, l3 = false, l4 = false, l5 = false;

    List<ImageButton> imageButtons = new ArrayList<>();


    public TreeFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        treeView = inflater.inflate(R.layout.fragment_tree, container, false);

        final SharedPreferences prefs = getActivity().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;


        collected = prefs.getInt("collected", 0);
        coll = prefs.getInt("collected_total", 0);

        System.out.println(collected);

        final ImageView imageView = treeView.findViewById(R.id.CorrWrong);
        final ImageView imageView2 = treeView.findViewById(R.id.CorrWrong1);
        final ImageView imageView3 = treeView.findViewById(R.id.CorrWrong2);
        final ImageView imageView4 = treeView.findViewById(R.id.CorrWrong3);
        final ImageView imageView5 = treeView.findViewById(R.id.CorrWrong4);
        final ImageView imageView6 = treeView.findViewById(R.id.CorrWrong5);


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        final TextView collectedView = treeView.findViewById(R.id.collectedTV);
        collectedView.setText("Collected: " + coll);

        if (coll <= 2) {
            imageView.setVisibility(View.VISIBLE);
        } else if (coll > 2 && coll <= 100) {
            imageView.setVisibility(View.GONE);
            imageView2.setVisibility(View.VISIBLE);
        } else if (coll > 100 && coll <= 160) {
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.VISIBLE);

        } else if (coll > 160 && coll <= 240) {
            imageView3.setVisibility(View.GONE);
            imageView4.setVisibility(View.VISIBLE);

        } else if (coll > 240 && coll <= 320) {
            imageView4.setVisibility(View.GONE);
            imageView5.setVisibility(View.VISIBLE);

        } else if (coll > 320) {
            imageView5.setVisibility(View.GONE);
            imageView6.setVisibility(View.VISIBLE);

        }

        RelativeLayout relativeLayout = treeView.findViewById(R.id.content);
        for (int i = 0; i < collected; i++) {
            ImageButton imageButton = new ImageButton(getContext());
            Random random = new Random();
            float candyX = (float) random.nextInt(screenWidth - 50);
            float candyY = 120f + (float) random.nextInt(600 - 50);
            System.out.println(candyX + " : " + candyY);
            imageButton.setX(candyX);
            imageButton.setY(candyY);
            imageButton.setLayoutParams(new RelativeLayout.LayoutParams(150, 150));
            imageButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.sun));

            relativeLayout.addView(imageButton);
            imageButtons.add(imageButton);
        }


        final ImageView imageView1 = treeView.findViewById(R.id.spark_button);
        imageView1.setImageResource(R.drawable.open_close);


        View.OnClickListener onClick = new View.OnClickListener() {
            public void onClick(View view) {
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.sun_load);
                mp.start();

                ImageButton button = (ImageButton) view;//safe cast since this is only added to buttons.
                //process button value..

                AnimationDrawable explosionAnimation = (AnimationDrawable) imageView1.getDrawable();
                explosionAnimation.start();

                if (explosionAnimation.isOneShot()) {
                    imageView1.setImageResource(R.drawable.close_open);
                    AnimationDrawable explosionAnimation1 = (AnimationDrawable) imageView1.getDrawable();
                    explosionAnimation1.start();
                }

                System.out.println("clicking");
                button.setVisibility(View.GONE);

                coll = coll + 1;
                collected = collected - 1;
                SharedPreferences.Editor editor;

                editor = prefs.edit();
                editor.putInt("collected", collected);
                editor.commit();


                editor = prefs.edit();
                editor.putInt("collected_total", coll);
                editor.commit();


                collectedView.setText("Collected: " + coll);

                if (coll > 2 && coll <= 100) {
                    imageView.setVisibility(View.GONE);
                    imageView2.setVisibility(View.VISIBLE);
                    if (l1 == false) {
                        MediaPlayer mp1 = MediaPlayer.create(getContext(), R.raw.levelup);
                        mp1.start();
                        l1 = true;
                    }
                } else if (coll > 100 && coll <= 160) {
                    imageView2.setVisibility(View.GONE);
                    imageView3.setVisibility(View.VISIBLE);
                    if (l2 == false) {
                        MediaPlayer mp1 = MediaPlayer.create(getContext(), R.raw.levelup);
                        mp1.start();
                        l2 = true;
                    }

                } else if (coll > 160 && coll <= 240) {
                    imageView3.setVisibility(View.GONE);
                    imageView4.setVisibility(View.VISIBLE);
                    if (l3 == false) {
                        MediaPlayer mp1 = MediaPlayer.create(getContext(), R.raw.levelup);
                        mp1.start();
                        l3 = true;
                    }

                } else if (coll > 240 && coll <= 320) {
                    imageView4.setVisibility(View.GONE);
                    imageView5.setVisibility(View.VISIBLE);
                    if (l4 == false) {
                        MediaPlayer mp1 = MediaPlayer.create(getContext(), R.raw.levelup);
                        mp1.start();
                        l4 = true;
                    }

                } else if (coll > 320) {
                    imageView5.setVisibility(View.GONE);
                    imageView6.setVisibility(View.VISIBLE);
                    if (l5 == false) {
                        MediaPlayer mp1 = MediaPlayer.create(getContext(), R.raw.levelup);
                        mp1.start();
                        l5 = true;
                    }

                }


            }

        };

        for (ImageButton imageButton : imageButtons) {
            imageButton.setOnClickListener(onClick);

        }


        return treeView;

    }
}
