package iz.iz.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import iz.iz.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private Button mScan, mAdd;
    private TextView format, content;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home, container, false);

        mScan = view.findViewById(R.id.scan_button);
        mAdd = view.findViewById(R.id.add_button);

        format = view.findViewById(R.id.scan_format);
        content = view.findViewById(R.id.scan_content);

        mScan.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scan_button:

        }
    }
}
