package com.thecloseapp.closechannelsample.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.starlightideas.close.sdk.CloseChannelController
import com.starlightideas.close.sdk.CloseChannelError
import com.thecloseapp.closechannelsample.R
import com.thecloseapp.closechannelsample.databinding.FragmentMessagesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessagesFragment : Fragment() {

    companion object {
        internal const val BUNDLE_CHANNEL_ID = "BundleChannelId"
        internal const val BUNDLE_CHANNEL_OPEN_IN_INFO_VIEW = "BundleChannelOpenInInfoView"
    }

    private var _binding: FragmentMessagesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messagesViewModel =
            ViewModelProvider(this).get(MessagesViewModel::class.java)

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMessages
        messagesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val channelId = arguments?.getString(BUNDLE_CHANNEL_ID)
        val channelOpenInInfoView =
            arguments?.getBoolean(BUNDLE_CHANNEL_OPEN_IN_INFO_VIEW, false) ?: false
        loadChannel(channelId, channelOpenInInfoView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadChannel(channelId: String?, channelOpenInInfoView: Boolean) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Default) {
                val closeChannelController =
                    CloseChannelController.getInstance(requireActivity().application)

                withContext(Dispatchers.Main) {
                    val onSuccess = { fragment: Fragment ->
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.messages_container_view, fragment)
                            ?.commit()

                        binding.textMessages.visibility = View.GONE
                    }

                    val onFailure = { closeChannelError: CloseChannelError ->
                        Toast.makeText(context, "Error:$closeChannelError", Toast.LENGTH_LONG)
                            .show()
                    }

                    if (channelOpenInInfoView) {
                        closeChannelController.getChannelInfoFragment(
                            channelId,
                            onSuccess,
                            onFailure
                        )
                    } else {
                        closeChannelController.getChannelMessagesFragment(
                            channelId,
                            onSuccess,
                            onFailure
                        )
                    }
                }
            }
        }
    }
}