/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mondemineur;

/**
 *
 * @author Hugo Paris
 */
public class Cellule {
    int _x, _y;
    int status;
    
    
    
    
    Cellule(int x, int y , int status) {
      _x = x;
      _y = y;
      this.status = status ;
    }

    public int getX() { return _x; }

    public int getY() { return _y; }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
