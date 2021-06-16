package com.gauthier.remuemeninges.Modele;

import android.util.Log;

import com.gauthier.remuemeninges.Controle.Controle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public class Carte implements Comparable {
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

    /**
     * attribuer un numéro de carte
     */
    public void resultNumCarte() {
        if (numCarte == null) {
            numCarte = 1;
        } else if (numCarte != null) {
            numCarte = controle.getLesCartes().size();
        }
    }

    //constructeurs
    public Carte() {
        this.categorie = 1;
        this.question = "coucou";
        this.indice = "les";
        this.reponse = "cocos";
        this.langue = "fr";
        this.level = 1;
    }

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


    /**
     * convertir une carte au format JSONArray
     * @return
     */
    public JSONObject convertToJSONObject() {

        JSONObject carte = new JSONObject();
        try {
            if (getNumCarte() != 0) {
                String numCarte = getNumCarte().toString();
                carte.put("id", numCarte);
                carte.put("langue", "fr");
            }
            carte.put("question", getQuestion());
            carte.put("indice", getIndice());
            carte.put("reponse", getReponse());
            carte.put("category", getCategorie());
            carte.put("level", getLevel());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return carte;
    }

    /**
     * transformer un JSONObject en objet de type Carte
     * @param lesDonneesJSON
     * @return
     */
    public static Carte convertJSonToCarte(JSONObject lesDonneesJSON) {
        Carte carte = null;
        int numCarte = 0;
        try {
            if (lesDonneesJSON.getInt("id") != 0) {
                numCarte = lesDonneesJSON.getInt("id");
            }

            String langue;
            if (lesDonneesJSON.getString("langue") == null) {
                langue = "coucou";
            } else {
                langue = lesDonneesJSON.getString("langue");
            }

            String question;
            if (lesDonneesJSON.getString("question") == null) {
                question = "coucou";
            } else {
                question = lesDonneesJSON.getString("question");
            }

            String indice;
            if (lesDonneesJSON.getString("indice") == null) {
                indice = "coucou";
            } else {
                indice = lesDonneesJSON.getString("indice");
            }

            String reponse;
            if (lesDonneesJSON.getString("reponse") == null) {
                reponse = "coucou";
            } else {
                reponse = lesDonneesJSON.getString("reponse");
            }

            int categorie;
            if (lesDonneesJSON.getInt("category") != 0) {
                categorie = lesDonneesJSON.getInt("category");
            } else {
                categorie = 1;
            }

            int level;
            if (lesDonneesJSON.getInt("level") != 0) {
                level = lesDonneesJSON.getInt("level");
            } else {
                level = 1;
            }

            carte = new Carte(numCarte, langue, question, indice, reponse, categorie, level, new Date());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return carte;
    }

    public void carteModifiee(ArrayList<Carte> lesCartes, String output) {
        JSONObject object;
        try {
            object = new JSONObject(output);
            Carte carte = convertJSonToCarte(object);
            for (Carte card : lesCartes) {
                if (card.getNumCarte().equals(carte.getNumCarte())) {
                    int index = lesCartes.indexOf(card);
                    lesCartes.set(index, carte);
                }
            }
        } catch (JSONException e) {
            Log.d("erreur enreg", "conversion JSON impossible" + e.toString() + "******************");
            e.printStackTrace();
        }
    }

    /**
     * Parcourir la liste de carte et rechercher dans les textes de question
     * s'il y a le mot clé correspondant et recréer une liste de cartes
     *
     * @param lesCartes
     * @param keyword
     * @return
     */
    public static ArrayList<Carte> search(ArrayList<Carte> lesCartes, String keyword) {
        ArrayList<Carte> listMotCle = new ArrayList<>();
        for (Carte carte : lesCartes) {
            if (carte.question.contains(keyword)) {
                listMotCle.add(carte);
            }
        }
        return listMotCle;
    }


    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public void setCategorie(Integer categorie) {
        this.categorie = categorie;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    //getters et setters
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

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public void setNumCarte(Integer numCarte) {
        this.numCarte = numCarte;
    }

    public String getLangue() {
        return langue;
    }

    public Integer getLevel() {
        return level;
    }

    public Date getDatecreation() {
        return datecreation;
    }


}
