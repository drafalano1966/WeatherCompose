package com.example.weathercompose.di

import org.koin.dsl.module

val app = module {
    includes(
        repo,
        viewModel,
    )
}