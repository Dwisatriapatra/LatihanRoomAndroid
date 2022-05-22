package com.example.latihanroomandroid.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.latihanroomandroid.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        profile_button_logout.setOnClickListener {
            logout()
        }
    }

    private fun initView() {
        val sharedPreferences = requireContext().getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", "")
        val password = sharedPreferences.getString("PASSWORD", "")
        profile_username.setText(username)
        profile_password.setText(password)
    }

    private fun logout(){
        AlertDialog.Builder(requireContext())
            .setTitle("LOGOUT")
            .setMessage("Yakin ingin logout?")
            .setNegativeButton("Tidak"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }.setPositiveButton("Ya"){ _: DialogInterface, _: Int ->
                //clear shared preference, so the user must login again to access home after logging out
                val sharedPreferences = requireContext().getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
                val sf = sharedPreferences.edit()
                sf.clear()
                sf.apply()


                val mIntent = activity?.intent
                activity?.finish()
                startActivity(mIntent)
            }.show()
    }
}