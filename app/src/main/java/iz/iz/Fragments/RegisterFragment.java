package iz.iz.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import iz.iz.Activities.HomeActivity;
import iz.iz.Model.User;
import iz.iz.R;
import iz.iz.Utils.InputValidation;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#} factory method to
 * create an instance of this fragment.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    final String domain = "@IZ.com";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutOwnerNumber;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputLayout textInputLayoutNumber;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextOwnerNumber;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private TextInputEditText textInputEditTextNumber;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private User user;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private DatabaseReference childref;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register, container, false);

        initViews(view);
        initListeners();
        initObjects();

        return view;
    }

    /**
     * This method is to initialize views
     */
    private void initViews(View view) {
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) view.findViewById(R.id.textInputLayoutName);
        textInputLayoutOwnerNumber = (TextInputLayout) view.findViewById(R.id.textInputLayoutOwnerNumber);
        textInputLayoutPassword = (TextInputLayout) view.findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) view.findViewById(R.id.textInputLayoutConfirmPassword);
        textInputLayoutNumber = (TextInputLayout) view.findViewById(R.id.textInputLayoutNumber);

        textInputEditTextName = (TextInputEditText) view.findViewById(R.id.textInputEditTextName);
        textInputEditTextOwnerNumber = (TextInputEditText) view.findViewById(R.id.textInputEditTextOwnerNumber);
        textInputEditTextPassword = (TextInputEditText) view.findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) view.findViewById(R.id.textInputEditTextConfirmPassword);
        textInputEditTextNumber = (TextInputEditText) view.findViewById(R.id.textInputEditTextNumber);

        appCompatButtonRegister = (AppCompatButton) view.findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) view.findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(getContext());
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childref = databaseReference.child("User");
        progressDialog = new ProgressDialog(getContext());
        user = new User();
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                register();
                break;

            case R.id.appCompatTextViewLoginLink:
                changeFragment(new LoginFragment());
                break;
        }
    }


        public void register(){

            final String namestr = textInputEditTextName.getText().toString().trim();
            final String passwordstr = textInputEditTextPassword.getText().toString().trim();
            final String numberstr = textInputEditTextNumber.getText().toString().trim();
            final String ownerNumber = textInputEditTextOwnerNumber.getText().toString();

            progressDialog.setMessage("Signing Up...");
            progressDialog.show();

            auth.createUserWithEmailAndPassword(numberstr + domain, passwordstr)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if(task.isSuccessful()){
                                firebaseUser = auth.getCurrentUser();
                                user = new User(namestr, numberstr, passwordstr, ownerNumber,
                                        firebaseUser.getUid());
                                addToDatabase(user);
                            }
                            else{
                                Toast.makeText(getContext(), "Sign Up failed " +
                                                task.getException()
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }

    private void addToDatabase(User user) {
        firebaseUser = auth.getCurrentUser();

        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        childref.child(user.getNumber()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            Snackbar.make(getView(), "Login Successful", Snackbar.LENGTH_SHORT);
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getContext(), "Failed to add User to the database",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextOwnerNumber.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
        textInputEditTextNumber.setText(null);
    }

    public void changeFragment(Fragment f) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.mainframe, f);

        fragmentTransaction.commit();
    }

}
