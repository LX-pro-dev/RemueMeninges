package com.gauthier.remuemeninges.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Member;
import com.gauthier.remuemeninges.R;

public class MainActivity extends AppCompatActivity {
    private Controle controle;
    private Member member;

    /*
    1 faire une menu :
    choix de la langue (doit définir automatiquement la langue des cartes à proposer ou à créer)
    enregistrement du user (définition de son profil (admin, créateur de carte, joueur)
    choix du mode nuit
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controle = Controle.getInstance(this);
        member = Member.getInstance(this);
        if (member.isAdmin(member) || member.isCreator(member)) {
            ecouteMenu((Button) findViewById(R.id.home_btn_play), CardActivity.class);
            ecouteMenu((Button) findViewById(R.id.home_btn_create), CreateCardActivity.class);
            ecouteMenu((Button) findViewById(R.id.home_btn_list), HistoActivity.class);
        } else {
            findViewById(R.id.home_btn_create).setVisibility(View.GONE);
            ecouteMenu((Button) findViewById(R.id.home_btn_play), CardActivity.class);
            ecouteMenu((Button) findViewById(R.id.home_btn_list), HistoActivity.class);
        }
    }

    /**
     * Ouvrir l'activity correspondante
     *
     * @param btn
     * @param classe
     */
    public void ecouteMenu(Button btn, final Class classe) {
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            //pour gérer un événement sur on objet graphique
            // on recherche l'objet graphique ac R.id
            // et on applique setOnClickListener() qui redéfinie la méthode onClick(View v)
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, classe);
                //permet de ne pas garder en mémoire l'activité courante lorsque l'on ouvre une nouvelle activity
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
