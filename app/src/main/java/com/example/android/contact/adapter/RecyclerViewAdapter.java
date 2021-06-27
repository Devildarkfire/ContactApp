package com.example.android.contact.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.contact.ContactDetail;
import com.example.android.contact.EnterPersonDetail;
import com.example.android.contact.R;
import com.example.android.contact.data.MyDbHelper;
import com.example.android.contact.model.Contact;
import com.example.android.contact.utils.DbBitmapUtility;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Contact> contactList;
    private Contact contact;

    public RecyclerViewAdapter(Context context, List<Contact> contactList){
        this.context=context;
        this.contactList=contactList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        contact = contactList.get(position);
        holder.contactName.setId(contact.getId());
        holder.contactName.setText(contact.getName());
        holder.contactNumber.setText(contact.getPhoneNumber());
        Bitmap ImageBitmap = DbBitmapUtility.getImage(contact.getImage());
        holder.contactImage.setImageBitmap(ImageBitmap);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView contactName;
        private TextView contactNumber;
        private ImageView contactImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.contact_name);
            contactNumber = itemView.findViewById(R.id.contact_number);
            contactImage = itemView.findViewById(R.id.person_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contact = contactList.get(getAdapterPosition());
                    Bitmap ImageBitmap = DbBitmapUtility.getImage(contact.getImage());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    ImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Intent intent = new Intent(view.getContext(), ContactDetail.class);
                    intent.putExtra("MyName",  contact.getName());
                    intent.putExtra("MyNumber",contact.getPhoneNumber());
                    intent.putExtra("MyId",contact.getId());
                    intent.putExtra("MyImage",byteArray);
                    view.getContext().startActivity(intent);
                }
            });
        }

    }

}
