package com.example.calls

import Contact
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private val contactList: MutableList<Contact>,
    private val onEdit: (Contact) -> Unit,
    private val onDelete: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contact_name)
        val emailTextView: TextView = itemView.findViewById(R.id.contact_email)
        val phoneTextView: TextView = itemView.findViewById(R.id.contact_phone)
        val editButton: Button = itemView.findViewById(R.id.edit_button)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.nameTextView.text = contact.name
        holder.emailTextView.text = contact.email
        holder.phoneTextView.text = contact.phone.toString() // Convert Long to String for display

        holder.editButton.setOnClickListener { onEdit(contact) }
        holder.deleteButton.setOnClickListener { onDelete(contact) }
    }

    override fun getItemCount() = contactList.size
}
