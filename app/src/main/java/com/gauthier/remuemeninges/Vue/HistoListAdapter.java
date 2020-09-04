package com.gauthier.remuemeninges.Vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

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
     * @param lesCartes
     */
    public HistoListAdapter(Context contexte, ArrayList<Carte> lesCartes){
        this.lesCartes = lesCartes;
        this.inflater=LayoutInflater.from(contexte);
        this.controle=Controle.getInstance(null);
        this.contexte=contexte;
    }

    /**
     * retourne le nb de ligne de la liste
     * @return
     */
    @Override
    public int getCount() {
        return lesCartes.size();
    }

    /**
     * retourne l'item de la ligne actuelle
     * @param i = position
     * @return
     */
    @Override
    public Object getItem(int i) {
        return lesCartes.get(i);
    }

    /**
     * retourne un indice par rapport à la ligne actuelle
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * retourne la ligne (view) formaté avec gestion des événements
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
        if(convertView==null) {
            holder = new ViewHolder();
            //la ligne est construite avec un formatage (inflater) relié à layout_list_histo
            convertView = inflater.inflate(R.layout.layout_list_histo, null);
            //chaque propriété du holder est relié à une propriété graphique
            holder.btDeleteCard=(ImageButton)convertView.findViewById(R.id.btDeleteCard);
            holder.txtListDateCard=(TextView)convertView.findViewById(R.id.txtListDateCreationCard);
            holder.txtListCategory=(TextView)convertView.findViewById(R.id.txtListCategory);
            holder.txtListQuestion=(TextView)convertView.findViewById(R.id.txtListQuestion);

            //affecter le holder à la vue
            convertView.setTag(holder);
        }else{
            //récupération du holder dans la ligne existante
            holder=(ViewHolder)convertView.getTag();
        }

        //valorisation du contenu du holder (donc de la ligne)
        holder.txtListDateCard.setText(lesCartes.get(position).getNumCarte());
        holder.txtListCategory.setText(lesCartes.get(position).getCategorie());
        holder.txtListQuestion.setText(lesCartes.get(position).getQuestion());
        holder.btDeleteCard.setTag(position);
        //clic sur la croix pour supprimer le profil enregistré
        holder.btDeleteCard.setOnClickListener(new View.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                int position = (int) v.getTag();
                //demande de suppression au controleur
                controle.delCarte(lesCartes.get(position));
                //rafraichir la liste
                notifyDataSetChanged();
            }

        } );
        holder.txtListDateCard.setTag(position);
        //clic sur le reste de la ligne pour afficher le profil enregistré
        holder.txtListDateCard.setOnClickListener(new View.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                //récupère la position de la ligne
                int position = (int) v.getTag();
                //demande de l'affichage du profil dans CalculActivity
                ((HistoActivity)contexte).afficheCarte(lesCartes.get(position));
            }
        } );

        holder.txtListQuestion.setTag(position);
        //clic sur le reste de la ligne pour afficher le profil enregistré
        holder.txtListQuestion.setOnClickListener(new View.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                //récupère la position de la ligne
                int position = (int) v.getTag();
                //demande de l'affichage du profil dans CalculActivity
                ((HistoActivity)contexte).afficheCarte(lesCartes.get(position));
            }
        } );
        return convertView;
    }


    private class ViewHolder{
        ImageButton btDeleteCard;
        TextView txtListDateCard;
        TextView txtListCategory;
        TextView txtListQuestion;

    }
}
