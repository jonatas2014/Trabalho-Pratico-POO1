
package entidades;

/**
 *
 * @author jonatas
 */
public class RG {
    
    private String numeroRG;

    public String getNumeroRG() {
        return numeroRG;
    }

    public void setNumeroRG(String numeroRG) {
        this.numeroRG = numeroRG;
    }
    
    @Override
    public String toString(){
        return "RG: " + this.getNumeroRG();
    }
    
}
