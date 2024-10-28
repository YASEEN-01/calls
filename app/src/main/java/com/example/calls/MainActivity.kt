package com.example.calls

import Contact
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactList: MutableList<Contact>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.contact_recycler_view)
        val addContactButton: Button = findViewById(R.id.add_contact_button)

        database = FirebaseDatabase.getInstance().getReference("contacts")

        contactList = mutableListOf()
        contactAdapter = ContactAdapter(contactList, ::onEditContact, ::onDeleteContact)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter

        loadContacts()

        addContactButton.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadContacts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactList.clear()
                for (contactSnapshot in snapshot.children) {
                    val contact = contactSnapshot.getValue(Contact::class.java)
                    if (contact != null) {
                        // Set the key for the contact
                        contactList.add(contact.copy(key = contactSnapshot.key ?: ""))
                    }
                }
                contactAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    private fun onEditContact(contact: Contact) {

        val intent = Intent(this, EditContactActivity::class.java).apply {
            putExtra("CONTACT_KEY", contact)
        }
        startActivity(intent)
    }

    private fun onDeleteContact(contact: Contact) {
        val contactKey = contact.key
        if (contactKey != null) {
            database.child(contactKey).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    contactList.remove(contact)
                    contactAdapter.notifyDataSetChanged()
                } else {

                }
            }
        }
    }
}