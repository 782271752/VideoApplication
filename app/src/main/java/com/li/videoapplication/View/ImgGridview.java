package com.li.videoapplication.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by li on 2014/7/21.
 */
public class ImgGridview extends GridView {

    public ImgGridview(Context context) {
        super(context);
    }

    public ImgGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgGridview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
