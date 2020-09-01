package com.gauthier.remuemeninges.Modele;

import android.util.Log;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Outils.AccesHTTP;
import com.gauthier.remuemeninges.Outils.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public class AccesDistant implements AsyncResponse{
    //constante
    private static final String SERVERADDR = "http://192.168.1.3/coach/serveurcoach.php";//au lieu de /coach/serveurcoach.php"
    private Controle controle;
    /**
     * constructeur
     */
    public AccesDistant() {
        controle=Controle.getInstance(null);
    }

    /**
     * retour du serveur distant
     * @param output
     */
    @Override
    public void processFinish(String output) {
        Log.d("serveur", output + "************");
        //découper le message reçu avec un %
        String[] message = output.split("%");
        //dans message[0] : "enreg", "dernier", "erreur"
        //dans message[1] : le reste du message

        //s'il y a 2 cases
        if (message.length > 1) {
            if (message[0].equals("enreg")) {
                Log.d("enreg", message[1] + "******************");
            } else {
                if (message[0].equals("dernier")) {
                    Log.d("dernier", message[1] + "******************");
                    try {
                        JSONObject info= new JSONObject(message[1]);
                        Integer numCarte= info.getInt("numCarte");
                        Integer categorie= info.getInt("categorie");
                        String question= info.getString("question");
                        String reponse= info.getString("reponse");
                        String indice= info.getString("indice");
                        Carte carte= new Carte(numCarte,categorie,question,reponse,indice);
                        controle.setCarte(carte);
                    } catch (JSONException e) {
                        Log.d("erreur", "converssion JSON impossible"+ e.toString()+ "******************");
                    }
                } else {
                    if (message[0].equals("tous")) {
                        Log.d("tous", message[1] + "******************");
                        try {
                            JSONArray jsonInfo = new JSONArray(message[1]);
                            ArrayList<Carte> lesCartes = new ArrayList<>();
                            for(int i=0;i<jsonInfo.length();i++){
                                JSONObject info= new JSONObject(jsonInfo.get(i).toString());
                                Integer numCarte= info.getInt("numCarte");
                                Integer categorie= info.getInt("categorie");
                                String question= info.getString("question");
                                String reponse= info.getString("reponse");
                                String indice= info.getString("indice");
                                Carte carte= new Carte(numCarte,categorie,question,reponse,indice);
                                lesCartes.add(carte);
                            }
                            controle.setLesCartes(lesCartes);
                        } catch (JSONException e) {
                            Log.d("erreur", "converssion JSON impossible"+ e.toString()+ "******************");
                        }
                    }else{
                        if (message[0].equals("erreur")) {
                            Log.d("erreur", message[1] + "******************");
                        }
                    }
                }
            }
        }
    }

    public void envoi(String operation, JSONArray lesDonneesJSON) {
        AccesHTTP accesDonnees= new AccesHTTP();

        //lien de délégation
        accesDonnees.delegate = (AsyncResponse) this;

        //ajout paramètres
        accesDonnees.addParam("operation",operation);
        accesDonnees.addParam("lesdonnees",lesDonneesJSON.toString());

        accesDonnees.execute(SERVERADDR);
    }
}
