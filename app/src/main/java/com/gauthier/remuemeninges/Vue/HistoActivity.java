package com.gauthier.remuemeninges.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.gauthier.remuemeninges.Controle.Controle.carte;

public class HistoActivity extends AppCompatActivity {

    private Controle controle;
    private HistoListAdapter adapter;
    private SearchView histo_keyword;
    private RadioButton histoRbDate;
    private RadioButton histoRbLevel;
    private RadioButton histoRbCategoy;
    private ListView lstHisto;
    private Button btn_list_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histo);
        this.controle = Controle.getInstance(this);
        histo_keyword = (SearchView) findViewById(R.id.histo_keyword);
        histoRbDate = (RadioButton) findViewById(R.id.histoRbDate);
        histoRbLevel = (RadioButton) findViewById(R.id.histoRbLevel);
        histoRbCategoy = (RadioButton) findViewById(R.id.histoRbCategoy);
        ecouteRetourMenu();
        ArrayList<Carte> lesCartesFull;
        creerList();
        setListeners();
    }

    /**
     * créer une liste adapter
     */
    private void creerList() {
        ArrayList<Carte> lesCartes = controle.getLesCartes();
        //sort(lesCartes, Collections.<Carte>reverseOrder());
        ListView lstHisto = (ListView) findViewById(R.id.lstHisto);
        if (lesCartes != null) {
            adapter = new HistoListAdapter(this, tri(lesCartes));
        } else {
            adapter = new HistoListAdapter(this, new ArrayList<Carte>());
        }
        lstHisto.setAdapter(adapter);
    }

    /**
     * tri des cartes en fonction du rb sélectionné
     * @param lesCartes
     * @return
     */
    private ArrayList<Carte> tri(ArrayList<Carte> lesCartes) {
        //récupérer l'état du radiogroup (date, level catégorie)


        //récupération de la collection
        Collections.sort(lesCartes, new Comparator<Carte>() {
            @Override
            public int compare(Carte o1, Carte o2) {
                //tri en fonction des dates de création des cartes
                if (histoRbDate.isChecked()) {
                    if (o1.getDatecreation().after(o2.getDatecreation())) {
                        return 1;
                    } else if (o1.getDatecreation().equals(o2.getDatecreation())) {
                        return 0;
                    }
                    return -1;
                    //tri en fonction du niveau de difficulté de la carte
                } else if (histoRbLevel.isChecked()) {
                    return o1.getLevel().compareTo(o2.getLevel());
                    //tri en fonction de la catégorie de la carte
                } else if (histoRbCategoy.isChecked()) {
                    return o1.getCategorie().compareTo(o2.getCategorie());
                }
                return 0;   //j'ai mis ce return là sans être persuadé que ce soit bon...
            }
        });
        return lesCartes;
    }

    //écouter les événements (2 méthodes): changement de rb et ajout d'un mot clé / txtQuestion
    public void setListeners() {
        //attache l'écouteur au btn search
        histo_keyword.setOnClickListener((View.OnClickListener) this);
        histoRbDate.setOnClickListener((View.OnClickListener) this);
        histoRbLevel.setOnClickListener((View.OnClickListener) this);
        histoRbCategoy.setOnClickListener((View.OnClickListener) this);
        //récupérer le text
        String text = null;
        //faire un appel à Cartes.search() pour mettre à jour la liste des cartes
        adapter.updateItems(Carte.search(controle.getLesCartes(), text));

        //attacher l'écouteur du radiogroup de tri pour définir le mode de tri
        //récupérer le mode sélectionné
        adapter.updateItems(tri(adapter.getItems()));

    }

    /**
     * retour au menu
     */
    public void ecouteRetourMenu() {
        ((Button) findViewById(R.id.btn_list_home)).setOnClickListener(new ImageButton.OnClickListener() {
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                Intent intent = new Intent(HistoActivity.this, MainActivity.class);
                //permet de ne pas garder en mémoire l'activité courante lorsque l'on ouvre une nouvelle activity
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        });

        /**
         * demande d'afficher la carte dans CardActivity
         * @param carte
         */
        public void afficheCarte(Carte carte){
            Log.i("HistoAct", "afficheCarte()");
            controle.setCarte(carte);
            Intent intent = new Intent(HistoActivity.this, CardActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }


}

