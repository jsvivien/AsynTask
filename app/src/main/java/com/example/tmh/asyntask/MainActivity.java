package com.example.tmh.asyntask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Adapter mAdapter;
    private int SPAN_COUNT = 2;
    private int ID_NOTIFY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadAsynTask asynTask = new DownloadAsynTask();
        asynTask.setmContext(getApplicationContext());
        asynTask.execute();
    }


    private class DownloadAsynTask extends AsyncTask<Void, Integer, ArrayList<String>> {

        private Context mContext;

        public Context getmContext() {
            return mContext;
        }

        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

        //Xử lý Load
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return getAllImagePath();
        }

        //Cập nhật trạng thái Load
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //Xử lý kết quả Load
        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_image);
            mLayoutManager = new GridLayoutManager(getApplicationContext(), SPAN_COUNT);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new Adapter(strings);
            mRecyclerView.setAdapter(mAdapter);
            showNotification();
        }

        //Hàm thực hiện lấy ảnh từ MediaStore
        private ArrayList<String> getAllImagePath() {
            Uri mUri;
            Cursor mCursor;
            int mColumn_index_data;
            ArrayList<String> mListOfAllImages = new ArrayList<>();
            String absolutePathOfImage = null;
            mUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            mCursor = getContentResolver().query(mUri, projection, null, null, null);
            mColumn_index_data = mCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (mCursor.moveToNext()) {
                absolutePathOfImage = mCursor.getString(mColumn_index_data);
                mListOfAllImages.add(absolutePathOfImage);
            }
            return mListOfAllImages;
        }

        // Hien thi thong bao tren thanh he thong
        private void showNotification() {
            // Cài đặt intent sẽ được mở khi click vào notification
            Intent intent = new Intent(mContext, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                    intent, 0);

            // Khởi tạo đối tượng Builder cho Notification
            Notification.Builder builder = new Notification.Builder(mContext);
            builder.setAutoCancel(false);
            builder.setTicker(getString(R.string.ticker));
            builder.setContentTitle(getString(R.string.content_title));
            builder.setContentText(getString(R.string.content_text));
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(contentIntent);
            builder.setOngoing(true);
            builder.setSubText(getString(R.string.sub_text));   //API level 16

            // Khởi tạo đối tượng Notification
            Notification notification = builder.build(); //API level 16
            // Thiết đặt kiểu của cho Notification
            notification.flags = Notification.FLAG_AUTO_CANCEL; // Auto cancel

            // Nhồi đối tượng Notification được tạo trên vào đối tượng quản lý
            // NotificationManager
            NotificationManager nm = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(ID_NOTIFY, notification);
        }
    }

}
