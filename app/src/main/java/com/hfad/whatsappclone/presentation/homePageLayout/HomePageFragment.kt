package com.hfad.whatsappclone.presentation.homePageLayout

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.hfad.whatsappclone.R
import com.hfad.whatsappclone.databinding.FragmentHomePageBinding
import com.hfad.whatsappclone.presentation.homePageLayout.calls.CallFragment
import com.hfad.whatsappclone.presentation.homePageLayout.chat.ChatFragment
import com.hfad.whatsappclone.presentation.homePageLayout.contacts.ContactsFragment
import com.hfad.whatsappclone.presentation.homePageLayout.status.StatusFragment
import com.hfad.whatsappclone.util.OutlineProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    var fragmentList = arrayListOf(
        ChatFragment(),
        CallFragment(),
        StatusFragment()
    )

    private var requestReadContactsLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                activity?.let {
                    it.supportFragmentManager.beginTransaction()
                        .add(R.id.fragmentContainer, ContactsFragment(), "contacts_fragment")
                        .addToBackStack("contacts_fragment").commit()
                }
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {
                    launchContactsLauncherOnceAgain()
                }
            }
        }

    private fun launchContactsLauncherOnceAgain() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Permission needed")
                .setMessage("This app needs access to your contacts to function properly")
                .setPositiveButton("OK") { _, _ ->
                    requestReadContactsLauncher.launch(android.Manifest.permission.READ_CONTACTS)
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        binding.fabShowContacts.outlineProvider = OutlineProvider()
        binding.fabShowContacts.clipToOutline = true
        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Green1)
        binding.fabShowContacts.setOnClickListener {
            requestReadContactsLauncher.launch(android.Manifest.permission.READ_CONTACTS)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPagerAndTabLayout()
    }

    private fun setUpViewPagerAndTabLayout() {

        binding.viewPager2.adapter =
            object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return fragmentList.size
                }

                override fun createFragment(position: Int): Fragment {
                    return fragmentList[position]
                }
            }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Chats"
                1 -> tab.text = "Calls"
                2 -> tab.text = "Status"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                // Handle search action
                true
            }
            R.id.menu_camera -> {
                // Handle camera action
                true
            }
            R.id.create_new_group -> {
                // Handle more item 1 action
                true
            }
            R.id.create_new_broadcast -> {
                // Handle more item 2 action
                true
            }
            R.id.payments -> {
                // Handle more item 2 action
                true
            }
            R.id.settings -> {
                // Handle more item 2 action
                true
            }
            R.id.starred_message -> {
                // Handle more item 2 action
                true
            }
            R.id.linked_devices -> {
                // Handle more item 2 action
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requestReadContactsLauncher.unregister()
    }
}