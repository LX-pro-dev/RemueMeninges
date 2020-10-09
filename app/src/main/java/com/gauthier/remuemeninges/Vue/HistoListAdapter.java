package com.gauthier.remuemeninges.Vue;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;

import java.util.ArrayList;

/**
 * Created by Alexandre GAUTHIER on 18/05/2020.
 */
public class HistoListAdapter extends BaseAdapter {
    private ArrayList<Carte> lesCartes;
    private LayoutInflater inflater;
    private Controle controle;
    private Context contexte;

    /**
     * Constructeur
     *
     * @param lesCartes
     */
    public HistoListAdapter(Context contexte, ArrayList<Carte> lesCartes) {
        this.lesCartes = new ArrayList<>(lesCartes);
        this.inflater = LayoutInflater.from(contexte);
        this.controle = Controle.getInstance(null);
        this.contexte = contexte;
    }

    public ArrayList<Carte> getItems() {
        return lesCartes;
    }

    public void updateItems(ArrayList<Carte> items) {
        this.lesCartes = new ArrayList<>(items);
        notifyDataSetChanged();//raffraichissement de la collection
    }

    /**
     * retourne le nb de lignes de la liste
     *
     * @return
     */
    @Override
    public int getCount() {
        return lesCartes.size();
    }

    /**
     * retourne l'item de la ligne actuelle
     *
     * @param i = position
     * @return
     */
    @Override
    public Object getItem(int i) {
        return lesCartes.get(i);
    }

    /**
     * retourne un indice par rapport à la ligne actuelle
     *
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * retourne la ligne (view) formatée avec gestion des événements
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //déclaration d'un holder
        ViewHolder holder;
        // si la ligne n'existe pas encore
        if (convertView == null) {
            holder = new ViewHolder();
            //la ligne est construite avec un formatage (inflater) relié à layout_list_histo
            convertView = inflater.inflate(R.layout.layout_list_histo, null);
            //chaque propriété du holder est reliée à une propriété graphique
            holder.btDeleteCard = (ImageButton) convertView.findViewById(R.id.btDeleteCard);
            holder.btModifycard = (ImageButton) convertView.findViewById(R.id.btModifyCard);
            holder.txtListDateCard = (TextView) convertView.findViewById(R.id.histoDateCrea);
            holder.txtListCategory = (TextView) convertView.findViewById(R.id.histoCategory);
            holder.txtListQuestion = (TextView) convertView.findViewById(R.id.histoQuestion);
            holder.histoRatingBar = (RatingBar) convertView.findViewById(R.id.histoRatingBarLevel);

            //affecter le holder à la vue
            convertView.setTag(holder);
        } else {
            //récupération du holder dans la ligne existante
            holder = (ViewHolder) convertView.getTag();
        }

        //valorisation du contenu du holder (donc de la ligne)
        if (lesCartes.get(position).getDatecreation().toString() != null && lesCartes.get(position).getCategorie().toString() != null && lesCartes.get(position).getQuestion() != null) {
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            holder.txtListDateCard.setText(df.format("yyyy-MM-dd hh'h'mm", lesCartes.get(position).getDatecreation()));
            holder.txtListCategory.setText(lesCartes.get(position).getCategorie().toString());
            holder.txtListQuestion.setText(lesCartes.get(position).getQuestion());
            holder.histoRatingBar.setRating(lesCartes.get(position).getLevel());
        }


        //événement : clic sur bouton delete
        holder.btDeleteCard.setTag(position);
        Log.i("Histo holder", "position = " + position);
        //clic sur la croix pour supprimer le profil enregistré
        holder.btDeleteCard.setOnClickListener(new View.OnClickListener() {
            //pour gérer un événement sur un objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)
            public void onClick(View v) {
                int position = -1;
                try {
                    //on récupère la position de la ligne dans la liste
                    position = (int) v.getTag();
                    //demande de suppression au controleur
                    controle.delCarte(lesCartes.get(position));
                    Log.d("histolist delete",""+lesCartes.get(position).getNumCarte());

                } catch (Exception e) {

                }
                if (position == -1) {
                    Toast toast = Toast.makeText(contexte.getApplicationContext(), "erreur ! carte non supprimée !", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else {
                    //affiché message de destruction de la carte
                    Toast toast = Toast.makeText(contexte.getApplicationContext(), "carte supprimée", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //rafraichir la liste
                    notifyDataSetChanged();//pas de prise en compte de la modif dans la liste si on ne retourne pas d'abord sur l'accueil!
                }
            }

        });

        //événement : clic sur bouton modify
        holder.btModifycard.setTag(position);
        Log.i("Histo holder", "position = " + position);
        //clic sur la croix pour supprimer le profil enregistré
        holder.btModifycard.setOnClickListener(new View.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                //on récupère la position de la ligne dans la liste
                int position = (int) v.getTag();
                //demande de suppression au controleur
                controle.modifyCarte(lesCartes.get(position));
                //rafraichir la liste
                notifyDataSetChanged();
            }
        });

        //clic sur le reste de la ligne pour afficher la carte enregistrée (date ou question)
        holder.txtListDateCard.setTag(position);
        holder.txtListDateCard.setOnClickListener(new View.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                //récupère la position de la ligne dans la liste
                int position = (int) v.getTag();
                Log.i("HistoList onClick", "position = " + position);

                //demande de l'affichage de la carte dans CardActivity
                ((HistoActivity) contexte).afficheCarte(lesCartes.get(position));
            }
        });

        holder.txtListQuestion.setTag(position);
        //clic sur le reste de la ligne pour afficher le profil enregistré
        Log.i("HistoList onClick", "position = " + position);
        holder.txtListQuestion.setOnClickListener(new View.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)
            public void onClick(View v) {
                //récupère la position de la ligne
                int position = (int) v.getTag();
                //demande de l'affichage de la carte dans CardActivity
                ((HistoActivity) contexte).afficheCarte(lesCartes.get(position));
            }
        });
        return convertView;
    }


    private class ViewHolder {
        ImageButton btModifycard;
        RatingBar histoRatingBar;
        ImageButton btDeleteCard;
        TextView txtListDateCard;
        TextView txtListCategory;
        TextView txtListQuestion;

    }
}
