package zknu.cn.l_thread;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadImageActivity extends Activity implements OnClickListener {

    private Button btnGetImg;
    private Button btnAbort;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private static final String ImageUrl1 = "http://images.cnitblog" +
            ".com/i/169207/201408/112229149526951.png";
    private static final String ImageUrl = "https://timgsa.baidu" +
			".com/timg?image&quality=80&size=b9999_10000&sec=1496808305231&di" +
			"=e03a45ad7c7e5ec6a7639dfb20f14a60&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu" +
			".com%2Fbaike%2Fpic%2Fitem%2Fa6efce1b9d16fdfac666d143b08f8c5494ee7b10.jpg";
    ImageLoader loader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        initView();
    }
    private void initView() {
        btnGetImg = (Button) findViewById(R.id.btnGetImg);
        btnAbort = (Button) findViewById(R.id.btnAbort);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        btnGetImg.setOnClickListener(this);
        btnAbort.setOnClickListener(this);
        mProgressBar.setVisibility(View.INVISIBLE);
        mImageView = (ImageView) findViewById(R.id.mImageView);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnGetImg) {
            loader = new ImageLoader();
            loader.execute(ImageUrl);
            btnGetImg.setEnabled(false);
            btnAbort.setEnabled(true);
        } else if (id == R.id.btnAbort) {
            loader.cancel(true);
            btnGetImg.setEnabled(true);
            btnAbort.setEnabled(false);
        }
    }

    class ImageLoader extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
            mImageView.setImageResource(R.drawable.noimg);
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imgUri = params[0];
            try {
                URL url = null;
                HttpURLConnection conn = null;
                InputStream inputStream = null;
                OutputStream outputStream = null;
                String filename = "local_temp_image";
                try {
                    url = new URL(imgUri);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.setConnectTimeout(20 * 1000);
                    inputStream = conn.getInputStream();
                    outputStream = openFileOutput(filename,
                            Context.MODE_PRIVATE);
                    byte[] data = new byte[1024];
                    int seg = 0;
                    long total = conn.getContentLength();
                    long current = 0;
                    while (!isCancelled() && (seg = inputStream.read(data)) != -1) {
                        outputStream.write(data, 0, seg);
                        current += seg;
                        int progress = (int) ((float) current / total * 100);
                        publishProgress(progress);//
                        //SystemClock.sleep(100);
                    }
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                // return BitmapFactory.decodeStream(HandlerData(ImageUrl));
                return BitmapFactory.decodeFile(getFileStreamPath(filename)
                        .getAbsolutePath());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (isCancelled())
                return;
            mProgressBar.setProgress(values[0]);
            btnGetImg.setText(values[0] + "%");
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.setProgress(0);
            btnAbort.setEnabled(false);
            btnGetImg.setEnabled(true);
            super.onPostExecute(bitmap);
        }
    }
}
