package test.e_commerceapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemFragment extends Fragment {

    TextView name, price, desc;
    ImageView image;
    Button addToCart;
    Spinner quantitySpinner;
    String id = "";
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // progress dialog instance
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading, Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        // UI Components
        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);
        desc = view.findViewById(R.id.desc);
        image = view.findViewById(R.id.card_image);
        addToCart = view.findViewById(R.id.addToCart);

        // firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // get data of item from firebase
        databaseReference.child("items").child(getArguments().getString("id")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                price.setText(String.format("Rs. %s", dataSnapshot.child("price").getValue().toString()));
                desc.setText(dataSnapshot.child("desc").getValue().toString());

                id = dataSnapshot.child("id").getValue().toString();

                // TODO: Fetch Image
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // add to cart onclick
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(getActivity(), "Please login to continue", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(id)) {
                    progressDialog.show();

                    databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(id).exists()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Item already added in cart", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference
                                        .child("users")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .child("cart")
                                        .child(id)
                                        .setValue("1")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
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
