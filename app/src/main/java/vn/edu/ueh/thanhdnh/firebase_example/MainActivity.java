package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseFirestore db;
    private Button btAdd, btShow;
    private EditText etTitle, etContent, etImgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        btAdd = findViewById(R.id.btAdd);
        btShow = findViewById(R.id.btShow);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        etImgName = findViewById(R.id.etImgName);

        btAdd.setOnClickListener(this);
        btShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btAdd) {
            String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
            String content = etContent.getText() != null ? etContent.getText().toString().trim() : "";
            String imgName = etImgName.getText() != null ? etImgName.getText().toString().trim() : "img01";

            if (title.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề bài viết!", Toast.LENGTH_SHORT).show();
                return;
            }

            Article newArticle = new Article(title, content, imgName, 0);
            db.collection("articles").add(newArticle)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Đã thêm bài viết vào Firebase!", Toast.LENGTH_SHORT).show();
                        etTitle.setText("");
                        etContent.setText("");
                        etImgName.setText("img01");
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi thêm: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        } else if (view.getId() == R.id.btShow) {
            Intent intent = new Intent(this, ShowDataActivity.class);
            startActivity(intent);
        }
    }
}
