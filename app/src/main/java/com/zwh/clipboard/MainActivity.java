package com.zwh.clipboard;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {

    private TextView mMsgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMsgTv = (TextView) findViewById(R.id.msg_tv);
    }

    @Override
    protected void onResume(){
        super.onResume();

        mMsgTv.setText(getFilePath());

        new Thread(new Runnable() {
            @Override
            public void run() {
                readFile();
            }
        }).start();
    }

    private void readFile(){
        File file = new File(getFilePath());
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            inputStream.read(fileContent);
            final String fileContentStr = new String(fileContent);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    copy(fileContentStr, MainActivity.this);
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)

                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        }
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public void copy(String content, Context context){
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());

        mMsgTv.append("\n"+content.trim());
    }
    /**
     * 实现粘贴功能
     * add by wangqianzhou
     * @param context
     * @return
     */
    public String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    private String getFilePath(){
        return Environment.getExternalStorageDirectory().getPath()+"/clip";
    }
}
