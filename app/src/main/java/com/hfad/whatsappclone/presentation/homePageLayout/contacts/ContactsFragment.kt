package com.hfad.whatsappclone.presentation.homePageLayout.contacts

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.whatsappclone.R
import com.hfad.whatsappclone.databinding.FragmentContactsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ContactsFragment : Fragment(), IContactsView {

    private val contactsViewModel: ContactsViewModel by viewModels()
    private var binding: FragmentContactsBinding? = null
    private var deviceContacts: MutableList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        deviceContacts = ContactsManager(requireContext()).getContacts()

        deviceContacts.map {
            it.replace("+998", "").replace(" ", "")
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let { binding ->
            if (deviceContacts.isNotEmpty()) {
                val newList = ArrayList<String>()
                newList.add("000000")
                newList.add("8765")
                newList.add("12345")
                newList.add("1234")
                newList.add("1234567")
                newList.add("9876")
                val contactsAdapter = ContactsAdapter()
                contactsViewModel.getAllWhatsAppContacts(newList, this)
                binding.contactsRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.contactsRecyclerView.adapter = contactsAdapter
                CoroutineScope(Dispatchers.IO).launch {
                    contactsViewModel.whatsAppContactsList.collectLatest {
                        if (it.isNotEmpty()) {
                            withContext(Dispatchers.Main) {
                                contactsAdapter.submitList(it)
                                binding.subtitleText.text =
                                    "${contactsAdapter.currentList.size} Contacts"
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(context, "You have 0 contacts in your device", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun showError(error: String) {
        binding?.let { it.contactsProgressBar.visibility = View.GONE }
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }


    override fun hideProgressBar() {
        binding?.let { it.contactsProgressBar.visibility = View.GONE }
    }

    override fun showProgressBar() {
        binding?.let { it.contactsProgressBar.visibility = View.VISIBLE }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contact_fragment_menu, menu)
    }
}