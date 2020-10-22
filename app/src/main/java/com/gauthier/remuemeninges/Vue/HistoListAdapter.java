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
import com.gauthier.remuemeninges.Modele.Member;
import com.gauthier.remuemeninges.R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;

import static com.gauthier.remuemeninges.Vue.MainActivity.IS_ADMIN;
import static com.gauthier.remuemeninges.Vue.MainActivity.IS_CREATOR;

/**
 * Created by Alexandre GAUTHIER on 18/05/2020.
 */
public class HistoListAdapter extends BaseAdapter {
    private ArrayList<Carte> lesCartes;
    private LayoutInflater inflater;
    private Controle controle;
    private Context contexte;
    private Member member;

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
        this.member = Member.getInstance(null);
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

            FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            if (mFirebaseRemoteConfig.getBoolean(IS_ADMIN)) {

            } else if (mFirebaseRemoteConfig.getBoolean(IS_CREATOR)) {
                holder.btDeleteCard.setVisibility(View.GONE);
            } else {
                holder.btDeleteCard.setVisibility(View.GONE);
                holder.btModifycard.setVisibility(View.GONE);
            }

            //affecter le holder à la vue
            convertView.setTag(holder);
        } else {
            //récupération du holder dans la ligne existante
            holder = (ViewHolder) convertView.getTag();
        }

        //valorisation du contenu du holder (donc de la ligne)
        Carte card = lesCartes.get(position);
        if (card != null && card.getDatecreation() != null && card.getCategorie() != null && card.getQuestion() != null) {//NPE
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
                try {
                    //on récupère la position de la ligne dans la liste
                    //demande de suppression au controleur
                    Log.d("histolist delete", "" + lesCartes.get(position).getNumCarte());
                    controle.delCarte(lesCartes.get(position));

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(contexte.getApplicationContext(), "erreur ! carte non supprimée !", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
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
                Log.i("HistoL onClick modify", "position = " + position);

                //demande d'affichage de la carte dans CreateActivity
                ((HistoActivity) contexte).modifyCarte(lesCartes.get(position));
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
                //demande de l'affichage de la carte dans CardActivity
                ((HistoActivity) contexte).afficheCarte(lesCartes.get(position));
            }
        });
        return convertView;
    }

    /**
     * modifier le contenu d'une carte
     *
     * @param carte
     */
    public void modifyCard(Carte carte) {
        for (Carte card : lesCartes) {
            if (card.getNumCarte() == carte.getNumCarte()) {
                card = carte;
            }
        }
    }

    public void deleteCard(int idCardDeleted) {
        Carte card1 = null;
        for (Carte card : lesCartes) {
            if (card.getNumCarte() == idCardDeleted) {
                card1 = card;
            }
        }
        if (card1 != null) {
            lesCartes.remove(card1);
            //rafraichir la liste
            notifyDataSetChanged();
        }
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
