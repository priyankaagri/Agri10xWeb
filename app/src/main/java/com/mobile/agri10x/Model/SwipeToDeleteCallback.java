package com.mobile.agri10x.Model;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.mobile.agri10x.Farmer;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final ProductAdapter mAdapter;
    private Drawable icon;
    private ColorDrawable background;

    public SwipeToDeleteCallback(ProductAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;

    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();
        mAdapter.delete(position);
        Farmer.setUp(mAdapter);


    }
}
