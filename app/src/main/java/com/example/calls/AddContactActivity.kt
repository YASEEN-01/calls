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

class AddContactActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        Log.d("AddContactActivity", "Layout set successfully")

        nameInput = findViewById(R.id.name_input)
        if (nameInput == null) Log.e("AddContactActivity", "name_input is null")

        phoneInput = findViewById(R.id.phone_input)
        if (phoneInput == null) Log.e("AddContactActivity", "phone_input is null")

        emailInput = findViewById(R.id.email_input)
        if (emailInput == null) Log.e("AddContactActivity", "email_input is null")

        val addButton: Button = findViewById(R.id.add_button)
        if (addButton == null) Log.e("AddContactActivity", "add_button is null")

        database = FirebaseDatabase.getInstance().getReference("contacts")

        addButton.setOnClickListener {
            addContact()
        }
    }


    private fun addContact() {
        val name = nameInput.text.toString()
        val phoneInputValue = phoneInput.text.toString()
        val email = emailInput.text.toString()

        if (name.isNotEmpty() && phoneInputValue.isNotEmpty() && email.isNotEmpty()) {
            // No need to convert phoneInputValue to Long
            val contact =
                Contact(name = name, phone = phoneInputValue, email = email) // Use String for phone
            database.child(phoneInputValue).setValue(contact) // Using phone number as the unique ID
                .addOnSuccessListener {
                    Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}