package com.mitigasi1.webview;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.*;
import android.webkit.*;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	
	protected ProgressDialog pDialog;
    protected Dialog dialBox;
    protected WebView webView;
    protected ImageView imageView;
    protected LinearLayout llOfline;
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        startService(new Intent(getBaseContext(), ServiceNotifikasi.class));
        imageView = (ImageView)findViewById(R.id.imageview);
        llOfline = (LinearLayout)findViewById(R.id.llOffline);
        imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.reload();
				
			}
		});
        webView =(WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl("http://192.168.0.101/mitigasi/index.php");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
            	online();
                if(pDialog.isShowing())
                    pDialog.dismiss();
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }
                
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCod,String description, String failingUrl) {
            	offline();
            }
        });
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Sedang memuat...");
        pDialog.setCancelable(false);   
        dialBox = createDialogBox();
        
    }
    
    private Dialog createDialogBox(){
        dialBox = new AlertDialog.Builder(this)
                .setTitle("Keluar")
                .setMessage("Apakah Anda akan keluar dari Aplikasi Mitigasi Bencana ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialBox.dismiss();
                    }
                })
                .create();
        return dialBox;
    }
    
    @Override
    public void onPause() {
       super.onPause();
    }

    @Override
    public void onResume() {
       super.onResume();
    }

    @Override
    public void onDestroy() {
       super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        dialBox.show();
    }
    
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    
    void online(){
    	webView.setVisibility(0x00);
        llOfline.setVisibility(0x08);
    }
    
    void offline(){
    	webView.setVisibility(0x08);
        llOfline.setVisibility(0x00);
    }
    
    
}

