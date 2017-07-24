package com.example.vmmet.mypierotate.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.vmmet.mypierotate.R;
import com.example.vmmet.mypierotate.bean.PieRotateBean;
import com.example.vmmet.mypierotate.view.PieRotateView;

import java.util.ArrayList;
import java.util.List;

public class Test1Activity extends AppCompatActivity {
    private PieRotateView pierotateview1,pierotateview2,pierotateview3,pierotateview4;
    private TextView tv1,tv2,tv3,tv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        pierotateview1=(PieRotateView)findViewById(R.id.pierotate1);
        tv1=(TextView)findViewById(R.id.tv1);
        pierotateview2=(PieRotateView)findViewById(R.id.pierotate2);
        tv2=(TextView)findViewById(R.id.tv2);
        pierotateview3=(PieRotateView)findViewById(R.id.pierotate3);
        tv3=(TextView)findViewById(R.id.tv3);
        pierotateview4=(PieRotateView)findViewById(R.id.pierotate4);
        tv4=(TextView)findViewById(R.id.tv4);
        setPieRotate1(true,pierotateview1,tv1);
        setPieRotate1(false,pierotateview2,tv2);
        setPieRotate2(true, pierotateview3, tv3);
        setPieRotate2(false,pierotateview4,tv4);
    }
    private void setPieRotate1(boolean IsShowTextonMove,PieRotateView pieRotateView,final TextView tv) {
        final PieRotateBean pieRotate=new PieRotateBean();
        List<String> list_names=new ArrayList<>();
        list_names.add("1号机组");
        list_names.add("2号机组");
        list_names.add("3号机组");
        list_names.add("4号机组");
        list_names.add("5号机组");
        final List<Float> list_numbers=new ArrayList<>();
        list_numbers.add(100f);
        list_numbers.add(200f);
        list_numbers.add(300f);
        list_numbers.add(400f);
        list_numbers.add(400f);
        final List<Integer> list_colors=new ArrayList<>();
        list_colors.add(Color.parseColor("#FF7F00"));
        list_colors.add(Color.parseColor("#EE7AE9"));
        list_colors.add(Color.parseColor("#CD0000"));
        list_colors.add(Color.parseColor("#228B22"));
        list_colors.add(Color.parseColor("#1C86EE"));
        pieRotate.setList_colors(list_colors);
        pieRotate.setList_names(list_names);
        pieRotate.setList_numbers(list_numbers);
        pieRotate.setMax_number(1400f);
        pieRotate.setTextColor(Color.WHITE);
        pieRotate.setIsShowTextonMove(IsShowTextonMove);
        pieRotate.setIsShowOutSide(false);
        pieRotateView.setPieRotate(pieRotate);
        pieRotateView.setOnSelectionListener(new PieRotateView.onSelectionListener() {
            @Override
            public void onSelect(int id) {
                tv.setTextColor(list_colors.get(id));
                tv.setText("id="+id+" 数值="+list_numbers.get(id)+
                        " 所占百分比="+(list_numbers.get(id)/pieRotate.getMax_number()*100)+"%");
            }
        });

    }
    private void setPieRotate2(boolean IsShowTextonMove,PieRotateView pieRotateView,final TextView tv) {
        final PieRotateBean pieRotate=new PieRotateBean();
        List<String> list_names=new ArrayList<>();
        list_names.add("1号机组");
        list_names.add("2号机组");
        list_names.add("3号机组");
        list_names.add("4号机组");
        list_names.add("5号机组");
        final List<Float> list_numbers=new ArrayList<>();
        list_numbers.add(100f);
        list_numbers.add(200f);
//        list_numbers.add(300f);
//        list_numbers.add(400f);
//        list_numbers.add(400f);
        final List<Integer> list_colors=new ArrayList<>();
        list_colors.add(Color.parseColor("#FF7F00"));
        list_colors.add(Color.parseColor("#EE7AE9"));
        list_colors.add(Color.parseColor("#CD0000"));
        list_colors.add(Color.parseColor("#228B22"));
        list_colors.add(Color.parseColor("#1C86EE"));
        pieRotate.setList_colors(list_colors);
        pieRotate.setList_names(list_names);
        pieRotate.setList_numbers(list_numbers);
        pieRotate.setMax_number(300f);
        pieRotate.setTextColor(Color.WHITE);
        pieRotate.setIsShowTextonMove(IsShowTextonMove);
        pieRotate.setIsShowOutSide(false);
        pieRotateView.setPieRotate(pieRotate);
        pieRotateView.setOnSelectionListener(new PieRotateView.onSelectionListener() {
            @Override
            public void onSelect(int id) {
                tv.setTextColor(list_colors.get(id));
                tv.setText("id="+id+" 数值="+list_numbers.get(id)+
                        " 所占百分比="+(list_numbers.get(id)/pieRotate.getMax_number()*100)+"%");
            }
        });
    }
}
