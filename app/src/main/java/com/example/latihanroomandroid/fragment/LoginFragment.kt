package com.example.latihanroomandroid.fragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.example.latihanroomandroid.R
import com.example.latihanroomandroid.datastore.UserLoginManager
import com.example.latihanroomandroid.roomdatabase.UserDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var dbUser : UserDatabase? = null
    private lateinit var userLoginManager : UserLoginManager
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
        //val sharedPreferences = requireContext().getSharedPreferences("DATAUSER", Context.MODE_PRIVATE)

        userLoginManager = UserLoginManager(requireContext())
        userLoginManager.boolean.asLiveData().observe(viewLifecycleOwner){
            if(it == true){
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
    }

    //Authorization function for login action
    private fun loginAuth() : Boolean {
        if(login_input_username.text.isNotEmpty() && login_input_password.text.isNotEmpty()){

            dbUser = UserDatabase.getInstance(requireContext())
            userLoginManager = UserLoginManager(requireContext())

            //get input data user by user
            val inputanUsername = login_input_username.text.toString()
            val inputanPassword = login_input_password.text.toString()

            //get user if available by checking his email and password
            val nameOfUser = dbUser?.userDao()?.checkUserLoginData(inputanUsername, inputanPassword)
            return if(nameOfUser.isNullOrEmpty()){
                Toast.makeText(requireContext(), "email/password salah", Toast.LENGTH_SHORT).show()
                false
            }else{
                GlobalScope.launch {
                    userLoginManager.saveDataLogin(
                        nameOfUser,
                        inputanPassword,
                        inputanUsername
                    )
                    userLoginManager.setBoolean(true)
                }
                Toast.makeText(requireContext(), "Login berhasil", Toast.LENGTH_SHORT).show()
                true
            }
        }else{
            Toast.makeText(requireContext(), "email dan password harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}