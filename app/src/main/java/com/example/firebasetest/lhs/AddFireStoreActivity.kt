package com.example.firebasetest.lhs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.lhs.databinding.ActivityAddFireStoreBinding

class AddFireStoreActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddFireStoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFireStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FireStore crud 확인.

    }
}