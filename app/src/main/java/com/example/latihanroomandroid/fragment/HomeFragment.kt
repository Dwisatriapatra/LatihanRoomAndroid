package com.example.latihanroomandroid.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihanroomandroid.R
import com.example.latihanroomandroid.adapter.CatatanAdapter
import com.example.latihanroomandroid.datastore.UserLoginManager
import com.example.latihanroomandroid.roomdatabase.CatatanDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var dbCatatan : CatatanDatabase? = null
    private lateinit var userLoginManager : UserLoginManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLoginManager = UserLoginManager(requireContext())
        userLoginManager.username.asLiveData().observe(viewLifecycleOwner){
            home_username_text.text = "Halo, $it"
        }


        dbCatatan = CatatanDatabase.getInstance(requireContext())
        getDataCatatan()


        fab_tambah.setOnClickListener {
            InputDialogFragment().show(childFragmentManager, "InputDialogFragment")
        }

        fab_share.setOnClickListener{
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Hey Check out this Great app:")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }

//        logout.setOnClickListener {
//            logout()
//        }

        photo.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }


    private fun getDataCatatan() {

        rv_catatan.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val listCatatan = dbCatatan?.catatanDao()?.getAllCatatan()

        GlobalScope.launch {
            activity?.runOnUiThread{
                listCatatan.let {
                    //set adapter
                    rv_catatan.adapter = CatatanAdapter(it!!){
                        val clickedCatatan = bundleOf("DATACATATAN" to it)
                        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailFragment, clickedCatatan)
                    }
                }
            }
        }
    }
}