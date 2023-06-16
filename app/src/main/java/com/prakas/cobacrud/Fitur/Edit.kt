package com.prakas.cobacrud.Fitur

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.prakas.cobacrud.Network.ApiClient
import com.prakas.cobacrud.R
import com.prakas.cobacrud.Response.ResponseDataUpdateMahasiswa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Edit : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputNim: TextInputEditText
    private lateinit var inputNama: TextInputEditText
    private lateinit var inputTelepon: TextInputEditText
    private lateinit var btnUpdate: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

//        recyclerView = findViewById(R.id.recyclerView)
        inputNim = findViewById(R.id.inputNim)
        inputNama = findViewById(R.id.inputNama)
        inputTelepon = findViewById(R.id.inputTelepon)
        btnUpdate = findViewById(R.id.btnUpdate)

        val bundle = intent.extras
        if (bundle != null) {
            val nim = bundle.getString("nim")
            val nama = bundle.getString("nama")
            val telepon = bundle.getString("telepon")

            inputNim.setText(nim)
            inputNama.setText(nama)
            inputTelepon.setText(telepon)
        }

        btnUpdate.setOnClickListener {
            val nim = inputNim.text.toString()
            val nama = inputNama.text.toString()
            val telepon = inputTelepon.text.toString()

            updateDataMahasiswa(nim, nama, telepon)
        }
    }
    private fun updateDataMahasiswa(nim: String, nama: String, telepon: String) {
        ApiClient.apiService.updateMahasiswa(nim, nama, telepon)
            .enqueue(object : Callback<ResponseDataUpdateMahasiswa> {
                override fun onResponse(
                    call: Call<ResponseDataUpdateMahasiswa>,
                    response: Response<ResponseDataUpdateMahasiswa>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        if (responseData != null) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@Edit, "Data berhasil di update", Toast.LENGTH_SHORT).show()

                                val resultIntent = Intent()
                                resultIntent.putExtra("nim", nim)
                                resultIntent.putExtra("nama", nama)
                                resultIntent.putExtra("telepon", telepon)
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()

                            } else {
                                Toast.makeText(this@Edit, "Gagal di update", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@Edit, "Data Kosong", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorResponse = response.errorBody()?.string()
                        Toast.makeText(this@Edit, "API call failed: $errorResponse", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseDataUpdateMahasiswa>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(this@Edit, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}