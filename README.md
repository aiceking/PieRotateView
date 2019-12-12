# PieRotateView
**View-Load-ReTry**：这是一个可旋转，自动回弹，可惯性旋转的饼状图，多属性控制，使用简单，具备一个自定义View应有的基本素质
 
* **原理** ：自定义坐标象限，Move事件中不断叠加划过的角度，重绘arcPath，判断滑动或者点击到某个区块使用Region。
* **功能** ：
   * 1、可跟随手势旋转、旋转完自动回弹到区块中心。
   * 2、惯性旋转+旋转完后回弹到区块中心。
   * 3、点击某个区块自动旋转至该区块中心。
   * 4、指针箭头指到某个区块内发送区块select事件。
   * 5、提供了刷新入口(设置属性刷新、Data改变刷新)
* **基本素质** ：
   * 1、旋转屏幕状态不丢失，转之前啥样，回来还是啥样（处理好）
   * 2、暴露事件冲突接口，允许外界操作父控件的事件及该view自己的事件（因为这只是个View，没办法直接处理所有的滑动冲突场景）
   * 3、内存抖动要小，防止内存溢出。
-------------------
# 示例
## Demo演示了普通用法和涉及到SwipeRefreshLayout+AppBarLayout等类似的滑动冲突的用法（细节请看代码）。
* **1、普通使用** ：没啥可说的，只有Down的点在圆的范围内才可以响应事件，否则通知父控件拦截
* **2、下拉刷新及其他滑动冲突** ：
   * 1、Down的点在圆内，通过onPromiseParentTouchListener方法中使用SwipeRefreshLayout.setEnabled(promise)通知外界设置SwipeRefreshLayout不可以滑动。
   * 2、在SwipeRefreshLayout的OnRefreshListener中设置PierotateView的setEnableTouch(false)方法通知刷新期间，PierotateView不响应任何事件。
   * 3、同理，监听AppBarLayout的滚动高度来控制只有展开才允许SwipeRefreshLayout下拉刷新和PierotateView旋转,否则都禁止
   
| 常规使用      |下拉刷新及其他滑动冲突  |
| :--------:| :--------:|  
|![normal](https://github.com/AndroidCloud/PieRotateView/blob/master/DemoImg/demo1.gif)| ![fix](https://github.com/AndroidCloud/PieRotateView/blob/master/DemoImg/demo2.gif)| 
 <br />

# 使用  
* [基本API](#基本API)
* [使用](#使用)
    * [引入](#引入)
    * [布局XML中添加](#布局XML中添加与系统View使用方式一样宽高确定其一另一个取其相同值且圆的圆心由padding后的View中心圆的半径为宽高中的较小值和对应的padding决定)
    * [代码中设置Data和属性](#代码中设置Data和属性Demo中的SwipRefreshAppbarActivity和NormalActivity中有详细使用代码)

# 基本API
## Data实例类 PieRotateViewModel，以下为使用期间会接触到的属性，前三个属性用于构造PieRotateViewModel，别的属性都是为绘制准备的，不用关心，也不用去设置。

|属性  | 作用  |
| :--------| :--: |
| name| 表示该扇形区块的名字|
| num| 表示该扇形区块的数字（决定它的百分比占比，总数为所有的PieRotateViewModel num叠加）| 
| color| 表示该扇形区块的颜色| 
| percent| 表示该扇形区块所占的百分比| 

## PieRotateView

|方法  |参数  | 作用  |
| :--------| :--------| :--: |
|setEnableTouch  |boolean  | 设置是否可以消费事件（默认为true）  |
|setPieRotateViewModelList  |List<PieRotateViewModel>  | 设置Data  |
|setFling  |boolean  | 设置是否可以惯性滑动  |
|setRecoverTime  |int  | 设置回弹动画的时长  |
|setCircleColor  |int  | 设置中间遮罩圆的颜色  | 
|setTextColor  |int  | 设置中间Text文本的颜色  | 
|setArrawColor  |int  | 设置下方指针箭头的颜色  | 
|setOnSelectionListener  |PieRotateView.onSelectionListener  | 指针指向某个区块以及点击事件指向的区块事件回调  | 
|setOnPromiseParentTouchListener  |PieRotateView.onPromiseParentTouchListener  | 通知外界是否允许pierotateview之上的view拦截事件 | 
|notifyDataChangeChanged  |void  | 通知pierotateview数据源发生了改变，重置相关初始属性，刷新View | 
|notifySettingChanged  |void  | 通知pierotateview某些设置发生了改变，刷新View | 
 
# 使用
## 引入
Step 1. Add it in your root build.gradle at the end of repositories：

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.NoEndToLF:PieRotateView:1.0.2'
	}
 
## 布局XML中添加，与系统View使用方式一样，宽高确定其一，另一个取其相同值，且圆的圆心由padding后的View中心，圆的半径为宽高中的较小值和对应的padding决定
 
 ``` java
 <com.wxy.pierotateview.view.PieRotateView
                android:layout_width="match_parent"
                android:padding="20dp"
                android:id="@+id/pie"
                android:layout_height="wrap_content"></com.wxy.pierotateview.view.PieRotateView>

 ```
## 代码中设置Data和属性，Demo中的SwipRefreshAppbarActivity和NormalActivity中有详细使用代码
### 设置Data

``` java
        List<PieRotateViewModel> list = new ArrayList<>();
        list.add(new PieRotateViewModel("红扇", 20, getResources().getColor(R.color.colorAccent)));
        list.add(new PieRotateViewModel("绿扇", 10, getResources().getColor(R.color.colorPrimaryDark)));
        list.add(new PieRotateViewModel("蓝扇", 10, Color.BLUE));
        list.add(new PieRotateViewModel("黄扇", 60, Color.parseColor("#FF7F00")));
        list.add(new PieRotateViewModel("粉扇", 50, Color.parseColor("#EE7AE9")));
        pie.setPieRotateViewModelList(list);
```
	
### 刷新Data，可以重新使用setPieRotateViewModelList，或者重置list后调用pie.notifyDataChangeChanged();

``` java
swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pie.setEnableTouch(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        list.add(new PieRotateViewModel("红扇", 20, getResources().getColor(R.color.colorAccent)));
                        list.add(new PieRotateViewModel("绿扇", 10, getResources().getColor(R.color.colorPrimaryDark)));
                        pie.notifyDataChangeChanged();
                        swipe.setRefreshing(false);
                        pie.setEnableTouch(true);
                    }
                },2000);
            }
        });	
```
### 添加区块选中回调;
``` java
pie.setOnSelectionListener(new PieRotateView.onSelectionListener() {
            @Override
            public void onSelect(int position, String percent) {
                tvName.setText(list.get(position).getName() + "数额：" + list.get(position).getNum() + " 占比：" + percent);
                tvName.setTextColor(list.get(position).getColor());
            }
        });
```
### 添加事件处理回调，即通知外界是否要拦截事件;
``` java
pie.setOnPromiseParentTouchListener(new PieRotateView.onPromiseParentTouchListener() {
            @Override
            public void onPromiseTouch(boolean promise) {
                swipe.setEnabled(promise);
            }
        });
```
### 修改pieratateview的其他属性，如果设置属性在setPieRotateViewModelList之前则不需要调用notifySettingChanged()，因为setPieRotateViewModelList会让View重绘，属性自然会生效，反之设置下方的属性后，需要再调用notifySettingChanged()通知View刷新,

|方法  |参数  | 作用  |
| :--------| :--------| :--: |
|setFling  |boolean  | 设置是否可以惯性滑动  |
|setRecoverTime  |int  | 设置回弹动画的时长  |
|setCircleColor  |int  | 设置中间遮罩圆的颜色  | 
|setTextColor  |int  | 设置中间Text文本的颜色  | 
|setArrawColor  |int  | 设置下方指针箭头的颜色  | 



