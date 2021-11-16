package com.gauthier.remuemeninges.Modele;

/**
 * Created by Alexandre GAUTHIER on 2020/10/21.
 * Permet de définir les droits à un user
 */
public final class Member {
    //singleton pattern
    private static Member instance = null;//accessible par la classe
    private boolean isMember;
    private boolean isCreator;
    private boolean isAdmin;

    //Contructeur privé
    private Member() {
        super();
    }//ne déclare rien, on ne pourra faire new car déclaré en private

    /**
     * Création de l'instance
     * @return l'instance Member
     */
    public static final Member getInstance() {//au lieu de faire new pour créer le contrôleur
        if (Member.instance == null) {//si l'instance n'est pas déjà créée, on la créée
            Member.instance = new Member();
        }
        return Member.instance;
    }

    /**
     * Vérifie si le member est un administrateur
     * @return true si c'est un admin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Vérifie si le member est un créateur de cartes
     * @return true si c'est un créateur de cartes
     */
    public boolean isCreator() {
        return isCreator;
    }

    /**
     * Vérifie si le member est un simple utilisateur
     * @return true si c'est un simple utilisateur de l'app
     */
    public boolean isMember() {
        return isMember;
    }

    //////////
    // Setters
    //////////
    public void setMember(boolean member) {
        isMember = member;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
