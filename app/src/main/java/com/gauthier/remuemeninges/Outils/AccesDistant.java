package com.gauthier.remuemeninges.Outils;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.gauthier.remuemeninges.Controle.Controle;
import org.json.JSONObject;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public class AccesDistant implements AsyncResponse {
    // adresse du serveur distant
    private static final String SERVERADDR = "http://alexdev.remue-meninges.secondlab.net/serveur.php";
    private Controle controle;

    //////////////
    //Constructeur
    /////////////
    public AccesDistant() {
        controle = Controle.getInstance(null);
    }

    /**
     * Retour du serveur distant
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

    /**
     * Envoyer les données sous format JSON en fonction de l'opération effectuée
     * @param operation : tous (pour recevoir toutes les cartes), enreg, delete, modify
     * @param lesDonneesJSON
     */
    public void envoi(String operation, JSONObject lesDonneesJSON) {//JSONArray pour les envois de données de la carte à enregistrer
        if (lesDonneesJSON != null) {
            Log.d("envoi", operation + " " + lesDonneesJSON.toString());
        } else {
            Log.d("envoi", operation + " null");
        }
        AccesHTTP accesDonnees = new AccesHTTP();

        accesDonnees.setOperation(operation);
        // Lien de délégation
        accesDonnees.delegate = (AsyncResponse) this;

        switch (operation) {
            case "tous":
                // Ajout paramètres
                accesDonnees.addParam("operation", operation);
                accesDonnees.addParam("langue", "fr");//"fr" en dur car pas encore travaillé sur le choix de la langue

                Log.d("serveur tous", accesDonnees.toString());

                accesDonnees.execute(SERVERADDR, "GET");

                break;
            case "enreg":
                // Ajout paramètres
                if (lesDonneesJSON != null) {
                    accesDonnees.setBodyParams(lesDonneesJSON.toString());
                    Log.d("serveur enreg", accesDonnees.toString());
                }

                accesDonnees.execute(SERVERADDR, "POST");

                break;
            case "delete":
                // Ajout de paramètres
                if (lesDonneesJSON != null) {
                    accesDonnees.setBodyParams(lesDonneesJSON.toString());
                    Log.d("serveur delete", lesDonneesJSON.toString());
                }

                accesDonnees.execute(SERVERADDR, "DELETE");

                break;
            case "modify":
                // Ajout de paramètres
                if (lesDonneesJSON != null) {
                    accesDonnees.setBodyParams(lesDonneesJSON.toString());
                    Log.d("serveur modify", accesDonnees.toString());
                }

                accesDonnees.execute(SERVERADDR, "PUT");
                break;
        }
    }
}
