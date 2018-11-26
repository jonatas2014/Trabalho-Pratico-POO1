
package entidades;

import java.util.ArrayList;

/**
 *
 * @author jonatas
 */
public class Cliente extends Pessoa {// Classe em construção
    
    
    private ArrayList<Integer> carrinho = new ArrayList<Integer>();
   
    @Override
    public String getCpf() {
        return super.getCpf();
    }

    @Override
    public void setCpf(String cpf) {
        super.setCpf(cpf);
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }
    
    @Override
    public RG getRg() {
        return super.getRg();
    }

    @Override
    public void setRg(RG rg) {
        super.setRg(rg);
    }

    @Override
    public Endereco getEndereco(){
        return super.getEndereco();
    }
    
    @Override
    public void setEndereco(Endereco end){
        super.setEndereco(end);
    }
    
    @Override
    public String getTelefone() {
        return super.getTelefone();
    }

    @Override
    public void setTelefone(String telefone) {
        super.setTelefone(telefone);
    }
    
    /**
     * Método que adiciona itens ao carrinho de compras de um cliente.
     * @param codigoProduto 
     */
    public void addItemNoCarrinho(Integer codigoProduto){
        this.carrinho.add(codigoProduto);
    }
    /**
     * Após a confirmação do pagamento da venda o carrinho do cliente 
     * deve ser esvaziado.
     */
    public void resetCarrinho(){
        this.carrinho.clear();
    }
     
     
    
    // metodo de adicionar no carrinho de compras dele
}
