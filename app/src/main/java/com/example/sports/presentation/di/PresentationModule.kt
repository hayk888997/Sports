package com.example.sports.presentation.di

import com.example.sports.presentation.insertperformance.InsertPerformanceViewModel
import com.example.sports.presentation.performancelist.ListPerformancesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { InsertPerformanceViewModel(get()) }
    viewModel { ListPerformancesViewModel(get()) }
}
