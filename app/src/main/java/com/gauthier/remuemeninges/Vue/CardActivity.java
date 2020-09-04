package com.gauthier.remuemeninges.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;

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

    TextView reponseTV;
    Carte carte;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        init();
    }

    public void init() {
       carte = Controle.getInstance(this).getLesCartes().get(0);

        TextView questionTV = findViewById(R.id.cardTextViewQuestion);
        questionTV.setText(carte.getQuestion());

        //Button reponseBtn = findViewById(R.id.cardBtnAnswer);
        reponseTV = findViewById(R.id.cardTextViewResponse);
        Log.d ("CardActivity", "init reponse = " + carte.getReponse() );

        /*reponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reponseTV.setText(carte.getReponse());
            }
        });*/

    }

    public void cardBtnShowClue(View view) {
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
       //relancer le init();
        //pr faire un getCarte ac un nouveau num si j'ai fait un random
       // dans init il faudra remettre les indice et reponse ne invisible

    }
}
