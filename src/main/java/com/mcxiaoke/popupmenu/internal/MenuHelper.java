package com.mcxiaoke.popupmenu.internal;

import android.content.Context;
import android.view.Menu;

/**
 * User: mcxiaoke
 * Date: 13-10-2
 * Time: 下午11:57
 */
public final class MenuHelper {

    private MenuHelper() {
    }

    public static Menu createMenu(Context context) {
        return new MenuCompat(context);
    }
}
