package com.example.hotelapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hotelapp.OnboardingFragment
import com.example.hotelapp.R


class OnboardingAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3 // Número de páginas

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.newInstance(
                R.layout.onboarding_page_1,
                "Bienvenido",
                "Descubre nuestras funciones",
                R.drawable.cap2
            )

            1 -> OnboardingFragment.newInstance(
                R.layout.onboarding_page_2,
                "Organiza tus tareas",
                "Planifica y organiza tus actividades fácilmente",
                R.drawable.cap1
            )

            else -> OnboardingFragment.newInstance(
                R.layout.onboarding_page_3,
                "Comienza ahora",
                "Empieza a utilizar la aplicación",
                R.drawable.cap3
            )
        }
    }
}


