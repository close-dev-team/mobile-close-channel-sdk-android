package com.thecloseapp.closechannelsample.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.starlightideas.close.sdk.Channel
import com.starlightideas.close.sdk.CloseChannelController
import com.starlightideas.close.sdk.CloseChannelError
import com.thecloseapp.closechannelsample.BuildConfig
import com.thecloseapp.closechannelsample.databinding.DialogRegisterBinding
import com.thecloseapp.closechannelsample.databinding.DialogStorePropertyBinding
import com.thecloseapp.closechannelsample.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonRegisterUser.setOnClickListener {
            registerUserDialog()
        }
        binding.buttonAddChannel.setOnClickListener {
            addChannel()
        }
        binding.buttonRegisterPushInfo.setOnClickListener {
            registerPushInfo()
        }
        binding.buttonStoreChannelProperties.setOnClickListener {
            storeChannelPropertiesDialog()
        }
        binding.buttonLatestChannel.setOnClickListener {
            openLatestChannel()
        }

        val sampleAppVersion = BuildConfig.VERSION_NAME
        val sdkVersion = CloseChannelController.getVersion()
        binding.versionText.text =
            "Sample app v$sampleAppVersion. Powered by Close SDK v$sdkVersion"

        return root
    }

    private val genericOnFailure = { closeChannelError: CloseChannelError ->
        showToastLong("Error:$closeChannelError")
    }

    private fun registerUserDialog() {
        val registerDialogBinding: DialogRegisterBinding =
            DialogRegisterBinding.inflate(LayoutInflater.from(context))
        val registerDialogView = registerDialogBinding.root

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(registerDialogView)
            .setPositiveButton("register") { _, _ ->
                val registrationIdInputString = registerDialogBinding.registrationId.text.toString()
                val nicknameInputString = registerDialogBinding.nickname.text.toString()

                val uniqueId = registrationIdInputString.ifEmpty { null }
                val nickname = nicknameInputString.ifEmpty { null }

                registerUser(uniqueId, nickname)
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.cancel()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun registerUser(uniqueId: String?, nickname: String?) {
        val closeChannelController =
            CloseChannelController.getInstance(requireActivity().application)

        val onSuccess = { closeUserId: String ->
            showToastLong("register user with id:${closeUserId}")
            // You can save the closeUserId for later use
        }

        closeChannelController.registerUser(uniqueId, nickname, onSuccess, genericOnFailure)
    }

    private fun registerPushInfo() {
        val closeChannelController =
            CloseChannelController.getInstance(requireActivity().application)

        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    showToastLong("No Firebase token found")
                }

                val token = task.result
                val permissionGranted =
                    (NotificationManagerCompat.from(requireActivity().application)
                        .areNotificationsEnabled())

                val onSuccess = { isPushEnabled: Boolean ->
                    showToastLong("register push - isPushEnabled:${isPushEnabled}")
                }

                closeChannelController.registerPushInfo(
                    token,
                    permissionGranted,
                    onSuccess,
                    genericOnFailure
                )
            }
        } catch (ise: IllegalStateException) {
            Log.w(TAG, ise)
            showToastLong("Please setup firebase first, before you can register push")
        }
    }

    private fun addChannel() {
        val closeChannelController =
            CloseChannelController.getInstance(requireActivity().application)
        val closeCode = binding.closeCodeInput.text.toString()

        val onSuccess = { channel: Channel ->
            showToastLong("Added channel:${channel.name}")
        }

        closeChannelController.addChannel(closeCode, onSuccess, genericOnFailure)
    }

    private fun storeChannelPropertiesDialog() {
        val storePropertyDialogBinding: DialogStorePropertyBinding =
            DialogStorePropertyBinding.inflate(LayoutInflater.from(context))
        val storePropertyView = storePropertyDialogBinding.root

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(storePropertyView)
            .setPositiveButton("Store") { _, _ ->
                val propertyNameInputString =
                    storePropertyDialogBinding.propertyName.text.toString()
                val propertyValueInputString =
                    storePropertyDialogBinding.propertyValue.text.toString()

                val propertyName = propertyNameInputString.ifEmpty { null }
                val propertyValue = propertyValueInputString.ifEmpty { null }

                if (propertyName != null && propertyValue != null) {
                    val propertyMap = mapOf(propertyName to propertyValue)
                    storeChannelProperties(propertyMap)
                } else {
                    showToastLong("Property name and value must both be filled")
                }
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.cancel()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun storeChannelProperties(properties: Map<String, String>, channelId: String? = null) {
        val closeChannelController =
            CloseChannelController.getInstance(requireActivity().application)

        val onSuccess = { ->
            showToastLong("Properties stored successful")
        }

        closeChannelController.storeChannelProperties(
            properties,
            channelId,
            onSuccess,
            genericOnFailure
        )
    }

    private fun openLatestChannel() {
        val closeChannelController =
            CloseChannelController.getInstance(requireActivity().application)
        val onSuccess = {
            Log.i(TAG, "Opening latest Close Channel")
            Unit
        }
        closeChannelController.openChannelMessagesView(
            requireActivity(),
            null,
            onSuccess,
            genericOnFailure
        )
    }

    private fun showToastLong(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}