
package conexões;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author jonatas
 */
public class ConexaoBanco {
    
    private Connection conexao;
    /**
     * Conecta a um banco de dados (cria o banco se ele não existir)
     * @return true: conectou, false: erro de conexão 
     */
    public boolean conectar(){
        try{
            // Definição de qual banco será conectado
            String url = "jdbc:sqlite:banco_de_dados_Loja_Departamento/Loja_Departamento_SQLite.db";
            
            this.conexao = DriverManager.getConnection(url);
        }catch(SQLException e){
            
            System.err.println(e.getMessage());
            return false;
        }
        //linha apenas para saber se o banco foi conectado
        System.out.println("Conectou no banco de dados");
        return true;
    }
    /**
     * desconecta de um banco de dados
     * @return true: desconectou, false: erro na desconexão 
     */
    public boolean desconectar(){
        try{
            if (this.conexao.isClosed()== false){
                this.conexao.close();
            }
            
        }catch(SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        System.out.println("Desconectou do banco de dados");
        return true;
    }
    /**
     * Criar os statements para as consultas ao banco serem executados
     * @return o statement caso seja criado corretamente.
     */
    public Statement criarStatement(){
        try{
            return this.conexao.createStatement();
            
        }catch(SQLException e){
            System.out.println("Erro de criação de um statement");
            return null;
        }
    }
    /**
     * Criar os prepared statements (statements com parâmetros) para as consultas ao banco serem executados
     * @param sql consultas em SQL
     * @return o prepared statement caso seja criado corretamente.
     */
    public PreparedStatement criarPreparedStatement(String sql){
        try{
            return this.conexao.prepareStatement(sql);
            
        }catch(SQLException e){
            System.out.println("Erro de criação do prepared statement");
            return null;
        }
    }
    /**
     * Método que recupera a conexão com o banco
     * @return essa conexão
     */
    public Connection getConexao(){
        return this.conexao;
    }
}
