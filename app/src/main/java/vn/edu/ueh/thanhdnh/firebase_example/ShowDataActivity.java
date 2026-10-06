package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {
    private static final String TAG = "ShowDataActivity";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_data);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách bài viết");
        }

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.reclyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ArticleAdapter(this, articles, new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article, int position) {
                // Tăng view local
                article.increaseView();
                adapter.notifyItemChanged(position);

                // Cập nhật lượt xem lên Firestore
                if (article.getId() != null) {
                    db.collection("articles").document(article.getId())
                            .update("viewCount", article.getViewCount())
                            .addOnFailureListener(e -> Log.e(TAG, "Lỗi cập nhật viewCount", e));
                }

                // Mở màn hình chi tiết bài viết
                Intent intent = new Intent(ShowDataActivity.this, DetailActivity.class);
                intent.putExtra("article_key", article);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        // Lắng nghe dữ liệu thời gian thực từ Firestore collection "articles"
        db.collection("articles").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "Lỗi khi lấy dữ liệu: ", error);
                    return;
                }

                if (snapshots != null) {
                    articles.clear();
                    for (QueryDocumentSnapshot q : snapshots) {
                        Article article = q.toObject(Article.class);
                        article.setId(q.getId());
                        articles.add(article);
                    }
                    adapter.update(articles);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
