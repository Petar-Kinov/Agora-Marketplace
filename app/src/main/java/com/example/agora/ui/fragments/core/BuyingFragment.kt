package com.example.agora.ui.fragments.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agora.databinding.FragmentBuyingBinding
import com.example.agora.domain.core.viewModel.ItemsViewModel
import com.example.agora.ui.adapters.ItemsRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "BuyingFragment"
class BuyFragment : Fragment() {

    private var _binding : FragmentBuyingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ItemsViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDB: FirebaseFirestore

    private var recyclerView: RecyclerView? = null
    private val recyclerAdapter = ItemsRecyclerAdapter {

        val action = HomePageDirections.actionHomePageToItemDetailsFragment(it.item)
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseDB = Firebase.firestore
        viewModel = ViewModelProvider(requireActivity())[ItemsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBuyingBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.recyclerView
        recyclerView!!.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView!!.adapter = recyclerAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getItems()
        viewModel.items.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView?.adapter = null
        recyclerView = null
        _binding = null
    }

}