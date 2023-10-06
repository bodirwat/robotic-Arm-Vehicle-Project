package com.example.robot_arm_app

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.select_device_layout.*


class SelectDeviceActivity : AppCompatActivity() {

     private var  m_bluetoothAdapter: BluetoothAdapter? = null
    lateinit var  m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object{
        val EXTRA_ADDRESS: String = "Device_address"
    }



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_device_layout)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (m_bluetoothAdapter == null){

            return
        }
        if (!m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            startActivityForResult(enableBluetoothIntent , REQUEST_ENABLE_BLUETOOTH)
            select_device_refresh.setOnClickListener{ pairedDeviceList() }
        } }
    @SuppressLint("MissingPermission")
    private fun  pairedDeviceList() {

        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()
        if (m_pairedDevices.isNotEmpty()) {
            for (devices: BluetoothDevice in m_pairedDevices) {
                list.add(devices)
                Log.i("device", "" + devices)
            }
        }else{
            Toast.makeText(this, "no paired bluetooth device found", Toast.LENGTH_SHORT)
                .show()
        }
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val device: BluetoothDevice = list[position]
                val address: String = device.address
                val intent = Intent(this, ControlActivity::class.java)
                intent.putExtra(EXTRA_ADDRESS, address)
                startActivity(intent)

            }
    }








            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH){
            if (resultCode == RESULT_OK){
                if (m_bluetoothAdapter!!.isEnabled){
                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT)
                        .show()
                }
            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Bluetooth has been cancelled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}