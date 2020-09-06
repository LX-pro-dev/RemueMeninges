package com.gauthier.remuemeninges.Modele;

import com.gauthier.remuemeninges.Controle.Controle;
import org.json.JSONArray;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    private String langue;
    private Integer level;
    private Date datecreation;
    private Controle controle;




    public void resultNumCarte() {
        if (numCarte == null) {
            numCarte = 1;
        } else if (numCarte != null) {
            numCarte = controle.getLesCartes().size();
        }
    }

    //constructeur
    public Carte(String langue, String question, String indice, String reponse, Integer categorie, Integer level) {
        this.categorie = categorie;
        this.question = question;
        this.reponse = reponse;
        this.indice = indice;
        this.langue = langue;
        this.level = level;
    }
    public Carte(Integer numCarte, String langue, String question, String indice, String reponse, Integer categorie, Integer level, Date datecreation) {
        this.numCarte = numCarte;
        this.categorie = categorie;
        this.question = question;
        this.reponse = reponse;
        this.indice = indice;
        this.langue = langue;
        this.level = level;
        this.datecreation = datecreation;
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

    public String getLangue() { return langue; }

    public Integer getLevel() { return level;  }

    public Date getDatecreation() { return datecreation;  }

    /**
     * conversion de profil au format JSONArray
     * @return
     */
    public JSONArray convertToJSONArray(){
        List<Serializable> laListe= new ArrayList<>();
       // laListe.add(numCarte);
        laListe.add(langue);
        laListe.add(question);
        laListe.add(indice);
        laListe.add(reponse);
        laListe.add(categorie);
        laListe.add(level);
       // laListe.add(datecreation);
        return new JSONArray(laListe);
    }

}
