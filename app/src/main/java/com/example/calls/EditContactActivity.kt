package com.example.calls

import Contact
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditContactActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var database: DatabaseReference
    private lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact) // Make sure this layout exists

        // Initialize views
        nameInput = findViewById(R.id.name_input)
        phoneInput = findViewById(R.id.phone_input)
        emailInput = findViewById(R.id.email_input)
        val updateButton: Button = findViewById(R.id.update_button)
        val deleteButton: Button = findViewById(R.id.delete_button)

        database = FirebaseDatabase.getInstance().getReference("contacts")

        // Retrieve the contact passed in the intent
        contact = intent.getParcelableExtra("CONTACT_KEY") ?: run {
            Toast.makeText(this, "Contact not found", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if contact is not found
            return
        }

        // Populate fields with contact data
        populateFields()

        // Set click listeners
        updateButton.setOnClickListener {
            updateContact()
        }

        deleteButton.setOnClickListener {
            deleteContact()
        }
    }

    private fun populateFields() {
        nameInput.setText(contact.name)
        phoneInput.setText(contact.phone.toString())
        emailInput.setText(contact.email)
    }

    private fun updateContact() {
        val phoneInputValue = phoneInput.text.toString()

        // Validate phone number format
        if (phoneInputValue.isEmpty()) {
            Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the updated contact using the correct types
        val updatedContact = Contact(
            key = contact.key,  // Use existing key
            name = nameInput.text.toString(),
            phone = phoneInputValue, // Keep as String
            email = emailInput.text.toString()
        )

        // Use a unique identifier for the contact
        database.child(contact.key).setValue(updatedContact) // Use the unique key for Firebase
            .addOnSuccessListener {
                Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Log.e("EditContactActivity", "Failed to update contact", error)
                Toast.makeText(
                    this,
                    "Failed to update contact: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun deleteContact() {
        // Use a unique identifier for the contact
        database.child(contact.email).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Log.e("EditContactActivity", "Failed to delete contact", error)
                Toast.makeText(
                    this,
                    "Failed to delete contact: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}