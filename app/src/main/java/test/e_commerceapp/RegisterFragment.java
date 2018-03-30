package test.e_commerceapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {

    EditText name, contactNo, email, password, confirmPassword;
    TextInputLayout viewName, viewContactNo, viewEmail, viewPassword, viewConfirmPass;
    Button register;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // UI Components
        name = view.findViewById(R.id.name);
        contactNo = view.findViewById(R.id.contactNo);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPass);
        viewName = view.findViewById(R.id.viewName);
        viewContactNo = view.findViewById(R.id.viewContactNo);
        viewEmail = view.findViewById(R.id.viewEmail);
        viewPassword = view.findViewById(R.id.viewPassword);
        viewConfirmPass = view.findViewById(R.id.viewConfirmPass);
        register = view.findViewById(R.id.register);

        // firebase auth reference
        firebaseAuth = FirebaseAuth.getInstance();

        // database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // register onclick
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String Sname, Scontact, Semail, Spass, SconfirmPass;

                Sname = name.getText().toString();
                Scontact = contactNo.getText().toString();
                Semail = email.getText().toString();
                Spass = password.getText().toString();
                SconfirmPass = confirmPassword.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(SconfirmPass)) {
                    cancel = true;
                    focusView = viewConfirmPass;
                    viewConfirmPass.setError(getResources().getString(R.string.empty_field));
                } else if (!Spass.equals(SconfirmPass)) {
                    cancel = true;
                    focusView = viewConfirmPass;
                    viewConfirmPass.setError(getResources().getString(R.string.password_dont_match));
                    viewPassword.setError(getResources().getString(R.string.password_dont_match));
                } else if (!isPasswordValid(SconfirmPass)) {
                    cancel = true;
                    focusView = viewConfirmPass;
                    viewConfirmPass.setError(getResources().getString(R.string.invalid_password));
                }

                if (TextUtils.isEmpty(Spass)) {
                    cancel = true;
                    focusView = viewPassword;
                    viewConfirmPass.setError(getResources().getString(R.string.empty_field));
                } else if (!isPasswordValid(Spass)) {
                    cancel = true;
                    focusView = viewPassword;
                    viewConfirmPass.setError(getResources().getString(R.string.invalid_password));
                }

                if (TextUtils.isEmpty(Semail)) {
                    cancel = true;
                    focusView = viewEmail;
                    viewConfirmPass.setError(getResources().getString(R.string.empty_field));
                } else if (!isEmailValid(Semail)) {
                    cancel = true;
                    focusView = viewEmail;
                    viewConfirmPass.setError(getResources().getString(R.string.invalid_email));
                }

                if (TextUtils.isEmpty(Scontact)) {
                    cancel = true;
                    focusView = viewContactNo;
                    viewConfirmPass.setError(getResources().getString(R.string.empty_field));
                } else if (!isContactValid(Scontact)) {
                    cancel = true;
                    focusView = viewContactNo;
                    viewConfirmPass.setError(getResources().getString(R.string.invalid_contact));
                }

                if (TextUtils.isEmpty(Sname)) {
                    cancel = true;
                    focusView = viewName;
                    viewConfirmPass.setError(getResources().getString(R.string.empty_field));
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Loading, Please Wait...");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(Semail, Spass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.dismiss();
                                    User user = new User(Sname, Scontact);
                                    databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        return view;
    }

    boolean isContactValid(String contact) {
        return contact.length() == 10;
    }

    boolean isEmailValid(String email) {
        return email.contains("@");
    }

    boolean isPasswordValid(String pass) {
        return pass.length() >= 8;
    }

}

class User {
    public String name, contactNo;

    public User(String name, String contactNo) {
        this.name = name;
        this.contactNo = contactNo;
    }
}
