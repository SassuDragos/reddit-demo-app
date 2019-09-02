package com.sogard.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.sogard.ui.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_home)


        binding.viewModel = ViewModelProvider(this, SavedStateViewModelFactory(this.application, this)).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
    }

}