package com.example.admin.changeheadpic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;

import com.loopj.android.image.SmartImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * �Զ���ؼ�
 * ��װ���ܣ�
 * ��1��Բ��ͼƬ ���� onDraw(Canvas canvas) & Bitmap getCroppedBitmap(Bitmap bmp, int radius)
 * ��2��ͼƬ�ϴ� ���� My_Post(String filePath , final Context context) & My_Base64Coder_Post(String base64code , final Context context)
 * ��3�� �����ļ� ���� CreateFile()
 * ��4�� ͼƬչʾ ���� ShowView(Intent picdata , My_View my_icon , Context context , int post_way)
 */

public class MyView extends SmartImageView {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //���ƿؼ�����
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if (null == b) {
            return;
        }
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);
        int w = getWidth();
        Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    //����Բ�οؼ��Ŀ�߷���
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
                sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
         return output;
    }

    /*//ͼƬ�ϴ�����1����ͳͼƬ�ϴ���ʽ����ͼƬ��Դֱ��Post��������
    public void My_Post(String filePath, final Context context, String url) {
        //
		//����˵�� ��
		//filePath : ͼƬ��Դ�ĵ�ַ
		//context  : ��ǰActivity
		//url      : Post��ַ
		//
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", new File(filePath));    //name ����post�ֶε��ֶ���
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {

                    }

                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Toast.makeText(context, responseInfo.result, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //ͼƬ�ϴ�����2������Base64Code ���뽫ͼƬ��Դ��Դ���ַ�������ʽPost��������
    public void My_Base64Coder_Post(String base64code, final Context context, String url) {
		//
		// ����˵����
		// base64code : ͼƬ��Դ ת����� base64code �ַ���
		// context    : ��ǰActivity
		// url        : Post��ַ
		///
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", base64code);    //name ����post�ֶε��ֶ��� , ���ź�������post��ֵ
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Toast.makeText(context, responseInfo.result, Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    public void CreateFile() {
        //������ֻ��ڴ��в�������Ϊ��my_icon�����ļ����򴴽���
        File destDir = new File(Environment.getExternalStorageDirectory() + "/my_icon");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    public void SaveBitmap(Bitmap mBitmap) throws IOException {
        //�ڡ�my_icon���ļ����д��� ��my_icon.jpg��ͼƬ�ļ�����ü���ͼƬ��Դ
        File f = new File(Environment.getExternalStorageDirectory() + "/my_icon/my_icon.jpg");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //��ʾͼƬ���ϴ�
    @SuppressWarnings("deprecation")
    public void ShowView(Intent picdata, MyView my_icon, Context context, int post_way, String url) {
		/**
		 * ����˵����
		 * my_icon  : �ؼ�ʵ��
		 * context  : ��ǰActivity 
		 * post_way : ͼƬ�ϴ���ʽ��1/2��
		 */
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            try {
                SaveBitmap(photo);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = new BitmapDrawable(photo);
            my_icon.setImageDrawable(drawable);

            switch (post_way) {
                case 1:
                    //My_Post(Environment.getExternalStorageDirectory() + "/my_icon/my_icon.jpg", context, url);// �ϴ�ͼƬ
                    break;
                case 2:
                    //��ͼƬ��Դת��Ϊ Base64Code �ַ���
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                    byte[] b = stream.toByteArray();
                    String base64code = new String(Base64Coder.encodeLines(b));

                    //My_Base64Coder_Post(base64code, context, url);
                    break;

                default:
                    break;
            }
        }
    }
}
