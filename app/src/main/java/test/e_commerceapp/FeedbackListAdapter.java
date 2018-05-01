package test.e_commerceapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.ViewHolder> {

    private final List<FeedbackForm> mValues;

    public FeedbackListAdapter(List<FeedbackForm> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_feedback_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.subject.setText(holder.mItem.subject);
        holder.message.setText(holder.mItem.message);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        FeedbackForm mItem;
        TextView message, subject;

        ViewHolder(View view) {
            super(view);

            message = view.findViewById(R.id.message);
            subject = view.findViewById(R.id.subject);
        }

    }
}
