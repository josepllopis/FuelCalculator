package apps.lustven.fuelcalculator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

import apps.lustven.fuelcalculator.model.App;


public class AppAdapter extends ArrayAdapter<App> {
    private Context mContext;
    private int mResource;
    private MainActivity main;


    public AppAdapter(@NonNull Context context, int resource, @NonNull List<App> objects, MainActivity main) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.main = main;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);
        ImageView imageView = convertView.findViewById(R.id.imageB);
        TextView textView = convertView.findViewById(R.id.textName);



        imageView.setImageURI(Uri.parse(getItem(position).getImage()));
        textView.setText(getItem(position).getTitle());


        String UrlImage = getItem(position).getImage();
        Glide.with(main.getApplicationContext()).load(UrlImage).into(imageView);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getItem(position).getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                main.startActivity(intent);

            }
        });


        return convertView;
    }
}

