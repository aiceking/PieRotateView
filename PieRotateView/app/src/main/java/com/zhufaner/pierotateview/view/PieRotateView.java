package com.zhufaner.pierotateview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;


import com.zhufaner.pierotateview.bean.PieRotateBean;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PieRotateView extends View {
    private Paint piePaint,circlePaint,textPaint,OutSidePaint;
    private float left_x,Textradius;
    private float rotate_degrees,rotate_degrees1,rotate_degrees2,hasrotate_degrees;
    private float down_x,down_y,move_x,move_y;
    private float center_x,center_y;
    private float needrotateDegree, average_degree,onclickrotateDegree;
    private PieRotateBean pieRotate;
    private Timer timer;
    private TimerTask task;
    private int i,selectPosition;
    private boolean flag;
    private boolean isVelocityRotate;
    private boolean isFirstDraw;
    private onSelectionListener onSelectionListener;
    private VelocityTracker mVelocityTracker;
    private float 	velocity,velocity_abs;
    private ValueAnimator mAnimator;
    public void setOnSelectionListener(PieRotateView.onSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    if (i<100){
                        rotate_degrees=rotate_degrees-average_degree;
                        hasrotate_degrees=hasrotate_degrees-average_degree;
                        invalidate();
                    }
                    i++;
                    if (i==100){
                        flag=true;
                        timer.cancel();
                        task.cancel();
                    }
                    break;
                case 20:
                    if (i<100){
                        rotate_degrees=rotate_degrees-average_degree;
                        hasrotate_degrees=hasrotate_degrees-average_degree;
                        invalidate();
                    }
                    i++;
                    if (i==100){
                        flag=true;
                        timer.cancel();
                        task.cancel();
                    }
                    break;

            }

        }
    };
    public void setPieRotate(PieRotateBean pieRotate) {
        this.pieRotate = pieRotate;
        invalidate();
    }

    public PieRotateView(Context context) {
        this(context, null);
    }
    public PieRotateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public PieRotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        OutSidePaint=new Paint();
        OutSidePaint.setAntiAlias(true);
        OutSidePaint.setColor(Color.parseColor("#30000000"));
        piePaint=new Paint();
        piePaint.setAntiAlias(true);
        circlePaint=new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#45ffffff"));
        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        rotate_degrees=0;
        down_x=0;down_y=0;
        move_x=0;move_y=0;
        hasrotate_degrees=0;
        needrotateDegree=0;
        average_degree=0;
        onclickrotateDegree=0;
        i=0;
        selectPosition=-1;
        flag=true;
        isFirstDraw=true;
        isVelocityRotate=false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width/5f*3.5f);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        center_x=getWidth()/2f;
        center_y=getHeight()/2f;
        piePaint.setColor(Color.RED);
        //饼的部分
        if (pieRotate!=null){
            if (pieRotate.getTextColor()!=0){
                textPaint.setColor(pieRotate.getTextColor());
            }
            if (pieRotate.isShowOutSide()){
                drawOutSide(canvas);
            }else{
                drawNoOutSide(canvas);
            }
        }
    }
    public void drawOutSide(Canvas canvas){
        float multiple=11.5f;
        Textradius= (center_y-(center_y/multiple)) * 2.8f / 4f;
        textPaint.setTextSize((center_y-(center_y/multiple))/7.6f);
        left_x=(getWidth()-(getHeight()-(center_y/multiple*2f)))/2f;
        canvas.drawCircle(center_x, center_y,center_y,OutSidePaint);
        if (pieRotate.getList_numbers()!=null){
            for (int i=0;i<pieRotate.getList_numbers().size();i++){
                piePaint.setColor(pieRotate.getList_colors().get(i));
                float hasDrgree=0;
                //得到之前部分已经叠加的角度
                for (int j=0;j<i;j++){
                    hasDrgree=hasDrgree+pieRotate.getList_numbers().get(j) / pieRotate.getMax_number() * 360f;
                }
                if (isFirstDraw){
                    if (i==0){
                        hasrotate_degrees=90f-(pieRotate.getList_numbers().get(i) / pieRotate.getMax_number()* 360f/2f);
                        rotate_degrees=90f-(pieRotate.getList_numbers().get(i) / pieRotate.getMax_number()* 360f/2f);
                        onSelectionListener.onSelect(0);
                        isFirstDraw=false;

                    }
                }
                //画扇形
                canvas.drawArc(new RectF(left_x, center_y/multiple, getHeight()-center_y/multiple*2f + left_x, getHeight()-center_y/multiple),
                        hasDrgree+ rotate_degrees, pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 360f, true, piePaint);
                if (selectPosition>=0){
                    if (selectPosition==i){
                        textPaint.setTextSize((center_y-(center_y/multiple))/6.3f);
                        Textradius=(center_y-(center_y/multiple))*3.1f/4f;
                    }else{
                        textPaint.setTextSize((center_y-(center_y/multiple))/7.6f);
                        Textradius=(center_y-(center_y/multiple))*2.8f/4f;
                    }
                }
                if (flag) {
                    //画扇形中的text,先得到对应的点的坐标
                    float a = (hasDrgree + rotate_degrees + pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 360f / 2f + 360f) % 360f;
                    PointF pf = getTextPoint(a);
                    //Log.v("a==",a+"");
                    canvas.drawText(Math.round(pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 100) + "%", pf.x, pf.y, textPaint);
                }
            }
        }
        //中间的圆
        canvas.drawCircle(center_x, center_y, (getHeight()-2f*(center_y/multiple)) / 6f, circlePaint);
        textPaint.setTextSize(getHeight() / 6f/3.8f);
        canvas.drawText("总数:", center_x, center_y - getHeight() / 6f/5.2f,textPaint);
        canvas.drawText(trimFloat(pieRotate.getMax_number()),center_x,center_y+getHeight() / 6f/2.2f,textPaint);
        //下方的小三角
        Path p = new Path();
        p.moveTo(getWidth() / 2f, getHeight()-(center_y/multiple) - getHeight() / 20f);
        p.lineTo(getWidth() / 2f - getHeight() / 20f / 3f * 2f, getHeight()-(center_y/multiple));
        p.lineTo(getWidth() / 2f + getHeight() / 20f / 3f * 2f, getHeight()-(center_y/multiple));
        p.close();
        canvas.drawPath(p, circlePaint);
    }
    public void drawNoOutSide(Canvas canvas){
        Textradius= (center_y-4) * 2.8f / 4f;
        textPaint.setTextSize(center_y/7.8f);
        left_x=(getWidth()-(getHeight()-8))/2f;
        if (pieRotate.getList_numbers()!=null){
            for (int i=0;i<pieRotate.getList_numbers().size();i++){
                piePaint.setColor(pieRotate.getList_colors().get(i));
                float hasDrgree=0;
                //得到之前部分已经叠加的角度
                for (int j=0;j<i;j++){
                    hasDrgree=hasDrgree+pieRotate.getList_numbers().get(j) / pieRotate.getMax_number() * 360f;
                }
                if (isFirstDraw){
                        if (i==0){
                            hasrotate_degrees=90f-(pieRotate.getList_numbers().get(i) / pieRotate.getMax_number()* 360f/2f);
                            rotate_degrees=90f-(pieRotate.getList_numbers().get(i) / pieRotate.getMax_number()* 360f/2f);
                            onSelectionListener.onSelect(0);
                            isFirstDraw=false;

                        }
                }

                //画扇形
                canvas.drawArc(new RectF(left_x, 4, getHeight()-8 + left_x, getHeight()-4),
                        hasDrgree+ rotate_degrees, pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 360f, true, piePaint);
                if (selectPosition>=0){
                    if (selectPosition==i){
                        textPaint.setTextSize(center_y/6.3f);
                        Textradius=(center_y-4)*3.1f/4f;
                    }else{
                        textPaint.setTextSize(center_y/7.8f);
                        Textradius=(center_y-4)*2.8f/4f;
                    }
                }
                if (flag) {
                    //画扇形中的text,先得到对应的点的坐标
                    float a = (hasDrgree + rotate_degrees + pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 360f / 2f + 360f) % 360f;

                    PointF pf = getTextPoint(a);
                    //Log.v("a==",a+"");
                    canvas.drawText(Math.round(pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 100) + "%", pf.x, pf.y, textPaint);
                }
            }
        }
        //中间的圆
        canvas.drawCircle(center_x, center_y, getHeight() / 6f, circlePaint);
        textPaint.setTextSize(getHeight() / 6f/3.8f);
        canvas.drawText("总数:", center_x, center_y - getHeight() / 6f/5.2f,textPaint);
        canvas.drawText(trimFloat(pieRotate.getMax_number()),center_x,center_y+getHeight() / 6f/2.2f,textPaint);
        //下方的小三角
        Path p = new Path();
        p.moveTo(getWidth() / 2f, getHeight() - getHeight() / 19f-6f);
        p.lineTo(getWidth() / 2f - getHeight() / 19f / 3f * 2f, getHeight()-2f);
        p.lineTo(getWidth() / 2f + getHeight() / 19f / 3f * 2f, getHeight()-2f);
        p.close();
        canvas.drawPath(p, circlePaint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                down_x=x;
                down_y=y;
                rotate_degrees1=degree(down_x, down_y);
                if (timer!=null){
                    timer.cancel();
                    task.cancel();
                }
                if (getDistance(down_x, down_y,center_x,center_y) <= getHeight() / 2f) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else{
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                if (isVelocityRotate){
                    if (mAnimator!=null){
                        if (mAnimator.isRunning()){
                            mAnimator.end();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                move_x=x;
                move_y=y;
                rotate_degrees2=degree(move_x,move_y);
                rotate_degrees=hasrotate_degrees+(rotate_degrees2-rotate_degrees1);
                rotate_degrees=(rotate_degrees)%360f;
                if (rotate_degrees<0){
                    rotate_degrees=rotate_degrees+360f;
                }
                //得到当前所指的扇形区域
                getSelectPosition(true);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(x-down_x)<= ViewConfiguration.getTouchSlop()&&Math.abs(y-down_y)<=ViewConfiguration.getTouchSlop()){
                    getSelectPosition(down_x,down_y);
                    RotateForClick();
                }else{
                    hasrotate_degrees = hasrotate_degrees + (rotate_degrees2 - rotate_degrees1);
                    hasrotate_degrees=(hasrotate_degrees)%360f;
                    if (hasrotate_degrees<0){
                        hasrotate_degrees=hasrotate_degrees+360f;
                    }
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    final int pointerId = event.getPointerId(0);
                    velocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
                    final float velocityY = velocityTracker.getYVelocity(pointerId);
                    final float velocityX = velocityTracker.getXVelocity(pointerId);
                    if ((Math.abs(velocityY) > ViewConfiguration.getMinimumFlingVelocity())
                            || (Math.abs(velocityX) > ViewConfiguration.getMinimumFlingVelocity())) {
                        if (Math.max(Math.abs(velocityY),Math.abs(velocityX))>=2000){
                            velocity=Math.abs(velocityY)>Math.abs(velocityX)?velocityY:velocityX;
                            velocity_abs=Math.max(Math.abs(velocityY),Math.abs(velocityX));
                             mAnimator = ValueAnimator.ofFloat(velocity_abs,0);
                            mAnimator.setInterpolator(new LinearInterpolator());
                            mAnimator.setDuration((long)(velocity_abs/4.5f));
                            mAnimator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    isVelocityRotate=true;
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    Log.v("haha=","end");
                                    isVelocityRotate=false;
                                    //需要动画旋转的角度
                                    getSelectPosition(false);
                                    RotateForMove();
                                }
                            });
                            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float a=(float)animation.getAnimatedValue();
                                    float aa=(float)(Math.sin( Math.PI*0.832f * ((Math.abs(velocity)-a)/Math.abs(velocity)+0.2f)));
                                    float average=0;
                                    if (velocity==velocityY) {
                                        if (x > center_x) {
                                            average = -aa * velocity / 300f;
                                        } else {
                                            average = aa * velocity / 300f;
                                        }
                                    }else {
                                        if (y>center_y){
                                            average = aa * velocity / 300f;
                                        }else {
                                            average = -aa * velocity / 300f;
                                        }
                                    }
                                    rotate_degrees=rotate_degrees-average;
                                    hasrotate_degrees=hasrotate_degrees-average;
                                    rotate_degrees=(rotate_degrees)%360f;
                                    if (rotate_degrees<0){
                                        rotate_degrees=rotate_degrees+360f;
                                    }
                                    hasrotate_degrees=(hasrotate_degrees)%360f;
                                    if (hasrotate_degrees<0){
                                        hasrotate_degrees=hasrotate_degrees+360f;
                                    }
                                    getSelectPosition(true);
                                    invalidate();
                                    Log.v("a",aa+"");

                                }
                            });
                            mAnimator.start();
                        }else {
                            //需要动画旋转的角度
                            getSelectPosition(false);
                            RotateForMove();
                        }
                    }else {
                        //需要动画旋转的角度
                        getSelectPosition(false);
                        RotateForMove();
                    }

                }
                break;
        }
        return true;
    }
    private float getDistance(float x,float y,float x2,float y2){
        float xx = x - x2;
        float yy = y - y2;
        return (float)Math.sqrt(xx * xx+ yy * yy);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
        if (mVelocityTracker!=null){
        mVelocityTracker.recycle();
        }
        if (timer!=null){
            timer.cancel();
            task.cancel();
        }
        if (mAnimator!=null){
            mAnimator.cancel();
        }
    }

    public void RotateForMove(){
        i=0;
        average_degree = (needrotateDegree-90f)/100f;
        if (timer!=null){
            timer.cancel();
            task.cancel();
        }
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(10);
            }
        };
        timer.schedule(task, new Date(), 2);
    }
    public void RotateForClick(){
        i=0;
        if (onclickrotateDegree>270f){
            onclickrotateDegree=onclickrotateDegree-360f;
        }
        average_degree = (onclickrotateDegree-90f)/100f;
        if (timer!=null){
            timer.cancel();
            task.cancel();
        }
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(20);
            }
        };
        timer.schedule(task, new Date(), 3);
    }
    /**得到当前所指的扇形区域和需要动画旋转的角度*/
    public void getSelectPosition(float x, float y){
        if (pieRotate!=null){
            flag=pieRotate.isShowTextonMove();
            onclickrotateDegree=0;
            if (pieRotate.getList_numbers()!=null) {
                for (int i = 0; i < pieRotate.getList_numbers().size(); i++) {
                    float hasDrgree = 0;
                    //得到之前部分已经叠加的角度
                    for (int j = 0; j < i; j++) {
                        hasDrgree = hasDrgree + pieRotate.getList_numbers().get(j) / pieRotate.getMax_number() * 360f;
                    }
                    float a = pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 360f / 2f;
                    float b = (hasDrgree + rotate_degrees + pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 360f / 2f + 360f) % 360f;
                    PointF pointF=getTextPoint(b);
                    float line_c=getDistance(x, y, center_x, center_y);
                    float line_b=getDistance(pointF.x,pointF.y,center_x,center_y);
                    float line_a=getDistance(x,y,pointF.x,pointF.y);
                    float cosA=(line_b*line_b+line_c*line_c-line_a*line_a)/(2*line_b*line_c);
                    float degree= (float) Math.toDegrees(Math.acos(cosA));
                    if (degree<=a){
                        selectPosition=i;
                        onclickrotateDegree=b;
                        onSelectionListener.onSelect(selectPosition);
                        break;
                    }
                }
            }
        }
    }

    public void getSelectPosition(boolean isMove){
        if (pieRotate!=null){
            needrotateDegree=0;
            flag=pieRotate.isShowTextonMove();
            if (pieRotate.getList_numbers()!=null){
                for (int i=0;i<pieRotate.getList_numbers().size();i++){
                    float hasDrgree=0;
                    //得到之前部分已经叠加的角度
                    for (int j=0;j<i;j++){
                        hasDrgree=hasDrgree+pieRotate.getList_numbers().get(j) / pieRotate.getMax_number() * 360f;
                    }
                    float a=pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 360f / 2f;
                    float b=(hasDrgree + rotate_degrees + pieRotate.getList_numbers().get(i) / pieRotate.getMax_number() * 360f / 2f+360f) % 360f;
                    if(b>=270f){
                        if (b+a-360f-90f>0){
                            if (isMove){
                                selectPosition=i;
                                onSelectionListener.onSelect(selectPosition);
                            }else{
                                needrotateDegree=b-360f;
                            }
                            break;
                        }
                    }else{
                        if (Math.abs(b-90f)<a){
                            if (isMove){
                                selectPosition=i;
                                onSelectionListener.onSelect(selectPosition);
                            }else{
                                needrotateDegree=b;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    /**获得相对于X轴正方向的夹角*/

    public float degree(float x, float y){
        float detaX = x - center_x;
        float detaY = y - center_y;
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
    /** 获得对应角度的文字的坐标*/
    /** 传入角度需提前修正，必须大于0且小于360*/
    public PointF getTextPoint(float degree) {
        PointF p=new PointF();
        if (degree<0){
            degree=degree+360f;
        }
        if (degree%90==0){
            switch ((int)degree){
                case 0:
                case 360:
                    p.x=center_x+Textradius;
                    p.y=center_y;
                    break;
                case 90:
                    p.x=center_x;
                    p.y=center_y+Textradius;
                    break;
                case 180:
                    p.x=center_x-Textradius;
                    p.y=center_y;
                    break;
                case 270:
                    p.x=center_x;
                    p.y=center_y-Textradius;
                    break;
            }
        }else{
            switch ((int)degree/90){
                //第一象限内
                case 0:
                    p.x=center_x+(Textradius*(float)Math.cos(Math.toRadians(degree)));
                    p.y=center_y+(Textradius*(float) Math.sin(Math.toRadians(degree)));
                    break;
                //第二象限内
                case 1:
                    p.x=center_x-(Textradius*(float)Math.sin(Math.toRadians(degree-90)));
                    p.y=center_y+(Textradius*(float)Math.cos(Math.toRadians(degree-90)));
                    break;
                //第三象限内
                case 2:
                    p.x=center_x-(Textradius*(float)Math.cos(Math.toRadians(degree-180)));
                    p.y=center_y-(Textradius*(float)Math.sin(Math.toRadians(degree-180)));
                    break;
                //第四象限内
                case 3:
                    p.x=center_x+Textradius*(float)Math.sin(Math.toRadians(degree-270));
                    p.y=center_y-Textradius*(float)Math.cos(Math.toRadians(degree-270));
                    break;
            }
        }
        return p;
    }
    /**
     * float类型如果小数点后为零则显示整数否则保留
     */
    public static String trimFloat(float value) {
        if (Math.round(value) - value == 0) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);

    }
    public interface onSelectionListener{
        void onSelect(int id);
    }
}

