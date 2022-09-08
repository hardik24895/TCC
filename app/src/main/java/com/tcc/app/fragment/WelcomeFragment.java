package com.tcc.app.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.tcc.app.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class WelcomeFragment extends Fragment {

    private Activity mActivity;
    private View mView;
    private ArrayList<Drawable> mArrDrawable;
    private ImageView mImagePage;
    private int position;

    @SuppressLint("ValidFragment")
    public WelcomeFragment(int a) {
        position = a;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_welcome, container, false);
        mActivity = getActivity();
        mArrDrawable = new ArrayList<>();
        getBundle();
        getIds();
        setData();
        return mView;
    }

    private void getBundle() {
        Bundle bundle = new Bundle();
        if (bundle != null) {
            //position=bundle.getInt("position")  ;
        }
    }

    private void getIds() {
        mImagePage = mView.findViewById(R.id.image_wel_come_banner);
    }

    private void setData() {

        switch (position) {
            case 0:
                mImagePage.setImageResource(R.drawable.welcome1);
                break;
            case 1:
                mImagePage.setImageResource(R.drawable.welcome2);
                break;
            case 2:
                mImagePage.setImageResource(R.drawable.welcome3);
                break;
            case 3:
                mImagePage.setImageResource(R.drawable.welcome4);
                break;
            case 4:
                mImagePage.setImageResource(R.drawable.welcome5);
                break;
            case 5:
                mImagePage.setImageResource(R.drawable.welcome6);
                break;
        }

    }

}
