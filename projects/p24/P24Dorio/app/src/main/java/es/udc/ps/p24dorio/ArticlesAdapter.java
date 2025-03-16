package es.udc.ps.p24dorio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.udc.ps.p24dorio.databinding.ArticleTileBinding;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {
    private final ArrayList<Article> mDataset;
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ArticleTileBinding binding;

        public MyViewHolder(ArticleTileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Article article) {
            binding.tileTitle.setText(article.getTitle());
            binding.tileSubtitle.setText(article.getSubtitle());
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
    public ArticlesAdapter(ArrayList<Article> myDataset) {
        mDataset = myDataset;
    }
    @NonNull
    @Override
    public ArticlesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArticleTileBinding binding = ArticleTileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    private static OnItemClickListener clickListener;
    public void setClickListener(OnItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }
}
