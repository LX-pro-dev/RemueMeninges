package com.gauthier.remuemeninges.Modele;

import com.gauthier.remuemeninges.Controle.Controle;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public class Carte {
    //propriétés
    private Integer numCarte;
    private Integer categorie;
    private String question;
    private String reponse;
    private String indice;
    private Controle controle;

    public void resultNumCarte() {
        if (numCarte == null) {
            numCarte = 1;
        } else if (numCarte != null) {
            numCarte = controle.getLesCartes().size();
        }
    }

    //constructeur
    public Carte(Integer numCarte, Integer categorie, String question, String reponse, String indice) {
        this.numCarte = numCarte;
        this.categorie = categorie;
        this.question = question;
        this.reponse = reponse;
        this.indice = indice;
    }

    //getters
    public Integer getNumCarte() {
        return numCarte;
    }

    public Integer getCategorie() {
        return categorie;
    }

    public String getQuestion() {
        return question;
    }

    public String getReponse() {
        return reponse;
    }

    public String getIndice() {
        return indice;
    }
    /**
     * conversion de profil au format JSONArray
     * @return
     */
    public JSONArray convertToJSONArray(){
        List<Serializable> laListe= new ArrayList<>();
        laListe.add(numCarte);
        laListe.add(categorie);
        laListe.add(question);
        laListe.add(reponse);
        laListe.add(indice);
        return new JSONArray(laListe);
    }

}
