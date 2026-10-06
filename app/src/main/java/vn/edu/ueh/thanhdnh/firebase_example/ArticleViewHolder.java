package vn.edu.ueh.thanhdnh.firebase_example;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle;
    public TextView tvContent;
    public TextView tvViewCount;
    public ImageView imgCover;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvTitle = itemView.findViewById(R.id.tvTitle);
        this.tvContent = itemView.findViewById(R.id.tvContent);
        this.tvViewCount = itemView.findViewById(R.id.tvViewCount);
        this.imgCover = itemView.findViewById(R.id.imgCover);
    }
}
