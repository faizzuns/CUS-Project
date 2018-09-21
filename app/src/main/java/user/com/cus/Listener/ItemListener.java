package user.com.cus.Listener;

import user.com.cus.DataModel.Item.ItemSaved;

/**
 * Created by User on 28/01/2018.
 */

public interface ItemListener {
    void onFavouriteClicked(ItemSaved item);
    void onDecreaseClicked(ItemSaved item);
    void onIncreaseClicked(ItemSaved item);
    void onItemClick(ItemSaved item);
}
