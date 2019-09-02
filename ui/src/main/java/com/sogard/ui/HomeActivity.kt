package com.sogard.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.sogard.ui.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.view_top_posts.view.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_home
        )

        binding.viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(this.application, this)).get(
                HomeViewModel::class.java
            )

        binding.lifecycleOwner = this


    }

}