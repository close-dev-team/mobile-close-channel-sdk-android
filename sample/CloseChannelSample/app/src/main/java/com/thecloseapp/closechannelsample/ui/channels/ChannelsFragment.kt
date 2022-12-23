package com.thecloseapp.closechannelsample.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.thecloseapp.close.channel.sdk.Channel
import com.thecloseapp.close.channel.sdk.CloseChannelController
import com.thecloseapp.close.channel.sdk.CloseChannelError
import com.thecloseapp.closechannelsample.R
import com.thecloseapp.closechannelsample.databinding.FragmentChannelsBinding
import com.thecloseapp.closechannelsample.databinding.ItemChannelBinding
import com.thecloseapp.closechannelsample.ui.messages.MessagesFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ChannelsFragment : Fragment() {
    private var _binding: FragmentChannelsBinding? = null

    private val binding get() = _binding!!
    private var channelsViewModel: ChannelsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val channelsViewModel =
            ViewModelProvider(this).get(ChannelsViewModel::class.java)
        this.channelsViewModel = channelsViewModel

        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textInfoChannels
        channelsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        updateChannels()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateChannels() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Default) {
                val closeChannelController =
                    CloseChannelController.getInstance(requireActivity().application)

                withContext(Dispatchers.Main) {
                    val onSuccess = { channelList: List<Channel> ->
                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                            displayChannelList(binding.channelList, channelList)
                        }
                    }

                    val onFailure = { closeChannelError: CloseChannelError ->
                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                            Toast.makeText(context, "Error:$closeChannelError", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    closeChannelController.getChannels(onSuccess, onFailure)
                }
            }
        }
    }

    private fun displayChannelList(channelListView: LinearLayout, channelList: List<Channel>) {
        channelListView.removeAllViews()
        if (channelList.isNotEmpty()) {
            binding.textInfoChannels.visibility = View.GONE
            binding.channelLayout.visibility = View.VISIBLE

            channelList.forEach { channel ->
                val channelView = createChannelView(channel)
                channelListView.addView(channelView)
            }
        } else {
            channelsViewModel?.showInfoNoChannels()
            binding.textInfoChannels.visibility = View.VISIBLE
            binding.channelLayout.visibility = View.GONE
        }
    }

    private fun createChannelView(channel: Channel): ConstraintLayout {
        val channelBinding = ItemChannelBinding.inflate(LayoutInflater.from(context))
        val channelView = channelBinding.root

        // Name and images
        channelBinding.channelName.text = channel.name
        Glide.with(this).load(channel.backgroundImageUrl).into(channelBinding.channelBackground)
        Glide.with(this).load(channel.profileImageUrl).into(channelBinding.channelProfile)

        // Display the unread messages number (if bigger then 0)
        if (channel.unreadMessages > 0) {
            channelBinding.unreadMessages.text = channel.unreadMessages.toString()
            channelBinding.unreadMessages.visibility = View.VISIBLE
        } else {
            channelBinding.unreadMessages.visibility = View.GONE
        }

        // Display date
        channelBinding.dateLayout.visibility = View.VISIBLE
        channelBinding.showDateDay.text =
            SimpleDateFormat("d", Locale.ENGLISH).format(channel.startDateTime)
        var month = SimpleDateFormat("MMM", Locale.ENGLISH).format(channel.startDateTime).take(3)
        month += " '" + SimpleDateFormat("yy", Locale.ENGLISH).format(channel.startDateTime)
        channelBinding.showDateMonth.text = month

        channelBinding.channelItem.setOnClickListener {
            openChannel(channel)
        }

        return channelView
    }

    private fun openChannel(channel: Channel) {
        val navController = activity?.findNavController(R.id.nav_host_fragment_activity_sample_main)
        val bundle = bundleOf(
            MessagesFragment.BUNDLE_CHANNEL_ID to channel.id,
            MessagesFragment.BUNDLE_CHANNEL_OPEN_IN_INFO_VIEW to isOpenChannelInInfoView()
        )
        navController?.navigate(R.id.navigation_messages, bundle)
    }

    private fun isOpenChannelInInfoView() = binding.spinnerChannelView.selectedItem == "Info"
}