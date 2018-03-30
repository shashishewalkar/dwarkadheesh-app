package test.e_commerceapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // UI Components
        recyclerView = view.findViewById(R.id.recyclerView);

        // firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Dummy data for recycler view
        final List list = new ArrayList<ItemData>();
//        list.add(new ItemData("Test1", "", "Rs. 900", "", "12"));
//        list.add(new ItemData("Test2", "", "Rs. 1900", "", "13"));
//        list.add(new ItemData("Test3", "", "Rs. 700", "", "15"));
//        list.add(new ItemData("Test4", "", "Rs. 1700", "", "14"));

        //set layout for recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Adapter for recycler view
        final DataAdapter adapter = new DataAdapter(getFragmentManager(), list);

        // set adapter to recycler view
        recyclerView.setAdapter(adapter);

        // get items from firebase
        databaseReference.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(ItemData.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
