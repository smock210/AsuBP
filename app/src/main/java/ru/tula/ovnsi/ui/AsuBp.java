package ru.tula.ovnsi.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import ru.tula.ovnsi.R;

public class AsuBp extends Activity {
    String param = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asu_bp);
        Bundle extras = getIntent().getExtras();

        param = extras.getString("addres");
        Toast toast =Toast.makeText(getApplicationContext(),extras.getString("addres"),Toast.LENGTH_SHORT);
        toast.show();

        WebView wv = (WebView) findViewById(R.id.webKit);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setSavePassword(true);
        wv.getSettings().setSaveFormData(true);
        wv.setWebViewClient(new MyWebViewClient());

        wv.loadUrl("http://www.asubp.net/BillingAcountsCard.aspx");
    }


    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if(url.startsWith("http://www.asubp.net")) {

                view.loadUrl(url);

                return true;
            }else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
            if (url.equals("http://www.asubp.net/")) {


                    view.loadUrl("javascript:(function() { " +

                            "var panel = document.getElementById('CheckBox_KONS');" +
                            "var button = document.getElementById('Button1');" +
                            "var forms = document.getElementById('form1');" +
                            "panel.checked = true;" +
                            //"button.click();"+
                    /*"button.submit();"+*/
                            //"forms.submit();"+
                            "document.getElementById('Button1').click();" +
                            "return;" +
                            "})()");

            }
            if(url.equals("http://www.asubp.net/GeneralError.aspx?Action=Autoriz")){
                view.loadUrl("http://www.asubp.net/");
            }

            if (url.equals("http://www.asubp.net/About.aspx")) {

                view.loadUrl("javascript:(function() { " +
                        //"button.click();"+
                    /*"button.submit();"+*/
                        //"forms.submit();"+
                        "__doPostBack('ctl00$NavigationMenu','M_INSTRUMENTS\\\\M_INSTRUMENTS_BILLING_ACCOUNTS_SHEARCH');" +
                        "return;" +
                        "})()");
            }

            //__doPostBack('ctl00$NavigationMenu','M_INSTRUMENTS\\M_INSTRUMENTS_BILLING_ACCOUNTS_SHEARCH')
            if (url.equals( "http://www.asubp.net/BillingAcountsCard.aspx")){
                if (param.length()>0) {

                    String[] mass = param.split(",");

                    if (mass.length>3) {

                        view.loadUrl("javascript:(function() { " +
                                "document.getElementById('MainContent_TextBox_city').value ='"+mass[0]+"';" +
                                "document.getElementById('MainContent_TextBox_street').value ='"+mass[1]+"';" +
                                "document.getElementById('MainContent_TextBox_home').value = '"+mass[2]+"';" +
                                "document.getElementById('MainContent_TextBox_flat').value ='"+mass[3]+"';" +
                                "var button = document.getElementById('MainContent_Button_OK');" +

                                //"button.click();"+
                    /*"button.submit();"+*/
                                //"forms.submit();"+
                                "document.getElementById('Button1').click();" +
                                "return;" +
                                "})()");
                    }


                    Toast toast = Toast.makeText(getApplicationContext(), param, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }


    }


    @Override
    public void onBackPressed() {
        // do something on back.

        this.finish();
        return;
    }

}
