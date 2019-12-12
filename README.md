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
# 示例，Demo演示了普通用法和涉及到SwipeRefreshLayout+AppBarLayout等类似的滑动冲突的用法（细节请看代码）。
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
* [使用](#常规的使用还是很简单的)
    * [引入](#引入)
    * [布局XML中添加](#布局XML中添加)
    * [代码中设置Data和属性](#设置Data和属性)
