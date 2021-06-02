package com.andersenlab.andersen.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.andersenlab.andersen.Person
import com.andersenlab.andersen.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class ContactsListFragment : Fragment() {

    private var currentPosition = 0
    private var isLandscape: Boolean = false
    private var names = arrayListOf<String>()
    private lateinit var listView: ListView

    companion object {
        var data = arrayListOf<Person>()
        fun newInstance() =
                ContactsListFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (data.isEmpty()) {
            repeat(6) {
                val person = Person(
                        UUID.randomUUID().toString(),
                        Random.nextLong(10000000, 90000000).toString(),
                        "image"
                )
                data.add(person)
            }
        }
        names = arrayListOf()
        for (i in 0 until data.size) {
            names.add(data[i].name)
        }
        listView = view.findViewById(R.id.contacts_list)
        val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_activated_1,
                names
        )
        listView.adapter = adapter
        listView.onItemClickListener = OnItemClickListener { _, view, position, _ ->
            if (currentPosition != position) {
                currentPosition = position
                showDetails(position)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isLandscape = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)
        if (isLandscape) {
            listView.choiceMode = ListView.CHOICE_MODE_SINGLE
            showDetails(currentPosition)
        }
    }

    private fun showDetails(index: Int) {
        if (isLandscape) {
            listView.setItemChecked(currentPosition, true)
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.details, ContactDetailsFragment.newInstance(data[index], index))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
        } else {
            requireActivity().findViewById<FrameLayout>(R.id.contacts).visibility = View.GONE
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.details, ContactDetailsFragment.newInstance(data[index], index))
                    .addToBackStack(null)
                    .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentPosition", currentPosition)
        outState.putParcelableArrayList("data", ArrayList(data))
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelableArrayList<Person>("data") as ArrayList<Person>
            currentPosition = savedInstanceState.getInt("currentPosition")
        }
    }
}