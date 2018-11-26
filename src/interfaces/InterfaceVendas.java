
package interfaces;

import conexões.ConexaoBanco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author jonatas
 */
public abstract class InterfaceVendas {
    
    public abstract void vender();
    public abstract void estorno(int codigoVenda);
    
    /**
     * Método que auxilia as vendas verificando se o código do produto informado 
     * se refere a um produto já cadastrado.
     * @param codigo cádigo para verificação
     * @return true: prduto no banco , false: produto não existe
     * 
     */
    public boolean verificaCadastroProduto(int codigo){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
        
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
            
        int presenca = 0;
        
        String query = " SELECT *"
                +" FROM Produtos"
                +" WHERE Código_Produto = ?;";
             
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setInt(1,codigo);
            resultset = preparaStatement.executeQuery();
            
            if (resultset.next() == true)//Já há produto com essse código
                presenca = 1;
                           
        }catch(SQLException e){
            
            System.out.println("Erro na verificação no método verificaCadastroProduto");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão em método verificaCadastroProduto");
            }
           
        } 
        if (presenca == 1)
            return true;
        else
            return false;
        
    }
    /**
     * Método que auxilia a venda abatendo produtos do estoque ao serem adicionados
     * a um carrinho de compras.
     * @param codigo código do produto
     * @param qtd quantidade a ser retirada do estoque
     */
    public void abaterEstoque(int codigo, int qtd){
                
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        conexaoBanco.conectar();
        
        PreparedStatement preparaStatement = null;
        
        String sql = " UPDATE Produtos"
                + " SET "
                + " Qtd_Estoque = ? "
                + " WHERE Código_Produto = ? ;";
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(sql);
                           
                preparaStatement.setInt(1, this.retornaEstoqueV(codigo) - qtd );
                preparaStatement.setInt(2, codigo );            
                preparaStatement.executeUpdate();                        
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Falha em desconectar da parte de abaterEstoque em interfaceVendas");
            }
        }
    }
    /**
     * 
     * Método que retorna o estoque de produto apenas para auxiliar nas vendas.
     * @param codigoProduto código do produto
     * @return 
     */
    public int retornaEstoqueV(int codigoProduto){
        
        int estoque = 0;
        
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
               
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
             
        String query = " SELECT "
                +" Qtd_Estoque "
                +" FROM Produtos"
                +" WHERE Código_Produto = ?;";             
        try{
            System.out.println("-------------------------");
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setInt(1,codigoProduto);
            resultset = preparaStatement.executeQuery();
            
            estoque = resultset.getInt("Qtd_Estoque");
            
        }catch(SQLException e){
            
            System.out.println("Erro na busca método retorna estoque de interfaceVendas");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão método retorna estoque de interfaceVendas");
            }
           
        }
        return estoque;
    }
    /**
     * Método que retorna o valor atual de um produto.
     * @param codigo
     * @return 
     */
    public double valorProduto(int codigo){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
      
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
        
        double valor = 0;
            
        String query = " SELECT Valor"
                +" FROM Produtos"
                +" WHERE Código_Produto = ?;";
             
        try{            
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setInt(1,codigo);
            resultset = preparaStatement.executeQuery();
            
            valor = resultset.getDouble("Valor");
                                
        }catch(SQLException e){
            
            System.out.println("Erro na busca");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão na função de buscar produto");
            }
           
        }
        
        return valor;
    }
    /**
     * Método que gera um código novo para cada venda.
     * 
     * @return codigo um código para cadastrar uma nova venda
     */
    public int gerarCodigoVenda(){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        int codigo = 0;
        
        conexaoBanco.conectar();
        
        ResultSet resultset = null;
        
        Statement statement = null;
        
        String query = "SELECT MAX(Código_Venda)"
                +" FROM Vendas;";
		
        statement = conexaoBanco.criarStatement();
        try{
            resultset = statement.executeQuery(query);
            
            codigo = resultset.getInt("MAX(Código_Venda)");
            
        }catch(SQLException e){
            System.out.println("Erro na manipulação do banco no método gerarCódigoVenda");
        }finally{
            try{
            resultset.close();
            statement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Erro de desconexão no método gerarCódigoVenda");
            }
        }
        return codigo + 1;       
    }/**
     * Método que cadastra uma nova venda.
     * @param codigoVenda novo código da venda
     * @param tipo pode ser: "a vista", "misto" ou "parcelado"
     * @param Valor valor da compra feita pelo cliente
     * @param cpf cpf do cliente
     */
    public void cadastrarVenda(int codigoVenda, String tipo, Double valor, String cpf){
        Scanner sc = new Scanner(System.in);
        ConexaoBanco conexaoBanco = new ConexaoBanco();
                        
        conexaoBanco.conectar();
            
            String sqlInsert = " INSERT INTO Vendas ("
                + "Código_Venda,"
                + "Data,"
                + "Valor_total,"
                + "Tipo_Pagamento,"
                + "Numero_de_Parcelas,"
                + "CPF_Cliente"    
                +") VALUES(?,?,?,?,?,?)"
                +";";
        
       String dia, mes, ano, data = " ";
       System.out.println("--------------------------");
       System.out.println("Informe a data da compra");
       System.out.print("Informe o dia: ");
       dia = sc.nextLine();
       System.out.print("Informe o mês( 1 a 12 ): ");
       mes = sc.nextLine();
       System.out.print("Informe o ano: ");
       ano = sc.nextLine();
       System.out.println("--------------------------");
       data = ano+"-"+mes+"-"+dia;
       
       int nParcelas = 0;
       
       if (tipo.equals("a vista"))
           nParcelas = 1;
       if (tipo.equals("misto")){
           System.out.print("Informe o número de parcelas após o pagamento de 50% do valor: ");
           nParcelas = sc.nextInt();
       }
       if (tipo.equals("parcelado")){
           System.out.print("Informe o número de parcelas: ");
           nParcelas = sc.nextInt();
       }
       PreparedStatement preparaStatement = conexaoBanco.criarPreparedStatement(sqlInsert);
                            
               try{
                   preparaStatement.setInt(1, codigoVenda);
                   preparaStatement.setString(2, data );
                   preparaStatement.setDouble(3, valor );
                   preparaStatement.setString(4, tipo);
                   preparaStatement.setInt(5, nParcelas );
                   preparaStatement.setString(6, cpf );
                           
                   int resultado = preparaStatement.executeUpdate();
                   System.out.println("--------------------------");
                   if (resultado == 1)
                       System.out.println("Venda cadastrada !");
                   else
                       System.out.println("Venda não cadastrada");
                   System.out.println("--------------------------");
                }catch(SQLException e){
                    System.out.println("Venda não cadastrada, falha de inserção da venda");
                }finally{
                   if (preparaStatement != null)
                       try {
                           preparaStatement.close();
                   } catch (SQLException ex) {
                       System.out.println("Falha em fechar o prepared statement para");
                       System.out.println("a inserção de cadastros na tabela vendas");  
                   }
                   conexaoBanco.desconectar();
               }
    }
    /**
     * Método utilizado em vendas mistas ou parceladas, pois o cadastro é obrigatório
     * para compras mistas ou parceladas.
     * @param cpf
     * @return true cliente já está cadastrado, false cliente precisa ser cadastrado
     */
    public boolean verificaCadastroCliente(String cpf){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
        
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
            
        int presenca = 0;
        
        String query = " SELECT *"
                +" FROM Clientes"
                +" WHERE CPF = ?;";
             
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setString(1,cpf);
            resultset = preparaStatement.executeQuery();
            
            if (resultset.next() == true)//Já há cliente com essse cpf
                presenca = 1;
                           
        }catch(SQLException e){
            
            System.out.println("Erro na verificação se um cliente está ou não cadastrado");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão em verificação da parte se um cliente está ou não cadastrado");
            }
           
        } 
        if (presenca == 1)
            return true;
        else
            return false;
    }
    
    /**
     * Método que retorno o tipo (a vista, mista ou parcelada) de uma venda cadastrada.
     * @param códigoVenda código da venda
     */    
    public String retornaTipoVenda(int codigo){
        String tipo = " ";
        
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
               
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
             
        String query = " SELECT "
                +" Tipo_Pagamento "
                +" FROM Vendas"
                +" WHERE Código_Venda = ?;";
             
        try{
            System.out.println("-------------------------");
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setInt(1,codigo);
            resultset = preparaStatement.executeQuery();
            
            if (resultset.next() == false){
                System.out.println("Ops não há venda cadastrada com essse código");
                tipo = null;
            }
            else
                tipo = resultset.getString("Tipo_Pagamento");
            
        }catch(SQLException e){
            
            System.out.println("Erro na busca");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão");
            }
           
        }
        return tipo;
    }
    /**
     * Método que retorna data de uma venda.
     * @param codigo código da venda
     * @return 
     */
    public String retornaDataVenda(int codigo){
      String data = " ";
      
      ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
               
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
             
        String query = " SELECT "
                +" Data "
                +" FROM Vendas"
                +" WHERE Código_Venda = ?;";
             
        try{
            System.out.println("-------------------------");
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setInt(1,codigo);
            resultset = preparaStatement.executeQuery();
            
            if (resultset.next() == false){
                System.out.println("Ops não há venda cadastrada com essse código");
                data = null;
            }
            else
                data = resultset.getString("Data");
            
        }catch(SQLException e){
            
            System.out.println("Erro na busca");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão");
            }
           
        }
      
      return data;  
    }
    /**
     * Método que converte a string do padrão em data para o tipo date.
     * @param d a data em string
     * @return a data em Date
     * @throws ParseException 
     */
    public Date converteStringDate(String d) throws ParseException{
        String padrao = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(padrao);
        
        Date data = simpleDateFormat.parse(d);
              
        return data;
    }
    /**
     * Método que adiona itens de produtos ao estoque devido a um estorno.
     * @param codigo codigo do produto
     * @param qtd quantidade a ser devolvida
     */
    public void addEstoqueEstorno(int codigo, int qtd){
      ConexaoBanco conexaoBanco = new ConexaoBanco();
        conexaoBanco.conectar();
        
        PreparedStatement preparaStatement = null;
        
        String sql = " UPDATE Produtos"
                + " SET "
                + " Qtd_Estoque = ? "
                + " WHERE Código_Produto = ? ;";
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(sql);
                           
                preparaStatement.setInt(1, this.retornaEstoqueV(codigo) + qtd );
                preparaStatement.setInt(2, codigo );            
                preparaStatement.executeUpdate();                        
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Falha em desconectar da parte de adicionaEstoqueEstorno e em interfaceVendas");
            }  
        }
    }
    /**
     * Método que retorna o valor de uma venda.
     * @param codigoVenda código da venda realizada
     * @return valor o valor da venda.
     */
    public double retornaValorVenda(int codigoVenda){
        double valor = 0;
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
               
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
             
        String query = " SELECT "
                +" Valor_total "
                +" FROM Vendas"
                +" WHERE Código_Venda = ?;";
             
        try{
            System.out.println("-------------------------");
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setInt(1,codigoVenda);
            resultset = preparaStatement.executeQuery();
            
            if (resultset.next() == false){
                System.out.println("Ops não há venda cadastrada com essse código");
                valor = 0;
            }
            else
                valor = resultset.getDouble("Valor_total");
            
        }catch(SQLException e){
            
            System.out.println("Erro na busca, método retornaValorVenda");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão, método retornaValorVenda");
            }
           
        }
        return valor;
    }
    /**
     * Método que atualiza o valor total de uma venda após o estorno.
     * @param codigoVenda 
     */
    public void abaterValorEstornoVenda(int codigoVenda, double valor){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        conexaoBanco.conectar();
        
        PreparedStatement preparaStatement = null;
        
        String sql = " UPDATE Vendas"
                + " SET "
                + " Valor_total = ? "
                + " WHERE Código_Venda = ? ;";
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(sql);
                           
                preparaStatement.setDouble(1, this.retornaValorVenda(codigoVenda) - valor );
                preparaStatement.setInt(2, codigoVenda );            
                preparaStatement.executeUpdate();                        
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Falha em desconectar da parte de abaterValorEstornoVenda  em interfaceVendas");
            }  
        }
    }
    /**
     * Método que retorna o número de parcelas de uma venda cadastrada.
     * @param codigoVenda código da venda
     * @return número de parcelas da venda
     */
    public int retornaNumeroParcelas(int codigoVenda){
        int nParcelas = 0;
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
               
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
             
        String query = " SELECT "
                +" Numero_de_Parcelas "
                +" FROM Vendas"
                +" WHERE Código_Venda = ?;";
             
        try{
            System.out.println("-------------------------");
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setInt(1,codigoVenda);
            resultset = preparaStatement.executeQuery();
            
            if (resultset.next() == false){
                System.out.println("Ops não há venda cadastrada com essse código");
                nParcelas = 0;
            }
            else
                nParcelas = resultset.getInt("Numero_de_Parcelas");
            
        }catch(SQLException e){
            
            System.out.println("Erro na busca, método retornaNumeroParcelas");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão, método retornaNumeroParcelas");
            }
           
        }
        
        return nParcelas;
    }
}
