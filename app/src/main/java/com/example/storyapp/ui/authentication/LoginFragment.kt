package com.example.storyapp.ui.authentication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.storyapp.R
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.response.LoginResponse
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.models.LoginModels
import com.example.storyapp.ui.home.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!
    private val homeFragment = HomeFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLogin : TextView = view.findViewById(R.id.tv_login)
        tvLogin.setOnClickListener {
            val registerFragment = RegisterFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.authenticationFragment, registerFragment, RegisterFragment::class.java.simpleName)
                disallowAddToBackStack()
                commit()
            }
        }

        binding.btnLogin.setOnClickListener {
            val passwordText : String = binding.edLoginPassword.text.toString()
            if (passwordText.length < 8) {
                Toast.makeText(requireActivity(), "Password minimal 8 karakter!", Toast.LENGTH_SHORT).show()
            } else {
                login()
            }
        }
    }

    private fun login() {
        showLoading(true)
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        val userInfo = LoginModels(email, password)

        val service = ApiConfig().getApiService().login(userInfo)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        showLoading(false)
                        Toast.makeText(requireActivity(), responseBody.message, Toast.LENGTH_SHORT).show()
                        saveToken(responseBody.loginResult.token)

                        val fragmentManager = parentFragmentManager
                        fragmentManager.beginTransaction().apply {
                            replace(R.id.authenticationFragment, homeFragment, HomeFragment::class.java.simpleName)
                            disallowAddToBackStack()
                            commit()
                        }
                    }
                } else {
                    showLoading(false)
                    Toast.makeText(requireActivity(), response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(requireActivity(), "Gagal melakukan login", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token : String) {
        val preferences : SharedPreferences = requireActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE)
        preferences.edit().putString("TOKEN", token).apply()
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}