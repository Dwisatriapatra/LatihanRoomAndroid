package com.example.latihanroomandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.latihanroomandroid.R
import com.example.latihanroomandroid.dataclass.Catatan
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllDetails()
    }

    private fun getAllDetails() {
        val detail = arguments?.getParcelable<Catatan>("DATACATATAN")
        detail_judul.text = "Judul: ${detail?.judul}"
        detail_catatan.text = "Catatan: ${detail?.catatan}"
        detail_waktu_pembuatan.text = "Waktu: ${detail?.waktu}"
    }
}