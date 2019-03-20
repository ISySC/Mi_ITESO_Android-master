package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.EmailDetailsActivity;
import mx.iteso.miiteso.miiteso.model.ItemEmail;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class EmailAdapter extends ArrayAdapter<ItemEmail> {

    private List<ItemEmail> itemEmails;
    private Activity activity;

    public EmailAdapter(@NonNull Activity activity, int resource, ArrayList<ItemEmail> itemEmails) {
        super(activity, resource, itemEmails);

        this.activity = activity;
        this.itemEmails = itemEmails;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.item_email, parent, false);

        TextView lbl_from = (TextView) itemView.findViewById(R.id.lbl_from);
        TextView lbl_date = (TextView) itemView.findViewById(R.id.date);
        TextView lbl_subject = (TextView) itemView.findViewById(R.id.lbl_subject);
        TextView lbl_word = (TextView) itemView.findViewById(R.id.lbl_word);
        RelativeLayout rl_item_email = (RelativeLayout) itemView.findViewById(R.id.rl_item_email);

        lbl_from.setText(itemEmails.get(position).getLbl_from());
        lbl_date.setText(itemEmails.get(position).getLbl_date());
        lbl_subject.setText(itemEmails.get(position).getLbl_subject());
        lbl_word.setText(itemEmails.get(position).getLbl_word());

        lbl_from.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_date.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratLight));
        lbl_subject.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratLight));
        lbl_word.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));

        rl_item_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Metodos(getContext()).NavegarAPantalla(EmailDetailsActivity.class);
            }
        });

        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
