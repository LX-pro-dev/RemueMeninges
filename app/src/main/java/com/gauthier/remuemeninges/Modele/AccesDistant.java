package com.gauthier.remuemeninges.Modele;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Outils.AccesHTTP;
import com.gauthier.remuemeninges.Outils.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public class AccesDistant implements AsyncResponse {
    //constante
    private static final String SERVERADDR = "http://alexdev.remue-meninges.secondlab.net/serveur.php";
    private Controle controle;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
    int numCarte;
    private String langue;
    private String question;
    private String indice;
    private String reponse;
    private int categorie;
    private int level;

    /**
     * constructeur
     */
    public AccesDistant() {
        controle = Controle.getInstance(null);
    }

    /**
     * retour du serveur distant
     *
     * @param output
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void processFinish(String output, String operation) {
        Log.d("serveur", output + "************" + operation);

        switch (operation) {
            // "tous" pour récupérer toutes les cartes, à travailler : PUT, DELETE et POST
            case "tous":
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
                    controle.setLesCartes(lesCartes);
                } catch (JSONException | ParseException e) {
                    Log.d("erreur", "conversion JSON impossible" + e.toString() + "******************");
                    e.printStackTrace();
                }
                break;
        }

    }

    public void envoi(String operation, JSONObject lesDonneesJSON) {//JSONArray pour les envois de données de la carte à enregistrer
        AccesHTTP accesDonnees = new AccesHTTP();

        accesDonnees.setOperation(operation);
        //lien de délégation
        accesDonnees.delegate = (AsyncResponse) this;

        if (operation == "tous") {
            //ajout paramètres
            accesDonnees.addParam("operation", operation);
            accesDonnees.addParam("langue", "fr");//"fr" en dur car pas encore travaillé sur le choix de la langue
            Log.d("serveur tous", accesDonnees.toString());

            accesDonnees.execute(SERVERADDR, "GET");

        } else if (operation == "enreg") {
            //ajout paramètres
            accesDonnees.setBodyParams(lesDonneesJSON.toString());
            Log.d("serveur enreg", accesDonnees.toString());

            accesDonnees.execute(SERVERADDR, "POST");

        } else if (operation == "delete") {
            //ajout de paramètres
            Carte carte = convertJSonToCarte(lesDonneesJSON);
            accesDonnees.addParam("id", "" + carte.getNumCarte());
            accesDonnees.setBodyParams(lesDonneesJSON.toString());
            Log.d("serveur delete", accesDonnees.toString());

            accesDonnees.execute(SERVERADDR, "DELETE");

        } else if (operation == "modify") {
            //ajout de paramètres
            accesDonnees.setBodyParams(lesDonneesJSON.toString());
            Log.d("serveur modify", accesDonnees.toString());

            accesDonnees.execute(SERVERADDR, "PUT");
        }
    }

    private Carte convertJSonToCarte(JSONObject lesDonneesJSON) {
        try {
            int numCarte = lesDonneesJSON.getInt("id");
            String langue = lesDonneesJSON.getString("langue");
            String question = lesDonneesJSON.getString("question");
            String indice = lesDonneesJSON.getString("indice");
            String reponse = lesDonneesJSON.getString("reponse");
            int categorie = lesDonneesJSON.getInt("category");
            int level = lesDonneesJSON.getInt("level");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Carte carte = new Carte(numCarte, langue, question, indice, reponse, categorie, level, new Date());
        return carte;
    }
}
