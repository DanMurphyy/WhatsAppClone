package com.hfad.whatsappclone.presentation.homePageLayout.message

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.whatsappclone.R
import com.hfad.whatsappclone.databinding.FragmentMessageBinding
import com.hfad.whatsappclone.domain.model.ModelMessage
import com.hfad.whatsappclone.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MessageFragment : Fragment(), IMessageView {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private val messageViewModel: MessageViewModel by viewModels()
    lateinit var chatId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        arguments?.let {
            chatId = it.getString("chatId").toString()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.let { binding ->
            val messageAdapter = MessageAdapter(this)
            binding.messageRecyclerView.adapter = messageAdapter
            binding.messageRecyclerView.layoutManager = LinearLayoutManager(context)
            messageViewModel.getAllChatsMessages(chatId, this)
            Log.d("MessageFragment", "Received chatId: $chatId")
            CoroutineScope(Dispatchers.IO).launch {
                messageViewModel.messages.collectLatest { data ->
                    withContext(Dispatchers.Main) {
                        messageAdapter.submitList(data)
                        Log.d("MessageFragment", "Received data: $data")
                    }
                }
            }

            binding.btSendMessage.setOnClickListener {
                var message = binding.etMessageBox.text.toString()
                if (message.isNotBlank()) {
                    var messageModel = ModelMessage(
                        messageData = message,
                        messageType = "text",
                        messageSender = getUserId(),
                        messageReceiver = ""
                    )
                    messageViewModel.sendMessage(chatId,messageModel)
                }
            }
        }
    }


    override fun showProgressBar() {
        binding.messageProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.messageProgressBar.visibility = View.GONE
    }

    override fun showError(error: String) {
        binding.messageProgressBar.visibility = View.GONE
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun getUserId(): String {
        return messageViewModel.getUserId()
    }

    companion object {
        fun newInstance(activity: MainActivity, chatId: String) {
            val fragment = MessageFragment()
            val bundle = Bundle()
            bundle.putString("chatId", chatId)
            fragment.arguments = bundle
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, "message_fragment")
                .addToBackStack("message_fragment").commit()
        }
    }

    override fun clearMessages() {
        binding.etMessageBox.text.clear()
    }
}