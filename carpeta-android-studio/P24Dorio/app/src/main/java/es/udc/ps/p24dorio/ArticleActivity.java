package es.udc.ps.p24dorio;

import static es.udc.ps.p24dorio.MainActivity.ARTICLE;
import static es.udc.ps.p24dorio.MainActivity.DELETE;
import static es.udc.ps.p24dorio.MainActivity.POSITION;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import es.udc.ps.p24dorio.databinding.ActivityArticleBinding;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {

    static String TAG = "_TAG";
    // Name of the activity
    static String ACTIV = "Article";
    private ActivityArticleBinding binding;

    private Article article = null;
    private int articleId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, ACTIV + " onCreate");

        this.binding.backButton.setOnClickListener(this);
        this.binding.editButton.setOnClickListener(this);
        this.binding.removeButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.articleId = bundle.getInt(POSITION, -1);
            this.article = (Article) bundle.getParcelable(ARTICLE);
        }
        assert this.article != null;
        this.binding.articleTitle.setText(this.article.getTitle());
        this.binding.articleSubtitle.setText(this.article.getSubtitle());
        this.binding.articleContent.setText(this.article.getContent());
    }

    @Override
    public void onClick(View v) {
        this.article.setTitle(this.binding.articleTitle.getText().toString());
        this.article.setSubtitle(this.binding.articleSubtitle.getText().toString());
        this.article.setContent(this.binding.articleContent.getText().toString());

        if (v == binding.backButton) {
            Intent intent = new Intent();
            intent.putExtra(ARTICLE, this.article);
            intent.putExtra(POSITION, this.articleId);
            intent.putExtra(DELETE, false);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if(v == binding.editButton) {
            boolean state = !this.binding.articleTitle.isEnabled();
            this.binding.articleTitle.setEnabled(state);
            this.binding.articleSubtitle.setEnabled(state);
            this.binding.articleContent.setEnabled(state);
        } else if(v == binding.removeButton) {
            Intent intent = new Intent();
            intent.putExtra(POSITION, this.articleId);
            intent.putExtra(DELETE, true);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}
