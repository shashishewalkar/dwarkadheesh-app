package test.e_commerceapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

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

                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

//                    Toast.makeText(getActivity(), snapshot.getKey(), Toast.LENGTH_SHORT).show();

                    databaseReference.child("items").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
//                            Toast.makeText(getActivity(), dataSnapshot1.child("id").getValue().toString(), Toast.LENGTH_SHORT).show();
                            list.add(new CartItem(dataSnapshot1.getValue(ItemData.class), Long.valueOf(snapshot.getValue().toString())));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
