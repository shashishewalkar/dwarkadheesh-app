package test.e_commerceapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackFragment extends Fragment {

    EditText subject, message;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        subject = view.findViewById(R.id.subject);
        message = view.findViewById(R.id.message);

        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (subject.getText().toString().trim().equals("") || message.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading, Please Wait...");
                    progressDialog.show();

                    FirebaseDatabase.getInstance().getReference("feedback").push()
                            .setValue(new FeedbackForm(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    subject.getText().toString(),
                                    message.getText().toString()
                            )).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            getFragmentManager().beginTransaction().replace(R.id.main_content, new HomeFragment()).commitAllowingStateLoss();
                        }
                    });
                }

            }
        });

        return view;
    }
}

class FeedbackForm {

    public String user, subject, message;

    public FeedbackForm() {
    }


    public FeedbackForm(String user, String subject, String message) {
        this.user = user;
        this.subject = subject;
        this.message = message;
    }
}
