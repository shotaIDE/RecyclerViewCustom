package com.ide.recyclerviewcustom;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.ide.recyclerviewcustom.ScreenDetailFragment.SCREEN_IMAGE_TRANSITION_NAME;

public class ScreenCardListAdapter extends RecyclerView.Adapter<ScreenCardListAdapter.ViewHolder> {
    private final ArrayList<String> mData;
    private final Callback mListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout linearLayout;
        final TextView fieldTitle;
        final Button fieldButton;
        final ImageView fieldImage;

        public ViewHolder(View v) {
            super(v);
            linearLayout = v.findViewById(R.id.screen_card_container);
            fieldTitle = v.findViewById(R.id.screen_card_title);
            fieldButton = v.findViewById(R.id.screen_card_close);
            fieldImage = v.findViewById(R.id.screen_card_image);
        }
    }

    public interface Callback {
        void onRecyclerViewCustomListener(View view, int position);
    }

    ScreenCardListAdapter(ArrayList<String> data, Callback listener) {
        mData = data;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.screen_card_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.fieldTitle.setText(mData.get(position));
        viewHolder.fieldTitle.setHorizontallyScrolling(true);
        viewHolder.fieldTitle.setEllipsize(TextUtils.TruncateAt.END);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.fieldImage.setTransitionName(SCREEN_IMAGE_TRANSITION_NAME + String.valueOf(position));
        }

        // カードをタップ時
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecyclerViewCustomListener(v, viewHolder.getAdapterPosition());
            }
        });

        // カードの閉じるボタンをタップ時
        viewHolder.fieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCard(viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void removeCard(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
