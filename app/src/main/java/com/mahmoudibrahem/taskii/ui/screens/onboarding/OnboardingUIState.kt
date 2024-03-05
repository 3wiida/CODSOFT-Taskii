package com.mahmoudibrahem.taskii.ui.screens.onboarding


import com.mahmoudibrahem.taskii.R
import com.mahmoudibrahem.taskii.pojo.OnboardingPage

data class OnboardingUIState(
    val pages: List<OnboardingPage> = listOf(
        OnboardingPage(
            image = R.drawable.onboarding_1,
            headText = "Hello!",
            bodyText = "Welcome!!! Do you want clear task super fast with Taskii"
        ),
        OnboardingPage(
            image =  R.drawable.onboarding_2,
            headText = "Arrangement",
            bodyText = "Easily arrange work order for you to easily manage"
        ),
        OnboardingPage(
            image = R.drawable.onboarding_3,
            headText = "Solving",
            bodyText = "It has never been easier to complete tasks. Get started with us!"
        )
    ),
)
