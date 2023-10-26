package com.hfad.whatsappclone.presentation.homePageLayout.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.whatsappclone.databinding.FragmentChatBinding
import com.hfad.whatsappclone.presentation.MainActivity
import com.hfad.whatsappclone.presentation.homePageLayout.message.MessageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ChatFragment : Fragment(), IChatView {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.let { binding ->
            val chatAdapter = ChatAdapter(this)
            binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.chatRecyclerView.adapter = chatAdapter
            chatViewModel.fetchAllChats(this)
            CoroutineScope(Dispatchers.IO).launch {
                chatViewModel.chatList.collectLatest { data ->
                    Log.d("ChatFragment", "Received data: $data")
                    if (data.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            chatAdapter.submitList(data)
                        }
                    }
                }
            }
        }
    }

    override fun showError(error: String) {
        binding.chatProgressBar.visibility = View.GONE
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun hideProgressBar() {
        binding.chatProgressBar.visibility = View.GONE
    }

    override fun showProgressBar() {
        binding.chatProgressBar.visibility = View.VISIBLE
    }

    override fun openMessageFragment(chatId: String) {
        super.openMessageFragment(chatId)
        MessageFragment.newInstance(activity = activity as MainActivity,chatId)
    }

}