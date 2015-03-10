package com.li.videoapplication.utils;

import java.util.Random;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.li.videoapplication.R;
import com.li.videoapplication.activity.SearchActivity;
import com.li.videoapplication.activity.SearchResultActivity;
import com.li.videoapplication.entity.ViewPosition;

public class SearchKeyManager {
    // 关键字
    private String[] keys;
    private AnimationSet animation[];
    // 用户移动的TextView对象
    private TextView[] moveViews;
    private SearchActivity context;
    private int[] cr = {R.color.blue,R.color.light_green,R.color.purple,R.color.light_orange,R.color.pink };
    private int[] textSize = { 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private TranslateAnimation translateAnimation;
    private AlphaAnimation alphaAnimation;
    private AnimationSet animationSet;

    public SearchKeyManager(String[] keys, SearchActivity context) {
        this.keys = keys;
        this.context = context;
        moveViews = new TextView[keys.length];
        animation = new AnimationSet[keys.length];
        initViews();
    }

    /**
     * 创建需要进行显示的TextView
     */
    private void initViews() {
        for (int i = 0; i < keys.length; i++) {// 有几个关键字就生成几个View
            TextView tv = new TextView(context);
            moveViews[i] = tv;
            setViewProperties(tv, i);

            animation[i] = animationSet;
            int x = (int) (((ViewPosition) tv.getTag()).getStartX());
            int y = (int) (((ViewPosition) tv.getTag()).getStartY());
            AbsoluteLayout.LayoutParams newLayPms = new AbsoluteLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, x, y);

            tv.setLayoutParams(newLayPms);
            context.layout.addView(tv);
        }

    }

    /**
     * 设置TextView的一些属性
     *
     * @param tv
     */
    private void setViewProperties(TextView tv, int position) {
        ViewPosition vp = (ViewPosition) context.layout.getTag();
        startX = vp.getStartX();
        startY = vp.getStartY();
        endX = vp.getEndX();
        endY = vp.getEndY();
        tv.setText(keys[position]);
        Random r = new Random();
        tv.setTextColor(context.getResources().getColor(cr[r.nextInt(cr.length)]));
        tv.setTextSize(textSize[r.nextInt(textSize.length)]);
        createAnimationPosition(tv);
        setAnimation(tv);
        setListeners(tv);
    }

    /**
     * 为控件设置监听
     */

    private void setListeners(TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "" + ((TextView) v).getText(), Toast.LENGTH_LONG)
//                        .show();
                Intent intent=new Intent(context,SearchResultActivity.class);
                intent.putExtra("key",((TextView) v).getText());
                context.startActivity(intent);

            }
        });

    }

    /**
     * 设置TextView动画的坐标
     *
     * @param tv
     */
    private void createAnimationPosition(TextView tv) {
        Random r = new Random();
        tv.measure(0, 0);
        // 字体的宽和高
        int tvWidth = tv.getMeasuredWidth();
        int tvHeight = tv.getMeasuredHeight();
        // 通过字体大小，将字体的位置限制在屏幕以内
        int startX = this.startX;
        int endX = this.endX - tvWidth;
        int startY = this.startY;
        int endY = this.endY - tvHeight;

        int tvStratX = r.nextInt(endX - startX + 1) + startX;
        int tvStartY = r.nextInt(endY - startY + 1) + startY;
        int tvEndX = r.nextInt(endX - startX + 1) + startX;
        int tvEndY = r.nextInt(endY - startY + 1) + startY;
        boolean hasSamePosition = true;
        while (hasSamePosition) {
            if (!testPosition(tvEndX, tvEndY,tvWidth,tvHeight)) {// 没有碰撞
                hasSamePosition = false;
            } else {

                System.out.println(tv.getText() + "检测到碰撞，重新获取新坐标");
                tvStratX = r.nextInt(endX - startX + 1) + startX;
                tvStartY = r.nextInt(endY - startY + 1) + startY;
                tvEndX = r.nextInt(endX - startX + 1) + startX;
                tvEndY = r.nextInt(endY - startY + 1) + startY;
            }
        }
        ViewPosition textViewPosition = new ViewPosition();
        String aaa = tv.getText().toString();
        textViewPosition.setStartX(tvStratX);
        textViewPosition.setStartY(tvStartY);
        textViewPosition.setEndX(tvEndX);
        textViewPosition.setEndY(tvEndY);
        tv.setTag(textViewPosition);

    }

    /**
     * 为TextView设置动画
     *
     * @param tv
     */
    private void setAnimation(final TextView tv) {
        float tvStratX = ((ViewPosition) tv.getTag()).getStartX();
        float tvStartY = ((ViewPosition) tv.getTag()).getStartY();
        float tvEndX = ((ViewPosition) tv.getTag()).getEndX();
        float tvEndY = ((ViewPosition) tv.getTag()).getEndY();
        float moveToX = (tvEndX - tvStratX) / endX;
        float moveToY = (tvEndY - tvStartY) / endY;
        translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                moveToX, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, moveToY);
        translateAnimation.setDuration(1000);
        translateAnimation.setFillBefore(false);
        translateAnimation.setFillAfter(true);
        // translateAnimation.setRepeatCount(Animation.INFINITE);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.clearAnimation();
                moveView(tv);
            }
        });
        alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
    }

    /**
     * 开始播放动画
     */
    public void startAnimation() {
        for (int i = 0; i < moveViews.length; i++) {
            moveViews[i].startAnimation(animation[i]);
        }
    }

    /**
     * 动画完成之后移动控件的实际位置
     *
     * @param tv
     */
    private void moveView(TextView tv) {
        int x = (int) (((ViewPosition) tv.getTag()).getEndX());
        int y = (int) (((ViewPosition) tv.getTag()).getEndY());
        AbsoluteLayout.LayoutParams oldLayPms = (AbsoluteLayout.LayoutParams) tv
                .getLayoutParams();
        AbsoluteLayout.LayoutParams newLayPms = new AbsoluteLayout.LayoutParams(
                oldLayPms.width, oldLayPms.height, x, y);
        tv.setLayoutParams(newLayPms);
    }

    /**
     * 矩形检测碰撞(标准平面坐标)
     * @param aLeftTopX 左上角x坐标值 --矩形1
     * @param aLeftTopY 左上角y坐标值
     * @param aWidth    宽
     * @param aHeight   高
     * @param bLeftTopX 左上角x坐标值 --矩形2
     * @param bLeftTopY 左上角y坐标值
     * @param bWidth    宽
     * @param bHeight   高
     * @return 是否碰撞
     */
    public  boolean isIntersectingRect(int aLeftTopX, int aLeftTopY, int aWidth, int aHeight,
                                       int bLeftTopX, int bLeftTopY, int bWidth, int bHeight) {

        /**
         * bLeftTopX > aLeftTopX + aWidth  b在a的右边
         * bLeftTopY > aLeftTopY + aHeght  b在a的下面
         * bLeftTopX + bWidth < aLeftTopX  b在a的左边
         * bLeftTopX + bHeight < aLeftTopY b在a的上面
         */
        if (bLeftTopX > aLeftTopX + aWidth || bLeftTopY > aLeftTopY + aHeight ||
                bLeftTopX + bWidth < aLeftTopX || bLeftTopY + bHeight < aLeftTopY) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 检测是否碰撞
     * @param ranEndX
     * @param ranEndY
     * @param tvWidth
     * @param tvHeight
     * @return
     */
    public boolean testPosition(int ranEndX,
                                int ranEndY,int tvWidth,int tvHeight) {
        for (int i = 0; i < moveViews.length; i++) {//遍历所有已经存在的TextView
            if (moveViews[i] != null) {
                try {
                    int endX = ((ViewPosition) moveViews[i].getTag()).getEndX();
                    int endY = ((ViewPosition) moveViews[i].getTag()).getEndY();
                    moveViews[i].measure(0, 0);
                    if (isIntersectingRect(endX, endY, moveViews[i].getMeasuredWidth(), moveViews[i].getMeasuredHeight(), ranEndX,
                            ranEndY, tvWidth, tvHeight)) {// 证明碰撞了
                        return true;
                    }
                } catch (Exception e) {

                }
            } else {
                break;

            }
        }
        return false;
    }
}
