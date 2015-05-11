package ru.gubkin.lk.arduinoworksheet;

import android.app.FragmentManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.gubkin.lk.arduinoworksheet.bt.BluetoothHandler;


public class MainActivity extends ActionBarActivity {
    private static final String BUNDLE_NT_HANDLER = "bt_handler";
    private Context context;
    private Toolbar toolbar;
    private FrameLayout mainFrame;
    private BluetoothDevice connectedDevice;
    static private BluetoothHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mainFrame = (FrameLayout) findViewById(R.id.content_frame);
        setSupportActionBar(toolbar);

        handler = new BluetoothHandler(this);
//        showDevicesList();
        attachFragmentForDevice(null);
        context = this;
    }

    public void attachFragmentForDevice(BluetoothDevice device) {
        connectedDevice = device;
        MainActivityFragment componentsFragment = new MainActivityFragment();
//
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().add(R.id.content_frame, componentsFragment).addToBackStack(null).commit();
    }

    public void connectFallback() {
        showDevicesList();
    }

    public void showDevicesList(){
        SearchDevicesFragment searchFragment = new SearchDevicesFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().add(R.id.content_frame, searchFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        String regexString =  ".*temp:(.*?)";
        Pattern placeHolderPattern = Pattern.compile(regexString);
        Matcher matcher = placeHolderPattern.matcher("temp: 22 C");
        while(matcher.find()) {
           Log.i("PATTERN", "MATCHED start: " + matcher.start());
           Log.i("PATTERN", "MATCHED end: " + matcher.end());
            int end = matcher.end();
            String endLine = "temp: 22 C".substring(end);
           Log.i("PATTERN", "MATCHED" + endLine);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static BluetoothHandler getBluetoothHandler() {
        return handler;
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
    public BluetoothDevice getConnectedDevice() {
        return connectedDevice;
    }
    public FrameLayout getMainFrame() {
        return mainFrame;
    }
}
