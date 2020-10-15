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
                    Log.d("erreur tous", "conversion JSON impossible" + e.toString() + "******************");
                    e.printStackTrace();
                }
                break;

            case "delete":
                ArrayList<Carte> cards = controle.getLesCartes();
                for (Carte card : cards) {
                    if (card.getNumCarte() == Integer.parseInt(output)) {
                        carte1 = card;
                    }
                }
                cards.remove(carte1);
                Log.d("processFinish delete", "" + output);
                //suppression de la carte dans la liste de cartes
                break;

            case "enreg":
                JSONObject objet;
                try {
                    objet = new JSONObject(output);
                    cards = controle.getLesCartes();
                    Carte carte = convertJSonToCarte(objet);//faut-il un if(object != null)?
                    cards.add(carte);
                } catch (JSONException e) {
                    Log.d("erreur enreg", "conversion JSON impossible" + e.toString() + "******************");
                    e.printStackTrace();
                }
                break;

            case "modify":
                JSONObject object;
                try {
                    object = new JSONObject(output);
                    cards = controle.getLesCartes();
                    Carte carte = convertJSonToCarte(object);
                    for (Carte card : cards) {
                        if (card.getNumCarte().equals(carte.getNumCarte())) {
                            int index = cards.indexOf(card);
                            if (carte.getQuestion() != null) {
                                card.setQuestion(carte.getQuestion());
                            } else if (carte.getIndice() != null) {
                                card.setIndice(carte.getIndice());
                            } else if (carte.getReponse() != null) {
                                card.setReponse(carte.getReponse());
                            } else if (carte.getLevel() != null) {
                                card.setLevel(carte.getLevel());
                            } else if (carte.getCategorie() != null) {
                                card.setCategorie(carte.getCategorie());
                            }
                            controle.getLesCartes().set(index, card);
                        }
                    }
                } catch (JSONException e) {
                    Log.d("erreur enreg", "conversion JSON impossible" + e.toString() + "******************");
                    e.printStackTrace();
                }
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

    private Carte convertJSonToCarte(JSONObject lesDonneesJSON) {
        try {
            if (lesDonneesJSON.getInt("id") != 0) {
                numCarte = lesDonneesJSON.getInt("id");
            }
/////////////////////////////////////
            //pb ac toString() de lesDonneesJSON
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
}
