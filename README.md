# QGuideView
自定义引导蒙层控件，支持全屏模式和targetView模式<br>
![全屏模式gif](https://github.com/272664150/QGuideView/blob/master/screenshots/1.png)
                             
 全屏模式
 -----
 1. setOnlyOnceTag  是否只显示一次的标识
 2. setBackgroundColor  引导蒙层区域外的背景色
 3. setBackgroundResource  引导蒙层区域外的颜色id
 4. addDrawable  添加蒙层图片的资源id
 5. setOnDisplayListener  监听显示、消失、多次显示的事件
 6. show  显示引导蒙层
 7. hide  隐藏蒙层
 
         new QGuideView.Builder(this)
                 .setOnlyOnceTag(this.getClass().getSimpleName())
                 .setBackgroundColor(Color.parseColor("#80000000"))
                 .addDrawable(R.drawable.ic_launcher_foreground)
                 .addDrawable(R.drawable.ic_launcher_background)
                 .setOnDisplayListener(new QGuideView.OnDisplayListener() {
                     @Override
                     public void onShow(QGuideView guideView) {
                     }
 
                     @Override
                     public void onReappear(boolean isReappear) {
                     }
 
                     @Override
                     public void onDismiss() {
                     }
                 })
                 .build().show();

 targetView模式（自行拓展）
 -----
 1. setTargetView  引导蒙层要在哪个view上展示
 
        new QGuideView.Builder(this)
                .setOnlyOnceTag(this.getClass().getSimpleName())
                .setBackgroundColor(Color.parseColor("#80000000"))
                .setTargetView(findViewById(R.id.hello_tv))
                .addDrawable(R.drawable.ic_launcher_foreground)
                .addDrawable(R.drawable.ic_launcher_background)
                .setOnDisplayListener(new QGuideView.OnDisplayListener() {
                    @Override
                    public void onShow(QGuideView guideView) {
                    }

                    @Override
                    public void onReappear(boolean isReappear) {
                    }

                    @Override
                    public void onDismiss() {
                    }
                })
                .build().show();
