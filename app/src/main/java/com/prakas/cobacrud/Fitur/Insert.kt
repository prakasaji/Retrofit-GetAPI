package com.prakas.cobacrud.Fitur

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.prakas.cobacrud.Network.ApiClient
import com.prakas.cobacrud.R
import com.prakas.cobacrud.Response.ResponseDataInsertMahasiswa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Insert : AppCompatActivity() {
    private lateinit var inputNim: TextInputEditText
    private lateinit var inputNama: TextInputEditText
    private lateinit var inputTelepon: TextInputEditText
    private lateinit var btnTambah: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        inputNim = findViewById(R.id.inputNimIns)
        inputNama = findViewById(R.id.inputNamaIns)
        inputTelepon = findViewById(R.id.inputTeleponIns)
        btnTambah = findViewById(R.id.btnTambah)

        btnTambah.setOnClickListener {
            val nim = inputNim.text.toString()
            val nama = inputNama.text.toString()
            val telepon = inputTelepon.text.toString()

            if (nim.isNotEmpty() && nama.isNotEmpty() && telepon.isNotEmpty()) {
                insertMahasiswa(nim, nama, telepon)
            } else {
                Toast.makeText(this@Insert, "Tolong isi datanya", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertMahasiswa(nim: String, nama: String, telepon: String) {
        val apiService = ApiClient.apiService
        val call = apiService.insertMahasiswa(nim, nama, telepon)
        call.enqueue(object : Callback<ResponseDataInsertMahasiswa> {
            override fun onResponse(
                call: Call<ResponseDataInsertMahasiswa>,
                response: Response<ResponseDataInsertMahasiswa>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        Toast.makeText(this@Insert, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                        val resultIntent = Intent()
                        resultIntent.putExtra("nim", nim)
                        resultIntent.putExtra("nama", nama)
                        resultIntent.putExtra("telepon", telepon)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        Toast.makeText(this@Insert, "Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Toast.makeText(this@Insert, "API call failed: $errorResponse", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataInsertMahasiswa>, t: Throwable) {
                Log.e("Insert", t.stackTraceToString())
                Toast.makeText(this@Insert, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
}