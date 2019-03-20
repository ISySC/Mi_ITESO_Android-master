package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.model.ItemNotification;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class NotificationAdapter extends ArrayAdapter<ItemNotification> {

    private List<ItemNotification> itemNotifications;
    private Activity activity;
    private LinearLayout lyContainer;

    public NotificationAdapter(@NonNull Activity activity, int resource, ArrayList<ItemNotification> itemNotifications) {
        super(activity, resource, itemNotifications);

        this.activity = activity;
        this.itemNotifications = itemNotifications;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.item_notification, parent, false);

        TextView lbl_title_notification = (TextView) itemView.findViewById(R.id.lbl_title_notification);
        TextView lbl_date = (TextView) itemView.findViewById(R.id.date);
        lyContainer = itemView.findViewById(R.id.lyContainer);
        ImageView img = itemView.findViewById(R.id.img);

        lbl_title_notification.setText(itemNotifications.get(position).getEvento());
        lbl_date.setText(itemNotifications.get(position).getFechaEnvio() + " hrs");

        lbl_title_notification.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratMedium));
        lbl_date.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratLight));

        if (itemNotifications.get(position).getFechaLectura().equals("")) {
            lyContainer.setBackgroundColor(activity.getResources().getColor(R.color.color_background_notification));
            img.setVisibility(View.GONE);
        }
        else img.setVisibility(View.VISIBLE);
        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
