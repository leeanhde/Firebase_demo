package com.example.demofirebase.adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demofirebase.R;
import com.example.demofirebase.modals.ContactModal;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private final List<ContactModal> contacts;

    private final OnContactClickListener onContactClickListener;

    // Interface for click events
    public interface OnContactClickListener {
        void onContactClick(ContactModal contact);
    }

    public ContactAdapter(List<ContactModal> contactList, OnContactClickListener onContactClickListener) {
        this.contacts = contactList;
        this.onContactClickListener = onContactClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addContact(List<ContactModal> contactList) {
        contacts.clear();
        contacts.addAll(contactList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactModal contact = contacts.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvEmail.setText(contact.getEmail());

        if (!TextUtils.isEmpty(contact.getPhotoUri())) {
            Uri uri = Uri.parse(contact.getPhotoUri());
            Glide.with(holder.ivAvatar.getContext())
                    .load(uri)
                    .placeholder(R.drawable.baseline_coronavirus_24)
                    .error(R.drawable.baseline_coronavirus_24)
                    .circleCrop()
                    .into(holder.ivAvatar);
        } else {
            Glide.with(holder.ivAvatar.getContext())
                    .load(R.drawable.baseline_coronavirus_24)
                    .circleCrop()
                    .into(holder.ivAvatar);
        }

        holder.itemView.setOnClickListener(v -> onContactClickListener.onContactClick(contact));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvName;
        AppCompatTextView tvEmail;
        AppCompatImageView ivAvatar;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
