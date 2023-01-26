package com.example.agora.authentication

import com.google.firebase.auth.FirebaseAuth

class FirebaseHelper {
    companion object {
        private var instance: FirebaseAuth? = null

        fun getInstance(): FirebaseAuth {
            if (instance == null) {
                instance = FirebaseAuth.getInstance()
            }
            return instance!!
        }
    }
}