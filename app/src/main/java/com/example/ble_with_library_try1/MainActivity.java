package com.example.ble_with_library_try1;

import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "jullu";
    private BleManager bleManager;
    private TextView status;
    private List<Model> list = new ArrayList<>();
    private AvailableDeviceAdapter availableDeviceAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        availableDeviceAdapter = new AvailableDeviceAdapter(getApplicationContext(), list);
        status = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(availableDeviceAdapter);

        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);


        bleManager = BleManager.getInstance();

        bleManager.init(getApplication());

        Log.d(TAG, "is ble supported?  : " + bleManager.isSupportBle());

        bleManager.enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                scanAndConnect();


            }

        });

        findViewById(R.id.scanbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Scan();


            }

        });


    }


    void connectWithmac(String mac) {
        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {

                try {
                    status.setText("Conncetion try started");
                    Log.d(TAG, "onStartConnect: ");
                } catch (Exception e) {
                    Log.d(TAG, "onStartConnect: faileddddddddd  " + e);
                }
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {

                try {
                    Log.d(TAG, "onConnectFail: " + exception);
                    status.setText("connection failed with error as \n" + exception);
                } catch (Exception e) {
                    Log.d(TAG, "onConnectFail: trycatch " + e);
                }
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int _status) {

                try {
                    status.setText("Conncetion successful with :" + bleDevice.getName());
                    Log.d(TAG, "Conncetion successful with :" + bleDevice.getName());
                } catch (Exception e) {
                    Log.d(TAG, "onConnectionSuccess: faileddddddddd  " + e);
                }

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int _status) {
                try {
                    status.setText("Conncetion disconnected with :" + bleDevice.getName());
                    Log.d(TAG, "Conncetion disconnected with :" + bleDevice.getName());
                } catch (Exception e) {
                    Log.d(TAG, "onDisconnected: faileddddddddd  " + e);
                }

            }
        });
    }

    void connectWithDevice(BleDevice bleDevice) {

        bleManager.connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {

            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {

            }
        });
    }

    void scanAndConnect() {

        BleManager.getInstance().scanAndConnect(new BleScanAndConnectCallback() {
            @Override
            public void onScanStarted(boolean success) {
                if (success) status.setText("'successful'");
                if (!success) status.setText("not successful");

            }

            @Override
            public void onScanning(BleDevice bleDevice) {

                Log.d(TAG, "onScanning: " + bleDevice.getName());
                status.setText("ble device" + bleDevice.getName());
                Toast.makeText(MainActivity.this, "scanning", Toast.LENGTH_SHORT).show();
                list.add(new Model(bleDevice.getName(), bleDevice.getMac()));
                availableDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(BleDevice scanResult) {


                if (scanResult == null) Log.d(TAG, "onScanFinished: nulllllllllll");
                else {
                    Log.d(TAG, "onScanFinished: " + scanResult.getName());
                    status.setText(scanResult.getName());
                }
            }

            @Override
            public void onStartConnect() {
                Log.d(TAG, "onStartConnect: ");
                status.setText("connection started");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {

                Log.d(TAG, "onConnectFail: " + exception);
                status.setText(exception.toString());

            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int _status) {

                Log.d(TAG, "onConnectSuccess: " + _status);
                status.setText("success" + _status);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int _status) {
                Log.d(TAG, "onDisConnected: ");
                status.setText("disconn");
            }
        });
    }

    public void Scan() {

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Log.d(TAG, "onScanStarted: " + success);
                status.setText("Scan Started");
            }


            @Override
            public void onScanning(BleDevice bleDevice) {
                Log.d(TAG, "onScanning: ");
                try {
                    Toast.makeText(MainActivity.this, "Scanning", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onScanning: " + bleDevice.getName());
                    list.add(new Model(bleDevice.getName(), bleDevice.getMac()));
                    list.add(new Model("name", "mac"));
                    availableDeviceAdapter.notifyDataSetChanged();
                    status.setText("Scanning");
                } catch (Exception e) {
                    Log.d(TAG, "onScanning: " + e);
                }
            }


            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

                status.setText("Scan Ended");
                Log.d(TAG, "onScanFinished: " + scanResultList.toArray().toString());
                list.add(new Model("name1", "mac1"));
                availableDeviceAdapter.notifyDataSetChanged();

                if (scanResultList.isEmpty()) status.setText(" no device found");
            }
        });
    }

}