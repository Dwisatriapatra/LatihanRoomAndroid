package com.example.latihanroomandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.latihanroomandroid.R
import com.example.latihanroomandroid.dataclass.User
import com.example.latihanroomandroid.roomdatabase.UserDatabase
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RegisterFragment : Fragment() {
    private var dbUser : UserDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register_button_daftar.setOnClickListener {
            if(register_input_username.text.isNotEmpty() &&
                register_input_name.text.isNotEmpty() &&
                register_input_password.text.isNotEmpty() &&
                register_input_konfirmasi_password.text.isNotEmpty()){

                //check the similarity between the password field and confirm password
                if(register_input_password.text.toString() != register_input_konfirmasi_password.text.toString()){
                    Toast.makeText(requireContext(), "Password dan konfirmasi password harus sama", Toast.LENGTH_SHORT).show()
                }else{
                    //if similar, then input user data to user database
                    inputUserData()
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }else{
                Toast.makeText(requireContext(), "Semua data belum terisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inputUserData() {
        dbUser = UserDatabase.getInstance(requireContext())
        GlobalScope.async {
            //get all data from edit text
            val dataUsername = register_input_username.text.toString()
            val dataName = register_input_name.text.toString()
            val dataPassword = register_input_password.text.toString()

            //command for the room database to insert new user
            val command = dbUser?.userDao()?.insertNewUSer(User(null, dataUsername, dataName, dataPassword))
            //cehck if command worked or not
            activity?.runOnUiThread{
                if(command != 0.toLong()){
                    Toast.makeText(requireContext(), "Proses register berhasil", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Proses register gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}