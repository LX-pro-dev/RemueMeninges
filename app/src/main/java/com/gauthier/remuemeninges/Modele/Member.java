package com.gauthier.remuemeninges.Modele;

/**
 * Created by Alexandre GAUTHIER on 2020/10/21.
 */
public final class Member {
    //singleton pattern
    private static Member instance = null;//accessible par la classe

    /**
     * contructeur privé
     */
    private Member() {
        super();
    }//ne déclare rien, on ne pourra faire new car déclaré en private

    /**
     * création de l'instance
     * @return
     */
    public static final Member getInstance() {//au lieu de faire new pour créer le contrôleur
        if (Member.instance == null) {//si l'instance n'est pas déjà créée, on la créée
            Member.instance = new Member();
        }
        return Member.instance;
    }

    public boolean isAdmin() {
        return false;
    }

    public boolean isCreator() {
        return true;
    }

}
