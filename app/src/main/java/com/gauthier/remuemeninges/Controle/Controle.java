package com.gauthier.remuemeninges.Controle;

import android.content.Context;
import android.util.Log;
import com.gauthier.remuemeninges.Outils.AccesDistant;
import com.gauthier.remuemeninges.Modele.Carte;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.gauthier.remuemeninges.Modele.Carte.convertJSonToCarte;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public final class Controle {
    //singleton pattern
    private static Controle instance = null;//accessible par la classe
    private static Carte carte;
    private static AccesDistant accesDistant;
    private static Context contexte;
    private ArrayList<Carte> lesCartes = new ArrayList<>();
    private CardEventListener listener = null;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);


    /**
     * Contructeur privé
     */
    private Controle() {
        super();
    }//ne déclare rien, on ne pourra faire new car déclaré en private


    /**
     * Création de l'instance
     * @param contexte
     * @return l'instance
     */
    public static final Controle getInstance(Context contexte) {//au lieu de faire new pour créer le contrôleur
        if (contexte != null) {
            Controle.contexte = contexte;
        }
        if (Controle.instance == null) {//si l'instance n'est pas déjà créée, on la créée
            Controle.instance = new Controle();
            accesDistant = new AccesDistant();
            accesDistant.envoi("tous", null);//demande de récupérer tous les profils
        }
        return Controle.instance;
    }

    /**
     * Récupérer la liste des cartes
     * @return un tableau de cates
     */
    public ArrayList<Carte> getLesCartes() {
        return lesCartes;
    }

    /**
     * Ajouter la liste des cartes
     * @param lesCartes
     */
    public void setLesCartes(ArrayList<Carte> lesCartes) {
        this.lesCartes = lesCartes;
    }

    /**
     * Création de carte
     * @param langue
     * @param question
     * @param indice
     * @param reponse
     * @param categorie
     * @param level
     */
    public void creerCarte(String langue, String question, String indice, String reponse, Integer categorie, Integer level) {// création du profil
        //Appel de cette méthode dans Main pour obtenir les infos de profil
        //on crée des cartes en français uniquement pour l'instant voir s'il n'y a pas conflit avec afficheResult()
        Carte uneCarte = new Carte(langue, question, indice, reponse, categorie, level);
        uneCarte.setDatecreation(new Date());
        uneCarte.setNumCarte(lesCartes.get(lesCartes.size() - 1).getNumCarte() + 1);
        Log.i("numCarte", "***********" + lesCartes.get(lesCartes.size() - 1).getNumCarte() + 1);
        lesCartes.add(uneCarte);
        accesDistant.envoi("enreg", uneCarte.convertToJSONObject());// il passe par processFinish d'AccesDistant au lieu de passer par envoi() !
        Log.d("envoie bdd", uneCarte.convertToJSONObject().toString());
    }

    /**
     * Supprimer une carte dans la base distante et la collection
     * @param carte
     */
    public void delCarte(Carte carte) {
        Log.d("delCarte()", "numCarte =" + carte.getNumCarte());
        //requête serveur
        accesDistant.envoi("delete", carte.convertToJSONObject());
        Log.d("delCarte()", "json =" + carte.convertToJSONObject());
    }

    /**
     * Modifier une carte dans la base distante
     * @param carte
     */
    public void modifyCarte(Carte carte) {
        //requête serveur
        accesDistant.envoi("modify", carte.convertToJSONObject());
        //modification de la carte dans la liste de cartes
    }

    /**
     * Récupérer une carte
     * @return null si la carte est null
     */
    public Carte getCarte() {
        if (carte == null) {
            return null;
        }
        return carte;
    }

    /**
     * Ajouter une carte dans la base distante et la collection
     * @param carte
     */
    public void setCarte(Carte carte) {
        Log.i("Controle", "setCarte");
        Controle.carte = carte;
    }

    /**
     * Ecouter les boutons "modifier" et "supprimer" des cartes présentées dans la ListView d HistolistAdapter
     * @param listener
     */
    public void setListener(CardEventListener listener) {
        this.listener = listener;
    }

    /**
     * Suppression d'une carte de la liste à partir du retour du serveur
     * @param id de la carte à supprimer
     */
    public void cardDeleted(int id) {
        Carte carte1 = null;
        for (Carte card : lesCartes) {
            if (card.getNumCarte() == id) {
                carte1 = card;
            }
        }

        lesCartes.remove(carte1);
        if (listener != null) {
            // notification du listener
            listener.onCardDeleted(id);
        }
    }

    /**
     * Modifier une carte de la liste à partir du retour du serveur
     * @param output JSONObject
     */
    public void cardModified(String output) {
        Carte carte = new Carte();
        carte.carteModifiee(lesCartes, output);
        if (listener != null) {
            // notification du listener
            listener.onCardModified(carte);
        }
    }

    /**
     * ajouter une carte à la liste à partir du retour du serveur
     * @param output
     */
    public void addCard(String output) {
        Log.d("addCard", output);
        JSONObject objet;
        try {
            objet = new JSONObject(output);
            lesCartes.add(convertJSonToCarte(objet));
        } catch (JSONException e) {
            Log.d("erreur enreg", "conversion JSON impossible" + e.toString() + "******************");
            e.printStackTrace();
        }
    }

    /**
     * récupérer les cartes à partir d'un fichier json
     * @param output
     */
    public void createList(String output) {
        try {
            JSONArray jsonInfo = new JSONArray(output);
            ArrayList<Carte> lesCartes = new ArrayList<>();
            for (int i = 0; i < jsonInfo.length(); i++) {
                JSONObject info = new JSONObject(jsonInfo.get(i).toString());
                Integer numCarte = info.getInt("id");
                String langue = info.getString("langue");
                String question = info.getString("question");
                String indice = info.getString("indice");
                String reponse = info.getString("reponse");
                Integer categorie = info.getInt("category");
                Integer level = info.getInt("level");
                Date dateCreation = dateFormatter.parse(info.getString("datecreation"));
                Carte carte = new Carte(numCarte, langue, question, indice, reponse, categorie, level, dateCreation);
                lesCartes.add(carte);
            }
            this.setLesCartes(lesCartes);
        } catch (JSONException | ParseException e) {
            Log.d("erreur tous", "conversion JSON impossible" + e.toString() + "******************");
            e.printStackTrace();
        }
    }
}
