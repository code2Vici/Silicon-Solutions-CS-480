package siliconsolutions.cpp.phonecontactsbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by naborp on 5/19/2017.
 */

public class ContactBookArrayAdapter extends ArrayAdapter<ContactInfo> {//BaseExpandableListAdapter {

    private Context context;
    private int layoutResource;
    private List<ContactInfo> contactInfoList;
    private LayoutInflater layoutInflater;
    private String phoneNumber;
    private String emailAddress;

    public ContactBookArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ContactInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.contactInfoList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layoutResource, parent, false);

        phoneNumber = "";
        emailAddress = "";

        ImageButton callButton = (ImageButton) convertView.findViewById(R.id.callButton);
        ImageButton emailButton = (ImageButton) convertView.findViewById(R.id.emailButton);
        TextView textViewName = (TextView) convertView.findViewById(R.id.contactNameTextView);
        final TextView textViewNum = (TextView) convertView.findViewById(R.id.phoneNumTextView);
        TextView textViewAddress = (TextView) convertView.findViewById(R.id.addressTextView);
        TextView textViewEmail = (TextView) convertView.findViewById(R.id.emailAddressTextView);
        TextView newEntryTextView = (TextView) convertView.findViewById(R.id.newEntryTextView);
        TextView newEntryTextView1 = (TextView) convertView.findViewById(R.id.newEntryTextView1);
        TextView newEntryTextView2 = (TextView) convertView.findViewById(R.id.newEntryTextView2);
        TextView newEntryTextView3 = (TextView) convertView.findViewById(R.id.newEntryTextView3);
        TextView newEntryTextView4 = (TextView) convertView.findViewById(R.id.newEntryTextView4);

        //imageButton.setImageResource(R.drawable.default_contact_image);
        textViewName.setText(contactInfoList.get(position).getContactName());
        textViewNum.setText(contactInfoList.get(position).getPhoneNumber());
        textViewAddress.setText(contactInfoList.get(position).getPhysicalAddress());
        textViewEmail.setText(contactInfoList.get(position).getEmailAddress());


        if(newEntryTextView.getText().toString().isEmpty()) {}
        else {
            newEntryTextView.setText(contactInfoList.get(position).getLinkedInField());
        }
        if(newEntryTextView1.getText().toString().isEmpty()) {}
        else {
            newEntryTextView1.setText(contactInfoList.get(position).getInstagramField());
        }
        if(newEntryTextView2.getText().toString().isEmpty()) {}
        else {
            newEntryTextView2.setText(contactInfoList.get(position).getSnapchatField());
        }
        if(newEntryTextView3.getText().toString().isEmpty()) {}
        else {
            newEntryTextView3.setText(contactInfoList.get(position).getOtherField());
        }
        if(newEntryTextView4.getText().toString().isEmpty()) {}
        else {
            newEntryTextView4.setText(contactInfoList.get(position).getOtherField2());
        }

        phoneNumber = contactInfoList.get(position).getPhoneNumber();

        emailAddress = contactInfoList.get(position).getEmailAddress();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = contactInfoList.get(position).getPhoneNumber();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                view.getContext().startActivity(phoneIntent);
                Log.i("TEST", phoneNumber);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailAddress = contactInfoList.get(position).getEmailAddress();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress, null));
                /*emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress)*/;
                view.getContext().startActivity(emailIntent);
                Log.i("TEST", phoneNumber);
            }
        });

        return convertView;
    }



}
