package test.e_commerceapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    TextView name, email, contactNo;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // UI Components
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        contactNo = view.findViewById(R.id.contactNo);

        // firebase references
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // set data from user
        try {
            email.setText(firebaseAuth.getCurrentUser().getEmail());

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot userSnap = dataSnapshot.child("users").child(firebaseAuth.getCurrentUser().getUid());

                    name.setText(userSnap.child("name").getValue().toString());
                    contactNo.setText(userSnap.child("contactNo").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ignored) {
        }

        return view;
    }

}
