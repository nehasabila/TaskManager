package com.example.taskmanager.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.API.APIRequestData
import com.example.taskmanager.API.RetroServer
import com.example.taskmanager.Model.ResponseModel
import com.example.taskmanager.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class UbahActivity : AppCompatActivity() {
    var jId = 0
    var jJudul: String? = null
    var jDeskripsi: String? = null
    var jTanggal: String? = null
    var kJudul: String? = null
    var kDeskripsi: String? = null
    var kTanggal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah)
        val terima = intent
        jId = terima.getIntExtra("jId", -1)
        jJudul = terima.getStringExtra("jJudul")
        jDeskripsi = terima.getStringExtra("jDeskripsi")
        jTanggal = terima.getStringExtra("jTanggal")
        val vJudul = findViewById<TextView>(R.id.judul)
        val vDeskripsi = findViewById<TextView>(R.id.deskripsi)
        val vTanggal = findViewById<TextView>(R.id.tanggal)
        val btnUbah = findViewById<Button>(R.id.btn_ubah)
        vJudul.setText(jJudul)
        vDeskripsi.setText(jDeskripsi)
        vTanggal.setText(jTanggal)
        btnUbah.setOnClickListener(View.OnClickListener {
            kJudul = vJudul.getText().toString()
            kDeskripsi = vDeskripsi.getText().toString()
            kTanggal = vTanggal.getText().toString()
            updateData()
        })
    }

    private fun updateData() {
        val ardData = RetroServer.konekRetrofit().create(APIRequestData::class.java)
        val ubahData = ardData.ardUpdateData(jId, kJudul, kDeskripsi, kTanggal)
        ubahData.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val pesan = response.body()!!.pesan
                Toast.makeText(
                    this@UbahActivity,
                    "Pesan : $pesan",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(
                    this@UbahActivity,
                    "Gagal Terkoneksi | " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

