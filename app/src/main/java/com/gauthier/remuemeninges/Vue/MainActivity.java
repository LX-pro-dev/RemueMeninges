package com.gauthier.remuemeninges.Vue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.gauthier.remuemeninges.BuildConfig;
import com.gauthier.remuemeninges.Controle.Controle;
import com.gauthier.remuemeninges.Modele.Member;
import com.gauthier.remuemeninges.R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Controle controle;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private UUID uuid;
    public static String app_uuid;
    private static final String TAG = "MainActivity";
    // Remote Config keys en lien avec le fichier "remote_config_defaults.xml"
    public static final String APP_UUID = "app_uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_main);
        controle = Controle.getInstance(this);
        sharedPreferences = getPreferences(MODE_PRIVATE);//mode private : seul notre appli y a accès

        //vérification de l'uuid du device du user
        checkUuid();

        // Get Remote Config instance.
        remoteConfig();
    }

    /**
     * fetch visibility of different button from remote config service and activate it
     */
    private void fetchConfig() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d(TAG, "Config params updated: " + updated);
                        Toast.makeText(MainActivity.this, "Fetch and activate succeeded",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Fetch failed",
                                Toast.LENGTH_SHORT).show();
                    }

                    switch (mFirebaseRemoteConfig.getString(app_uuid)) {
                        case "1":
                            Member.getInstance().setAdmin(true);
                            break;
                        case "2":
                            Member.getInstance().setCreator(true);
                            break;
                        case "3":
                            Member.getInstance().setMember(true);
                            break;
                    }

                    buildVisibility();
                });
    }


    /**
     * gérer la présence des boutons en fonctions des autorisations octroyées par le statut du membre
     */
    private void buildVisibility() {
        Log.d("fetchButtonsVisibility", "app_uuid " + mFirebaseRemoteConfig.getString(app_uuid));
        ecouteMenu((Button) findViewById(R.id.home_btn_play), CardActivity.class);
        ecouteMenu((Button) findViewById(R.id.home_btn_create), CreateCardActivity.class);
        ecouteMenu((Button) findViewById(R.id.home_btn_list), HistoActivity.class);
        progressBar = findViewById(R.id.progress_circular);

        if (Member.getInstance().isAdmin() || Member.getInstance().isCreator()) {
            findViewById(R.id.home_btn_create).setVisibility(View.VISIBLE);
            findViewById(R.id.home_btn_play).setVisibility(View.VISIBLE);
            findViewById(R.id.home_btn_list).setVisibility(View.VISIBLE);
            findViewById(R.id.progress_circular).setVisibility(View.GONE);
        } else {
            findViewById(R.id.home_btn_play).setVisibility(View.VISIBLE);
            findViewById(R.id.home_btn_list).setVisibility(View.VISIBLE);
            findViewById(R.id.progress_circular).setVisibility(View.GONE);
        }
    }


    /**
     * Ouvrir l'activity correspondante
     * @param btn
     * @param classe
     */
    public void ecouteMenu(Button btn, final Class classe) {
        btn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            // Gérer un événement sur on objet graphique
            // 1) on recherche l'objet graphique ac R.id
            // 2) on applique setOnClickListener() qui redéfinie la méthode onClick(View v)
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, classe);
                // Ne pas garder en mémoire l'activité courante lorsque l'on ouvre une nouvelle activity
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    /**
     * Créer une instance de remoteConfig de Firebase
     */
    public void remoteConfig() {
        // [START get_remote_config_instance]
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // [END get_remote_config_instance]

        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development. Also use Remote Config
        // Setting to set the minimum fetch interval.
        // [START enable_dev_mode]
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(BuildConfig.DEBUG ? 0 : 60 * 15)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        // [END enable_dev_mode]

        // Set default Remote Config parameter values. An app uses the in-app default values, and
        // when you need to adjust those defaults, you set an updated value for only the values you
        // want to change in the Firebase console. See Best Practices in the README for more
        // information.
        // [START set_default_values]
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        // [END set_default_values]

        fetchConfig();
    }

    /**
     * Verifier si le device du suer est déjà enregistré sinon lui créer un uuid
     */
    public void checkUuid () {
        if (sharedPreferences.getString(APP_UUID, null) != null) {
            if (sharedPreferences.getString(APP_UUID, null).charAt(0) == '_') {
                app_uuid = sharedPreferences.getString(APP_UUID, null);
            } else {
                app_uuid = "_" + sharedPreferences.getString(APP_UUID, null);
            }
        } else {
            uuid = UUID.randomUUID();
            app_uuid = uuid.toString();
            String[] tab = app_uuid.split("-");
            app_uuid = "_";
            for (String g : tab) app_uuid += g;
            Log.d("generate app_uuid", app_uuid);
            sharedPreferences.edit().putString(APP_UUID, app_uuid).apply();
        }
        Log.d("uuid", "" + app_uuid);
    }
}
