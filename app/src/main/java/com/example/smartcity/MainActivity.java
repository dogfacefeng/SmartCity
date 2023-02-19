package com.example.smartcity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.example.smartcity.util.HttpCallBack;
import com.example.smartcity.util.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button button;
    private ImageView imgView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityResultLauncher<Void> launcher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
            // TODO: 2023/2/20  追求简单，使用Glide加载即可
//                Glide.with(MainActivity.this).load(result).into(imgView);
                // TODO: 2023/2/20 直接复制图片到缓存目录，不用写任何权限代码
                File file = transForBitMapToFile(result);
                if(file.exists()){
                    OkHttpUtil.doPostFile(file, new HttpCallBack() {
                        @Override
                        public void onSuccess(String json) {
                            Log.d(TAG, "onSuccess: " + json);
                        }

                        @Override
                        public void onError(String error) {
                            Log.d(TAG, "onError: " + error);
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "清重新打开", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ActivityResultLauncher<String> launcher1 = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                // TODO: 2023/2/20 直接使用Glide加载也可以
//                Glide.with(MainActivity.this).load(result).into(imgView);
                new Thread(() -> {
                    try {
                        FutureTarget<File> fileFutureTarget = Glide.with(MainActivity.this).asFile().load(result).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        File file = fileFutureTarget.get();
                        File file1 = new File(getCacheDir() + "1.png");
                        if (file1.exists()) {
                            file1.delete();
                        }
                        file.renameTo(file1);
                        OkHttpUtil.doPostFile(file1, new HttpCallBack() {
                            @Override
                            public void onSuccess(String json) {
                                Log.d(TAG, "onSuccess: " + json);
                            }

                            @Override
                            public void onError(String error) {
                                Log.d(TAG, "onError: " + error);
                            }
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();


            }
        });
        initView(launcher, launcher1);
    }

    private void initView(ActivityResultLauncher<Void> launcher, ActivityResultLauncher<String> launcher1) {
        button = findViewById(R.id.button);
        imgView = findViewById(R.id.my_imageView);
        textView = findViewById(R.id.http_text);
        button.setOnClickListener(view -> {
//            launcher.launch(null);
            launcher1.launch("image/*");
        });

    }

    public static File transForBitMapToFile(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        File file = new File(MyApplication.getContext().getCacheDir(), "1.png");
        if(file.exists()){
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


}