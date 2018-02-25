package iz.iz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import iz.iz.R;
import iz.iz.Fragments.*;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null)
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        else
            changeFragment(new LoginFragment());

    }

    public void changeFragment(Fragment f) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.mainframe, f);

        fragmentTransaction.commit();
    }
}
