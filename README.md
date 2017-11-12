# HenCoderTape
Hencoder仿写界面 效果图<br>


## 效果图
![image](https://github.com/ysemylord/HenCoderTape/blob/master/SVID_20171016_232721.gif)

## 使用方法

直接布局里引用即可:<br>
```
   <com.xyb.tape.ui.MyTap
        android:background="@android:color/white"
        android:id="@+id/my_tap"
        android:layout_width="match_parent"
        android:layout_height="80dp"/>
```
## 要点
+ 根据手势滑动内容
+ 左右两侧的最大滑动距离限制
+ 惯性滑动
+ 滑动完成后的距离调整


## 使用方法

### xml文件调用
```
    <com.xyb.tape.ui.MyTap
        app:startNum="30"
        app:endNum="140"
        app:internalNum="0.2"
        app:scaleGap="35"
        app:sortLineWidth="1dp"
        app:sortLineHeight="20dp"
        app:sortScaleColor="@android:color/darker_gray"
        app:longLineWidth="2dp"
        app:longLineHeight="40dp"
        app:longScaleColor="@android:color/darker_gray"
        app:indicatorLineWidth="5dp"
        app:indicatorLineHeight="55dp"
        app:indicatorScaleColor="@android:color/holo_green_light"
        app:textColor="#000000"
        app:textSize="18sp"
        app:textMarginTop="15dp"
        app:currentNum="35"
        android:background="@android:color/white"
        android:id="@+id/my_tap"
        android:layout_width="match_parent"
        android:layout_height="80dp"/>
```

### java代码

监听当前卷尺指示的值
```
    myTap.setmOuterInterface(new MyTap.OuterInterface() {
            @Override
            public void nowKg(String nowKG) {
                kgTV.setText(nowKG + "kg");
            }
        });
```
## 属性  
|**属性名称**|**意义**|**类型**|**默认值**|
|--|--|:--:|:--:|
|startScale      | 尺子的最小刻度值     | float| 20f（在尺子上显示就是46）|
|endScale      | 尺子的最大刻度值     | float| 30f（在尺子上显示就是30）|
|internalScale | 尺子一小格代表的值     | float| 0.1f|
|currentScale | 尺子初始时的刻度值     | float| startScale(为最小刻度值)|
|scaleGap | 尺子一小格的长度 | dimension| 60px|
|sortLineWidth | 尺子短刻度线的线宽度/粗细   | dimension| 5px|
|sortLineHeight | 尺子短刻度线的高度/长度 | dimension| 50px|
|sortScaleColor | 尺子短刻度线的颜色   | color| Color.GRAY|
|longLineWidth | 尺子长刻度线的线宽度/粗细   | dimension| 5px|
|longLineHeight | 尺子长刻度线的高度/长度 | dimension| 50px|
|longScaleColor | 尺子长刻度线的颜色   | color| Color.GRAY|
|indicatorLineWidth | 尺子指示器刻度线的线宽度/粗细   | dimension| 10px|
|idicatorLineHeight | 尺子指示器刻度线的高度/长度 | dimension| 150px|
|idicatorScaleColor | 尺子指示器刻度线的颜色   | color| Color.GREEN|
|textSize | 尺子数字文字大小  | dimension| 38px|
|textColor | 尺子数字文字颜色  | color| Color.GRAY|
|textMarginTop | 尺子文字距离长刻度线的距离  | dimension| 38px|
|topHorizontalLineHeight | 顶部横线高度 | dimension| 20px|
|topHorizontalLineColor | 顶部横线颜色 | color| Color.GRAY|


## 接口
```
  public interface OuterInterface {
        void nowScale(String nowScale);
  }
```
调用  
public void setOuterInterface(OuterInterface outerInterface)方法传入接口

