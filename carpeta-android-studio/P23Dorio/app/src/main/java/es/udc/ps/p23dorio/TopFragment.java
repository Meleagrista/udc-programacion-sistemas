package es.udc.ps.p23dorio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TopFragment extends Fragment implements View.OnClickListener {

    static String TAG = "_TAG";
    Button butA, butB, butC, butD;

    EditText edit_text;

    public interface OnButtonListener {
        void onText(String text);
        void onUrl(String url);

        void onDestroyDown();
    }

    OnButtonListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnButtonListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " implement OnButtonListener");
        }
    }

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_top, container, false);

        butA = view.findViewById(R.id.but_A);
        butB = view.findViewById(R.id.but_B);
        butC = view.findViewById(R.id.but_C);
        butD = view.findViewById(R.id.but_D);

        edit_text = view.findViewById(R.id.edit_url);

        butA.setOnClickListener(this);
        butB.setOnClickListener(this);
        butC.setOnClickListener(this);
        butD.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        String url = edit_text.getText().toString();

            if ((v == butA) || (v == butB)) {
                Button but = (Button) v;
                listener.onText(but.getText().toString());
            } else if(v == butC) {
                listener.onUrl(url);
            } else if(v == butD) {
                listener.onDestroyDown();
            }
        }
}