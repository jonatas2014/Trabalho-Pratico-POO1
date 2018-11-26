
package interfaces;

/**
 *
 * @author jonatas
 */
public interface InterfaceProdutos {
    
    public abstract void cadastrarProduto();
    public abstract void excluirProduto();
    public abstract void atualizarProduto();
    public abstract void buscarProduto();
    public abstract void addItemProduto(int qtd);
    public abstract void excluirItemProduto(int qtd);
    
    
}
