package com.myapp.project.tictactoe;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

/**
 * Adapter class to list saved games
 */
public class saveArrayAdapter extends ArrayAdapter<File>{
    public ArrayList<File> files;
    private Context context;

    public saveArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<File> objects) {
        super(context, resource, objects);
        files = objects;
        this.context = context;
    }


    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        File file = files.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.save_file_item, parent, false);
        }

        TextView fileName = (TextView) convertView.findViewById(R.id.saveTitle);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.deleteSave);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = files.remove(position);
                f.delete();
                notifyDataSetChanged();
            }
        });
        fileName.setText(file.getName());
        convertView.getTag(position);

        return convertView;
    }
}
