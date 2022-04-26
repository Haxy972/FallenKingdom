package fr.haxy972.fallen;

public enum GameStatut {

    LOBBY(), GAME(), END();


    public static GameStatut gameStatut;

    public static void setStatut(GameStatut statut) {
        gameStatut = statut;
    }

    public static GameStatut getStatut() {
        return gameStatut;

    }

    public static boolean isStatut(GameStatut statut) {

        if (gameStatut == statut) {
            return true;
        } else {
            return false;
        }


    }


}
