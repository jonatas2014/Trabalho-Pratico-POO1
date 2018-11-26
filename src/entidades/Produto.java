
package entidades;

/**
 *
 * @author jonatas
 */
public class Produto {
   private int codigo_Produto;
   private String nome_Produto;
   private double valor_Produto;
   private int qtd_Estoque_Produto;
 

    public int getCodigo_Produto() {
        return codigo_Produto;
    }

    public void setCodigo_Produto(int id_Produto) {
        this.codigo_Produto = id_Produto;
    }

    public String getNome_Produto() {
        return nome_Produto;
    }

    public void setNome_Produto(String nome_Produto) {
        this.nome_Produto = nome_Produto;
    }

    public double getValor_Produto() {
        return valor_Produto;
    }

    public void setValor_Produto(double valor) {
        this.valor_Produto = valor;
    }

    public int getQtd_Estoque_Produto() {
        return qtd_Estoque_Produto;
    }

    public void setQtd_Estoque_Produto(int qtd_Estoque_Produto) {
        this.qtd_Estoque_Produto = qtd_Estoque_Produto;
    }
    

    
   
   
}
