package com.gauthier.remuemeninges.Vue;

import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        init();
    }

    public void init() {
       ArrayList<Carte> listCartes = Controle.getInstance(this).getLesCartes();
       int indice = (int)(Math.random()*listCartes.size());
       carte = listCartes.get(indice);// faire un Math.random sur la taille du tableau de carte pour la non remise à chercher
       Log.i("CardActibvity","num de la carte : "+indice);
        int level = carte.getLevel();
        int categorie = carte.getCategorie();


        levelRB =findViewById(R.id.cardRatingBar);
        levelRB.setRating(level);

        categoryTV = findViewById(R.id.cardLblCategory);
        categoryTV.setText("cat"+categorie);


        questionTV = findViewById(R.id.cardTextViewQuestion);
        questionTV.setText(carte.getQuestion());

        clueTV = findViewById(R.id.cardTextViewClue);
        Log.d ("CardActivity", "init clue = " + carte.getIndice() );

        //Button reponseBtn = findViewById(R.id.cardBtnAnswer);
        reponseTV = findViewById(R.id.cardTextViewResponse);
        Log.d ("CardActivity", "init reponse = " + carte.getReponse() );
    }

    public void cardBtnShowClue(View view) {
        Log.d ("CardActivity", "indice = " + carte.getIndice() );
        clueTV.setText(carte.getIndice());
        clueTV.setVisibility(View.VISIBLE);
    }

    public void cardBtnShowAnswer(View view) {
        Log.d ("CardActivity", "reponse = " + carte.getReponse() );
        reponseTV.setText(carte.getReponse());
        reponseTV.setVisibility(View.VISIBLE);
    }

    public void cardBtndHome(View view) {
        //on creer une nouvelle intent on definit la class de depart ici this et la class d'arrivé ici SecondActivite
        Intent intent=new Intent(this,MainActivity.class);
        //on lance l'intent, cela a pour effet de stoper l'activité courante et lancer une autre activite ici SecondActivite
        startActivity(intent);
    }

    public void cardBtnEditNewCard(View view) {
       // clueTV.setVisibility(View.INVISIBLE);
       // reponseTV.setVisibility(View.INVISIBLE);
        //on creer une nouvelle intent on definit la class de depart ici this et la class d'arrivé ici SecondActivite
        Intent intent=new Intent(this,CardActivity.class);
        //on lance l'intent, cela a pour effet de stoper l'activité courante et lancer une autre activite ici SecondActivite
        startActivity(intent);
        //pr faire un getCarte ac un nouveau num si j'ai fait un random
       // dans init il faudra remettre les indices et reponse ne invisible

    }
}
