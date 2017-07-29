package ru.tula.ovnsi.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/*import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;*/

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;

import java.util.Iterator;

import ru.tula.ovnsi.R;


public class PoneQr extends Activity {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private FrameLayout preview;
    private TextView scanText;
    private ToggleButton startScan;
    private ImageScanner scanner;
    private boolean previewing = true;
    private String lastScannedCode;
    private net.sourceforge.zbar.Image codeImage;
    private  String retIma;
    private String strItog = "";

    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pone_qr);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        autoFocusHandler = new Handler();

        preview = (FrameLayout) findViewById(R.id.cameraPreview);


        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        resumeCamera();

        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void onPause() {
        super.onPause();

    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            //
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;

            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mPreview.refreshDrawableState();
            mCamera.stopPreview();
            mCamera.release();
            retIma = null;
            strItog = "";
            codeImage = null;
            mCamera = null;

        }
    }

    private void resumeCamera() {
        //scanText.setText(getString(R.string.scan_process_label));
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        //Camera.Size size = mCamera.getParameters().getPreviewSize();
        //mPreview.set
        preview.removeAllViews();
        preview.addView(mPreview);
        if (mCamera != null) {
            int parentX = preview.getWidth();
            int parentY = preview.getHeight();
            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            codeImage = new Image(size.width, size.height, "Y800");
            previewing = true;

            mPreview.refreshDrawableState();
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing && mCamera != null) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    private String m3362c(String str, int i) {
        String encodeToString;
        Throwable e;

        try {
            if (str.substring(0, 6).equals("ST0001")) {
                String str2;
                switch (str.charAt(6)) {
                    case 49 /*49*/:
                        str2 = new String(str.getBytes("ISO-8859-1"), "windows-1251");
                        encodeToString = Base64.encodeToString(str2.getBytes("windows-1251"), 0);
                        str = str2;
                        break;
                    case 50 /*50*/:
                        str2 = new String(str.getBytes("ISO-8859-1"), "UTF-8");
                        encodeToString = Base64.encodeToString(str2.getBytes("utf-8"), 0);

                        break;
                    case 51 /*51*/:
                        str2 = new String(str.getBytes("ISO-8859-1"), "KOI8-R");
                        encodeToString = Base64.encodeToString(str2.getBytes("KOI8-R"), 0);
                        str = str2;
                        break;
                    default:
                        encodeToString = Base64.encodeToString(str.getBytes(), 0);
                        break;
                }
            }
            encodeToString = Base64.encodeToString(str.getBytes(), 0);
            retIma = str;

        } catch (Throwable e3) {
            Throwable th = e3;
            encodeToString = null;
            e = th;
            return encodeToString;
        }
        return encodeToString;
    }

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
//            Log.d("CameraTestActivity", "onPreviewFrame data length = " + (data != null ? data.length : 0));
            codeImage.setData(data);
            int result = scanner.scanImage(codeImage);
            Iterator it = scanner.getResults().iterator();

            while (it.hasNext()) {
                String data2 = ((Symbol) it.next()).getData();
                if (!TextUtils.isEmpty(data2)) {
                    try {
                        m3362c(data2,-1);
                        //scanText.setText(retIma);
                    } catch (Throwable ignored) {
                        scanText.setText("error");
                    }
                }
            }

            lastScannedCode = retIma;
            if (lastScannedCode != null) {
                /*Toast toast =Toast.makeText(getApplicationContext(),getParamQr(lastScannedCode,"PAYERADDRESS"),Toast.LENGTH_SHORT);
                toast.show();*/

                releaseCamera();
                Intent intent = new Intent(PoneQr.this, ActivityMain.class);
//Передаем на следующую аквтиность слово в статическую переменную

                intent.putExtra("qr", getParamQr(lastScannedCode,"PAYERADDRESS"));
                //startActivity(intent);
                setResult(RESULT_OK, intent);
                finish();
            }
            camera.addCallbackBuffer(data);

        }
    };


    // Mimic continuous auto-focusing
    final Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

   /* public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       /* AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();*/
    }
    @Override
    public void onBackPressed() {
        // do something on back.
        releaseCamera();
        Intent intent = new Intent(PoneQr.this, ActivityMain.class);
//Передаем на следующую аквтиность слово в статическую переменную

        startActivity(intent);
        return;
    }

    public String getParamQr(String str, String teg){
        String[] sQr = str.toUpperCase().split("\\|");
        for (int i=1; i< sQr.length; i++){
            String[] chars = sQr[i].split("=");
            if (chars.length>1){
                if (chars[0].equals(teg)){
                    return chars[1];}
            }
        }
        return "";
    }

}
