package com.wxy.pierotateview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wxy.pierotateview.R;
import com.wxy.pierotateview.model.PieRotateViewModel;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.Nullable;

public class PieRotateView extends View {
    private Paint piePaint,circlePaint,textPaint;
    protected static final int DEFUALT_VIEW_WIDTH=400;
    protected static final int DEFUALT_VIEW_HEIGHT=400;
    private float radius,centerX,centerY;
    private int selectPosition;
    private boolean isFirstDraw;
    private float downX,downY,lastX,lastY;
    private float sum;
    public void setCircleColor(int circleColor) {
        circlePaint.setColor(circleColor);
    }
    public void setTextColor(int textColor) {
        textPaint.setColor(textColor);
    }

    public void setOnSelectionListener(PieRotateView.onSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }

    private onSelectionListener onSelectionListener;
    private List<PieRotateViewModel> pieRotateViewModelList;
    public void setPieRotateViewModelList(List<PieRotateViewModel> pieRotateViewModelList) {
        this.pieRotateViewModelList = pieRotateViewModelList;
        for (PieRotateViewModel pieRotateViewModel:pieRotateViewModelList){
            sum+=pieRotateViewModel.getNum();
        }
        selectPosition=1;
        invalidate();
    }

    public PieRotateView(Context context) {
        this(context,null);
    }

    public PieRotateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PieRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        piePaint=new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setColor(Color.BLUE);
        circlePaint=new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#75ffffff"));
        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        isFirstDraw=true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=0,height=0;
        int width_specMode= MeasureSpec.getMode(widthMeasureSpec);
        int height_specMode= MeasureSpec.getMode(heightMeasureSpec);
        switch (width_specMode){
            //宽度精确值
            case MeasureSpec.EXACTLY:
                switch (height_specMode){
                    //高度精确值
                    case MeasureSpec.EXACTLY:
                        width= MeasureSpec.getSize(widthMeasureSpec);
                        height= MeasureSpec.getSize(heightMeasureSpec);
                        break;
                    case MeasureSpec.AT_MOST:
                    case MeasureSpec.UNSPECIFIED:
                        width= MeasureSpec.getSize(widthMeasureSpec);
                        height=width;
                        break;
                }
                break;
            //宽度wrap_content
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                switch (height_specMode){
                    //高度精确值
                    case MeasureSpec.EXACTLY:
                        height= MeasureSpec.getSize(heightMeasureSpec);
                        width=height;
                        break;
                    case MeasureSpec.AT_MOST:
                    case MeasureSpec.UNSPECIFIED:
                        height=DEFUALT_VIEW_HEIGHT;
                        width=DEFUALT_VIEW_WIDTH;
                        break;
                }
                break;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius==0){
            radius= (Math.min(getWidth()-getPaddingLeft()-getPaddingRight(),getHeight())-getPaddingTop()-getPaddingBottom())/2;
                centerX=getPaddingLeft()+(getWidth()-getPaddingLeft()-getPaddingRight())/2;
                centerY=getPaddingTop()+(getHeight()-getPaddingTop()-getPaddingBottom())/2;
        }
        //先画所有扇形
        drawDataArc(canvas);
        if (isFirstDraw){
            if (onSelectionListener!=null){
                onSelectionListener.onSelect(selectPosition);
            }
            isFirstDraw=false;
        }
        //画中间的圆形
        canvas.drawCircle(centerX,centerY,radius/2.4f,circlePaint);
        //画Textf
        textPaint.setTextSize(radius/4.2f);
        textPaint.setColor(Color.WHITE);
        canvas.drawText(trimFloat(pieRotateViewModelList.get(selectPosition).getNum()/sum)+"%",centerX,centerY-getTextOffset(textPaint,"20%"),textPaint);
        //画指针
        float arrowRadius=radius/4f;
        canvas.save();
        Path area=new Path();
        area.addCircle(centerX,centerY,radius, Path.Direction.CCW);
        canvas.clipPath(area);
        Path arrowPath=new Path();
        arrowPath.moveTo(centerX,centerY+radius-arrowRadius);
        arrowPath.lineTo(centerX-arrowRadius/2,centerY+radius);
        arrowPath.lineTo(centerX+arrowRadius/2,centerY+radius);
        arrowPath.close();
        canvas.drawPath(arrowPath,circlePaint);
        canvas.restore();
    }
    public float getTextOffset(Paint paint, String text){
        Rect bounds=new Rect();
        paint.getTextBounds(text,0,text.length(),bounds);
        float offset=(bounds.top+bounds.bottom)/2;
        return offset;
    }
    private void drawDataArc(Canvas canvas) {
        if (pieRotateViewModelList!=null&&pieRotateViewModelList.size()>0){
            float hasDregee=0;
            for (int i=0;i<pieRotateViewModelList.size();i++){
                piePaint.setColor(pieRotateViewModelList.get(i).getColor());
                switch (i){
                    case 0:
                        hasDregee=90f-pieRotateViewModelList.get(i).getNum()/sum*360f/2f;
                        break;
                    default:
                        hasDregee=hasDregee+  pieRotateViewModelList.get(i-1).getNum()/sum*360f  ;
                }
                pieRotateViewModelList.get(i).setStartDegree(hasDregee);
                pieRotateViewModelList.get(i).setEndDegree(pieRotateViewModelList.get(i).getNum()/sum*360f);
                canvas.drawArc(new RectF(centerX-radius, centerY-radius,
                                centerX+radius, centerY+radius),
                        pieRotateViewModelList.get(i).getStartDegree(), pieRotateViewModelList.get(i).getEndDegree(), true, piePaint);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
    /**获得相对于X轴正方向的夹角*/

    public float degree(float x, float y){
        float detaX = x - centerX;
        float detaY = y - centerY;
        float degree=0;
        if (detaX==0&&detaY!=0){
            //在Y轴上
            if (detaY>0){
                degree=90;
            }else{
                degree=270;
            }
        }else if(detaY==0&&detaX!=0){
            //在X轴上
            if (detaX>0){
                degree=0;
            }else{
                degree=180;
            }
        }else if (detaX>0&&detaY>0){
            //第一象限内
            float tan = Math.abs(detaY / detaX);
            degree=(float)Math.toDegrees(Math.atan(tan));
        }else if (detaX<0&&detaY>0){
            //第二象限内
            float tan = Math.abs(detaX / detaY);
            degree=90+(float)Math.toDegrees(Math.atan(tan));
        }else if (detaX<0&&detaY<0){
            //第三象限内
            float tan = Math.abs(detaY / detaX);
            degree=180+(float)Math.toDegrees(Math.atan(tan));
        }else if (detaX>0&&detaY<0){
            //第四象限内
            float tan = Math.abs(detaX / detaY);
            degree=270+(float)Math.toDegrees(Math.atan(tan));
        }else{
            //在原点位置
            degree=0;
        }
        return degree;
    }

    public static int trimFloat(float value) {
        DecimalFormat df   =new   DecimalFormat("#.00");
        return (int)(Float.parseFloat(df.format(value))*100f);

    }
    public interface onSelectionListener{
        void onSelect(int id);
    }
}
