package test.e_commerceapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.MessageFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    List<CartItem> cartList;
    FragmentManager fragmentManager;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public CartAdapter(FragmentManager fragmentManager, List<CartItem> cartList) {
        this.cartList = cartList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_row, parent, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {

        holder.name.setText(cartList.get(position).getItemData().getName());
        holder.quantity.setSelection((int) cartList.get(position).getQuantity() - 1);
        holder.price.setText(MessageFormat.format("Rs. {0}", cartList.get(position).getItemData().getPrice() * cartList.get(position).getQuantity()));

        holder.quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Long quant = Long.valueOf(adapterView.getItemAtPosition(i).toString());
                databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("cart").child(cartList.get(position).getItemData().getId()).setValue(quant);
                holder.price.setText(MessageFormat.format("Rs. {0}", cartList.get(position).getItemData().getPrice() * quant));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}

class CartViewHolder extends RecyclerView.ViewHolder {

    TextView name, price;
    Spinner quantity;

    public CartViewHolder(View view) {
        super(view);

        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);
        quantity = view.findViewById(R.id.quantity);
    }
}

class CartItem {
    ItemData itemData;
    long quantity;

    public ItemData getItemData() {
        return itemData;
    }

    public long getQuantity() {
        return quantity;
    }

    public CartItem(ItemData itemData, long quantity) {
        this.itemData = itemData;
        this.quantity = quantity;

    }
}
