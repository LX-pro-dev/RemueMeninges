package com.gauthier.remuemeninges.Outils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Alexandre GAUTHIER on 14/05/2020.
 */
public abstract class Serializer {
    /**
     * sérialisation d'un objet
     * @param filename nom du fichier où l'on enregistre notre objet
     * @param object objet à enregistrer
     * @param context besoin d'un context pour sérialiser, ds ce cas c'est MainActivity
     */
    public static void serialize(String filename, Object object, Context context){
        try{
            FileOutputStream file= context.openFileOutput(filename, Context.MODE_PRIVATE);
            //déclare un fichier que l'on va ouvrir
            ObjectOutputStream oos;
            //on crée un flux en outputStream pour pouvoir écrire dans ce fichier
            try{
                oos=new ObjectOutputStream(file);//création du fichier
                oos.writeObject(object);//on écrit dans le fichier l'objet reçu en param
                oos.flush();//on le pousse le flux
                oos.close();//on ferme le flux
            }catch(IOException e){
                //erreur de sérialisation
                e.printStackTrace();
            }
        }catch (FileNotFoundException e) {
            //fichier non trouvé
            e.printStackTrace();
        }
    }

    /**
     * désérialisation d'un objet
     * @param filename
     * @param context
     * @return
     */
    public static Object deSerialize(String filename, Context context) {
        try {
            FileInputStream file = context.openFileInput(filename);//ouvre le fichier en input
            ObjectInputStream ois; //création d'un flux en inputStream
            try {
                ois = new ObjectInputStream(file);//on crée le flux pour récupérer l'objet
                try {
                    Object object = ois.readObject();// on récupère l'objet
                    ois.close();//on ferme le flux
                    return object;//on renvoie l'objet
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // fichier non trouvé
            e.printStackTrace();
        }
        return null;//si on n'a pas récupéré l'objet on nrenvoie null
    }
}
