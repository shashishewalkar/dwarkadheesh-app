package test.e_commerceapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {

    private FragmentManager fragmentManager;
    private List<ItemData> dataList;

    public DataAdapter(FragmentManager fragmentManager, List dataList) {
        this.fragmentManager = fragmentManager;
        this.dataList = dataList;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        holder.title.setText(dataList.get(position).getName());
        holder.price.setText(MessageFormat.format("Rs. {0}", dataList.get(position).getPrice()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", dataList.get(position).getId());
                ItemFragment itemFragment = new ItemFragment();
                itemFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.main_content, itemFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class DataViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView title, price;
    CardView cardView;

    DataViewHolder(View view) {
        super(view);

        imageView = view.findViewById(R.id.card_image);
        title = view.findViewById(R.id.card_title);
        price = view.findViewById(R.id.card_price);
        cardView = view.findViewById(R.id.card);
    }
}
