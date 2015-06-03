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
import android.widget.FrameLayout;

import java.io.IOException;

import ru.gubkin.lk.arduinoworksheet.connect.bt.BluetoothHandler;


public class MainActivity extends ActionBarActivity {
    String makerStudio = "98:D3:31:50:4A:1B";
    private static final String BUNDLE_NT_HANDLER = "bt_handler";
    public static boolean debug = false;
    private Context context;
    private Toolbar toolbar;
    private FrameLayout mainFrame;
    private BluetoothDevice connectedDevice;
    private BluetoothHandler handler;
    private ProgressDialog progressDialog;
    private MainActivityFragment componentsFragment;

    private int location;
    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                connectedDevice = device;
            }
            if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                connectedDevice = null;
                checkDeviceState();
                showDevicesList();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                connectedDevice = null;
                checkDeviceState();
                showDevicesList();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mainFrame = (FrameLayout) findViewById(R.id.content_frame);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        handler = new BluetoothHandler(this);


        if (debug) {
            startMainFragment();
        } else {
            showDevicesList();
        }
//        tryToConnect(null);
        context = this;

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter1);
        this.registerReceiver(mReceiver, filter2);
        this.registerReceiver(mReceiver, filter3);
    }

    public void tryToConnect(BluetoothDevice device) {
        if (!debug) {
            showProgressDialog();
            try {
                handler.tryToConnect(device.getAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startMainFragment() {
        hideProgressDialog();
        if (!debug) {
            toolbar.setTitle(connectedDevice.getName());
            storeDevice();
        }
        componentsFragment = new MainActivityFragment(handler);

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, componentsFragment).commit();
    }

    public void connectFallback() {
        showDevicesList();
    }

    public void showDevicesList(){

        toolbar.setTitle("Доступные устройства");

        SearchDevicesFragment searchFragment = new SearchDevicesFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, searchFragment).addToBackStack(null).commit();
    }

    public void storeDevice() {
        String deviceKey = "DEVICE";
        getPreferences(MODE_PRIVATE).edit().putString(deviceKey, connectedDevice.getAddress()).commit();
    }

    public void checkDeviceState() {
        String deviceKey = "DEVICE";
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String alreadyConnectedId = preferences.getString(deviceKey, "");
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

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    public BluetoothDevice getConnectedDevice() {
        return connectedDevice;
    }
    public FrameLayout getMainFrame() {
        return mainFrame;
    }
}
