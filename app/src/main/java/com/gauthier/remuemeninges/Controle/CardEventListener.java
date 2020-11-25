package com.gauthier.remuemeninges.Controle;

import com.gauthier.remuemeninges.Modele.Carte;

/**
 * Created by Alexandre GAUTHIER on .
 */
public interface CardEventListener {
    void onCardDeleted(int idCardDeleted);

    void onCardModified(Carte carte);

}
