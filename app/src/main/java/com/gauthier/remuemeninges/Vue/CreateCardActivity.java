package com.gauthier.remuemeninges.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;

import static com.gauthier.remuemeninges.Vue.HistoListAdapter.toModify;

public class CreateCardActivity extends AppCompatActivity {

    // Propriétés pour manipuler les objets graphiques du xml
    private EditText txtQuestion;
    private EditText txtReponse;
    private EditText txtIndice;
    private RadioButton rbCat1;
    private RadioButton rbCat2;
    private RadioButton rbCat3;
    private RadioButton rbCat4;
    private RadioButton rbCat5;
    private RadioButton rbCat6;
    private RatingBar ratingLevel;
    private Controle controle;
    private Carte carte;
    private boolean isModifyCard = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        init();
    }

    private void init() {
        ratingLevel = findViewById(R.id.creaRatingBarLevel);
        txtQuestion = findViewById(R.id.creaTxtQuestion);
        txtReponse = findViewById(R.id.creaTxtReponse);
        txtIndice = findViewById(R.id.creaTxtIndice);
        rbCat1 = findViewById(R.id.creaRbCat1);
        rbCat2 = findViewById(R.id.creaRbCat2);
        rbCat3 = findViewById(R.id.creaRbCat3);
        rbCat4 = findViewById(R.id.creaRbCat4);
        rbCat5 = findViewById(R.id.creaRbCat5);
        rbCat6 = findViewById(R.id.creaRbCat6);

        carte = Controle.getInstance(this).getCarte();

        if (carte != null) {
            txtQuestion.setText(carte.getQuestion());
            txtReponse.setText(carte.getReponse());
            txtIndice.setText(carte.getIndice());
            ratingLevel.setRating(carte.getLevel());

            switch (carte.getCategorie()) {
                case 1:
                    rbCat1.setChecked(true);
                    break;

                case 2:
                    rbCat2.setChecked(true);
                    break;

                case 3:
                    rbCat3.setChecked(true);
                    break;

                case 4:
                    rbCat4.setChecked(true);
                    break;

                case 5:
                    rbCat5.setChecked(true);
                    break;

                case 6:
                    rbCat6.setChecked(true);
                    break;
            }
            ecouteEnregistrer();

        } else {
            this.controle = Controle.getInstance(this);//création de l'instance controle (singleton)
            ecouteEnregistrer();
        }
    }

    /**
     * Pour écouter les événements lien au bouton enregistré
     */
    private void ecouteEnregistrer() {
        findViewById(R.id.creaBtnEnregistrer).setOnClickListener(new Button.OnClickListener() {
            //pour gérer un événement sur un objet graphique
            // 1) rechercher l'objet graphique ac R.id
            // 2) appliquer setOnClickListener() qui redéfinie la méthode onClick(View v)


            public void onClick(View v) {
                Log.d("createcard enregistrer", "onclick");
                String question = null;
                String reponse = null;
                String indice = null;
                Integer categorie = 6;
                Integer level = 1;

                // Récupération des données saisies
                try {
                    recoverData(question, reponse, indice, level, categorie);
                } catch (Exception e) {
                }

                // Contrôle des données saisies
                checkData(question, reponse, indice, categorie, level);

                toModify = false;
                clearCard();
                Log.d("createCard", isModifyCard + "");
            }
        });
    }

    /**
     * Afficher la carte avec ses infos
     * @param question
     * @param indice
     * @param reponse
     * @param level
     * @param categorie
     */
    private void afficheResult(String question, String indice, String reponse, int level, int categorie) {
        //Création de la carte et récupération des infos
        this.controle.creerCarte("fr", question, indice, reponse, categorie, level);
    }

    /**
     * Remettre la carte à 0
     */
    private void clearCard() {
        txtQuestion.getText().clear();
        txtReponse.getText().clear();
        txtIndice.getText().clear();
        rbCat1.setChecked(true);
        ratingLevel.setRating(1);
        Controle.getInstance(this).setCarte(null);
    }

    /**
     * Retour à l'accueil
     * @param view
     */
    public void creaBtnHome(View view) {
        Intent intent = new Intent(CreateCardActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Modifier la carte et en informer l'utilisateur
     * @param question
     * @param indice
     * @param reponse
     * @param categorie
     * @param level
     */
    public void modify(String question, String indice, String reponse, Integer categorie, Integer level){
        controle = Controle.getInstance(CreateCardActivity.this);
        int index = controle.getLesCartes().indexOf(carte);
        Carte card = new Carte(carte.getNumCarte(), "fr", question, indice, reponse, categorie, level, carte.getDatecreation());
        controle.getLesCartes().set(index, card);
        Controle.getInstance(CreateCardActivity.this).modifyCarte(card);

        Toast toast = Toast.makeText(CreateCardActivity.this, "La carte a été modifiée", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    /**
     * Afficher la carte et en informer l'utilisateur
     * @param question
     * @param indice
     * @param reponse
     * @param categorie
     * @param level
     */
    public void affiche (String question, String indice, String reponse, Integer categorie, Integer level){
        afficheResult(question, indice, reponse, level, categorie);
        Toast toast = Toast.makeText(CreateCardActivity.this, "La carte a été créée", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    /**
     * Récupération des données saisies
     * @param question
     * @param reponse
     * @param indice
     * @param level
     * @param categorie
     */
    public void recoverData(String question, String reponse, String indice, int level, int categorie) {
        // Eviter les probèmes de saisie : char au lieu d'un int
        question = txtQuestion.getText().toString();
        reponse = txtReponse.getText().toString();
        indice = txtIndice.getText().toString();
        level = (int) ratingLevel.getRating();
        if (rbCat1.isChecked()) {//test pour un bouton radio
            categorie = 1;
        } else if (rbCat2.isChecked()) {
            categorie = 2;
        } else if (rbCat3.isChecked()) {
            categorie = 3;
        } else if (rbCat4.isChecked()) {
            categorie = 4;
        } else if (rbCat5.isChecked()) {
            categorie = 5;
        }
    }

    /**
     * Vérification des données saisies
     * @param question
     * @param reponse
     * @param indice
     * @param categorie
     * @param level
     */
    public void checkData(String question, String reponse, String indice, Integer categorie, Integer level) {
        if (question == null && reponse == null) {
            Toast.makeText(CreateCardActivity.this, "saisie incorrecte", Toast.LENGTH_LONG).show();
        } else {
            if (toModify) {
                modify(question, indice, reponse, categorie, level);
            } else {
                affiche(question, indice, reponse, level, categorie);
            }
        }
    }
}

