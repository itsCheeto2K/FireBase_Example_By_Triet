package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private ImageView ivDetailImage;
    private TextView tvDetailTitle;
    private TextView tvDetailView;
    private TextView tvDetailContent;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailContent = findViewById(R.id.tvDetailContent);
        tvDetailView = findViewById(R.id.tvDetailView);

        Article article = (Article) getIntent().getSerializableExtra("article_key");

        if (article != null) {
            // Hiển thị dữ liệu ban đầu
            tvDetailTitle.setText(article.getTitle() != null ? article.getTitle() : "");
            tvDetailView.setText("Views: " + article.getViewCount());
            tvDetailContent.setText(article.getContent() != null ? article.getContent() : "");
            ivDetailImage.setImageResource(article.getImageResId(this));

            // Lắng nghe realtime từ Firebase khi có thay đổi (title, content, views, ảnh...)
            if (article.getId() != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                listenerRegistration = db.collection("articles").document(article.getId())
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Log.e(TAG, "Lỗi khi lắng nghe chi tiết bài viết: ", error);
                                    return;
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    Article updatedArticle = snapshot.toObject(Article.class);
                                    if (updatedArticle != null) {
                                        updatedArticle.setId(snapshot.getId());
                                        tvDetailTitle.setText(updatedArticle.getTitle() != null ? updatedArticle.getTitle() : "");
                                        tvDetailView.setText("Views: " + updatedArticle.getViewCount());
                                        tvDetailContent.setText(updatedArticle.getContent() != null ? updatedArticle.getContent() : "");
                                        ivDetailImage.setImageResource(updatedArticle.getImageResId(DetailActivity.this));
                                    }
                                }
                            }
                        });
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết bài viết");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
