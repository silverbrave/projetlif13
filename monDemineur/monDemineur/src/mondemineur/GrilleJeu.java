/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mondemineur;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Hugo Paris
 */
public class GrilleJeu {

    //Cellule vide
    public static final int VIDE = 0;

    //La cellule contient une bombe
    public static final int BOMBE = -1;

    //La cellule est flag comme étant une bombe
    public static final int FLAG = -2;

    //La cellule à un ?
    public static final int IDK = -3;

    //La cellule n'est pas encore découverte.
    public static final int COUVERTE = -5;

    //La cellule est flaggé sans y avoir de bombe dessous.
    public static final int WRONGFLAG = -8;

    //La cellule contient une bombe qui a explosée.
    public static final int EXPLODE = -9;

    //Liste vide
    public static final LinkedList LISTEVIDE = new LinkedList();

    private Cellule[][] grille; // grille des bombes cachés
    private Cellule[][] grilleExterieur; // grille des bombes vue de l'exterieur
    private int bombes, flags;
    private int lignes, colonnes;
    private int celluleRestantes;
    private boolean pointInterrogation;

    public GrilleJeu(int nbLignes, int nbColonnes, int nbBombes, int firstLigne, int firstColonne) {
        bombes = nbBombes;
        lignes = nbLignes;
        colonnes = nbColonnes;
        grille = new Cellule[lignes][colonnes];
        grilleExterieur = new Cellule[lignes][colonnes];

        initialiseGrille(firstLigne, firstColonne);
    }

    public GrilleJeu(int bombes, int lignes, int colonnes) {
        this.bombes = bombes;
        this.lignes = lignes;
        this.colonnes = colonnes;
    }
    

    public int getBombes() {
        return bombes;
    }

    public int getFlags() {
        return flags;
    }

    public int getLignes() {
        return lignes;
    }

    public int getColonnes() {
        return colonnes;
    }

    // 0 si toutes les bombes ont été flaggés.
    public int getCelluleRestantes() {
        return celluleRestantes;
    }

    public Cellule[][] getGrilleExterieur() {
        return grilleExterieur;
    }

    public Cellule[][] getGrille() {
        return grille;
    }

    // retourne true si toutes les bombes ont été identifiés
    public boolean estFini() {
        return (celluleRestantes == 0);
    }

    public boolean doPointInterrogation() {
        return pointInterrogation;
    }

    public void doPointInterrogation(boolean b) {
        pointInterrogation = b;
    }

    public void initialiseGrille(int firstLigne, int firstColonne) {
        flags = 0;
        celluleRestantes = (lignes * colonnes) - bombes;

        // initialise la grille avec des cases vides, et la grille exterieur de case couverte
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                grille[i][j] = new Cellule(i, j, VIDE);
                grilleExterieur[i][j] = new Cellule(i, j, COUVERTE);
            }
        }

        // place les bombes dans la grille
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < bombes; i++) {
            while (true) {
                int l = Math.abs(random.nextInt() % lignes);
                int c = Math.abs(random.nextInt() % colonnes);
                if (grille[l][c].getStatus() != BOMBE || (l != firstLigne && c != firstColonne)) {
                    grille[l][c].setStatus(BOMBE);
                    updateNbBombes(l, c);
                    break;
                }
            }
        }
    }

    private void updateNbBombes(int l, int c) {
        int r1 = Math.max(l - 1, 0);
        int r2 = Math.min(l + 1, lignes - 1);
        int c1 = Math.max(c - 1, 0);
        int c2 = Math.min(c + 1, colonnes - 1);

        for (int i = r1; i <= r2; i++) {
            for (int j = c1; j <= c2; j++) {
                if (grille[i][j].getStatus() != BOMBE) {
                    grille[i][j].setStatus(grille[i][j].getStatus() + 1);
                }
            }
        }
    }

    public Cellule flag(int l, int c) { // clic droit
        Cellule contenuGrilleExterieur = grilleExterieur[l][c];
        switch (contenuGrilleExterieur.getStatus()) {
            case COUVERTE:
                grilleExterieur[l][c].setStatus(FLAG);
                flags += 1;
                break;
            case FLAG:
                grilleExterieur[l][c].setStatus(pointInterrogation ? IDK : COUVERTE);//operateur ternaire magique
                flags -= 1;
                break;
            case IDK:
                grilleExterieur[l][c].setStatus(COUVERTE);
                break;
            default:
                break;
        }

        return grilleExterieur[l][c];
    }

    private List aReveler(int l, int c) {//renvoie une liste de case à révéler
        // On fait rien si la case a déjà été révélé
        if (grilleExterieur[l][c].getStatus() != COUVERTE && grilleExterieur[l][c].getStatus() != IDK) {
            return LISTEVIDE;
        }

        int val = grille[l][c].getStatus();

        // Si on révele une bombe = fin du jeu
        if (val == BOMBE) {
            grilleExterieur[l][c].setStatus(EXPLODE);
            return finPartie();
        }

        LinkedList cellulesOuvertes = new LinkedList();
        grilleExterieur[l][c].setStatus(val);
        if (val == VIDE) {
            revelerCelluleVide(l, c, cellulesOuvertes); // empty cell: open all adjacent blank cells
        } else {
            celluleRestantes -= 1;
            cellulesOuvertes.add(getCellule(l, c));
        }

        return cellulesOuvertes;
    }

    public List revele(int l, int c)//clic gauche
    {
        List<Cellule> list = new ArrayList(aReveler(l, c));
        for (Cellule cel : list) {
            //on met le contenu de la grille interne sur la grille exterieure pour révéler la case
            grilleExterieur[cel.getX()][cel.getY()].setStatus(grille[cel.getX()][cel.getY()].getStatus());
        }

        return list;

    }

    //on aReveler recursivement les cases vides adjacentes
    //on utilise une liste pour garder en mémoire les cellules déjà révélé
    private void revelerCelluleVide(int l, int c, List cellulesOuvertes) {
        celluleRestantes -= 1;
        cellulesOuvertes.add(getCellule(l, c));

        int r1 = Math.max(l - 1, 0);
        int r2 = Math.min(l + 1, lignes - 1);
        int c1 = Math.max(c - 1, 0);
        int c2 = Math.min(c + 1, colonnes - 1);
        for (int i = r1; i <= r2; i++) {
            for (int j = c1; j <= c2; j++) {
                if (i >= 0 && i < lignes && j >= 0 && j < colonnes) {
                    int val = grille[i][j].getStatus();
                    if (grilleExterieur[i][j].getStatus() == COUVERTE && val != BOMBE) {
                        grilleExterieur[i][j].setStatus(val);
                        if (val == VIDE) {
                            revelerCelluleVide(i, j, cellulesOuvertes);
                        } else {
                            celluleRestantes -= 1;
                            cellulesOuvertes.add(getCellule(i, j));
                        }
                    }
                }
            }
        }
    }

    public Cellule getCellule(int l, int c) {
        return grilleExterieur[l][c];
    }
    
    public List finPartie()
    {
        List<Cellule> listFinal = new ArrayList();
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if(grille[i][j].getStatus()==BOMBE)
                listFinal.add(grille[i][j]); // remplissage de la liste avec toute les bombes
            }
        }
        return listFinal;
    }
}
