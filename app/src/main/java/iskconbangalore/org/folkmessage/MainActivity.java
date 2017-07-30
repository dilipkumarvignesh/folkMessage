package iskconbangalore.org.folkmessage;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.support.v4.app.ContextCompat;
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {
    TextToSpeech t1;
    EditText ed1;
    Button b1;
    String final_message, jNumber;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    JSONObject obj = new JSONObject();
    JSONArray jA = new JSONArray();
    Button sendBtn;
    Button btnDownload;
    Button add,addName;
    EditText txtGoogleId;
    EditText txtMessage;
    int addVariableCount = 2;

    String GoogleId;
    String message;
   // List<EditText> edittexts;
    LinearLayout rel;
    int i;
    SmsManager smsMgr;
    IntentFilter filter;
    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";
    private static final String EXTRA_NAME = "name";
    private static final String EXTRA_NUMBER = "number";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    // private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Log.d("Network", "network" + networkInfo.isConnected());
        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        txtGoogleId = (EditText) findViewById(R.id.txtGoogleId);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        add = (Button) findViewById(R.id.btnAdd);
        addName = (Button) findViewById(R.id.btnAddName);
        rel = (LinearLayout) findViewById(R.id.list);
        smsMgr = SmsManager.getDefault();

        filter = new IntentFilter(SENT);
        filter.addAction(DELIVERED);

        message = txtMessage.getText().toString();
        //  senderName = sender.getText().toString();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("info","send btn click working");
                sendSMSMessage();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                add();
            }
        });

        addName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addName();
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("info", "onClick working");

                download_excel();
            }
        });



        Log.d("Network", "network" + networkInfo.isConnected());
        if (networkInfo != null && networkInfo.isConnected()) {
            btnDownload.setEnabled(true);
        } else {
            btnDownload.setEnabled(false);
        }
       // getGoogleId("Hello");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void add() {

        message = txtMessage.getText().toString();
        message = message + "<"+addVariableCount+">";
        addVariableCount = addVariableCount + 1;
        txtMessage.setText(message);
        txtMessage.setSelection(txtMessage.getText().length());

    }

    private void addName() {

        message = txtMessage.getText().toString();
        message = message + " <name>";
//        addVariableCount = addVariableCount + 1;
        txtMessage.setText(message);
        txtMessage.setSelection(txtMessage.getText().length());

    }

    private String getGoogleId(String goog)
    {
       // goog = "https://docs.google.com/spreadsheets/d/1xk8AY8MOWiqwC3qvFEyOVN-wBdMtDW8QtirmcUkocrU/edit#gid=0";
        String[] words=goog.split("/");
        return words[5];
    }
    private void download_excel() {
        //  DownloadWebpageTask myTask = new DownloadWebpageTask();
        GoogleId = txtGoogleId.getText().toString();
        Log.d("info", "googleId=" + GoogleId);
        DownloadWebpageTask dow = new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                Toast.makeText(getApplicationContext(),
                        "Downloading Excel", Toast.LENGTH_LONG).show();
                processJson(object);

            }

        });

       // GoogleId ="https://docs.google.com/spreadsheets/d/1xk8AY8MOWiqwC3qvFEyOVN-wBdMtDW8QtirmcUkocrU/edit#gid=0";
        String final_google_id = getGoogleId(GoogleId);

        dow.execute("https://spreadsheets.google.com/tq?key=" + final_google_id);

    }

    private void processJson(JSONObject object) {
//        Toast.makeText(getApplicationContext(),
//                "Excel Downloaded", Toast.LENGTH_LONG).show();

        try {
            JSONArray rows = object.getJSONArray("rows");
            Log.d("info","rows="+rows);
            Toast.makeText(getApplicationContext(),rows.length()+
                    "Contacts Downloaded", Toast.LENGTH_LONG).show();

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                int Col_len = columns.length();
                Log.d("info","col_len="+Col_len);
                Log.d("info","row_len="+rows.length());
                JSONObject obj = new JSONObject();
                Log.d("info", "Download row=" + row);
                Log.d("info", "Download column=" + columns);
              //  ArrayList<String> al=new ArrayList<String>();
                 String temp ="";
                for (int z=0;z<Col_len;z++) {
                    Log.d("info", "ZValue=" + z);

                    Log.d("info", "ColInfo=" + columns.get(z).equals("null"));

                    if (!columns.get(z).equals("null")) {
                        Log.d("info","Column Values = "+columns.getJSONObject(z));
                        if (columns.getJSONObject(z).has("f") == true) {
                            temp = columns.getJSONObject(z).getString("f");
                            if (temp != null)
                                obj.put("" + z, temp);
                        } else if (columns.getJSONObject(z).has("v") == true) {
                            temp = columns.getJSONObject(z).getString("v");
                            if (temp != null)
                                obj.put("" + z, temp);
                        }
                        Log.d("info", "tempValue=" + temp);
                    }
                    else{
                        Log.d("info","Null values present");
                    }
                }


                jA.put(obj);

            }
            Log.d("info", "values=" + jA);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void sendSMSMessage() {
        Log.d("info","send btn click working inside SendSMS");
        Log.d("info","values="+PackageManager.PERMISSION_GRANTED);

        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else
        {
            finalSendSms();
        }
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.SEND_SMS},
//                MY_PERMISSIONS_REQUEST_SEND_SMS);
//         }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
                    finalSendSms();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
    public void finalSendSms()
    {

        final int childcount = rel.getChildCount();
        message = txtMessage.getText().toString();
        Log.d("info", "message=" + message);
//        for (int i = 0; i < childcount; i++) {
//            View v = rel.getChildAt(i);
//            EditText vEdit = (EditText) v;
//            String butVal = vEdit.getText().toString();
//            Log.d("info", "butval=" + butVal);
//            message = message.replace("<" + i + ">", butVal);
//            txtMessage.setText(message);
//            Log.d("info", "final_message=" + message);
//        }
        for (int i = 0; i < jA.length(); i++) {
            try {
                String jName="";
               jNumber="";
                String temp = "";
               final_message = message;
                JSONObject object = jA.getJSONObject(i);
                int object_count = object.length();
                for(int j=0;j<object_count;j++)
                {
                    if (j==0)
                    {
                         jName = object.get(""+j).toString();
                    }
                    else if(j==1)
                    {
                         jNumber = object.get(""+j).toString();
                    }
                    else {
                        temp = object.get("" + j).toString();
                        final_message = final_message.replace("<" + j + ">", temp);
                    }
                }

               //jName = object.get("Name").toString();
                Log.d("info", "name=" + jName);    //1xk8AY8MOWiqwC3qvFEyOVN-wBdMtDW8QtirmcUkocrU
                final_message = final_message.replace("<name>", jName);
                Log.d("info", "message=" + final_message);
              //  smsManager.sendTextMessage(jNumber, null,final_message, null, null);
               // message = message.replace(jName,"<name>");
                sendText(jNumber,jName,i,final_message);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("info","jNumber:"+jNumber);
//                        sendTextMsg(jNumber,final_message);
//                    }
//                }, 1000+i*100);
                   //message1="";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(getApplicationContext(), jA.length() + " SMS sent.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void sendTextMsg(String jNo, String final_msg)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(jNo, null,final_msg, null, null);
    }

    private void sendText(String conNumber, String conName, int requestCode,String message)
    {
        Intent sentIntent = new Intent(SENT);
        Intent deliveredIntent = new Intent(DELIVERED);

        sentIntent.putExtra(EXTRA_NUMBER, conNumber);
        sentIntent.putExtra(EXTRA_NAME, conName);

        PendingIntent sentPI = PendingIntent.getBroadcast(this, requestCode, sentIntent, 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, requestCode, deliveredIntent, 0);

        smsMgr.sendTextMessage(conNumber, null, message, sentPI, deliveredPI);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (SENT.equals(intent.getAction()))
            {
                String name = intent.getStringExtra("name");
                String number = intent.getStringExtra("number");

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                       // toastShort("SMS sent to " + name + " & " + number);
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        toastShort("Generic failure");
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        toastShort("No service");
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        toastShort("Null PDU");
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        toastShort("Radio off");
                        break;
                }
            }
            else if (DELIVERED.equals(intent.getAction()))
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        //toastShort("SMS delivered");
                        break;

                    case Activity.RESULT_CANCELED:
                        toastShort("SMS not delivered");
                        break;
                }
            }
        }
    };

    private void toastShort(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
