package es.udc.ps.p23dorio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DownFragment extends Fragment {

    static String TAG = "_TAG";
    WebView myWebView;
    String text, url;
    TextView myText;

    public DownFragment() {
        // Required empty public constructor
        Log.d(TAG, "DowFragment created.");
    }

    public void setText(String text){
        this.text = text;
    }

    public void setUrl(String url){
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_down, container, false);
        myWebView = view.findViewById(R.id.webview);
        if (this.url != null){
            loadUrl(this.url);
        }
        myText = view.findViewById(R.id.view_text_down);
        if (this.text != null){
            loadText(this.text);
        }
        return view;
    }

    public void loadUrl(String url){
        myWebView.loadUrl(url);
    }

    public void loadText(String text){ myText.setText(text); }

    public void onDestroyItself() { Log.d(TAG, "Fragment onDestroyItself called."); }
}