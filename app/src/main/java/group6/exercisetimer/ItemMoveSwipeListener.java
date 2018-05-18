package group6.exercisetimer;

public interface ItemMoveSwipeListener {
//    onItemMove : 當item移動完的時候
    boolean onItemMove(int fromPosition, int toPosition);
//    onItemSwipe : 當item滑動完的時候
    void onItemSwipe(int position);
}
