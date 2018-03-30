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

public class SignInFragment extends Fragment {

    EditText username, password;
    Button login, register;
    TextInputLayout viewUsername, viewPassword;

    FirebaseAuth firebaseAuth;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // UI Components
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        viewUsername = view.findViewById(R.id.viewUsername);
        viewPassword = view.findViewById(R.id.viewPassword);
        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);

        // Firebase auth reference
        firebaseAuth = FirebaseAuth.getInstance();

        // register onclick
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.main_content, new RegisterFragment()).commit();
            }
        });

        // login onclick
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Semail = username.getText().toString();
                String Spassword = password.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(Spassword)) {
                    viewPassword.setError(getResources().getString(R.string.empty_field));
                    cancel = true;
                    focusView = password;
                } else if (!isPassswordValid(Spassword)) {
                    viewPassword.setError(getResources().getString(R.string.invalid_password));
                    cancel = true;
                    focusView = password;
                }

                if (TextUtils.isEmpty(Semail)) {
                    viewUsername.setError(getResources().getString(R.string.empty_field));
                    cancel = true;
                    focusView = username;
                } else if (!isEmailValid(Semail)) {
                    viewUsername.setError(getResources().getString(R.string.invalid_email));
                    cancel = true;
                    focusView = username;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    // login

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(Semail, Spassword)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.dismiss();
                                }
                            });
                }

            }
        });

        return view;
    }

    boolean isEmailValid(String email) {
        return email.contains("@");
    }

    boolean isPassswordValid(String pass) {
        return pass.length() >= 8;
    }

}
