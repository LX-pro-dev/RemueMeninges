package com.gauthier.remuemeninges.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.R;

public class MainActivity extends AppCompatActivity {
    private Controle controle;
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
            controle= Controle.getInstance(this);
            ecouteMenu((Button)findViewById(R.id.home_btn_play),CardActivity.class);
            ecouteMenu((Button)findViewById(R.id.home_btn_list),CreateCardActivity.class);

    }

    /**
     * Ouvrir l'activity correspondante
     * @param btn
     * @param classe
     */
    public void ecouteMenu(Button btn, final Class classe){
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
        } );
    }
    /**
     création d'un menu pour les options (langue, difficulté, mode nuit...)
     */
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu., menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //if (id == R.id.action_settings) {
            return true;
        }
       // return super.onOptionsItemSelected(item);
    //}
*/


}
