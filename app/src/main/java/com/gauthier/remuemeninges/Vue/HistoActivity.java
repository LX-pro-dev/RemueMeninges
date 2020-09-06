package com.gauthier.remuemeninges.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;
import java.util.ArrayList;
import java.util.Collections;

public class HistoActivity extends AppCompatActivity {

    private Controle controle;//j'ai changé de type Object à type Controle pour avoir accès aux méthodes de Controle getLesCartes() et SetLesCartes()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histo);
        this.controle = Controle.getInstance(this);
        ecouteRetourMenu();
        creerList();
    }

    /**
     * créer une liste adapter
     */
    private void creerList() {
        ArrayList<Carte> lesCartes = controle.getLesCartes();
        Collections.sort(lesCartes, Collections.<Carte>reverseOrder());
        if (lesCartes != null) {
            ListView lstHisto = (ListView) findViewById(R.id.lstHisto);
            HistoListAdapter adapter = new HistoListAdapter(this, lesCartes);
            lstHisto.setAdapter(adapter);
        }
    }

    /**
     * retour au menu
     *
     * */
    public void ecouteRetourMenu(){
        ((Button)findViewById(R.id.btn_list_home)).setOnClickListener(new ImageButton.OnClickListener() {
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
     * demande d'afficher la carte dans CardActivity
     * @param carte
     */
    public void afficheCarte(Carte carte){
        controle.setCarte(carte);
        Intent intent = new Intent(HistoActivity.this, CardActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
