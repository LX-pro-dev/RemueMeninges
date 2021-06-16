package com.gauthier.remuemeninges.Outils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
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
    int numCarte;
    private Carte carte;
    private Carte carte1;


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
     * @param operation
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void processFinish(String output, String operation) {
        Log.d("serveur", output + "************" + operation);

        switch (operation) {

            // "tous" pour récupérer toutes les cartes, à travailler : PUT, DELETE et POST
            case "tous":
                controle.createList(output);

                break;

            case "delete":
                controle.cardDeleted(Integer.parseInt(output));
                Log.d("processFinish delete", "" + output);
                break;

            case "enreg":
                controle.addCard(output);
                Log.d("processFinish enreg", "" + output);

                break;

            case "modify":
                controle.cardModified(output);
                Log.d("processFinish modify", "" + output);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + operation);
        }
    }

    public void envoi(String operation, JSONObject lesDonneesJSON) {//JSONArray pour les envois de données de la carte à enregistrer
        if (lesDonneesJSON != null) {
            Log.d("envoi", operation + " " + lesDonneesJSON.toString());
        } else {
            Log.d("envoi", operation + " null");
        }
        AccesHTTP accesDonnees = new AccesHTTP();

        accesDonnees.setOperation(operation);
        //lien de délégation
        accesDonnees.delegate = (AsyncResponse) this;

        switch (operation) {
            case "tous":
                //ajout paramètres
                accesDonnees.addParam("operation", operation);
                accesDonnees.addParam("langue", "fr");//"fr" en dur car pas encore travaillé sur le choix de la langue

                Log.d("serveur tous", accesDonnees.toString());

                accesDonnees.execute(SERVERADDR, "GET");

                break;
            case "enreg":
                //ajout paramètres
                if (lesDonneesJSON != null) {
                    accesDonnees.setBodyParams(lesDonneesJSON.toString());
                    Log.d("serveur enreg", accesDonnees.toString());
                }

                accesDonnees.execute(SERVERADDR, "POST");

                break;
            case "delete":
                //ajout de paramètres
                if (lesDonneesJSON != null) {
                    accesDonnees.setBodyParams(lesDonneesJSON.toString());
                    Log.d("serveur delete", lesDonneesJSON.toString());
                }

                accesDonnees.execute(SERVERADDR, "DELETE");

                break;
            case "modify":
                //ajout de paramètres
                if (lesDonneesJSON != null) {
                    accesDonnees.setBodyParams(lesDonneesJSON.toString());
                    Log.d("serveur modify", accesDonnees.toString());
                }

                accesDonnees.execute(SERVERADDR, "PUT");
                break;
        }
    }
}
