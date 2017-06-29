package com.example.admin.changeheadpic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private String head_portrait_url = "http://p4.so.qhimg.com/t012acbd2ad8c398334.jpg"; //头像初始网络资源地址
    private String post_url = "http://192.168.1.182:8081/admin/upload_file"; //图像上传post地址
    private MyView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = (MyView)findViewById(R.id.head_portrait);
        myView.setImageUrl(head_portrait_url);
        myView.CreateFile();
        myView.setOnClickListener(new PicOnClickListener());
    }

    /**
     * 选择照片的对话框
     */
    private class PicOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("设置头像...")
                    .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, 1);
                        }
                    })
                    .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/my_photo.jpg")));
                            startActivityForResult(intent, 2);//这里会报错，原因是这里是模拟器的url
                        }
                    }).show();
        }
    }

    /**
     * 回调处理的方法
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                startPhotoZoom(data.getData());
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory() + "/my_photo.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    myView.ShowView(data, myView, MainActivity.this, 1, post_url);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, requestCode, data);
    }

    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }
}
