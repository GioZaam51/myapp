package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.interfaces.FragmentCommunicator

class MainActivity : AppCompatActivity(), FragmentCommunicator {

    private lateinit var loaderView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loaderView = findViewById(R.id.view)
        loaderView.visibility = View.GONE
    }

    override fun showLoader() {
        loaderView.visibility = View.VISIBLE
        loaderView.playAnimation()
    }

    override fun hideLoader() {
        loaderView.visibility = View.GONE
        loaderView.pauseAnimation()
    }
}
