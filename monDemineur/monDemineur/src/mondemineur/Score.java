/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mondemineur;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Florian Hugo et Kevin
 */
public class Score implements Serializable {

    private int difficulte;
    private int temps;
    private String pseudo ;

    public Score(int difficulte, int temps , String pseudo) {
        this.difficulte = difficulte;
        this.temps = temps;
        this.pseudo = pseudo ;
    }

    public Score() {
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void updateScore() {
        try {
            String filename = null;
            if (difficulte == 1 ){
                filename = "score1.xml";
            }else if (difficulte == 2)
            {
                filename = "score2.xml";
            } else if ( difficulte ==3)
            {
                filename = "score3.xml";
            }else {
                return;
            }
            XMLEncoder encoder = new XMLEncoder(new FileOutputStream(filename));
            try {
                // serialisation de l'objet
                encoder.writeObject(this);
                encoder.flush();
            } finally {
                // fermeture de l'encodeur
                encoder.close();
            }
            
        }   catch (FileNotFoundException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
