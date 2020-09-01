package com.gauthier.remuemeninges.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;

import java.util.ArrayList;
import java.util.Collections;

public class HistoActivity extends AppCompatActivity {

    private Object controle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histo);
        this.controle= Controle.getInstance(this);
        ecouteRetourMenu();
        creerList();
    }

    /**
     * retour au menu
     *
     * */
    public void ecouteRetourMenu(){
        ((ImageButton)findViewById(R.id.btAccueil)).setOnClickListener(new ImageButton.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                Intent intent = new Intent(HistoActivity.this, MainActivity.class);
                //permet de ne pas garder en mémoire l'activité courante lorsque l'on ouvre une nouvelle activity
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        } );
    }

    /**
     * créer une liste adapter
     */
    private void creerList(){
        ArrayList<Carte> lesCartes= controle.getLesCartes();
        Collections.sort(lesCartes, Collections.<Carte>reverseOrder());
        if(lesCartes!=null){
            ListView lstHisto=(ListView)findViewById(R.id.lstHisto);
            HistoListAdapter adapter = new HistoListAdapter(this,lesCartes);
            lstHisto.setAdapter(adapter);
        }
    }

    /**
     * demande d'afficher le profil dans CardActivity
     * @param carte
     */
    public void afficheProfil(Carte carte){
        controle.setCarte(carte);
        Intent intent = new Intent(HistoActivity.this, CardActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    private class ViewHolder{
        ImageButton btList;
        TextView txtListDate;
        TextView txtListIMG;

    }
}
