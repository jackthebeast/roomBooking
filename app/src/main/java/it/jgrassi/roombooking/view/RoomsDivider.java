package it.jgrassi.roombooking.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import it.jgrassi.roombooking.R;

/**
 * Created by jacop on 29/10/2017.
 */

public class RoomsDivider extends RecyclerView.ItemDecoration {

private Drawable divider;

public RoomsDivider(Context context) {
        divider = context.getResources().getDrawable(R.drawable.rooms_divider);
        }

@Override
public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
        View child = parent.getChildAt(i);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int top = child.getBottom() + params.bottomMargin;
        int bottom = top + divider.getIntrinsicHeight();

        divider.setBounds(left, top, right, bottom);
        divider.draw(c);
        }
        }
        }