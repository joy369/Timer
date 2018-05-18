package group6.exercisetimer;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ActionItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ItemMoveSwipeListener itemMoveSwipeListener;

    public ActionItemTouchHelperCallback(ItemMoveSwipeListener itemMoveSwipeListener) {
        this.itemMoveSwipeListener = itemMoveSwipeListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        Define a item can be moved up/down
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        Define a item can be moved right/left
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//        set by makeMovementFlags()
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return itemMoveSwipeListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemMoveSwipeListener.onItemSwipe(viewHolder.getAdapterPosition());
    }

    //    While clicking
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

//        While moving
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
//            make item be transparent 70%
            viewHolder.itemView.setAlpha(0.7f);
        }
    }

    //    Recover color after move
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(1.0f);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            Get width
            float width = viewHolder.itemView.getWidth();
//            count transparent by width
            float alphaValue = 1 - Math.abs(dX) / width;

            viewHolder.itemView.setAlpha(alphaValue);
        }
    }

}
