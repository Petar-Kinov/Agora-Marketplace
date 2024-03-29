package com.example.agora.ui.fragments.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.request.RequestOptions
import com.example.agora.data.authentication.login.LoginViewModelFactory
import com.example.agora.databinding.FragmentSettingsBinding
import com.example.agora.domain.auth.viewModel.AuthViewModel
import com.example.agora.util.GlideApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

private const val TAG = "SettingsFragment"
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(requireActivity(), LoginViewModelFactory())[AuthViewModel::class.java]
    }
    private lateinit var pickMediaActivityResultLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        pickMediaActivityResultLauncher =  registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                loadAvatar(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        val storageRef = Firebase.storage.reference.child("avatars/${FirebaseAuth.getInstance().currentUser!!.uid}")
        storageRef.downloadUrl.addOnSuccessListener {
            loadAvatar(it)
        }.addOnFailureListener {
            Log.d(TAG, "bind: Failed to load avatar ")
        }

        binding.deleteAccBtn.setOnClickListener {
            //Disabled until i put confirmation on it to avoid accidental deletion
//            authViewModel.deleteUser()
//            val action =
//                SettingsFragmentDirections.actionSettingsFragmentToLoginActivity()
//            findNavController().navigate(action)
        }

        binding.myAvatarIV.setOnClickListener {
            pickMediaActivityResultLauncher.launch("image/*")
        }

        binding.updateBtn.setOnClickListener {
            val imageView = binding.myAvatarIV
            val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            imageView.draw(canvas)

            authViewModel.uploadAvatar(bitmap)
        }

        return view
    }

    private fun loadAvatar(uri : Uri) {
        val options: RequestOptions = RequestOptions()
            .circleCrop()
        GlideApp.with(requireContext()).load(uri).apply(options).into(binding.myAvatarIV)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}