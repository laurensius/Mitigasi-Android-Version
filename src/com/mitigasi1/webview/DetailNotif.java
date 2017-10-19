package com.mitigasi1.webview;

import android.annotation.SuppressLint;

import android.view.Window;
import android.view.WindowManager;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.*;
import android.webkit.*;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class DetailNotif extends Activity {
	
	protected ProgressDialog pDialog;
    protected Dialog dialBox;
    protected WebView webView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");
        Toast.makeText(getApplicationContext(), uri, Toast.LENGTH_LONG).show();
        webView =(WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                if(pDialog.isShowing())
                    pDialog.dismiss();
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(pDialog.isShowing())
                    pDialog.dismiss();
            }
        });
//        http://localhost/mitigasi/?mod=detailnotif
        String str = "http://192.168.0.101/mitigasi/?mod=detailnotif".concat(uri);
        webView.loadUrl(str);
        pDialog = new ProgressDialog(DetailNotif.this);
        pDialog.setMessage("Sedang memuat...");
        pDialog.setCancelable(false);   
//        dialBox = createDialogBox();
    }
    
//    private Dialog createDialogBox(){
//        dialBox = new AlertDialog.Builder(this)
//                .setTitle("Keluar")
//                .setMessage("Apakah Anda akan keluar dari Aplikasi Mitigasi Bencana ?")
//                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        finish();
//                    }
//                })
//                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialBox.dismiss();
//                    }
//                })
//                .create();
//        return dialBox;
//    }
    
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
//        dialBox.show();
//    	Intent i = new Intent(this,MainActivity.class);
//    	startActivity(i);
    	finish();
    }
}

