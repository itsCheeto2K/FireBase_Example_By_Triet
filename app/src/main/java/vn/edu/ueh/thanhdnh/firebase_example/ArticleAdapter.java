package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private Context context;
    private List<Article> articleList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Article article, int position);
    }

    public ArticleAdapter(Context context, List<Article> articleList, OnItemClickListener listener) {
        this.context = context;
        this.articleList = articleList;
        this.listener = listener;
    }

    public void update(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);

        holder.tvTitle.setText(article.getTitle() != null ? article.getTitle() : "");
        holder.tvContent.setText(article.getContent() != null ? article.getContent() : "");
        holder.tvViewCount.setText("Views: " + article.getViewCount());
        holder.imgCover.setImageResource(article.getImageResId(context));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(article, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList != null ? articleList.size() : 0;
    }
}
