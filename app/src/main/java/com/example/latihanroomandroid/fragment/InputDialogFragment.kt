package com.example.latihanroomandroid.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.latihanroomandroid.R
import com.example.latihanroomandroid.dataclass.Catatan
import com.example.latihanroomandroid.roomdatabase.CatatanDatabase
import kotlinx.android.synthetic.main.fragment_input_dialog.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InputDialogFragment : DialogFragment() {
    private var dbCatatan : CatatanDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_dialog, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbCatatan = CatatanDatabase.getInstance(requireContext())

        //action for button "tambah data"
        tambah_button_input.setOnClickListener {
            GlobalScope.async {
                //get judul and catatan value
                val judul = tambah_input_judul.text.toString()
                val catatan = tambah_input_catatan.text.toString()

                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                val formatted = current.format(formatter)

                //command for room database
                val command = dbCatatan?.catatanDao()?.insertCatatan(Catatan(null, judul, formatted, catatan))
                activity?.runOnUiThread {
                    if(command != 0.toLong()){
                        Toast.makeText(requireContext(), "Catatan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Catatan gagal ditambahkan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dismiss()
        }

    }

    override fun onDetach() {
        super.onDetach()
        activity?.recreate()
    }
}