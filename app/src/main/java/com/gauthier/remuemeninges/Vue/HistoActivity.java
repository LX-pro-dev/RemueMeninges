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
        // Chercher la mise à jour de la liste de cartes sur le net
       this.controle = Controle.getInstance(this);
        //S'inscrire à l'événement delete ou modify du contrôleur
        controle.setListener(this);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        //Se désinscrire du listener
        controle.setListener(null);
    }

    /**
     * Créer une ListAdapter
     */
    private void creerList() {
        if (controle.getLesCartes() != null) {
            ArrayList<Carte> lesCartes = controle.getLesCartes();
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
     * Tri des cartes en fonction du rb sélectionné
     * @param lesCartes
     * @return
     */
    private ArrayList<Carte> tri(ArrayList<Carte> lesCartes) {

        Log.i("TAG", "nb cartes = " + lesCartes.size());
        // Récupérer l'état du radiogroup (date, level catégorie)
        // Récupération de la collection
        Collections.sort(lesCartes, (o1, o2) -> {
            // Tri en fonction des dates de création des cartes
            if (histoRbDate.isChecked()) {
                triDate(o1,o2);
                // Tri en fonction du niveau de difficulté de la carte
            } else if (histoRbLevel.isChecked()) {
                return o1.getLevel().compareTo(o2.getLevel());
                // Tri en fonction de la catégorie de la carte
            } else if (histoRbCategoy.isChecked()) {
                return o1.getCategorie().compareTo(o2.getCategorie());
            }
            return 0;
        });
        Log.i("TAG", "nb cartes après tri = " + lesCartes.size());
        return lesCartes;
    }

    public int triDate(Carte o1, Carte o2) {
        if (o1.getDatecreation() == null || o2.getDatecreation() == null) {
            return 0;
        } else if (o1.getDatecreation().after(o2.getDatecreation())) {
            return 1;
        } else if (o1.getDatecreation().equals(o2.getDatecreation())) {
            return 0;
        }
        return -1;
    }

    // Ecouter les événements (2 méthodes): changement de rb et ajout d'un mot clé / txtQuestion
    public void setListeners() {
        // Attache l'écouteur au btn search
        linkListenerToBtnSearch();
    }

    /**
     * Attache le listener sur le bouton search
     */
    public void linkListenerToBtnSearch() {
        histo_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                Log.i("TAG", s.toString());
                if (s.toString().isEmpty()) {
                    adapter.updateItems(controle.getLesCartes());
                } else {
                    // Faire un appel à Cartes.search() pour mettre à jour la liste des cartes
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
    }

    /**
     * Retour au menu
     */
    public void ecouteRetourMenu() {
        ((Button) findViewById(R.id.btn_list_home)).setOnClickListener(new ImageButton.OnClickListener() {
            //
            // Gérer un événement sur on objet graphique
            // On recherche l'objet graphique ac R.id
            // Et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)

            public void onClick(View v) {
                Intent intent = new Intent(HistoActivity.this, MainActivity.class);
                // Permet de ne pas garder en mémoire l'activité courante lorsque l'on ouvre une nouvelle activity
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    /**
     * Demande d'afficher la carte dans CardActivity
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
     * Demande d'afficher la carte dans CreateCardActivity
     * @param carte
     */
    public void modifyCarte(Carte carte) {
        Log.i("HistoAct", "modifyCarte()");
        controle.setCarte(carte);
        Intent intent = new Intent(HistoActivity.this, CreateCardActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Suppression de la carte
     * @param idCardDeleted id de la carte à supprimer
     */
    @Override
    public void onCardDeleted(int idCardDeleted) {
        // Suppression de la carte
        adapter.deleteCard(idCardDeleted);

        // Afficher message de destruction de la carte
        Toast toast = Toast.makeText(this, "carte supprimée", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    /**
     * Modifier une carte
     * @param carte carte à modifier
     */
    @Override
    public void onCardModified(Carte carte) {
        adapter.modifyCard(carte);

        //Afficher message de destruction de la carte
        Toast toast = Toast.makeText(this, "carte modifiée", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}





