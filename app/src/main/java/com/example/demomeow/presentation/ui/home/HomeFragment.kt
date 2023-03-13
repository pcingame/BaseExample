package com.example.demomeow.presentation.ui.home

import androidx.fragment.app.viewModels
import com.example.demomeow.base.BaseFragment
import com.example.demomeow.databinding.FragmentHomeBinding

class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    override val viewModel: HomeViewModel by viewModels()

}
