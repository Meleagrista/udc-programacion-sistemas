package es.udc.ps.p24dorio;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.udc.ps.p24dorio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    private ArrayList<Article> ArticlesData;
    private ActivityMainBinding binding;

    private ActivityResultLauncher<Intent> myStartActivityForResult;

    static String TAG = "_TAG";
    String ACTIV = "MainActivity";

    static String ARTICLE = "01";
    static String POSITION = "02";
    static String DELETE = "03";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, ACTIV + " onCreate");
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.recyclerView = binding.categoriesRv;

        this.binding.addButton.setOnClickListener(this);
        this.binding.deleteButton.setOnClickListener(this);

        this.myStartActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Article updatedArticle = data.getParcelableExtra(ARTICLE);
                            int position = data.getIntExtra(POSITION, -1);
                            boolean delete = data.getBooleanExtra(DELETE, false);

                            if (position != -1) {
                                if (delete) {
                                    this.deleteArticle(position);
                                } else {
                                    if (updatedArticle != null) {
                                        this.ArticlesData.set(position, updatedArticle);
                                    }
                                }
                                this.initRecycler(this.ArticlesData);
                            }
                        }
                    }}
        );
    }

    private void initRecycler(ArrayList<Article> articles) {
        ArticlesAdapter mAdapter = new ArticlesAdapter(articles);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setClickListener((view, position) -> {
            //Toast.makeText(getApplicationContext(), "item " + position,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
            Article sendArticle = ArticlesData.get(position);
            intent.putExtra(ARTICLE, sendArticle);
            intent.putExtra(POSITION, (int) position);
            this.myStartActivityForResult.launch(intent);
        });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.addButton) {
            showDialog();
        } else if(v == binding.deleteButton) {
            deleteArticles();
        }
    }

    private void addArticles(int max) {
        if(ArticlesData == null) {
            ArticlesData = new ArrayList<>();
        }
        for (int i=0; i< max; i++) {
            ArticlesData.add(new Article(i));
        }
        initRecycler(ArticlesData);
    }
    private void deleteArticles() {
        int data_size = ArticlesData.size();
        for (int i=0; i < data_size; i++) {
            deleteArticle(0);
        }
        initRecycler(ArticlesData);
    }

    private void deleteArticle(int item) {
        ArticlesData.remove(item);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.input);

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton(R.string.accept, (dialog, which) -> {
            String userInput = input.getText().toString();
            try {
                int numberOfElements = Integer.parseInt(userInput);
                addArticles(numberOfElements); // Pass the integer to the method
            } catch (NumberFormatException e) {
                // Handle invalid input
                Toast.makeText(MainActivity.this, R.string.invalid, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, ACTIV + " onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, ACTIV + " onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, ACTIV + " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, ACTIV + " onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, ACTIV + " onDestroy");
    }
}