package com.gauthier.remuemeninges.Controle;

import android.content.Context;
import android.util.Log;

import com.gauthier.remuemeninges.Modele.AccesDistant;
import com.gauthier.remuemeninges.Modele.Carte;

import java.util.ArrayList;
import java.util.Date;

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

    /**
     * contructeur privé
     */
    private Controle() {
        super();
    }//ne déclare rien, on ne pourra faire new car déclaré en private


    /**
     * création de l'instance
     *
     * @param contexte
     * @return
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
     * récupérer la liste des cartes
     *
     * @return
     */
    public ArrayList<Carte> getLesCartes() {
        return lesCartes;
    }    //faire getLescartes().get(0) pour récupérer la première

    /**
     * ajouter la liste des cartes
     *
     * @param lesCartes
     */
    public void setLesCartes(ArrayList<Carte> lesCartes) {

        this.lesCartes = lesCartes;
    }

    /**
     * Création de carte
     *
     * @param langue
     * @param question
     * @param indice
     * @param reponse
     * @param categorie
     * @param level
     */
    public void creerCarte(String langue, String question, String indice, String reponse, Integer categorie, Integer level) {// création du profil
        // Appelle de cette méthode dans Main pour obtenir les infos de profil
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
     * supprimer une carte dans la base distante et la collection
     *
     * @param carte
     */
    public void delCarte(Carte carte) {
        Log.d("delCarte()", "numCarte =" + carte.getNumCarte());
        //requête serveur
        accesDistant.envoi("delete", carte.convertToJSONObject());
        Log.d("delCarte()", "json =" + carte.convertToJSONObject());

    }

    /**
     * ajouter une carte dans la base distante et la collection
     *
     * @param carte
     */
    public void setCarte(Carte carte) {
        Log.i("Controle", "setCarte");
        Controle.carte = carte;
    }

    /**
     * modifier une carte dans la base distante
     *
     * @param carte
     */
    public void modifyCarte(Carte carte) {
        //requête serveur
        accesDistant.envoi("modify", carte.convertToJSONObject());
        //modification de la carte dans la liste de cartes
    }

    public Carte getCarte() {
        if (carte == null) {
            return null;
        }
        return carte;
    }


}
