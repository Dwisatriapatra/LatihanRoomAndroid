package com.example.latihanroomandroid.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.latihanroomandroid.R
import com.example.latihanroomandroid.roomdatabase.UserDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private var dbUser : UserDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //action for registering new account
        val sharedPreferences = requireContext().getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
        if(sharedPreferences.contains("USERNAME")){
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
        }else{
            login_belum_punya_akun.setOnClickListener {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
            }

            //action for login authorization
            login_button_login.setOnClickListener {
                if(loginAuth()){
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }

    //Authorization function for login action
    private fun loginAuth() : Boolean {
        if(login_input_username.text.isNotEmpty() && login_input_password.text.isNotEmpty()){

            dbUser = UserDatabase.getInstance(requireContext())

            //get input data user by user
            val inputanEmail = login_input_username.text.toString()
            val inputanPassword = login_input_password.text.toString()

            //get user if available by checking his email and password
            val user = dbUser?.userDao()?.checkUserLoginData(inputanEmail, inputanPassword)
            return if(user.isNullOrEmpty()){
                Toast.makeText(requireContext(), "email/password salah", Toast.LENGTH_SHORT).show()
                false
            }else{
                //add username of user in a shared preference after checking user availability
                val sharedPreference = requireContext().getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)
                val sf = sharedPreference.edit()
                sf.putString("USERNAME", user)
                sf.putString("PASSWORD", inputanPassword)
                sf.apply()
                true
            }
        }else{
            Toast.makeText(requireContext(), "email dan password harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}