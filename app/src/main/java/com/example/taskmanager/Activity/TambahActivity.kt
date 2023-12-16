package com.example.taskmanager.Activity

import android.os.Bundle
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

class TambahActivity : AppCompatActivity() {
    private var btnSimpan: Button? = null
    private var judul: String? = null
    private var deskripsi: String? = null
    private var tanggal: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        val jJudul = findViewById<TextView>(R.id.judul)
        val jDeskripsi = findViewById<TextView>(R.id.deskripsi)
        val jTanggal = findViewById<TextView>(R.id.tanggal)
        btnSimpan = findViewById(R.id.btn_simpan)
        btnSimpan?.run {
            setOnClickListener {
                judul = jJudul.text.toString()
                deskripsi = jDeskripsi.text.toString()
                tanggal = jTanggal.text.toString()
                if (judul!!.trim { it <= ' ' } == "") {
                    jJudul.error = "Masukkan Nama"
                } else if (deskripsi!!.trim { it <= ' ' } == "") {
                    jDeskripsi.error = "Masukkan Deskripsi"
                } else if (tanggal!!.trim { it <= ' ' } == "") {
                    jTanggal.error = "Masukkan Tanggal"
                } else {
                    createData()
                }
            }
        }
    }

    private fun createData() {
        var ardData: APIRequestData = RetroServer.konekRetrofit().create(APIRequestData::class.java)
        val simpanData: Call<ResponseModel> = ardData.ardCreateData(judul, deskripsi, tanggal)
        simpanData.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val pesan: String = response.body()!!.pesan
                Toast.makeText(
                    this@TambahActivity,
                    "Pesan : $pesan",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            override fun onFailure(call: Call<ResponseModel>, m: Throwable) {
                Toast.makeText(
                    this@TambahActivity,
                    "Gagal Terkoneksi | " + m.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}



