package com.andersenlab.andersen.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.andersenlab.andersen.Person
import com.andersenlab.andersen.R

class ContactDetailsFragment : Fragment() {

    private lateinit var person: Person
    private lateinit var buttonEdit: Button
    private lateinit var buttonOk: Button
    private var index = 0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameTextView = view.findViewById<TextView>(R.id.name)
        nameTextView.text = person.name
        val phoneTextView = view.findViewById<TextView>(R.id.phone)
        phoneTextView.text = person.phone
        val editPhoneTextView = view.findViewById<TextView>(R.id.editPhone)
        phoneTextView.text = person.phone
        val editNameTextView = view.findViewById<TextView>(R.id.editName)
        phoneTextView.text = person.phone
        buttonEdit = view.findViewById(R.id.buttonEdit)
        buttonOk = view.findViewById(R.id.buttonOk)
        buttonEdit.setOnClickListener {
            nameTextView.visibility = GONE
            phoneTextView.visibility = GONE
            editNameTextView.visibility = VISIBLE
            editPhoneTextView.visibility = VISIBLE
            editNameTextView.text = person.name
            editPhoneTextView.text = person.phone
            buttonOk.visibility = VISIBLE
            buttonEdit.visibility = GONE
        }
        buttonOk.setOnClickListener {
            nameTextView.visibility = VISIBLE
            phoneTextView.visibility = VISIBLE
            editNameTextView.visibility = GONE
            editPhoneTextView.visibility = GONE
            buttonOk.visibility = GONE
            buttonEdit.visibility = VISIBLE

            val newName = editNameTextView.text.toString()
            val newPhone = editPhoneTextView.text.toString()
            person.name = newName
            person.phone = newPhone
            nameTextView.text = newName
            phoneTextView.text = newPhone
            ContactsListFragment.data[index].name = newName
            ContactsListFragment.data[index].phone = newPhone
        }
    }

    companion object {
        fun newInstance(person: Person, index: Int) =
            ContactDetailsFragment().apply {
                this.person = person
                this.index = index
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("person", person)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            this.person = savedInstanceState.getParcelable<Person>("person")!! as Person
        }
    }
}