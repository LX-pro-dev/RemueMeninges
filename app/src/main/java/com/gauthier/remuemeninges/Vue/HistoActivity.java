package com.gauthier.remuemeninges.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gauthier.remuemeninges.Controle.CardEventListener;
import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Carte;
import com.gauthier.remuemeninges.R;

import java.util.ArrayList;
import java.util.Collections;

public class HistoActivity extends AppCompatActivity implements CardEventListener {

    private Controle controle;
    private HistoListAdapter adapter;
    private EditText histo_keyword;
    private RadioButton histoRbDate;
    private RadioButton histoRbLevel;
    private RadioButton histoRbCategoy;
    private ListView lstHisto;
    private Button btn_list_home;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histo);
        this.controle = Controle.getInstance(this);
        histo_keyword = (EditText) findViewById(R.id.histo_keyword);
        histoRbDate = (RadioButton) findViewById(R.id.histoRbDate);
        histoRbLevel = (RadioButton) findViewById(R.id.histoRbLevel);
        histoRbCategoy = (RadioButton) findViewById(R.id.histoRbCategoy);
        progressBar = findViewById(R.id.progress_histo);
        ecouteRetourMenu();
        ArrayList<Carte> lesCartesFull;
        creerList();
        setListeners();
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        //on va chercher la maj de la liste de cartes sur le net
        this.controle = Controle.getInstance(this);
        //on s'inscrit à l'événement delete ou modify du controleur
        controle.setListener(this);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        //on se désinscrit au listener
        controle.setListener(null);
    }

    /**
     * créer une ListAdapter
     */
    private void creerList() {
        if (controle.getLesCartes() != null) {
            ArrayList<Carte> lesCartes = controle.getLesCartes();
            //sort(lesCartes, Collections.<Carte>reverseOrder());
            ListView lstHisto = (ListView) findViewById(R.id.lstHisto);
            progressBar.setVisibility(View.GONE);
            lstHisto.setVisibility(View.VISIBLE);

            if (lesCartes != null) {
                adapter = new HistoListAdapter(this, tri(lesCartes));
            } else {
                adapter = new HistoListAdapter(this, new ArrayList<Carte>());
            }
            lstHisto.setAdapter(adapter);
        }

    }

    /**
     * tri des cartes en fonction du rb sélectionné
     *
     * @param lesCartes
     * @return
     */
    private ArrayList<Carte> tri(ArrayList<Carte> lesCartes) {//crash car les cartes crééer n'ont pas de date

        Log.i("TAG", "nb cartes = " + lesCartes.size());
        //récupérer l'état du radiogroup (date, level catégorie)
        //récupération de la collection
        Collections.sort(lesCartes, (o1, o2) -> {
            //tri en fonction des dates de création des cartes
            if (histoRbDate.isChecked()) {
                if (o1.getDatecreation() == null || o2.getDatecreation() == null) {
                    return 0;
                } else if (o1.getDatecreation().after(o2.getDatecreation())) {
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
        });
        Log.i("TAG", "nb cartes après tri = " + lesCartes.size());
        return lesCartes;
    }

    //écouter les événements (2 méthodes): changement de rb et ajout d'un mot clé / txtQuestion
    public void setListeners() {
        //attache l'écouteur au btn search
        histo_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                Log.i("TAG", s.toString().toLowerCase());
                if (s.toString().isEmpty()) {
                    adapter.updateItems(controle.getLesCartes());
                } else {
                    //appeler la méthode search pour l'applicquer à la liste
                    //faire un appel à Cartes.search() pour mettre à jour la liste des cartes
                    adapter.updateItems(Carte.search(adapter.getItems(), s.toString()));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        histoRbDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateItems(tri(adapter.getItems()));
            }
        });
        histoRbLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateItems(tri(adapter.getItems()));
            }
        });
        histoRbCategoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.updateItems(tri(adapter.getItems()));
            }
        });
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
    }

    /**
     * demande d'afficher la carte dans CardActivity
     *
     * @param carte
     */
    public void afficheCarte(Carte carte) {
        Log.i("HistoAct", "afficheCarte()");
        controle.setCarte(carte);
        Intent intent = new Intent(HistoActivity.this, CardActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    /**
     * demande d'afficher la carte dans CreateCardActivity
     *
     * @param carte
     */
    public void modifyCarte(Carte carte) {
        Log.i("HistoAct", "modifyCarte()");
        controle.setCarte(carte);
        Intent intent = new Intent(HistoActivity.this, CreateCardActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onCardDeleted(int idCardDeleted) {
        //suppression de la carte
        adapter.deleteCard(idCardDeleted);

        //affiché message de destruction de la carte
        Toast toast = Toast.makeText(this, "carte supprimée", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    @Override
    public void onCardModified(Carte carte) {
        adapter.modifyCard(carte);

        //affiché message de destruction de la carte
        Toast toast = Toast.makeText(this, "carte modifiée", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }
}





