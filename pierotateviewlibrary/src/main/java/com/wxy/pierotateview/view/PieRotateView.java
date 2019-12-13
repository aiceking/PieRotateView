package com.wxy.pierotateview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.wxy.pierotateview.model.PieRotateViewModel;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.Nullable;

public class PieRotateView extends View  {
    private String TAG="PieRotateView";
    private Paint piePaint,circlePaint,textPaint,arrawPaint;
    protected static final int DEFUALT_VIEW_WIDTH=400;
    protected static final int DEFUALT_VIEW_HEIGHT=400;
    private float radius,centerX,centerY;
    private int selectPosition;
    private boolean isClick;
    private float downX,downY;
    private float lastRotateDregee,rotateDregee,moveRotateDregee;
    private float sum;
    private int mActivePointerId;
    private  final int INVALID_POINTER = -1;
    protected VelocityTracker mVelocityTracker;
    private ValueAnimator recoverAnim;//回弹动画
    private  float recoverStartValue;
    private ValueAnimator flingAnim;//回弹动画
    private  float flingStartValue;
    private boolean isRecover;

    public void setEnableTouch(boolean enableTouch) {
        this.enableTouch = enableTouch;
    }

    private boolean enableTouch;
    public void setOnPromiseParentTouchListener(PieRotateView.onPromiseParentTouchListener onPromiseParentTouchListener) {
        this.onPromiseParentTouchListener = onPromiseParentTouchListener;
    }

    private onPromiseParentTouchListener onPromiseParentTouchListener;

    public boolean isFling() {
        return isFling;
    }

    private boolean isFling;
    private int flingDirection;//fling旋转的方向：1顺时针,2逆时针
    //是否可以惯性转动
    public void setFling(boolean fling) {
        isFling = fling;
    }

    public void setRecoverTime(int recoverTime) {
        this.recoverTime = recoverTime;
    }

    private int recoverTime;
    public void setCircleColor(int circleColor) {
        circlePaint.setColor(circleColor);
    }
    public void setTextColor(int textColor) {
        textPaint.setColor(textColor);
    }
    public void setArrawColor(int arrawColor) {
        arrawPaint.setColor(arrawColor);
    }

    public void setOnSelectionListener(PieRotateView.onSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }
    //刷新view
    public void notifyDataChanged(){
        if (recoverAnim!=null){
            recoverAnim.cancel();
        }
        if (flingAnim!=null){
            flingAnim.cancel();
        }
        isRecover=false;
        isClick=false;
        sum=0;
        moveRotateDregee=0;
        for (PieRotateViewModel pieRotateViewModel:pieRotateViewModelList){
            sum+=pieRotateViewModel.getNum();
        }
        selectPosition=-1;
        invalidate();
    }
    //刷新view
    public void notifySettingChanged(){
        invalidate();
    }
    private onSelectionListener onSelectionListener;
    private List<PieRotateViewModel> pieRotateViewModelList;
    public void setPieRotateViewModelList(List<PieRotateViewModel> pieRotateViewModelList) {
        if (recoverAnim!=null){
            recoverAnim.cancel();
        }
        if (flingAnim!=null){
            flingAnim.cancel();
        }
        isRecover=false;
        isClick=false;
        sum=0;
        moveRotateDregee=0;
        this.pieRotateViewModelList = pieRotateViewModelList;
        for (PieRotateViewModel pieRotateViewModel:pieRotateViewModelList){
            sum+=pieRotateViewModel.getNum();
        }
        selectPosition=-1;
        invalidate();
    }
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putFloat("moveRotateDregee",moveRotateDregee);
        bundle.putInt("circleColor",circlePaint.getColor());
        bundle.putInt("arrawColor",arrawPaint.getColor());
        bundle.putInt("textColor",textPaint.getColor());
        bundle.putInt("recoverTime",recoverTime);
        bundle.putBoolean("isFling",isFling);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("superState");
            moveRotateDregee=bundle.getFloat("moveRotateDregee");
            circlePaint.setColor(bundle.getInt("circleColor"));
            arrawPaint.setColor(bundle.getInt("arrawColor"));
            textPaint.setColor(bundle.getInt("textColor"));
            recoverTime=bundle.getInt("recoverTime");
            isFling=bundle.getBoolean("isFling");
            super.onRestoreInstanceState(state);
        }
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
        arrawPaint=new Paint();
        arrawPaint.setAntiAlias(true);
        arrawPaint.setColor(Color.parseColor("#75ffffff"));
        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        recoverTime=300;
        enableTouch=true;
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
        if (pieRotateViewModelList==null||pieRotateViewModelList.size()==0){
            Log.v(TAG,"pieRotateViewModelList为空");
            return;
        }
        if (pieRotateViewModelList.size()==1){
            Log.v(TAG,"pieRotateViewModelList的size不能为1");
            return;
        }
        if (radius==0){
            radius= (Math.min(getWidth()-getPaddingLeft()-getPaddingRight(),getHeight())-getPaddingTop()-getPaddingBottom())/2;
                centerX=getPaddingLeft()+(getWidth()-getPaddingLeft()-getPaddingRight())/2;
                centerY=getPaddingTop()+(getHeight()-getPaddingTop()-getPaddingBottom())/2;
        }
        //先画所有扇形
        drawDataArc(canvas);
        //画中间的圆形
        canvas.drawCircle(centerX,centerY,radius/2.4f,circlePaint);
        //画Text
        textPaint.setTextSize(radius/4.2f);
        canvas.drawText(pieRotateViewModelList.get(selectPosition).getPercent(),centerX,centerY-getTextOffset(textPaint,"20%"),textPaint);
        //画指针
        float arrowRadius=radius/5f;
        canvas.save();
        Path area=new Path();
        area.addCircle(centerX,centerY,radius, Path.Direction.CCW);
        canvas.clipPath(area);
        Path arrowPath=new Path();
        arrowPath.moveTo(centerX,centerY+radius-arrowRadius);
        arrowPath.lineTo(centerX-arrowRadius/2,centerY+radius);
        arrowPath.lineTo(centerX+arrowRadius/2,centerY+radius);
        arrowPath.close();
        canvas.drawPath(arrowPath,arrawPaint);
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
                pieRotateViewModelList.get(i).setPercent(trimFloat(pieRotateViewModelList.get(i).getNum()/sum)+"%");
                pieRotateViewModelList.get(i).setCenterDregee(getDregee(hasDregee+moveRotateDregee+(pieRotateViewModelList.get(i).getNum()/sum*360f/2f)));
                pieRotateViewModelList.get(i).setSelfDregee(pieRotateViewModelList.get(i).getNum()/sum*360f);
                if (pieRotateViewModelList.get(i).getPath()!=null){
                    Path path=pieRotateViewModelList.get(i).getPath();
                    path.reset();
                    path.moveTo(centerX,centerY);
                    path.arcTo(new RectF(centerX-radius, centerY-radius,
                            centerX+radius, centerY+radius),hasDregee+moveRotateDregee, pieRotateViewModelList.get(i).getSelfDregee());

                }else {
                    Path path=new Path();
                    path.moveTo(centerX,centerY);
                    path.arcTo(new RectF(centerX-radius, centerY-radius,
                            centerX+radius, centerY+radius),hasDregee+moveRotateDregee, pieRotateViewModelList.get(i).getSelfDregee());
                    pieRotateViewModelList.get(i).setPath(path);
                }
                canvas.drawPath(pieRotateViewModelList.get(i).getPath(),piePaint);
                //不是点击事件就是move或者Fling事件
                if (!isClick){
                Region re=new Region();
                RectF rf=new RectF();
                pieRotateViewModelList.get(i).getPath().computeBounds(rf,true);
                re.setPath(pieRotateViewModelList.get(i).getPath(),new Region((int)rf.left,(int)rf.top,(int)rf.right,(int)rf.bottom));
                if (re.contains((int) centerX,(int)(centerY+radius/2))){
                    if (selectPosition!=i){
                    selectPosition=i;
                    if (onSelectionListener!=null){
                        onSelectionListener.onSelect(selectPosition,pieRotateViewModelList.get(selectPosition).getPercent());
                    }
                    }
                    //这是恢复到指针中心位置
                    if (isRecover){
                        isRecover=false;
                        if (pieRotateViewModelList.get(selectPosition).getCenterDregee()>270f){
                            float degree=360f-pieRotateViewModelList.get(selectPosition).getCenterDregee()+90f;
                            recover(0,degree);
                        }else if (pieRotateViewModelList.get(selectPosition).getCenterDregee()<90f){
                            float degree=90f-pieRotateViewModelList.get(selectPosition).getCenterDregee();
                            recover(0,degree);
                        }else {
                            recover(pieRotateViewModelList.get(selectPosition).getCenterDregee()-90f,0);
                        }
                    }
                }
                }

            }

        }
    }
private float getDregee(float dregee){
        return dregee<0?(dregee+360f)%360f:dregee%360f;
}

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (enableTouch){
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            if (getDistance(event.getX(),event.getY(),centerX,centerY)>radius){
                getParent().requestDisallowInterceptTouchEvent(false);
                if (onPromiseParentTouchListener!=null){
                    onPromiseParentTouchListener.onPromiseTouch(true);
                }
                return false;
            }else {
                if (onPromiseParentTouchListener!=null){
                    onPromiseParentTouchListener.onPromiseTouch(false);
                }
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enableTouch){
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()& MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (recoverAnim!=null){
                    recoverAnim.cancel();
                }
                if (flingAnim!=null){
                    flingAnim.cancel();
                }
                isRecover=false;
                isClick=false;
                mActivePointerId=event.getPointerId(0);
                downX= event.getX();
                downY= event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //如果有新的手指按下，就直接把它当作当前活跃的指针
                final int index = event.getActionIndex();
                mActivePointerId = event.getPointerId(index);
                //并且刷新上一次记录的旧坐标值
                downX=(int) event.getX(index);
                downY=(int) event.getY(index);
                break;
            case MotionEvent.ACTION_MOVE:
                int activePointerIndex = event.findPointerIndex(mActivePointerId);
                if (activePointerIndex == INVALID_POINTER) {
                    break;
                }
                rotateDregee=degree(event.getX(activePointerIndex),event.getY(activePointerIndex))-lastRotateDregee;
                float a=moveRotateDregee;
                moveRotateDregee=getDregee(moveRotateDregee+rotateDregee);
                //判断旋转的方向
                if (a<moveRotateDregee){
                    flingDirection=1;
                }else {
                    flingDirection=2;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (onPromiseParentTouchListener!=null){
                    onPromiseParentTouchListener.onPromiseTouch(true);
                }
                mActivePointerId = INVALID_POINTER;
                float clickX = downX - event.getX();
                float clickY = downY -  event.getY();
                if (Math.abs(clickX)<= ViewConfiguration.get(getContext()).getScaledTouchSlop()&&
                        Math.abs(clickY)<= ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    isClick=true;
                    isRecover=true;
                    selectRecover((int) event.getX(),(int)event.getY());
                }else {
                    if (isFling){
                        int pointerId = event.getPointerId(0);
                        mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
                        float velocityX = mVelocityTracker.getXVelocity(pointerId);
                        float velocityY = mVelocityTracker.getYVelocity(pointerId);
                        double velocityLinear=Math.sqrt(Double.parseDouble(String.valueOf(velocityX*velocityX+""))+Double.parseDouble(String.valueOf(velocityY*velocityY+"")));
                        if (velocityLinear>1000){
                            if (velocityLinear>5000){
                                dealFling(velocityLinear/5.2,(int) (velocityLinear/4.2));
                            }else {
                                dealFling(velocityLinear/9,(int) (velocityLinear/4.6));
                            }
                        }
                        else {
                            isRecover=true;
                            invalidate();
                        }

                    }else {
                        isRecover=true;
                        invalidate();
                    }


                }
                break;
        }
        if (mActivePointerId != INVALID_POINTER) {
            lastRotateDregee = degree(event.getX(event.findPointerIndex(mActivePointerId)),event.getY(event.findPointerIndex(mActivePointerId)));
        }else {
            lastRotateDregee = degree(event.getX(),event.getY()) ;
        }
        return true;
        }else {
            return super.onTouchEvent(event);
        }
    }
    private float getDistance(float x,float y,float x2,float y2){
        float xx = x - x2;
        float yy = y - y2;
        return (float)Math.sqrt(xx * xx+ yy * yy);
    }

    //处理惯性滚动事件
    private void dealFling(double velocityLinear,int time) {
        if (flingDirection==2){
            flingStartValue= (float) velocityLinear;
        }else {
            flingStartValue=0;
        }
        if (flingAnim==null) {
            flingAnim = new ValueAnimator();
            flingAnim.setInterpolator(new DecelerateInterpolator());
            flingAnim.setDuration(time);
            if (flingDirection==2){
                flingAnim.setFloatValues((float)velocityLinear, 0);
            }else {
                flingAnim.setFloatValues(0, (float)velocityLinear);
            }
            flingAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentValue = (float) animation.getAnimatedValue()-flingStartValue;
                    flingStartValue=(float) animation.getAnimatedValue();
                    moveRotateDregee=getDregee(moveRotateDregee+currentValue);
                    invalidate();
                }
            });
            flingAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isRecover=true;
                    invalidate();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            flingAnim.start();
        }else {
            if (!flingAnim.isRunning()){
                flingAnim.setDuration(time);
                if (flingDirection==2){
                    flingAnim.setFloatValues((float)velocityLinear, 0);
                }else {
                    flingAnim.setFloatValues(0, (float)velocityLinear);
                }
                flingAnim.start();
            }
        }
    }

    //点击事件旋转到点击所选的区域
    private void selectRecover(int x,int y){
        for (int i=0;i<pieRotateViewModelList.size();i++){
            Region re=new Region();
            RectF rf=new RectF();
            pieRotateViewModelList.get(i).getPath().computeBounds(rf,true);
            re.setPath(pieRotateViewModelList.get(i).getPath(),new Region((int)rf.left,(int)rf.top,(int)rf.right,(int)rf.bottom));
            if (re.contains(x,y)){
                selectPosition=i;
                if (onSelectionListener!=null){
                    onSelectionListener.onSelect(selectPosition,pieRotateViewModelList.get(selectPosition).getPercent());
                }
                    if (pieRotateViewModelList.get(i).getCenterDregee()>270f){
                        float degree=360f-pieRotateViewModelList.get(i).getCenterDregee()+90f;
                        recover(0,degree);
                    }else if (pieRotateViewModelList.get(i).getCenterDregee()<90f){
                        float degree=90f-pieRotateViewModelList.get(i).getCenterDregee();
                        recover(0,degree);
                    }else {
                        recover(pieRotateViewModelList.get(i).getCenterDregee()-90f,0);
                }
                return;
            }
        }
    }
    private void recover(final float start, final float end){
        if (start>end){
            recoverStartValue=start;
        }else {
            recoverStartValue=0;
        }
        if (recoverAnim==null) {
            recoverAnim = new ValueAnimator();
            recoverAnim.setInterpolator(new DecelerateInterpolator());
            recoverAnim.setDuration(recoverTime);
            recoverAnim.setFloatValues(start, end);
            recoverAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentValue = (float) animation.getAnimatedValue()-recoverStartValue;
                        recoverStartValue=(float) animation.getAnimatedValue();
                    moveRotateDregee=getDregee(moveRotateDregee+currentValue);
                    invalidate();
                }
            });
            recoverAnim.start();
        }else {
            if (!recoverAnim.isRunning()){
                recoverAnim.setDuration(recoverTime);
                recoverAnim.setFloatValues(start, end);
                recoverAnim.start();
            }
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);
        //如果抬起的那根手指，刚好是当前活跃的手指，那么
        if (pointerId == mActivePointerId) {
            //另选一根手指，并把它标记为活跃
            int newPointerIndex =  pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
            //把上一次记录的坐标，更新为新手指的当前坐标
            downX = (int) ev.getX(newPointerIndex);
            downY =(int)  ev.getY(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }
    /**获得相对于X轴正方向的夹角*/

    private float degree(float x, float y){
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
        void onSelect(int position,String percent);
    }
    public interface onPromiseParentTouchListener{
        void onPromiseTouch(boolean promise);
    }
}
