package com.gauthier.remuemeninges.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity {

    RatingBar levelRB;
    TextView categoryTV;
    TextView questionTV;
    TextView reponseTV;
    TextView clueTV;
    Carte carte;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        init();
    }

    public void init() {
        carte = Controle.getInstance(this).getCarte();
        if (carte != null) {
            // Récupération et affichage des informations de la carte
            questionTV = findViewById(R.id.cardTextViewQuestion);
            questionTV.setText(carte.getQuestion());

            reponseTV = findViewById(R.id.cardTextViewResponse);
            reponseTV.setText(carte.getReponse());

            clueTV = findViewById(R.id.cardTextViewClue);
            clueTV.setText(carte.getIndice());

            categoryTV = findViewById(R.id.cardLblCategory);
            String category = getCategory(carte.getCategorie());
            categoryTV.setText(category);

            levelRB = findViewById(R.id.cardRatingBar);
            levelRB.setRating((int) carte.getLevel());

            // Remettre à vide la carte
            Controle.getInstance(this).setCarte(null);

        } else {
            // Récupération de la liste des cartes
            ArrayList<Carte> listCartes = Controle.getInstance(this).getLesCartes();
            // Tirage au sort du numéro de carte à afficher
            int indice = (int) (Math.random() * listCartes.size());
            // Copie de la carte dans la list des cartes à l'indice "indice"
            carte = listCartes.get(indice);
            Log.i("CardActibvity", "num de la carte : " + indice);

            //// Récupération et affichage des informations de la carte :
            // Le niveau de difficulté
            levelRB = findViewById(R.id.cardRatingBar);
            levelRB.setRating((int) carte.getLevel());

            // la catégorie
            categoryTV = findViewById(R.id.cardLblCategory);
            String category = getCategory(carte.getCategorie());
            categoryTV.setText(category);

            // la question
            questionTV = findViewById(R.id.cardTextViewQuestion);
            questionTV.setText(carte.getQuestion());

            // l'indice
            clueTV = findViewById(R.id.cardTextViewClue);
            Log.d("CardActivity", "init clue = " + carte.getIndice());

            // la réponse
            reponseTV = findViewById(R.id.cardTextViewResponse);
            Log.d("CardActivity", "init reponse = " + carte.getReponse());
        }
    }

    /**
     * récupérer le string de la catégorie correspondante
     *
     * @param categorie
     * @return
     */
    private String getCategory(Integer categorie) {
        int cat = categorie;
        String s;
        switch (cat) {
            case 1:
                s = getString(R.string.categorie1);
                return s;
            case 2:
                s = getString(R.string.categorie2);
                return s;
            case 3:
                s = getString(R.string.categorie3);
                return s;
            case 4:
                s = getString(R.string.categorie4);
                return s;
            case 5:
                s = getString(R.string.categorie5);
                return s;
            case 6:
                s = getString(R.string.categorie6);
                return s;
        }
        return null;
    }

    /**
     * montrer le contenu du textview de l'indice
     * @param view
     */
    public void cardBtnShowClue(View view) {
        Log.d("CardActivity", "indice = " + carte.getIndice());
        clueTV.setText(carte.getIndice());
        clueTV.setVisibility(View.VISIBLE);
    }

    /**
     * montrer le contenu de la réponse
     * @param view
     */
    public void cardBtnShowAnswer(View view) {
        Log.d("CardActivity", "reponse = " + carte.getReponse());
        reponseTV.setText(carte.getReponse());
        reponseTV.setVisibility(View.VISIBLE);
    }

    /**
     * retour à la page d'accueil
     * @param view
     */
    public void cardBtndHome(View view) {
        // Création d'une nouvel intent
        Intent intent = new Intent(this, MainActivity.class);
        // Lancer l'intent
        startActivity(intent);
    }

    /**
     * afficher une nouvelle carte
     * @param view
     */
    public void cardBtnEditNewCard(View view) {
        // Les contenus (indice et réponse) de la carte sont rendus invisibles
        clueTV.setVisibility(View.INVISIBLE);
        reponseTV.setVisibility(View.INVISIBLE);
        // Relancer le tirage d'une carte
        init();
    }
}
