
package criarBanco;

import conexões.ConexaoBanco;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author jonatas
 */
public class Tabelas {
    
    private final ConexaoBanco conexao;
    
    /**
     * Constrói as tabelas desssa classe no banco apropriado.
     * 
     * @param pconexao a conexão com o banco apropriado.
     */
    public Tabelas(ConexaoBanco pconexao){
        this.conexao = pconexao;
    }
    /**
     * Cria a tabela Clientes.
     */
    public void criarTabelaCliente(){
        
        String sql = "CREATE TABLE Clientes"
                + "("
                +"CPF text PRIMARY KEY,"
                +"Nome text NOT NULL,"
                +"RG text NOT NULL,"
                +"Endereço text,"
                +"Telefone text,"
                +"Programa_Fidelidade text"// opções sim - s e não - n
                +");";
        //executando o sql de criar tabelas
        boolean conectou = false;
        try{
            conectou = this.conexao.conectar();
            Statement stmt = this.conexao.criarStatement();
            stmt.execute(sql);
            System.out.println("Tabela Clientes criada");
        }catch(SQLException e){
            System.out.println("Erro na criação da tabela Clientes");
        }
        finally{
            if (conectou)
                this.conexao.desconectar();
        }
    }
    /**
     * Cria a tabela Produtos.
     */
    public void criarTabelaProduto(){
        
        String sql = " CREATE TABLE IF NOT EXISTS Produtos"
                + "("
                +"Código_Produto integer PRIMARY KEY,"
                +"Nome text NOT NULL,"
                +"Valor double NOT NULL,"
                +"Qtd_Estoque integer"
                +");";
                
        boolean conectou = false;
        try{
             conectou = conexao.conectar();
             Statement statement = conexao.criarStatement();
             statement.execute(sql);
        }catch(SQLException e){
            System.out.println("Falha na criação da tabela de produtos");
        }finally{
            if (conectou)
                this.conexao.desconectar();           
        }
    }
    /**
     * Cria a tabela Vendas.
     */
    public void criarTabelaVendas(){
        String sql = " CREATE TABLE IF NOT EXISTS Vendas"
                + "("
                +"Código_Venda integer PRIMARY KEY,"
                +"Data text NOT NULL,"// Escrever no formato : YYYY-MM-DD
                +"Valor_total double NOT NULL,"
                +"Tipo_Pagamento text,"// Opções: a vista, misto, parcelado
                +"Numero_de_Parcelas int,"
                +"CPF_Cliente text"
                +");";
                
        boolean conectou = false;
        try{
             conectou = conexao.conectar();
             Statement statement = conexao.criarStatement();
             statement.execute(sql);
        }catch(SQLException e){
            System.out.println("Falha na criação da tabela de vendas");
        }finally{
            if (conectou)
                this.conexao.desconectar();           
        }
    }
    /**
     * Cria a tabela Funcionários.
     */
    public void criarTabelaFuncionarios(){
         String sql = "CREATE TABLE IF NOT EXISTS Funcionarios"
                + "("
                +"CPF text PRIMARY KEY,"
                +"Nome text NOT NULL,"
                +"RG text NOT NULL,"
                +"Endereço text,"
                +"Telefone text"                
                +");";
        //executando o sql de criar tabelas
        boolean conectou = false;
        try{
            conectou = this.conexao.conectar();
            Statement stmt = this.conexao.criarStatement();
            stmt.execute(sql);
            System.out.println("Tabela Funcionarios criada");
        }catch(SQLException e){
            System.out.println("Erro na criação da tabela Funcionarios");
        }
        finally{
            if (conectou)
                this.conexao.desconectar();
        }
    }
    
    
  
    
    
   
    
}
