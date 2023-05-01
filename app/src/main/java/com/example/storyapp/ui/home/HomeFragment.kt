package com.example.storyapp.ui.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.R
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.response.ListStoryItem
import com.example.storyapp.databinding.FragmentHomeBinding
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.ui.addstory.AddStoryActivity
import com.example.storyapp.ui.authentication.LoginFragment
import com.example.storyapp.ui.storylocations.StoryLocationsActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private var recyclerView : RecyclerView? = null

    private val binding get() = _binding!!
    private lateinit var storiesViewModel: StoriesViewModel

    private val mainViewModel : MainViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        storiesViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(StoriesViewModel::class.java)

        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListStory.layoutManager = layoutManager



        getData()

//        recyclerView?.layoutManager = layoutManager
//        recyclerView = view.findViewById(R.id.rv_listStory)

//        storiesViewModel.getAllStories(getToken())
//        storiesViewModel.storiesData.observe(viewLifecycleOwner) { storiesData ->
//            setStoriesData(storiesData)
//        }

        storiesViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(requireActivity(), AddStoryActivity::class.java)
            requireActivity().startActivity(intent)
        }

        val apiConfig = ApiConfig
        apiConfig.token = getToken()
    }

    private fun getData() {
        val adapter = StoriesPagingAdapter()
        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.story.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun getToken() : String {
        val preferences : SharedPreferences = requireActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE)
        val token = preferences.getString("TOKEN", null)
        return token.toString()
    }

    private fun setStoriesData(items : List<ListStoryItem>) {
        val listPhotoUrl = ArrayList<String>()
        val listCreatedAt = ArrayList<String>()
        val listName = ArrayList<String>()
        val listDescription = ArrayList<String>()
        val listId = ArrayList<String>()
        for (item in items){
            listPhotoUrl.add(item.photoUrl)
            listName.add(item.name)
            listDescription.add(item.description)
            listCreatedAt.add(item.createdAt)
            listId.add(item.id)
        }
        val adapter = StoriesAdapter(listPhotoUrl, listName, listId)
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView?.layoutManager = layoutManager
        recyclerView = view?.findViewById(R.id.rv_listStory)
        recyclerView?.adapter = adapter
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout, menu)
        inflater.inflate(R.menu.maps, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> showDialog()
            R.id.maps -> startActivity(Intent(requireActivity(), StoryLocationsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.logoutdialog)
        val yesBtn = dialog.findViewById<Button>(R.id.dialogBtnYes)
        val noBtn = dialog.findViewById<Button>(R.id.dialogBtnNo)
        yesBtn.setOnClickListener {
            val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences("user_token", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("TOKEN").commit()

            val loginFragment = LoginFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.authenticationFragment, loginFragment, LoginFragment::class.java.simpleName)
                commit()
            }
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}