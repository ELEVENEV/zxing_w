package com.example.startpage.zxing;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.startpage.R;
import com.google.zxing.client.android.CaptureActivity2;

/**
 * Created by sdj on 2017/11/24.
 */

public class ScanResultActivity extends AppCompatActivity {
    private static final int SCAN_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION = 110;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        findViewById(R.id.scanTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 22) {
                    if (ContextCompat.checkSelfPermission(ScanResultActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ScanResultActivity.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                    } else {
                        startScanActivity();
                    }
                } else {
                    startScanActivity();
                }
            }
        });
    }

    private void startScanActivity() {
        Intent intent = new Intent(ScanResultActivity.this, CaptureActivity2.class);
        intent.putExtra(CaptureActivity2.USE_DEFUALT_ISBN_ACTIVITY, true);
        startActivityForResult(intent, SCAN_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanActivity();
                } else {
                    Toast.makeText(ScanResultActivity.this, "请手动打开摄像头权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int checkSelfPermission(String permission) {
        return super.checkSelfPermission(permission);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SCAN_REQUEST_CODE) {
                //Todo Handle the isbn number entered manually
                String isbn = data.getStringExtra("CaptureIsbn");
                if (!TextUtils.isEmpty(isbn)) {
                    //todo something
                    Toast.makeText(this, "解析到的内容为" + isbn, Toast.LENGTH_LONG).show();
                    if (isbn.contains("http")) {
                        Intent intent = new Intent(this, WebViewActivity1.class);
                        intent.putExtra(WebViewActivity1.RESULT, isbn);
                        startActivity(intent);
                    }
                }
            }
        }
    }
}
