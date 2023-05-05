package com.example.storyapp.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.storyapp.R
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.response.RegisterResponse
import com.example.storyapp.databinding.FragmentRegisterBinding
import com.example.storyapp.models.RegisterModels
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLogin : TextView = view.findViewById(R.id.tv_login)
        tvLogin.setOnClickListener {
            val loginFragment = LoginFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.authenticationFragment, loginFragment, LoginFragment::class.java.simpleName)
                disallowAddToBackStack()
                commit()
            }
        }

        binding.btnRegister.setOnClickListener {
            val passwordText : String = binding.edRegisterPassword.text.toString()
            if (passwordText.length < 8) {
                Toast.makeText(requireActivity(), "Password minimal 8 karakter!", Toast.LENGTH_SHORT).show()
            } else {
                register()
            }
        }
    }

    private fun register() {
        showLoading(true)
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        val userInfo = RegisterModels(name, email, password)

        val service = ApiConfig().getApiService().register(userInfo)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.message == "User created" && !responseBody.error) {
                        showLoading(false)
                        Toast.makeText(requireActivity(), responseBody.message, Toast.LENGTH_SHORT).show()
                        val loginFragment = LoginFragment()
                        val fragmentManager = parentFragmentManager
                        fragmentManager.beginTransaction().apply {
                            replace(R.id.authenticationFragment, loginFragment, LoginFragment::class.java.simpleName)
                            disallowAddToBackStack()
                            commit()
                        }
                    }
                } else {
                    showLoading(false)
                    Toast.makeText(requireActivity(), response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(requireActivity(), "Gagal melakukan register", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}