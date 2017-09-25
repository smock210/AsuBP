package ru.tula.ovnsi.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ru.tula.ovnsi.R;
import ru.tula.ovnsi.utils.BoxAdapter;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Identity.NAMESPACE;

public class AsuBp extends Activity {
    String param = "";
    ListView listView;
    BoxAdapter boxAdapter;
    ArrayList<stringAddresList> test = new ArrayList<stringAddresList>();
    private static final String NAMESPACE = "http://www.asubp.net/webservice";
    private static final String METHOD_NAME = "BillingFromAdress"; //метод веб-сервиса
    private static final String SOAP_ACTION = "http://www.asubp.net/webservice/BillingFromAdress"; // берем из xml страницы веб-сервиса
    private static String URL = "http://www.asubp.net/ubpservice.asmx?wsdl"; //  адрес веб-сервиса


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asu_bp);
        Bundle extras = getIntent().getExtras();
        //test.add(new stringAddresList("tula", "11", "home"));
        boxAdapter = new BoxAdapter(this, test);
        listView = (ListView) findViewById((R.id.listBP));

        param = extras.getString("addres");
        /*ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);*/
        /*Toast tot = Toast.makeText(getApplicationContext(),param,Toast.LENGTH_SHORT);

        tot.show();*/
        new MyTask(this).execute();



        //listView.setAdapter(adapter);

        listView.setAdapter(boxAdapter);

        //System.out.println("********Count31 :0555 " );


        /*Toast toast =Toast.makeText(getApplicationContext(),extras.getString("addres"),Toast.LENGTH_SHORT);
        toast.show();*/

        /*WebView wv = (WebView) findViewById(R.id.webKit);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setSavePassword(true);
        wv.getSettings().setSaveFormData(true);
        wv.setWebViewClient(new MyWebViewClient());

        wv.loadUrl("http://www.asubp.net/BillingAcountsCard.aspx");*/
    }


    /*private class MyWebViewClient extends WebViewClient
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
                          /*  "document.getElementById('Button1').click();" +
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
                      /*  "__doPostBack('ctl00$NavigationMenu','M_INSTRUMENTS\\\\M_INSTRUMENTS_BILLING_ACCOUNTS_SHEARCH');" +
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
                               /* "document.getElementById('Button1').click();" +
                                "return;" +
                                "})()");
                    }


                    Toast toast = Toast.makeText(getApplicationContext(), param, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }


    }*/


    @Override
    public void onBackPressed() {
        // do something on back.

        this.finish();
        return;
    }

    public class MyTask extends AsyncTask<Void, Void, ArrayList<stringAddresList>> {


        String response = "";
        ArrayList<stringAddresList> retur = new ArrayList<stringAddresList>();

        private ProgressDialog mProgressDialog;
        private Context c;

        public MyTask(Context c) {
            this.c = c;
            mProgressDialog = new ProgressDialog(c);
        }

        public void onPreExecute() {
            super.onPreExecute();

            //mProgressDialog = new ProgressDialog(c);
            mProgressDialog.setMessage("Поиск...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);

            mProgressDialog.show();
        }

        @Override
        protected ArrayList<stringAddresList> doInBackground(Void... arg0) {

            final SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


            request.addProperty("adress", param); // веб-сервис принимает один параметр text в виде строки

            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);

            envelope.dotNet = true;

            try {

                List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                //headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("admin:123".getBytes()))); // авторизация на веб-сервисе

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60000);
                androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

                /*SoapObject result = (SoapObject) envelope.bodyIn;
                response = result.toString();*/

                SoapObject resultRequestSOAP = (SoapObject) envelope.bodyIn;

                SoapObject root = (SoapObject) resultRequestSOAP.getProperty(0);
                SoapObject s_deals = (SoapObject) resultRequestSOAP.getProperty("BillingFromAdressResult");
                for (int i = 0; i < s_deals.getPropertyCount(); i++) {
                    Object property = s_deals.getProperty(i);
                    //System.out.println("********Count : " + property.toString());
                    if (property instanceof SoapObject) {
                        if (((SoapObject)property).getPropertyCount()>0) {
                            SoapObject category_list = (SoapObject) property;
                            String org = category_list.getProperty("value1").toString();
                            String num = category_list.getProperty("value2").toString();
                            String adr = category_list.getProperty("value4").toString();
                            //System.out.println("********Count1 : " + num);
                            stringAddresList srtAdd = new stringAddresList(num, org, adr);
                            retur.add(srtAdd);
                        }
                    }
                }

            } catch (IOException e) {
                response = e.toString();
                //Toast.makeText(AsuBp.this, response, Toast.LENGTH_SHORT).show();
                retur.add(new stringAddresList("","",response));
            } catch (XmlPullParserException e) {
                response = e.toString();
                retur.add(new stringAddresList("","",response));
                //Toast.makeText(AsuBp.this, response, Toast.LENGTH_SHORT).show();
            }
            return retur;
        }

        @Override
        public void onPostExecute(ArrayList<stringAddresList> res) {

            if (res.size()> 0) {

                test.clear();
                for (stringAddresList a:res
                     ) {
                    test.add(new stringAddresList(a.idBp,a.orgBp, a.addres));
                }
                boxAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(AsuBp.this, "Подходящих подписок не найдено", Toast.LENGTH_SHORT).show();
            }
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

        }
    }

}
