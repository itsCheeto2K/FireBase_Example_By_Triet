package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Article implements Serializable {
    private String id;
    private String title;
    private String content;
    private int imgCover;
    private String imgName;
    private int viewCount;
    private long timestamp = System.currentTimeMillis();

    public Article() {
        // Constructor rỗng cần thiết cho Firebase Firestore
    }

    public Article(String title, String content, int imgCover, int viewCount) {
        this.title = title;
        this.content = content;
        this.imgCover = imgCover;
        this.viewCount = viewCount;
        this.timestamp = System.currentTimeMillis();
    }

    public Article(String title, String content, String imgName, int viewCount) {
        this.title = title;
        this.content = content;
        this.imgName = imgName;
        this.viewCount = viewCount;
        this.timestamp = System.currentTimeMillis();
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImgCover() {
        return imgCover;
    }

    public void setImgCover(int imgCover) {
        this.imgCover = imgCover;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void increaseView() {
        this.viewCount++;
    }

    public int getImageResId(Context context) {
        if (imgCover != 0) {
            return imgCover;
        }
        if (imgName != null && !imgName.trim().isEmpty() && context != null) {
            int resId = context.getResources().getIdentifier(imgName.trim(), "drawable", context.getPackageName());
            if (resId != 0) {
                return resId;
            }
        }
        return R.drawable.img01;
    }
}
