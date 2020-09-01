package com.gauthier.remuemeninges.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.R;

class MainActivity extends AppCompatActivity {
    private Controle controle;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controle= Controle.getInstance(this);
        ecouteMenu((ImageButton)findViewById(R.id.btnJouer),CardActivity.class);
        ecouteMenu((ImageButton)findViewById(R.id.btnNewCard),CreateCardActivity.class);

        }

    /**
     * Ouvrir l'activity correspondante
     * @param btn
     * @param classe
     */
    public void ecouteMenu(ImageButton btn, final Class classe){
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
}
