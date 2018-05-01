package test.e_commerceapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    TextView total_price;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // UI compoenets
        recyclerView = view.findViewById(R.id.recyclerView);
        total_price = view.findViewById(R.id.total);

        //set layout for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        // dummy data for cart
        final List<CartItem> list = new ArrayList<CartItem>();

        // Adapter for recycler view
        final CartAdapter adapter = new CartAdapter(getFragmentManager(), list);

        // set adapter to recycler view
        recyclerView.setAdapter(adapter);

        // get data from firebase
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();

                final int[] total = {0};
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

//                    Toast.makeText(getActivity(), snapshot.getKey(), Toast.LENGTH_SHORT).show();

                    databaseReference.child("items").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
//                            Toast.makeText(getActivity(), dataSnapshot1.child("id").getValue().toString(), Toast.LENGTH_SHORT).show();
                            ItemData item = dataSnapshot1.getValue(ItemData.class);
                            list.add(new CartItem(item, Long.valueOf(snapshot.getValue().toString())));
                            adapter.notifyDataSetChanged();

                            total[0] += item.price;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                total_price.setText("" + total[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        view.findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list.isEmpty()) {
                    Toast.makeText(getActivity(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading, Please Wait");
                    progressDialog.show();

                    databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            DatabaseReference ref = databaseReference.child("orders").push();
                            ref.child("user").setValue(firebaseAuth.getCurrentUser().getUid());

                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date date = new Date();
                            String dateStr = format.format(date);

                            ref.child("date").setValue(dateStr);
                            ref.child("order").setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Your order has been placed", Toast.LENGTH_SHORT).show();
                                    getFragmentManager().beginTransaction().replace(R.id.main_content, new HomeFragment()).commitAllowingStateLoss();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        return view;
    }

}
