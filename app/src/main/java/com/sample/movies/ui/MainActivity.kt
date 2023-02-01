package com.sample.movies.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.sample.movies.R
import com.sample.movies.databinding.ActivityMainBinding
import com.sample.movies.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var networkUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
        handleNetwork()
    }

    override fun onPause() {
        super.onPause()
        networkUtils.unRegister()
    }

    private fun handleNetwork() {
        networkUtils.getNetworkLiveData().observe(this) { isConnected: Boolean ->
            if (!isConnected) {
                with(binding) {
                    textViewNetworkStatus.text =
                        getString(R.string.text_no_connectivity)
                    networkStatusLayout.visibility = android.view.View.VISIBLE
                    networkStatusLayout.setBackgroundColor(
                        androidx.core.content.res.ResourcesCompat.getColor(
                            resources,
                            R.color.colorStatusNotConnected, null
                        )
                    )
                }
            } else {
                with(binding) {
                    textViewNetworkStatus.text =
                        getString(R.string.text_connectivity)
                    networkStatusLayout.setBackgroundColor(
                        androidx.core.content.res.ResourcesCompat.getColor(
                            resources, R.color.colorStatusConnected, null
                        )
                    )
                    networkStatusLayout.animate().alpha(1f)
                        .setStartDelay(ANIMATION_DURATION.toLong())
                        .setDuration(ANIMATION_DURATION.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                binding.networkStatusLayout.visibility = android.view.View.GONE
                            }
                        })
                }
            }
        }
    }
}

private const val ANIMATION_DURATION = 1000