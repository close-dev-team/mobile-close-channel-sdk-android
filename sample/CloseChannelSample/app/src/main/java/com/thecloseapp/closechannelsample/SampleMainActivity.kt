package com.thecloseapp.closechannelsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.starlightideas.close.sdk.CloseChannelNotification
import com.thecloseapp.closechannelsample.databinding.ActivitySampleMainBinding
import com.thecloseapp.closechannelsample.ui.notifications.MessagesFragment

class SampleMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySampleMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySampleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_sample_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_channels, R.id.navigation_messages
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (intent != null) {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {
        val tappedOnCloseNotification = CloseChannelNotification.from(intent)
        if (tappedOnCloseNotification != null) {
            val channelId = tappedOnCloseNotification.channelId

            val navController = findNavController(R.id.nav_host_fragment_activity_sample_main)
            val bundle = bundleOf(
                MessagesFragment.BUNDLE_CHANNEL_ID to channelId,
                MessagesFragment.BUNDLE_CHANNEL_OPEN_IN_INFO_VIEW to tappedOnCloseNotification.openInInfoView
            )
            navController.navigate(R.id.navigation_messages, bundle)
        }
    }
}