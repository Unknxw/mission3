package fr.cned.emdsgil.fr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fr.cned.emdsgil.suividevosfrais.R;

class FraisHfAdapter extends BaseAdapter {

    private final List<FraisHf> lesFrais;
    private final LayoutInflater inflater;
    private final int key;

    FraisHfAdapter(Context context, List<FraisHf> lesFrais, int key) {
        inflater = LayoutInflater.from(context);
        this.lesFrais = lesFrais;
        this.key = key;
    }

    @Override
    public int getCount() {
        return lesFrais.size();
    }

    @Override
    public Object getItem(int index) {
        return lesFrais.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    /**
     * Affichage dans la liste
     */
    @Override
    public View getView(final int index, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_liste, parent, false);
            holder.setTxtListJour((TextView) convertView.findViewById(R.id.txtListJour));
            holder.setTxtListMontant((TextView) convertView.findViewById(R.id.txtListMontant));
            holder.setTxtListMotif((TextView) convertView.findViewById(R.id.txtListMotif));
            holder.setIb((ImageButton) convertView.findViewById(R.id.cmdSuppHf));
            holder.getIb().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<FraisHf> costsMonths = Objects.requireNonNull(Global.listFraisMois.get(key)).getCosts();
                    costsMonths.remove(index);
                    Serializer.serialize(Global.listFraisMois, MainActivity.instance);
                    FraisHfAdapter.super.notifyDataSetChanged();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.getTxtListJour().setText(String.format(Locale.FRANCE, "%d", lesFrais.get(index).getDay()));
        holder.getTxtListMontant().setText(String.format(Locale.FRANCE, "%.2f", lesFrais.get(index).getPrice()));
        holder.getTxtListMotif().setText(lesFrais.get(index).getMotif());
        return convertView;
    }


    private static class ViewHolder {

        private TextView txtListJour;
        private TextView txtListMontant;
        private TextView txtListMotif;
        private ImageButton ib;

        public TextView getTxtListJour() {
            return txtListJour;
        }

        public void setTxtListJour(TextView txtListJour) {
            this.txtListJour = txtListJour;
        }

        public TextView getTxtListMontant() {
            return txtListMontant;
        }

        public void setTxtListMontant(TextView txtListMontant) {
            this.txtListMontant = txtListMontant;
        }

        public TextView getTxtListMotif() {
            return txtListMotif;
        }

        public void setTxtListMotif(TextView txtListMotif) {
            this.txtListMotif = txtListMotif;
        }

        public ImageButton getIb() {
            return ib;
        }

        public void setIb(ImageButton ib) {
            this.ib = ib;
        }
    }

}
