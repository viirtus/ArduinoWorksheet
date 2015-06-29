package ru.gubkin.lk.arduinoworksheet;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shamanland.fab.FloatingActionButton;

import ru.gubkin.lk.arduinoworksheet.connect.ConnectionHandler;
import ru.gubkin.lk.arduinoworksheet.connect.bt.BluetoothHandler;
import ru.gubkin.lk.arduinoworksheet.connect.tcp.TcpHandler;


public class MainActivity extends ActionBarActivity {
    private static final String BUNDLE_NT_HANDLER = "bt_handler";
    public static boolean debug = true;
    String makerStudio = "98:D3:31:50:4A:1B";
    private Context applicationContext;
    private Toolbar toolbar;
    private FloatingActionButton addButton;
    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                showDevicesList();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                showDevicesList();
            }
        }
    };
    private FrameLayout mainFrame;
    private static ConnectionHandler handler;
    private ProgressDialog progressDialog;
    private IntentFilter filter2;
    private IntentFilter filter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mainFrame = (FrameLayout) findViewById(R.id.content_frame);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        addButton = ((FloatingActionButton) findViewById(R.id.add_f_button));

        if (debug) {
            startMainFragment();
        } else {
            showDevicesList();
        }
//        tryToConnectBluetooth(null);
        applicationContext = getApplicationContext();

        filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, filter2);
        registerReceiver(mReceiver, filter3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public void tryToConnectBluetooth(String device) {
        if (!debug) {
            showProgressDialog();
            handler = new BluetoothHandler(this, device);
            handler.connectRequest();
        }
    }

    public void tryToConnectTcp(String info) {
        showProgressDialog();
        String[] info_ = info.split(":");
        String ip = info_[0];
        int port = Integer.parseInt(info_[1]);
        handler = new TcpHandler(this, ip, port);
        handler.connectRequest();
    }

    public void startMainFragment() {
        hideProgressDialog();
        if (!debug) {
            toolbar.setTitle("");
        }

        addButton.setVisibility(View.GONE);

        MainActivityFragment componentsFragment = new MainActivityFragment();
        componentsFragment.setRetainInstance(true);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, componentsFragment).commit();
    }

    public void connectionFallback(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        hideProgressDialog();
        showDevicesList();
    }

    public void showDevicesList(){
        String c = getResources().getString(R.string.unit_celsius);
        toolbar.setTitle("Доступные устройства" + c);
        addButton.setVisibility(View.VISIBLE);
        SearchDevicesFragment searchFragment = new SearchDevicesFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, searchFragment).addToBackStack(null).commit();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up servo_control_button_add, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProgressDialog () {
        progressDialog.setMessage("Подключение...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static ConnectionHandler getHandler() {
        return handler;
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    public FrameLayout getMainFrame() {
        return mainFrame;
    }
}


