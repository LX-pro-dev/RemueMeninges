package com.gauthier.remuemeninges.Outils;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public class AccesHTTP extends AsyncTask<String, Integer, Long> {
    // propriétés
    public String ret=""; // information retournée par le serveur
    public AsyncResponse delegate=null; // gestion du retour asynchrone
    private String parametres = ""; // paramètres à envoyer en POST au serveur
    private String operation =  "";

    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Constructeur : ne fait rien
     */
    public AccesHTTP() {
        super();
    }

    /**
     * Construction de la chaîne de paramètres à envoyer en POST au serveur
     * @param nom
     * @param valeur
     */
    public void addParam(String nom, String valeur) {
        try {
            if (parametres.equals("")) {
                // premier paramètre
                parametres = URLEncoder.encode(nom, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
            }else{
                // paramètres suivants (séparés par &)
                parametres += "&" + URLEncoder.encode(nom, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode appelée par la méthode execute
     * permet d'envoyer au serveur une liste de paramètres en GET
     * @param options contient l'adresse du serveur dans la case 0 de urls
     * @return null
     */
    @Override
    protected Long doInBackground(String... options) {

        // pour éliminer certaines erreurs
        System.setProperty("http.keepAlive", "false");
        // objets pour gérer la connexion, la lecture et l'écriture
        PrintWriter writer = null;
        BufferedReader reader = null;
        HttpURLConnection connexion = null;

        try {
            // création de l'url à partir de l'adresse reçu en paramètre, dans urls[0]
            URL url = new URL(options[0] + "?" + parametres);
            // ouverture de la connexion
            connexion = (HttpURLConnection) url.openConnection();
            // choix de la méthode POST pour l'envoi des paramètres
            connexion.setRequestMethod(options[1]);

            if (options[1] == "POST") {
                connexion.setDoOutput(true);
                connexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connexion.setFixedLengthStreamingMode(parametres.getBytes().length);
                // création de la requete d'envoi sur la connexion, avec les paramètres
                writer = new PrintWriter(connexion.getOutputStream());
                writer.print(parametres);
                // Une fois l'envoi réalisé, vide le canal d'envoi
                writer.flush();
            }

            // Récupération du retour du serveur
            reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            ret = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // fermeture des canaux d'envoi et de réception
            try{
                writer.close();
            }catch(Exception e){}
            try{
                reader.close();
            }catch(Exception e){}
        }
        return null;
    }

    /**
     * Sur le retour du serveur, envoi l'information retournée à processFinish
     * @param result
     */
    @Override
    protected void onPostExecute(Long result) {
        // ret contient l'information récupérée
        delegate.processFinish(this.ret , this.operation);
    }


}
