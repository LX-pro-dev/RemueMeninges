package com.gauthier.remuemeninges.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.R;
public class CreateCardActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        init();
    }

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
    private Controle controle;

    private void init(){
        txtQuestion=(EditText)findViewById(R.id.creaTxtQuestion);
        txtReponse=(EditText)findViewById(R.id.creaTxtReponse);
        txtIndice=(EditText)findViewById(R.id.creaTxtIndice);
        rbCat1=(RadioButton)findViewById(R.id.creaRbCat1);
        rbCat2=(RadioButton)findViewById(R.id.creaRbCat2);
        rbCat3=(RadioButton)findViewById(R.id.creaRbCat3);
        rbCat4=(RadioButton)findViewById(R.id.creaRbCat4);
        rbCat5=(RadioButton)findViewById(R.id.creaRbCat5);
        rbCat6=(RadioButton)findViewById(R.id.creaRbCat6);
        this.controle= Controle.getInstance(this);//création de l'instance controle (singleton)
        ecouteEnregistrer();
        recupCarte();
        }

    private void ecouteEnregistrer(){
        ((Button)findViewById(R.id.btnEnregistrer)).setOnClickListener(new Button.OnClickListener(){
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"test",Toast.LENGTH_LONG).show();
                String question = null;
                String reponse = null;
                String indice = null;
                Integer categorie = 6;
                Integer numCarte = 0;

                //récupération des données saisies
                try {// pour éviter les pb de saisie : char au lieu d'un int
                    question = txtQuestion.getText().toString();
                    reponse = txtReponse.getText().toString();
                    indice = txtIndice.getText().toString();
                } catch (Exception e) {
                }//l'exception ne fera rien
                // comme on a initailisé les variables à 0 on va tester si elles sont restées à 0!

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
        });
    }
    /**
     * récupération du profil s'il a été sérialisé
     */
    public void recupCarte(){
        if(controle.getNumCarte()!=null){
            txtQuestion.setText(controle.getTxtQuestion().toString());
            txtReponse.setText(controle.getTxtReponse().toString());
            txtIndice.setText(controle.getTxtIndice().toString());
            switch (controle.getCategorie()) {
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
            //remettre à vide le profil
            controle.setCarte(null);
            //simulation du click sur le bouton calcul
            // ((Button)findViewById(R.id.btnCalc)).performClick();
        }
    }
}

