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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public class AccesDistant implements AsyncResponse{
    //constante
    private static final String SERVERADDR = "http://alexdev.remue-meninges.secondlab.net/serveur.php";
    private Controle controle;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void processFinish(String output, String operation) {
        Log.d("serveur", output + "************");
        //découper le message reçu avec un %
        //String[] message = output.split("%");
        //dans message[0] : "enreg", "dernier", "erreur"
        //dans message[1] : le reste du message

        switch (operation) {
            case "tous" :
                try {
                    JSONArray jsonInfo = new JSONArray(output);
                    ArrayList<Carte> lesCartes = new ArrayList<>();
                    for(int i=0;i<jsonInfo.length();i++){
                        JSONObject info= new JSONObject(jsonInfo.get(i).toString());
                        Integer numCarte= info.getInt("id");
                        String langue = info.getString("langue");
                        String question= info.getString("question");
                        String indice= info.getString("indice");
                        String reponse= info.getString("reponse");
                        Integer categorie= info.getInt("category");
                        Integer level = info.getInt("level");
                        //LocalDateTime dateCreation = LocalDateTime.parse(info.getString("datecreation"));
                        Date dateCreation = dateFormatter.parse(info.getString("datecreation"));
                        Carte carte= new Carte(numCarte,langue, question,indice, reponse, categorie,level,dateCreation);
                        lesCartes.add(carte);
                    }
                    controle.setLesCartes(lesCartes);
                } catch (JSONException | ParseException e) {
                    Log.d("erreur", "conversion JSON impossible"+ e.toString()+ "******************");
                    e.printStackTrace();
                }
                break;
        }

        /*
        //s'il y a 2 cases
        if (message.length > 1) {
            if (message[0].equals("enreg")) {
                Log.d("enreg", message[1] + "******************");
            } else {
                if (message[0].equals("dernier")) {
                    Log.d("dernier", message[1] + "******************");
                    try {
                        JSONObject info= new JSONObject(message[1]);
                        //id,langue,question,incide,reponse,category,level,dateCreation
                        Integer numCarte= info.getInt("numCarte");
                        String langue = info.getString("langue");
                        String question= info.getString("question");
                        String indice= info.getString("indice");
                        String reponse= info.getString("reponse");
                        Integer categorie= info.getInt("categorie");
                        Integer level = info.getInt("level");
                        LocalDateTime dateCreation = LocalDateTime.parse(info.getString("datecreation"));
                        Carte carte= new Carte(numCarte,langue, question,indice, reponse, categorie,level,dateCreation);
                        controle.setCarte(carte);
                    } catch (JSONException e) {
                        Log.d("erreur", "converssion JSON impossible"+ e.toString()+ "******************");
                    }
                } else {
                    if (message[0].equals("tous")) {
                        Log.d("tous", message[1] + "******************");

                    }else{
                        if (message[0].equals("erreur")) {
                            Log.d("erreur", message[1] + "******************");
                        }
                    }
                }
            }
        }*/
    }

    public void envoi(String operation, JSONArray lesDonneesJSON) {
        AccesHTTP accesDonnees= new AccesHTTP();

        accesDonnees.setOperation(operation);
        //lien de délégation
        accesDonnees.delegate = (AsyncResponse) this;

        //ajout paramètres
        accesDonnees.addParam("operation",operation);
        accesDonnees.addParam("langue","fr");
       // accesDonnees.addParam("lesdonnees",lesDonneesJSON.toString());

        accesDonnees.execute(SERVERADDR,"GET");
    }
}
