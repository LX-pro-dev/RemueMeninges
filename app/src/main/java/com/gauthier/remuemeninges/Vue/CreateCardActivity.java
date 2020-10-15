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

public class CreateCardActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        init();
    }
/*
on devra proposer d'écrire la carte aussi en anglais par défaut pour que les autres créateurs
de carte dans d'autres langues qui ne connaissent pas la langue dans laquelle on a écrit notre
carte puissent la traduire dans la leur
*/

    //propriétés pour manipuler les objets graphiques du xml
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


    private void init() {
        ratingLevel = (RatingBar) findViewById(R.id.creaRatingBarLevel);
        txtQuestion = (EditText) findViewById(R.id.creaTxtQuestion);
        txtReponse = (EditText) findViewById(R.id.creaTxtReponse);
        txtIndice = (EditText) findViewById(R.id.creaTxtIndice);
        rbCat1 = (RadioButton) findViewById(R.id.creaRbCat1);
        rbCat2 = (RadioButton) findViewById(R.id.creaRbCat2);
        rbCat3 = (RadioButton) findViewById(R.id.creaRbCat3);
        rbCat4 = (RadioButton) findViewById(R.id.creaRbCat4);
        rbCat5 = (RadioButton) findViewById(R.id.creaRbCat5);
        rbCat6 = (RadioButton) findViewById(R.id.creaRbCat6);

        carte = Controle.getInstance(this).getCarte();//je veux récupérer la carte envoyée depuis histo mais elle est conservée et je ne peux plus passer dans creation de carte, il me fait passer directement la carte que je voulais modifier

        if (carte != null) {
            isModifyCard = true;
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


    private void ecouteEnregistrer() {
        ((Button) findViewById(R.id.creaBtnEnregistrer)).setOnClickListener(new Button.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)


            public void onClick(View v) {
                Log.d("createcard enregistrer", "onclick");
                String question = null;
                String reponse = null;
                String indice = null;
                Integer categorie = 6;
                Integer level = 1;

                //récupération des données saisies
                try {// pour éviter les pb de saisie : char au lieu d'un int
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
                } catch (Exception e) {
                }//l'exception ne fera rien
                // comme on a initailisé les variables à 0 on va tester si elles sont restées à 0!

                //controle des données saisies
                if (question == null && reponse == null) {
                    Toast.makeText(CreateCardActivity.this, "saisie incorrecte", Toast.LENGTH_LONG).show();
                } else {
                    if (isModifyCard) {
                        controle = Controle.getInstance(CreateCardActivity.this);
                        int index = controle.getLesCartes().indexOf(carte);
                        Carte card = new Carte(carte.getNumCarte(), "fr", question, indice, reponse, categorie, level, carte.getDatecreation());
                        controle.getLesCartes().set(index, card);//NPE!!!
                        controle.getInstance(CreateCardActivity.this).modifyCarte(card);
                        Toast toast = Toast.makeText(CreateCardActivity.this, "La carte a été modifiée", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        isModifyCard = false;
                    } else {
                        afficheResult(question, indice, reponse, level, categorie);
                        Toast toast = Toast.makeText(CreateCardActivity.this, "La carte a été créée", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                }
                clearCard();
            }
        });
    }

    /**
     * afficher la carte avec ses infos
     *
     * @param question
     * @param indice
     * @param reponse
     * @param level
     * @param categorie
     */
    private void afficheResult(String question, String indice, String reponse, int level, int categorie) {
        //création de la carte et récupération des infos
        this.controle.creerCarte("fr", question, indice, reponse, categorie, level);

    }

    /**
     * remettre la carte à 0
     */
    private void clearCard() {
        txtQuestion.getText().clear();
        txtReponse.getText().clear();
        txtIndice.getText().clear();
        rbCat1.setChecked(true);
        ratingLevel.setRating(1);
    }

    public void creaBtnHome(View view) {
        Intent intent = new Intent(CreateCardActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

