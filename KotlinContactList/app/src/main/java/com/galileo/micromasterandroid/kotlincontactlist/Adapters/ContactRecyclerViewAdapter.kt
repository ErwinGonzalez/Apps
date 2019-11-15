package com.galileo.micromasterandroid.kotlincontactlist.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.galileo.micromasterandroid.kotlincontactlist.DataModels.Contact
import com.galileo.micromasterandroid.kotlincontactlist.R

class ContactRecyclerViewAdapter
        internal constructor(context : Context, callback:OnClickCallback):RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactListViewHolder>(){

    private val inflater : LayoutInflater
    private var contactList: List<Contact>? = null
    private var clickCallback: OnClickCallback? = null

    init{
        inflater = LayoutInflater.from(context)
        clickCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val itemView = inflater.inflate(R.layout.contact_item,parent,false)
        return ContactListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if(contactList != null)
            contactList!!.size
        else
            0
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        if(contactList != null){
            val contact = contactList!![position]
            holder.contactNameTextView.setText(contact.name)
            holder.contactPhoneTextView.setText(contact.phone)
        }
    }

    internal fun setList(contactList: List<Contact>){
        this.contactList  = contactList
        notifyDataSetChanged()
    }

    inner class ContactListViewHolder (itemView: View): ViewHolder(itemView),View.OnClickListener{

        val contactNameTextView: TextView
        val contactPhoneTextView: TextView

        init{
            contactNameTextView = itemView.findViewById(R.id.contact_name)
            contactPhoneTextView = itemView.findViewById(R.id.contact_phone)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view:View){
            if(clickCallback!= null)
                clickCallback?.handleClick(adapterPosition)
        }

    }
    interface OnClickCallback{
        fun handleClick(position: Int)
    }
}