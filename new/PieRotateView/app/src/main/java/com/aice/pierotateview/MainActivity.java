package com.aice.pierotateview;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.tv_name)
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<PieRotateViewModel> list = new ArrayList<>();
        list.add(new PieRotateViewModel("红扇", 20, getResources().getColor(R.color.colorAccent)));
        list.add(new PieRotateViewModel("绿扇", 10, getResources().getColor(R.color.colorPrimaryDark)));
        list.add(new PieRotateViewModel("蓝扇", 10, Color.BLUE));
        list.add(new PieRotateViewModel("黄扇", 60, Color.parseColor("#FF7F00")));
        list.add(new PieRotateViewModel("粉扇", 50, Color.parseColor("#EE7AE9")));
        pie.setPieRotateViewModelList(list);
        pie.setRecoverTime(300);
        pie.setOnSelectionListener(new PieRotateView.onSelectionListener() {
            @Override
            public void onSelect(int position,String percent) {
                tvName.setText(list.get(position).getName()+"数额："+list.get(position).getNum()+" 占比："+percent);
                tvName.setTextColor(list.get(position).getColor());
            }
        });
    }
}
