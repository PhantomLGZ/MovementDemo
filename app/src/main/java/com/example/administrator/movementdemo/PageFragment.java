package com.example.administrator.movementdemo;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

public class PageFragment extends Fragment {
    @LayoutRes int layoutRes;

    public static PageFragment newInstance(@LayoutRes int layoutRes){
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt("layoutRes", layoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        ViewStub stub = (ViewStub) view.findViewById(R.id.stub);
        stub.setLayoutResource(layoutRes);
        stub.inflate();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            layoutRes = args.getInt("layoutRes");
        }
    }
}
