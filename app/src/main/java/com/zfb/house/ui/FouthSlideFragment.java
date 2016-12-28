package com.zfb.house.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.zfb.house.R;

public class FouthSlideFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.intro4, container, false);
        Button btn= (Button) v.findViewById(R.id.btn_intro_open);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //Intent.FLAG_ACTIVITY_CLEAR_TOP表示销毁目标Activity，和它之上的所有Activity，重新创建目标Activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
               getActivity().finish();
            }
        });
        return v;
    }
}
