package com.gauthier.remuemeninges.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity{
    /*
    1 tirer une carte au sort (sans remise donc exclure les num précédents) parmi la liste
    il faudrait sauvegarder les num de cartes déjà tirées dans une var
    2 afficher le contenu sauf l'indice et la réponse
    (s'il n'y a pas d'indice soit mettre le bouton en grisé,
    soit remplir automatiquement : "il n'y a pas d'indice")
    3 afficher l'indice ou la réponse qd on appui sur le bouton
    4 relancer un tirage au sort si on appui sur le bouton nouvelle carte
    5 retourner à l'accueil si on appui sur le bouton accueil
    6 si on appui sur un bouton "nouvelle carte" ou "accueil", il faudrait afficher
    une boite de dialogue pour définir le niveau de difficulté de la question (et en faire une moyenne)
     */
    RatingBar levelRB;
    TextView categoryTV;
    TextView questionTV;
    TextView reponseTV;
    TextView clueTV;
    Carte carte;
    Controle controle;

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        init();
    }

    public void init() {
            Log.i("CardActivity", "init()");
        carte = Controle.getInstance(this).getCarte();
        if (carte != null) {
            Log.i("CardActivity","test init()"+carte.getQuestion());
            questionTV = findViewById(R.id.cardTextViewQuestion);
            questionTV.setText(carte.getQuestion());
            reponseTV = findViewById(R.id.cardTextViewResponse);
            reponseTV.setText(carte.getReponse().toString());
            clueTV = findViewById(R.id.cardTextViewClue);
            clueTV.setText(carte.getIndice());
            categoryTV = findViewById(R.id.cardLblCategory);
            categoryTV.setText(carte.getCategorie().toString());
            levelRB = findViewById(R.id.cardRatingBar);
            levelRB.setRating((int)carte.getLevel());
        } else {
            //récupération de la liste des cartes
            ArrayList<Carte> listCartes = Controle.getInstance(this).getLesCartes();
            //tirage au sort du numéro de carte à afficher
            int indice = (int) (Math.random() * listCartes.size());
            //copie de la carte dans la list des cartes à l'indice "indice"
            carte = listCartes.get(indice);
            //pour savoir si j'ai bien une nouvelle carte de tirée car beaucoup de carte avec le même contenu
            Log.i("CardActibvity", "num de la carte : " + indice);

            //récupération et affichage des informations de la carte

            //le niveau de difficulté
            levelRB = findViewById(R.id.cardRatingBar);
            levelRB.setRating((int) carte.getLevel());

            //la catégorie
            categoryTV = findViewById(R.id.cardLblCategory);
            categoryTV.setText(carte.getCategorie().toString());

            //la question
            questionTV = findViewById(R.id.cardTextViewQuestion);
            questionTV.setText(carte.getQuestion());

            //l'indice
            clueTV = findViewById(R.id.cardTextViewClue);
            Log.d("CardActivity", "init clue = " + carte.getIndice());

            //la réponse
            reponseTV = findViewById(R.id.cardTextViewResponse);
            Log.d("CardActivity", "init reponse = " + carte.getReponse());
        }
    }

    /**
     * montrer le contenu du textview de l'indice
     * @param view
     */
    public void cardBtnShowClue(View view) {
        Log.d ("CardActivity", "indice = " + carte.getIndice() );
        clueTV.setText(carte.getIndice());
        clueTV.setVisibility(View.VISIBLE);
    }

    /**
     * montrer le contenu de la réponse
     * @param view
     */
    public void cardBtnShowAnswer(View view) {
        Log.d ("CardActivity", "reponse = " + carte.getReponse() );
        reponseTV.setText(carte.getReponse());
        reponseTV.setVisibility(View.VISIBLE);
    }

    /**
     * récupération de la carte si elle a été sérialisée
     * @return
     */
    public void recupCarte(Controle controle){
        //Log.i("recupCarte",controle.getTxtQuestion());
        if(controle.getTxtQuestion()!=null){
            questionTV.setText(controle.getTxtQuestion());
            reponseTV.setText(controle.getTxtReponse());
            clueTV.setText(controle.getTxtIndice());
            categoryTV.setText(controle.getCategorie());
            levelRB.setRating(controle.getLevel());

            //vider la carte lorsque l'on ressort de CardActivity
            //controle.setCarte(null);
        }
    }
    /**
     * retour à la page d'accueil
     * @param view
     */
    public void cardBtndHome(View view) {
        //on crée une nouvelle intent
        Intent intent=new Intent(this,MainActivity.class);
        //on lance l'intent
        startActivity(intent);
    }

    /**
     * afficher une nouvelle carte
     * @param view
     */
    public void cardBtnEditNewCard(View view) {
       //les contenus (indice et réponse) de la carte sont rednu invisibles
        clueTV.setVisibility(View.INVISIBLE);
       reponseTV.setVisibility(View.INVISIBLE);
       //on relance le tirage d'une carte
       init();
    }
}
