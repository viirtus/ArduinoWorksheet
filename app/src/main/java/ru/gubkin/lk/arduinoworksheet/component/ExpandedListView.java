package ru.gubkin.lk.arduinoworksheet.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Андрей on 09.05.2015.
 */
public class ExpandedListView extends ListView {
    private android.view.ViewGroup.LayoutParams params;

    // Предыдущее значение количества элементов
    private int mOldCount = 0;

    public ExpandedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Если количество позиций изменилось, пересчитываем высоту
        if (getCount() != mOldCount) {
            params = getLayoutParams();

            if(getCount() > 0)
                params.height =
                        getCount() * (getChildAt(0).getHeight() + getDividerHeight())
                                - getDividerHeight();
            else
                params.height = 0;

            setLayoutParams(params);
            mOldCount = getCount();
        }
        super.onDraw(canvas);
    }
}