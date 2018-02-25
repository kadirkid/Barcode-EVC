package iz.iz.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import iz.iz.Activities.HomeActivity;
import iz.iz.R;
import iz.iz.Utils.InputValidation;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    final String domain = "@IZ.com";

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutNumber;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextNumber;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login, container, false);
        initViews(view);
        initListeners();
        initObjects();

        
        return view;
    }

    private void initViews(View view) {
        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        textInputLayoutNumber = view.findViewById(R.id.textInputLayoutNumberL);
        textInputLayoutPassword = view.findViewById(R.id.textInputLayoutPasswordL);

        textInputEditTextNumber = view.findViewById(R.id.textInputEditTextNumberL);
        textInputEditTextPassword = view.findViewById(R.id.textInputEditTextPasswordL);

        appCompatButtonLogin = view.findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = view.findViewById(R.id.textViewLinkRegister);
    }

    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loggin in...");
        progressDialog.setCancelable(false);
        inputValidation = new InputValidation(getContext());
    }

    public void login(){
        String numberstr = textInputEditTextNumber.getText().toString().trim();
        String passwordstr = textInputEditTextPassword.getText().toString().trim();

        progressDialog.show();
        auth.signInWithEmailAndPassword(numberstr + domain, passwordstr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if(task.isSuccessful()){

                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            Log.i("Login Failed ", task.toString());
                        }
                    }
                });
    }

   /*
   private boolean verify() {

        if (!inputValidation.isInputEditTextFilled(textInputEditTextNumber, textInputLayoutNumber, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextNumber, textInputLayoutNumber, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return false;
        }
        return true;
    }
    */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appCompatButtonLogin:
//                if(verify())
                login();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterFragment
                changeFragment(new RegisterFragment());
                break;
        }
    }

    /**
     * This method is to change the current fragment
     */
    public void changeFragment(Fragment f) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.mainframe, f);

        fragmentTransaction.commit();
    }

}
