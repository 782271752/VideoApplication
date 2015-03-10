package com.li.videoapplication.activity;




import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.DialogUtils;
import com.li.videoapplication.utils.HttpUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.SharePreferenceUtil;
import com.li.videoapplication.utils.ToastUtils;

import org.apache.http.NameValuePair;

import java.util.List;

public class WebviewActivity extends Activity {
    /** Called when the activity is first created. */
	
	private WebView webView;
//	private TextView titletv;
    private String code="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.web);
        
        
//        webView =new WebView(this);
        webView=(WebView)findViewById(R.id.webview);
//        titletv=(TextView)findViewById(R.id.title);
        webView.getSettings().setJavaScriptEnabled(true);
        
//        Intent intent=this.getIntent();
//        Bundle bundle=intent.getExtras();
//        String url=bundle.getString("url");
        String url="https://openapi.youku.com/v2/oauth2/authorize?client_id="+ExApplication.id+"&response_type=code&redirect_uri="+ExApplication.url+"&state=123";
//        String title=intent.getExtras().getString("title");
//        titletv.setText(title);
        webView.loadUrl(url);

        
        
        webView.setWebViewClient(new WebViewClient()
        {

			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				view.loadUrl(url);
                Log.e("----",url);
                String urlString=url;
                String aString[]=urlString.split("code=");
                String bString[]=aString[1].split("&state=123");
//	for (int i = 0; i < bString.length; i++) {
                System.out.println(bString[0]);
                code=bString[0];
                DialogUtils.showWaitDialog(WebviewActivity.this);
	            new PostCode().execute();
                return true;
			}
        	
        });
    }
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack())
		{
			webView.goBack();
			return true;
		}
		else {
			return super.onKeyDown(keyCode, event);
			}
		}

    @Override
    protected void onStop() {
        super.onStop();
        webView.onPause();
    }

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (webView != null) {
			webView.clearHistory();
			webView.removeAllViewsInLayout();
			webView.clearDisappearingChildren();
			webView.clearFocus();
			webView.clearView();
			webView.destroy();
		}
	}
    static String result="";
    private class PostCode extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {

            result= JsonHelper.getAccessToken(code);
            Log.e("result",result);
            if(!result.equals("")){

                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogUtils.cancelWaitDialog();
            if (s.equals("s")){
                SharePreferenceUtil.setPreference(WebviewActivity.this,"token",result);
                ExApplication.accesstoken=result;

                ToastUtils.showToast(WebviewActivity.this,"授权成功，请重新点击上传");
                WebviewActivity.this.finish();

            }else{
                ToastUtils.showToast(WebviewActivity.this,"失败");
            }

        }
    }
	
		
	}