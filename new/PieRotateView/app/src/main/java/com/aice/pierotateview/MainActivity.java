package com.aice.pierotateview;

import android.graphics.Color;
import android.os.Bundle;

import com.wxy.pierotateview.model.PieRotateViewModel;
import com.wxy.pierotateview.view.PieRotateView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.pie)
    PieRotateView pie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<PieRotateViewModel> list=new ArrayList<>();
        list.add(new PieRotateViewModel("1",50,getResources().getColor(R.color.colorAccent)));
        list.add(new PieRotateViewModel("1",10,getResources().getColor(R.color.colorPrimaryDark)));
        list.add(new PieRotateViewModel("1",10, Color.BLUE));
        pie.setPieRotateViewModelList(list);
    }
}
