package zknu.cn.l_thread;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnHandler, btnAsyncTask, btnRunOnUiThread, btnHandlerPost;
    private TextView tvHandler, tvAsyncTask, tvRunOnUiThread, tvHandlerPost;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHandler = (Button) findViewById(R.id.handler);
        btnAsyncTask = (Button) findViewById(R.id.AsyncTask);
        btnRunOnUiThread = (Button) findViewById(R.id.runOnUiThread);
        btnHandlerPost = (Button) findViewById(R.id.HandlerPost);
        tvHandler = (TextView) findViewById(R.id.tv_hander);
        tvAsyncTask = (TextView) findViewById(R.id.tv_AsyncTask);
        tvRunOnUiThread = (TextView) findViewById(R.id.tv_runOnUiThread);
        tvHandlerPost = (TextView) findViewById(R.id.tv_HandlerPost);

        btnHandler.setOnClickListener(this);
        btnAsyncTask.setOnClickListener(this);
        btnRunOnUiThread.setOnClickListener(this);
        btnHandlerPost.setOnClickListener(this);
        initHandler();
    }
    private void initHandler() {
        handler=new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case 1:
                        Bundle bundle=msg.getData();
                        String str=bundle.getString("data");
                        tvHandler.setText(str);
                        break;
                }
            }
        };
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.handler:
                handler();
                break;

            case R.id.AsyncTask:
                asyncTask();
                break;
            case R.id.runOnUiThread:
                runOnUiThread();
                break;
            case R.id.HandlerPost:
               handlerPost();
                break;

        }
    }
    private void handlerPost() {
        new Thread(){
            int count = 0;
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        count++;
                        if (count>50) {
                            count=0;
                        }else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    tvHandlerPost.setText(String.valueOf(count));
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void runOnUiThread() {
        new Thread(){
            int count = 0;
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        count++;
                        if (count>50) {
                            count=0;
                        }else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    tvRunOnUiThread.setText(String.valueOf(count));
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void handler() {
        new Thread() {
            int count = 0;
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        count++;
                        if (count>50) {
                            count=0;
                        }else {
                            Message msg=handler.obtainMessage();
                            Bundle bundle=new Bundle();
                            bundle.putString("data", String.valueOf(count));
                            msg.setData(bundle);
                            msg.what=1;
                            handler.sendMessage(msg);
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void asyncTask() {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, LoadImageActivity.class);
        startActivity(intent);
    }
}
