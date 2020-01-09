package com.sr.team.calculator;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private TextView tvFnum, tvSnum;
    private int count=0;
    private String expression="";
    private String text="";
    private Double result=0.0;
    private Button mode,toggle,square,xpowy,log,sin,cos,tan,sqrt,fact;
    private int toggleMode=1;
    private int angleMode=1;
     ImageView imvScan;
    InterstitialAd mInterstitialAd;
    private ClipboardManager myClipboard;
    private ClipData myClip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*banner ad*/

        AdView mAdView = findViewById(R.id.adView);
        MobileAds.initialize(this, getResources().getString(R.string.banner_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /*mInterstitial Ad*/
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        tvFnum =  findViewById(R.id.tv_fnum);
        tvSnum =  findViewById(R.id.tv_snum);
        mode =  findViewById(R.id.mode);
        toggle =  findViewById(R.id.toggle);
        square = findViewById(R.id.square);
        xpowy =  findViewById(R.id.xpowy);
        log =  findViewById(R.id.log);
        sin =  findViewById(R.id.sin);
        cos = findViewById(R.id.cos);
        tan =  findViewById(R.id.tan);
        sqrt=  findViewById(R.id.sqrt);
        fact =  findViewById(R.id.factorial);
        imvScan =  findViewById(R.id.imvScan);

        tvSnum.setText("");

        //tags to change the mode from degree to radian and vice versa
        mode.setTag(1);
        //tags to change the names of the buttons performing different operations
        toggle.setTag(1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adShow();
        onAddLodded();
    }

    public void onClick(View v)
    {
        toggleMode=(int)toggle.getTag();
        angleMode=((int)mode.getTag());
        switch (v.getId()) {

            case R.id.toggle:
                //change the button text if switch button is clicked
                if(toggleMode==1)
                {
                    toggle.setTag(2);
                    square.setText(R.string.cube);
                    xpowy.setText(R.string.tenpow);
                    log.setText(R.string.naturalLog);
                    sin.setText(R.string.sininv);
                    cos.setText(R.string.cosinv);
                    tan.setText(R.string.taninv);
                    sqrt.setText(R.string.cuberoot);
                    fact.setText(R.string.Mod);
                }
                else if(toggleMode==2)
                {
                    toggle.setTag(3);
                    square.setText(R.string.square);
                    xpowy.setText(R.string.epown);
                    log.setText(R.string.log);
                    sin.setText(R.string.hyperbolicSine);
                    cos.setText(R.string.hyperbolicCosine);
                    tan.setText(R.string.hyperbolicTan);
                    sqrt.setText(R.string.inverse);
                    fact.setText(R.string.factorial);
                }
                else if(toggleMode==3)
                {
                    toggle.setTag(1);
                    sin.setText(R.string.sin);
                    cos.setText(R.string.cos);
                    tan.setText(R.string.tan);
                    sqrt.setText(R.string.sqrt);
                    xpowy.setText(R.string.xpown);
                }
                break;

            case R.id.mode:
                //change the angle property for trignometric operations if mode button is clicked
                if(angleMode==1)
                {
                    mode.setTag(2);
                    mode.setText(R.string.mode2);
                }
                else
                {
                    mode.setTag(1);
                    mode.setText(R.string.mode1);
                }
                break;

            case R.id.num0:
                tvSnum.setText(tvSnum.getText() + "0");
                break;

            case R.id.num1:
                tvSnum.setText(tvSnum.getText() + "1");
                break;

            case R.id.num2:
                tvSnum.setText(tvSnum.getText() + "2");
                break;

            case R.id.num3:
                tvSnum.setText(tvSnum.getText() + "3");
                break;


            case R.id.num4:
                tvSnum.setText(tvSnum.getText() + "4");
                break;

            case R.id.num5:
                tvSnum.setText(tvSnum.getText() + "5");
                break;

            case R.id.num6:
                tvSnum.setText(tvSnum.getText() + "6");
                break;

            case R.id.num7:
                tvSnum.setText(tvSnum.getText() + "7");
                break;

            case R.id.num8:
                tvSnum.setText(tvSnum.getText() + "8");
                break;

            case R.id.num9:
                tvSnum.setText(tvSnum.getText() + "9");
                break;

            case R.id.pi:
                tvSnum.setText(tvSnum.getText() + "pi");
                break;

            case R.id.dot:
                if (count == 0 && tvSnum.length() != 0) {
                    tvSnum.setText(tvSnum.getText() + ".");
                    count++;
                }
                break;

            case R.id.clear:
                tvFnum.setText("");
                tvSnum.setText("");
                count = 0;
                expression = "";
                break;

            case R.id.backSpace:
                text= tvSnum.getText().toString();
                if(text.length()>0)
                {
                    if(text.endsWith("."))
                    {
                        count=0;
                    }
                    String newText=text.substring(0,text.length()-1);
                    //to delete the data contained in the brackets at once
                    if(text.endsWith(")"))
                    {
                        char []a=text.toCharArray();
                        int pos=a.length-2;
                        int counter=1;
                        //to find the opening bracket position
                        for(int i=a.length-2;i>=0;i--)
                        {
                            if(a[i]==')')
                            {
                                counter++;
                            }
                            else if(a[i]=='(')
                            {
                                counter--;
                            }
                            //if decimal is deleted b/w brackets then count should be zero
                            else if(a[i]=='.')
                            {
                                count=0;
                            }
                            //if opening bracket pair for the last bracket is found
                            if(counter==0)
                            {
                                pos=i;
                                break;
                            }
                        }
                        newText=text.substring(0,pos);
                    }
                    //if tvSnum edit text contains only - sign or sqrt or any other text functions
                    // at last then clear the Text view tvSnum
                    if(newText.equals("-")||newText.endsWith("sqrt")||newText.endsWith("log")||newText.endsWith("ln")
                            ||newText.endsWith("sin")||newText.endsWith("asin")||newText.endsWith("asind")||newText.endsWith("sinh")
                            ||newText.endsWith("cos")||newText.endsWith("acos")||newText.endsWith("acosd")||newText.endsWith("cosh")
                            ||newText.endsWith("tan")||newText.endsWith("atan")||newText.endsWith("atand")||newText.endsWith("tanh")
                            ||newText.endsWith("cbrt"))
                    {
                        newText="";
                    }
                    //if pow sign is left at the last or divide sign
                    else if(newText.endsWith("^")||newText.endsWith("/"))
                        newText=newText.substring(0,newText.length()-1);
                    else if(newText.endsWith("pi")||newText.endsWith("e^"))
                        newText=newText.substring(0,newText.length()-2);
                    tvSnum.setText(newText);
                }
                break;

            case R.id.plus:
                operationClicked("+");
                break;

            case R.id.minus:
                operationClicked("-");
                break;

            case R.id.divide:
                operationClicked("/");
                break;

            case R.id.multiply:
                operationClicked("*");
                break;

            case R.id.sqrt:
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    toggleMode=(int)toggle.getTag();
                    if(toggleMode==1)
                        tvSnum.setText("sqrt(" + text + ")");
                    else if(toggleMode==2)
                        tvSnum.setText("cbrt(" + text + ")");
                    else
                        tvSnum.setText("1/(" + text + ")");
                }
                break;

            case R.id.square:
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    if(toggleMode==2)
                        tvSnum.setText("(" + text + ")^3");
                    else
                        tvSnum.setText("(" + text + ")^2");
                }
                break;

            case R.id.xpowy:
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    if(toggleMode==1)
                        tvSnum.setText("(" + text + ")^");
                    else if(toggleMode==2)
                        tvSnum.setText("10^(" + text + ")");
                    else
                        tvSnum.setText("e^(" + text + ")");
                }
                break;

            case R.id.log:
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    if(toggleMode==2)
                        tvSnum.setText("ln(" + text + ")");
                    else
                        tvSnum.setText("log(" + text + ")");
                }
                break;

            case R.id.factorial:
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    if(toggleMode==2)
                    {
                        tvFnum.setText("(" + text + ")%");
                        tvSnum.setText("");
                    }
                    else
                    {
                        String res="";
                        try
                        {
                            CalculateFactorial cf=new CalculateFactorial();
                            int []arr=cf.factorial((int)Double.parseDouble(String.valueOf(new ExtendedDoubleEvaluator().evaluate(text))));
                            int res_size=cf.getRes();
                            if(res_size>20)
                            {
                                for (int i=res_size-1; i>=res_size-20; i--)
                                {
                                    if(i==res_size-2)
                                        res+=".";
                                    res+=arr[i];
                                }
                                res+="E"+(res_size-1);
                            }
                            else
                            {
                                for (int i=res_size-1; i>=0; i--)
                                {
                                    res+=arr[i];
                                }
                            }
                            tvSnum.setText(res);
                        }
                        catch (Exception e)
                        {
                            if(e.toString().contains("ArrayIndexOutOfBoundsException"))
                            {
                                tvSnum.setText("Result too big!");
                            }
                            else
                                tvSnum.setText("Invalid!!");
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case R.id.sin:
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    if(angleMode==1)
                    {
                        double angle=Math.toRadians(new ExtendedDoubleEvaluator().evaluate(text));
                        if(toggleMode==1)
                            tvSnum.setText("sin(" + angle + ")");
                        else if(toggleMode==2)
                            tvSnum.setText("asind(" + text + ")");
                        else
                            tvSnum.setText("sinh(" + text + ")");
                    }
                    else
                    {
                        if(toggleMode==1)
                            tvSnum.setText("sin(" + text + ")");
                        else if(toggleMode==2)
                            tvSnum.setText("asin(" + text + ")");
                        else
                            tvSnum.setText("sinh(" + text + ")");
                    }
                }
                break;

            case R.id.cos:
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    if(angleMode==1)
                    {
                        double angle=Math.toRadians(new ExtendedDoubleEvaluator().evaluate(text));
                        if(toggleMode==1)
                            tvSnum.setText("cos(" + angle + ")");
                        else if(toggleMode==2)
                            tvSnum.setText("acosd(" + text + ")");
                        else
                            tvSnum.setText("cosh(" + text + ")");
                    }
                    else
                    {
                        if(toggleMode==1)
                            tvSnum.setText("cos(" + text + ")");
                        else if(toggleMode==2)
                            tvSnum.setText("acos(" + text + ")");
                        else
                            tvSnum.setText("cosh(" + text + ")");
                    }
                }
                break;

            case R.id.tan:
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    if(angleMode==1)
                    {
                        double angle=Math.toRadians(new ExtendedDoubleEvaluator().evaluate(text));
                        if(toggleMode==1)
                            tvSnum.setText("tan(" + angle + ")");
                        else if(toggleMode==2)
                            tvSnum.setText("atand(" + text + ")");
                        else
                            tvSnum.setText("tanh(" + text + ")");
                    }
                    else
                    {
                        if(toggleMode==1)
                            tvSnum.setText("tan(" + text + ")");
                        else if(toggleMode==2)
                            tvSnum.setText("atan(" + text + ")");
                        else
                            tvSnum.setText("tanh(" + text + ")");
                    }
                }
                break;

            case R.id.posneg:
                if (tvSnum.length() != 0) {
                    String s = tvSnum.getText().toString();
                    char arr[] = s.toCharArray();
                    if (arr[0] == '-')
                        tvSnum.setText(s.substring(1, s.length()));
                    else
                        tvSnum.setText("-" + s);
                }
                break;

            case R.id.equal:
                /*for more knowledge on DoubleEvaluator and its tutorial go to the below link
                http://javaluator.sourceforge.net/en/home/*/
                if (tvSnum.length() != 0) {
                    text = tvSnum.getText().toString();
                    expression = tvFnum.getText().toString() + text;
                }
                tvFnum.setText("");
                if (expression.length() == 0)
                    expression = "0.0";
                try {
                    //evaluate the expression
                    result = new ExtendedDoubleEvaluator().evaluate(expression);
                    //insert expression and result in sqlite database if expression is valid and not 0.0
                    if (String.valueOf(result).equals("6.123233995736766E-17"))
                    {
                        result=0.0;
                        tvSnum.setText(result + "");
                    }
                    else if(String.valueOf(result).equals("1.633123935319537E16"))
                        tvSnum.setText("infinity");
                    else
                        tvSnum.setText(result + "");
                    if (!expression.equals("0.0"))
                        Log.d("TestHistory","Add History");
                     /*   dbHelper.insert("SCIENTIFIC", expression + " = " + result);*/
                } catch (Exception e) {
                    tvSnum.setText("Invalid Expression");
                    tvFnum.setText("");
                    expression = "";
                    e.printStackTrace();
                }
                break;

            case R.id.openBracket:
                tvFnum.setText(tvFnum.getText() + "(");
                break;
            case R.id.closeBracket:
                if(tvSnum.length()!=0)
                    tvFnum.setText(tvFnum.getText() + tvSnum.getText().toString()+ ")");
                else
                    tvFnum.setText(tvFnum.getText() + ")");
                break;



            case R.id.imvScan:
                showPopupScanType();
                break;
        }
    }

    private void operationClicked(String op) {
        if (tvSnum.length() != 0) {
            String text = tvSnum.getText().toString();
            tvFnum.setText(tvFnum.getText() + text + op);
            tvSnum.setText("");
            count = 0;
        } else {
            String text = tvFnum.getText().toString();
            if (text.length() > 0) {
                String newText = text.substring(0, text.length() - 1) + op;
                tvFnum.setText(newText);
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");
                Toast.makeText(this, "Cancelled scan", Toast.LENGTH_LONG).show();
                onAddLodded();
                adShow();
            } else {
                Log.e("Scan", "Scanned");
                showPopup(""+result.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void openScanner(Collection<String> scannerType, int promptId) {
        IntentIntegrator integrator =  new IntentIntegrator(this);
        // use forSupportFragment or forFragment method to use fragments instead of activity
        integrator.setDesiredBarcodeFormats(scannerType);
        integrator.setPrompt(this.getString(promptId));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }


    private void showPopup(final String scanresult) {
        final Dialog d = new Dialog(this, android.R.style.Theme_Translucent);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.scanner_result);
        d.setCancelable(false);

        final Button finish = d.findViewById(R.id.finish);
        final TextView result = d.findViewById(R.id.Scanresult);
        final ImageView imvCopy = d.findViewById(R.id.imvCopy);

        result.setText("Scanned: "+scanresult);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddLodded();
                adShow();
                d.dismiss();
            }
        });

        imvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                myClip = ClipData.newPlainText("Result", scanresult);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied",Toast.LENGTH_SHORT).show();
            }
        });


        d.show();
    }
    private void showPopupScanType() {
        final Dialog d = new Dialog(this, android.R.style.Theme_Translucent);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.scanner_type);
        d.setCancelable(false);

        final Button barcode = d.findViewById(R.id.barcode);
        final Button qrcode = d.findViewById(R.id.qrcode);
        final Button finish = d.findViewById(R.id.finish);



        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner(IntentIntegrator.ONE_D_CODE_TYPES, R.string.barcodescanner);
                d.dismiss();
            }
        });

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner(IntentIntegrator.QR_CODE_TYPES, R.string.qr_code_scanner);
                d.dismiss();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddLodded();
                adShow();
                d.dismiss();
            }
        });


        d.show();
    }

    public void adShow() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void onAddLodded() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

    }
}