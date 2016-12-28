package com.zfb.house.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lemon.util.GlideUtil;
import com.zfb.house.R;

public class ImageFragment extends Fragment {

    private String url;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        url = args.getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photo, container, false);
        ImageView ivPhoto = (ImageView) v.findViewById(R.id.ivPhoto);
        Glide.with(getActivity()).load(url).placeholder(R.drawable.default_banner).into(ivPhoto);
        return v;
    }
}
