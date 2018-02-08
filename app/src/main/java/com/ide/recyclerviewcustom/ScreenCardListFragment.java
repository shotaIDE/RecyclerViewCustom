package com.ide.recyclerviewcustom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

public class ScreenCardListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_card_list, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.screen_card_list);

        ScreenCardLayoutManager manager = new ScreenCardLayoutManager(getContext());
        String[] list = getContext().getResources().getStringArray(R.array.sample_raw_data);
        ScreenCardListAdapter adapter = new ScreenCardListAdapter(new ArrayList<>(Arrays.asList(list)),
                (ScreenCardListAdapter.Callback)getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        // スワイプされたときの挙動を定義
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 横にスワイプされたら要素を消す
                int swipedPosition = viewHolder.getAdapterPosition();
                ScreenCardListAdapter adapter = (ScreenCardListAdapter) recyclerView.getAdapter();
                adapter.removeCard(swipedPosition);
            }
        };
        (new ItemTouchHelper(callback)).attachToRecyclerView(recyclerView);

        return view;
    }
}
