package com.gauthier.remuemeninges.Controle;

import android.content.Context;
import android.util.Log;

import com.gauthier.remuemeninges.Modele.AccesDistant;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.Outils.Serializer;
import com.gauthier.remuemeninges.Vue.CreateCardActivity;

import org.json.JSONArray;

import java.time.LocalDateTime;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public final class Controle{
    //singleton pattern
    private static Controle instance=null;//accessible par la classe
    private static Carte carte;
    private static String nomFichier="savecarte";
    //private static AccesLocal accesLocal;
    private static AccesDistant accesDistant;
    private static Context contexte;
    private ArrayList<Carte> lesCartes=new ArrayList<Carte>();
    /**
     * contructeur privé
     */
    private Controle(){
        super();}//ne déclare rien, on ne pourra faire new car déclaré en private


    /**
     * création de l'instance
     * @return instance
     */
    public static final Controle getInstance(Context contexte){//au lieu de faire new pour créer le contrôleur
        if(contexte!=null){
            Controle.contexte=contexte;
        }
        if(Controle.instance==null){//si l'instance n'est pas déjà créée, on la créée
            Controle.instance= new Controle();
            // accesLocal=new AccesLocal(contexte);
            accesDistant= new AccesDistant();
            //accesDistant.envoi("dernier",new JSONArray());//demande de récupérer le dernier profil
            accesDistant.envoi("tous",new JSONArray());//demande de récupérer tous les profils
            //profil=accesLocal.recupDernier();//pour récupérer la dernière ligne de la table en SQLite
            //  recupSerialize(contexte);
        }
        return Controle.instance;
    }
    //faire getLescartes().get(0) pour récupérer la première
    public ArrayList<Carte> getLesCartes() {
        return lesCartes;
    }

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
    public void creerCard(String langue, String question, String indice, String reponse, Integer categorie, Integer level){// création du profil
        // Appelle de cette méthode dans Main pour obtenir les infos de profil
        Carte uneCarte=new Carte(langue,question,indice,reponse,categorie,level);
        lesCartes.add(uneCarte);
        Log.d("date",new Date()+"*************");
        //accesLocal.ajout(carte);
        accesDistant.envoi("enreg",uneCarte.convertToJSONArray());
        // Serializer.serialize(nomFichier,profil,contexte);
    }

    /**
     * pour supprimer un profil dans la bas distante et la collection
     * @param carte
     */
    public void delCarte(Carte carte){
        accesDistant.envoi("del",carte.convertToJSONArray());
        lesCartes.remove(carte);
    }

    public void setCarte(Carte carte){
        Controle.carte=carte;
        ((CreateCardActivity)contexte).recupCarte();
    }

    /**
     * récupération de l'objet sérialisé : la carte
     * @param contexte
     *//*
    private static void recupSerialize(Context contexte){
        carte = (Carte) Serializer.deSerialize(nomFichier,contexte);
    }*/


    /**
     * récupérer le numéro de l'objet sérialisé
     * @return
     */
    public Integer getNumCarte(){
        if(carte==null){
            return null;
        }
        return carte.getNumCarte();
    }

    /**
     * récupérer la langue de l'objet sérialisé
     * @return
     */
    public String getTxtLangue(){
        if(carte==null){
            return null;
        }
        return carte.getLangue();
    }

    /**
     * récupérer la question de l'objet sérialisé
     * @return
     */
    public String getTxtQuestion(){
        if(carte==null){
            return null;
        }
        return carte.getQuestion();
    }

    /**
     * récupérer la réponse de l'objet sérialisé
     * @return
     */
    public String getTxtReponse(){
        if(carte==null){
            return null;
        }
        return carte.getReponse();
    }

    /**
     * récupérer l'indice de l'objet sérialisé
     * @return
     */
    public String getTxtIndice(){
        if(carte==null){
            return null;
        }
        return carte.getIndice();
    }

    /**
     * récupérer la categorie l'objet sérialisé
     * @return
     */
    public Integer getCategorie(){
        if(carte==null){
            return null;
        }
        return carte.getCategorie();
    }

    /**
     * récupérer le niveau de difficulté de l'objet sérialisé
     * @return
     */
    public Integer getLevel(){
        if(carte==null){
            return null;
        }
        return carte.getLevel();
    }

    /**
     * récupérer la date de création de l'objet sérialisé
     * @return
     */
    public Date getDate(){
        if(carte==null){
            return null;
        }
        return carte.getDatecreation();
    }

}
