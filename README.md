# HenCoderTape
Hencoder仿写界面<br>

###效果图
![image](https://github.com/ysemylord/HenCoderTape/blob/master/SVID_20171016_142609.gif)

## 使用方法

直接布局里引用即可:<br>
```
   <com.xyb.tape.ui.MyTap
        android:background="@android:color/white"
        android:id="@+id/my_tap"
        android:layout_width="match_parent"
        android:layout_height="80dp"/>
```
## java代码

设置起始值和结束值

```
  myTap.setStartKg(30f);
  myTap.setEndKg(40f);
```

监听当前卷尺指示的值
```
    myTap.setmOuterInterface(new MyTap.OuterInterface() {
            @Override
            public void nowKg(String nowKG) {
                kgTV.setText(nowKG + "kg");
            }
        });
```
