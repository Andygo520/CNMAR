package com.example.administrator.cnmar.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRCodeActivity extends Activity {
    private TextView tvTitle;
    private NetworkImageView imageView;
    //    判断是从哪个Fragment跳转到该页面的判断标志
    private int FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        AppExit.getInstance().addActivity(this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        imageView = (NetworkImageView) findViewById(R.id.ivQRCode);

        Intent intent = getIntent();
        FLAG = intent.getIntExtra("flag", 999);
//        显示企业信息的二维码
        if (FLAG == 1) {
            //        得到从列表界面传递过来的名称以及二维码的相对路径
            String companyName = intent.getStringExtra("companyName");
            String path1 = intent.getStringExtra("path1");

            tvTitle.setText(companyName);
            //       获取图片的路径，路径=绝对路径+相对路径
            String path = UrlHelper.URL_IMAGE + path1;
            VolleyHelper.showImageByUrl(QRCodeActivity.this, path, imageView);
        }
//        显示产品信息的二维码
        else if (FLAG == 2) {
            //        得到从列表界面传递过来的名称以及二维码的相对路径
            final String productName = intent.getStringExtra("productName");
            String path1 = intent.getStringExtra("path1");

            tvTitle.setText(productName);
            //       获取图片的路径，路径=绝对路径+相对路径
            String path = UrlHelper.URL_IMAGE + path1;
            VolleyHelper.showImageByUrl(QRCodeActivity.this, path, imageView);

        }

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                File file = new File(Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_PICTURES).getAbsolutePath()+ "/cnmar/");
//                file.mkdirs();// 如果/storage/sdcard0/CNMAR/这个文件夹不存在，就创建该文件夹
                Bitmap bitmap = ((BitmapDrawable) ((ImageView) imageView).getDrawable()).getBitmap();
//                saveImage();
                // 先拼接好一个路径：在内存卡/或是手机内存上做好文件夹
                String filePath = Environment.getExternalStorageDirectory() + "/cnmar/";
                Log.d("filePath",filePath);
                saveImageToGallery(QRCodeActivity.this,bitmap);
                return true;
            }
        });


    }

    public void saveImage() {
        // 先拼接好一个路径：在内存卡/或是手机内存上做好文件夹
        String filePath = Environment.getExternalStorageDirectory() + "/cnmar/";
        File localFile = new File(filePath);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        //拼接好文件路径和名称
        File finalImageFile = new File(localFile, System.currentTimeMillis() + ".jpg");
        if (finalImageFile.exists()) {
            finalImageFile.delete();
        }
        try {
            finalImageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(finalImageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = ((BitmapDrawable) ((ImageView) imageView).getDrawable()).getBitmap();
        if (bitmap == null) {
            Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        try {
//            FileOutputStream fos = new FileOutputStream(finalImageFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(this, "图片保存在：" + finalImageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //发广播告诉相册有图片需要更新，这样可以在图册下看到保存的图片了
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(finalImageFile);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    public  void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "cnmar");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(this, "图片保存在：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }
}
