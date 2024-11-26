package com.example.hotelapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.hotelapp.adapters.OnboardingAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        // Configurar el adaptador del ViewPager2
        viewPager.adapter = OnboardingAdapter(this)

        // Configurar el TabLayout con el ViewPager2 para mostrar los dots
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }
}
