package teamcool.tradego;
/**
 * Created by lguti on 7/20/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}