package com.trashapp.aitrashbin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;

public class MainActivity extends AppCompatActivity {
    public Handler mHandler;
    private int version = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView fire_tip = findViewById(R.id.textView4);
        fire_tip.setVisibility(View.GONE);
    }

    public void clicked(View view) {
        final TextView textView = findViewById(R.id.textView2);
        final Button button = findViewById(R.id.button2);
        final TextView fire_tip = findViewById(R.id.textView4);
        button.setClickable(false);
        button.setText("获取中...");
        class Mhandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj.toString().equals("状态：未满")) {
                    textView.setTextColor(Color.parseColor("#008000"));
                } else if (msg.obj.toString().equals("状态：有大物件")) {
                    textView.setTextColor(Color.parseColor("#cf7500"));
                } else {
                    textView.setTextColor(Color.RED);
                }
                textView.setText(msg.obj.toString());
                button.setClickable(true);
                button.setText("点击重新获取");
            }
        }
        textView.setText("获取中...");
        mHandler = new Mhandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res, res2;
                Message msg = Message.obtain();
                try {
                    res = Jsoup.connect("http://58.87.111.240:8000/").get().text();
                    msg.what = 1;
                    msg.obj = res;
                } catch (Exception e) {
                    res = e.toString();
                    msg.what = 2;
                    msg.obj = "获取信息失败，请点击重试。\n" + res;
                }
                try {
                    res2 = Jsoup.connect("http://58.87.111.240:8000/fire.htm").get().text();
                }catch (Exception e){
                    res2 = e.toString();
                }
                System.out.println(res2);
                if (res2.equals("1")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fire_tip.setVisibility(View.VISIBLE);
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fire_tip.setVisibility(View.GONE);
                        }
                    });
                }
                mHandler.sendMessage(msg);
            }
        }).start();


    }

    public void info_clicked(View view) {

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

    public void update_check(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = "";
                try {
                    res = Jsoup.connect("http://58.87.111.240:8000/update.htm").get().text();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "连接出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if (res.isEmpty()) {
                    return;
                }
                String get_version = res.substring(0, res.indexOf(' '));
                final String download_url = res.substring(res.indexOf(' ') + 1);
                if (Integer.parseInt(get_version) > version) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "查找到新版本", Toast.LENGTH_SHORT).show();
                            Uri uri = Uri.parse(download_url);
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        }
                    });

                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();


    }
//    public void clicked(View view){
//        TextView textView = findViewById(R.id.textView2);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String text = editText.getText().toString();
//        textView.setText(text);
//    }
//    public void sendMessage(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }
}
