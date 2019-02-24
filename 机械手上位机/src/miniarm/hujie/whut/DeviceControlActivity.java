/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package miniarm.hujie.whut;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    /*private TextView mDataField;*/
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    
    /*EditText edtSend;
	ScrollView svResult;
	Button btnSend;*/
    private TextView servoTx0;
    private SeekBar servoSBar0;
    
    private TextView servoTx1;
    private SeekBar servoSBar1;
    
    private TextView servoTx2;
    private SeekBar servoSBar2;
    
    private TextView servoTx3;
    private SeekBar servoSBar3;
    private int currentProcess;
    
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            
            Log.e(TAG, "mBluetoothLeService is okay");
            // Automatically connects to the device upon successful start-up initialization.
            //mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //连接成功
            	Log.e(TAG, "Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //断开连接
                mConnected = false;
                invalidateOptionsMenu();
                seekBarsEanble(false);
                /*btnSend.setEnabled(false);*/
               /* clearUI();*/
            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //可以开始干活了
            {
            	mConnected = true;
            	/*mDataField.setText("");
            	ShowDialog();
            	btnSend.setEnabled(true);*/
            	seekBarsEanble(true);
            	Log.e(TAG, "In what we need");
            	invalidateOptionsMenu();
            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //收到数据
            	Log.e(TAG, "RECV DATA");
            	String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            	if (data != null) {
                	/*if (mDataField.length() > 500)
                		mDataField.setText("");
                    mDataField.append(data); 
                    svResult.post(new Runnable() {
            			public void run() {
            				svResult.fullScroll(ScrollView.FOCUS_DOWN);
            			}
            		});*/
                }
            }
        }
    };
    
    protected void seekBarsEanble(boolean flag) {
		servoSBar0.setEnabled(flag);
		servoSBar1.setEnabled(flag);
		servoSBar2.setEnabled(flag);
		servoSBar3.setEnabled(flag);
	}
    
    
    /*private void clearUI() {
        mDataField.setText(R.string.no_data);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {                                        //初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
       /* mDataField = (TextView) findViewById(R.id.data_value);
        edtSend = (EditText) this.findViewById(R.id.edtSend);
        edtSend.setText("www.jnhuamao.cn");
        svResult = (ScrollView) this.findViewById(R.id.svResult);
        
        btnSend = (Button) this.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new ClickEvent());
		btnSend.setEnabled(false);*/

        servoSBar0 = (SeekBar) findViewById(R.id.ServoSkBar0);
		servoTx0 = (TextView) findViewById(R.id.servoTx0);
		servoSBar1 = (SeekBar) findViewById(R.id.ServoSkBar1);
		servoTx1 = (TextView) findViewById(R.id.servoTx1);
		servoSBar2 = (SeekBar) findViewById(R.id.ServoSkBar2);
		servoTx2 = (TextView) findViewById(R.id.servoTx2);
		servoSBar3 = (SeekBar) findViewById(R.id.ServoSkBar3);
		servoTx3 = (TextView) findViewById(R.id.servoTx3);
		seekBarsEanble(false);
		
		servoSBar0.setMax(180); // 设置滑块最大值
		servoSBar0.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			// 设置监听器
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				String writeValue = "000";
				if(currentProcess < 10){
					writeValue = "a" + "00" + String.valueOf(currentProcess) + "0" + "z";
				}else if(currentProcess >= 10 && currentProcess < 100){
					writeValue = "a" + "0" + String.valueOf(currentProcess) + "0" + "z";
				}else if(currentProcess > 100){
					writeValue = "a" + String.valueOf(currentProcess) + "0" + "z";
				}
				mBluetoothLeService.WriteValue(writeValue);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currentProcess = progress;
				servoTx0.setText("底部舵机运动值为: " + String.valueOf(progress)); // 显示当前值
			}
		});
		
		servoSBar1.setMax(180); // 设置滑块最大值
		servoSBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			// 设置监听器
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				String writeValue = "000";
				if(currentProcess <= 70){
					writeValue = "a" + "0701" + "z";
				}else if(currentProcess > 70 && currentProcess < 100){
					writeValue = "a" + "0" + String.valueOf(currentProcess) + "1" + "z";
				}else if(currentProcess >= 100 && currentProcess < 120){
					writeValue = "a" + String.valueOf(currentProcess) + "1" + "z";
				}else if(currentProcess >= 120){
					writeValue = "a" + "1201" + "z";
				}
				if(currentProcess <= 70){
					servoTx1.setText("头部舵机运动值已至最小 ！"); // 显示当前值
				}else if(currentProcess >= 120){
					servoTx1.setText("头部舵机运动值已至最大！"); // 显示当前值
				}else{
					servoTx1.setText("头部舵机运动值为: " + String.valueOf(currentProcess)); // 显示当前值
				}
				mBluetoothLeService.WriteValue(writeValue);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currentProcess = progress;
			}
		});
		
		servoSBar2.setMax(180); // 设置滑块最大值
		servoSBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			// 设置监听器
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				String writeValue = "000";
				if(currentProcess <= 30){
					writeValue = "a" + "0302" + "z";
				}else if(currentProcess > 30 && currentProcess < 100){
					writeValue = "a" + "0" + String.valueOf(currentProcess) + "2" + "z";
				}else if(currentProcess >= 100 && currentProcess < 150){
					writeValue = "a" + String.valueOf(currentProcess) + "2" + "z";
				}else if(currentProcess >= 150){
					writeValue = "a" + "1502" + "z";
				}
				if(currentProcess <= 30){
					servoTx2.setText("左部舵机运动值已至最小 ！"); // 显示当前值
				}else if(currentProcess >= 150){
					servoTx2.setText("左部舵机运动值已至最大！"); // 显示当前值
				}else{
					servoTx2.setText("左部舵机运动值为: " + String.valueOf(currentProcess)); // 显示当前值
				}
				mBluetoothLeService.WriteValue(writeValue);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currentProcess = progress;
			}
		});
		
		servoSBar3.setMax(180); // 设置滑块最大值
		servoSBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			// 设置监听器
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				String writeValue = "000";
				if(currentProcess <= 60){
					writeValue = "a" + "0603" + "z";
				}else if(currentProcess > 60 && currentProcess < 100){
					writeValue = "a" + "0" + String.valueOf(currentProcess) + "3" + "z";
				}else if(currentProcess >= 100 && currentProcess < 170){
					writeValue = "a" + String.valueOf(currentProcess) + "3" + "z";
				}else if(currentProcess >= 170){
					writeValue = "a" + "1703" + "z";
				}
				if(currentProcess <= 60){
					servoTx3.setText("右部舵机运动值已至最小 ！"); // 显示当前值
				}else if(currentProcess >= 170){
					servoTx3.setText("右部舵机运动值已至最大！"); // 显示当前值
				}else{
					servoTx3.setText("右部舵机运动值为: " + String.valueOf(currentProcess)); // 显示当前值
				}
				mBluetoothLeService.WriteValue(writeValue);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currentProcess = progress;
			}
		});
		
        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService=" + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));
        
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(mGattUpdateReceiver);
        //unbindService(mServiceConnection);
        if(mBluetoothLeService != null)
        {
        	mBluetoothLeService.close();
        	mBluetoothLeService = null;
        }
        Log.d(TAG, "We are in destroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                              //点击按钮
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
            	if(mConnected)
            	{
            		mBluetoothLeService.disconnect();
            		mConnected = false;
            	}
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void ShowDialog()
    {
    	Toast.makeText(this, "连接成功，现在可以正常通信！", Toast.LENGTH_SHORT).show();
    }

 // 按钮事件
	/*class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == btnSend) {
				if(!mConnected) return;
				
				if (edtSend.length() < 1) {
					Toast.makeText(DeviceControlActivity.this, "请输入要发送的内容", Toast.LENGTH_SHORT).show();
					return;
				}
				mBluetoothLeService.WriteValue(edtSend.getText().toString());
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if(imm.isActive())
					imm.hideSoftInputFromWindow(edtSend.getWindowToken(), 0);
				//todo Send data
			}
		}

	}*/
	
    private static IntentFilter makeGattUpdateIntentFilter() {                        //注册接收的事件
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }
}
