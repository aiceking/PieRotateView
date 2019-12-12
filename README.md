# PieRotateView
**View-Load-ReTry**：这是一个可旋转，自动回弹，可惯性旋转的饼状图，多属性控制，使用简单，具备一个自定义View应有的基本素质
 
- **原理** ：自定义坐标象限，Move事件中不断叠加划过的角度，重绘arcPath，判断滑动或者点击到某个区块使用Region。
- **功能** ：'可旋转、旋转完自动回弹到区块中心'、*惯性旋转+最后回弹、*点击某个区块自动旋转过去、*指针箭头指到某个区块事件。
- **基本素质** ：内存抖动基本为0、旋转屏幕状态不丢失，转之前啥样，回来还是啥样、暴露事件冲突接口

-------------------
# 示例，Demo演示了普通用法和涉及到SwipeRefreshLayout+AppBarLayout等类似的滑动冲突的用法。
| 常规使用      |下拉刷新及其他滑动冲突  |
| :--------:| :--------:|  
|![normal](https://github.com/AndroidCloud/PieRotateView/blob/master/DemoImg/demo1.gif)| ![fix](https://github.com/AndroidCloud/PieRotateView/blob/master/DemoImg/demo2.gif)| 
 <br />

