
package interfaces;

import conexões.ConexaoBanco;
import entidades.Cliente;
import entidades.Funcionario;
import entidades.Produto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import entidades.RG;
import entidades.Endereco;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonatas
 */
public class InterfaceUsuario extends InterfaceVendas implements InterfaceProdutos, InterfaceFuncionarios, InterfaceClientes  {
    
    public InterfaceUsuario(){
        
    }

    /**
     *Método que cadastra um produto caso ele seja novo.
     */
    @Override
    public void cadastrarProduto(){
        
        Scanner sc = new Scanner(System.in);
        ConexaoBanco conexaoBanco = new ConexaoBanco();
     
        Produto novo_produto = new Produto();
        
        System.out.println("Informe os dados do produto");
        System.out.print("Informe o código do produto: ");
        int codigo = sc.nextInt();
        novo_produto.setCodigo_Produto(codigo);
        Scanner sc1 = new Scanner(System.in);
        System.out.print("Informe o nome do produto: ");
        String nome_produto = sc1.nextLine();
        novo_produto.setNome_Produto(nome_produto);
        System.out.print("Informe o valor do produto: ");
        double v_produto = sc.nextDouble();
        novo_produto.setValor_Produto(v_produto);
        System.out.print("Informe quantidade adquirida do produto: ");
        int qtd = sc.nextInt();
        novo_produto.setQtd_Estoque_Produto(qtd);
        
        System.out.println("------------------------");
        boolean presente = this.verificaProduto(codigo);
       if(!presente){
        conexaoBanco.conectar();
            
            String sqlInsert = " INSERT INTO Produtos ("
                + "Código_Produto,"
                + "Nome,"
                + "Valor,"
                + "Qtd_Estoque"
                +") VALUES(?,?,?,?)"
                +";";
        
       PreparedStatement preparaStatement = conexaoBanco.criarPreparedStatement(sqlInsert);
            
               try{
                   preparaStatement.setInt(1, novo_produto.getCodigo_Produto());
                   preparaStatement.setString(2, novo_produto.getNome_Produto());
                   preparaStatement.setDouble(3, novo_produto.getValor_Produto());
                   preparaStatement.setInt(4, novo_produto.getQtd_Estoque_Produto() );
                           
                   int resultado = preparaStatement.executeUpdate();
                   
                   if (resultado == 1){
                       System.out.println("------------------------");
                       System.out.println("Produto inserido!");
                       System.out.println("------------------------");
                   }
                       
                   else{
                       System.out.println("------------------------");
                       System.out.println("Produto não inserido!");
                       System.out.println("------------------------");
                   }
                }catch(SQLException e){
                    System.out.println("Produto não inserido, falha de inserção do produto");
                }finally{
                   if (preparaStatement != null)
                       try {
                           preparaStatement.close();
                   } catch (SQLException ex) {
                       System.out.println("Falha em fechar o prepared statement para");
                       System.out.println("a inserção de registros na tabela produtos");  
                   }
                   conexaoBanco.desconectar();
               }
         } 
       else{
           System.out.println("----------------------------");
           System.out.println("Já há produto com esse código");
           System.out.println("Produto não cadastrado");
           System.out.println("----------------------------");
       }
        }
        
    /**
     *Exclui um produto cadastrado.
     */
    @Override
    public void excluirProduto(){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
        
        PreparedStatement preparaStatement = null;
        
        String sql = " DELETE FROM Produtos"
                + " WHERE Código_Produto = ? ;";
        
        System.out.println("------------------------------------------------------------");
        System.out.println("Atenção só é possível excluir informando o código do produto");
        
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(sql);
            System.out.print("Informe o código do produto: ");
            Scanner sc = new Scanner(System.in);
            preparaStatement.setInt(1,sc.nextInt());
            
            int linhasDeletadas = preparaStatement.executeUpdate();
            //Informa o usuário 
            
            System.out.println("Foram deletados " + linhasDeletadas + " produtos ");
            System.out.println("-------------------------------------------------");
            if (linhasDeletadas == 0){
                System.out.println("--------------------------------");
                System.out.println("Não há produto com esse código!!");
                System.out.println("--------------------------------");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Falha em desconectar da parte de deletar");
            }
        }
    }
    /**
     * Esse método permite que o usuário atualize o nome e o valor de um produto
     *  a partir de código do produto.
     */
    @Override
    public void atualizarProduto(){
               
        System.out.print("Informe o código do produto o qual você deseja alterá-lo: ");
        Scanner sc = new Scanner(System.in);
        int cdg = sc.nextInt();
        
        System.out.println("----------------------");
            
        System.out.println("Opções");
        System.out.println(" 1 - Alterar nome ");
        System.out.println(" 2 - Alterar valor ");
        System.out.print("Escolha: ");
        int escolha = sc.nextInt();
             // Apenas escolhendo 1 ou 2 é que há necessidade de conexão com o banco
             // por isso, o default não precisa de try catch
             // Cada switch case tem que ter sua propria conexao 
             // e seu proprio prepared statement
            switch (escolha){
                case 1:
                    ConexaoBanco conexaoBanco1 = new ConexaoBanco();
        
                    conexaoBanco1.conectar();
        
                    PreparedStatement preparaStatement1 = null;
                    String sqlNome = " UPDATE Produtos"
                            + " SET "
                            + " Nome = ? "
                            + " WHERE Código_Produto = ? ;";
                    try{
                        System.out.println("----------------------------");
                        preparaStatement1 = conexaoBanco1.criarPreparedStatement(sqlNome);
                        System.out.print("Informe o novo nome: "); 
                        Scanner sc1 = new Scanner(System.in);
                        preparaStatement1.setString(1, sc1.nextLine());
                        preparaStatement1.setInt(2, cdg );
                        preparaStatement1.executeUpdate();
                        System.out.println("Produto renomeado");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o nome");
                    } finally{
                        try{
                            preparaStatement1.close();
                            conexaoBanco1.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de renomear produto");
                         }
                     }                    
                    break;
                case 2:
                    ConexaoBanco conexaoBanco2 = new ConexaoBanco();
        
                    conexaoBanco2.conectar();
        
                    PreparedStatement preparaStatement2 = null;
                    String sqlValor = " UPDATE Produtos"
                            + " SET "
                            + " Valor = ? "
                            + " WHERE Código_Produto = ? ;";
                    try{
                        System.out.println("----------------------------");
                        preparaStatement2 = conexaoBanco2.criarPreparedStatement(sqlValor);
                        System.out.print("Informe o novo valor: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement2.setDouble(1, sc2.nextDouble());
                        preparaStatement2.setInt(2, cdg );
                        preparaStatement2.executeUpdate();
                        System.out.println("Produto atualizado com o novo valor");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o valor");
                    }finally{
                        try{
                            preparaStatement2.close();
                            conexaoBanco2.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de alterar o valor do produto");
                         }
                     }                    
                    break;
                default:
                    System.out.println("Escolha inválida");                   
            }                         
    }

    /**
     * Busca um produto pelo código do produto
     * caso não haja produto com código 
     * imprime mensagem avisando.
     */
    @Override
    public void buscarProduto(){
       ConexaoBanco conexaoBanco = new ConexaoBanco();
        boolean presente = false;
        conexaoBanco.conectar();
      
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
            
        String query = " SELECT *"
                +" FROM Produtos"
                +" WHERE Código_Produto = ?;";
             
        try{
            System.out.println("-------------------------");
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            System.out.print("Informe o código do produto: ");
            Scanner sc = new Scanner(System.in);
            int cdg = sc.nextInt();
            preparaStatement.setInt(1,cdg);
            resultset = preparaStatement.executeQuery();
            
            presente = this.verificaProduto(cdg);
            
            if (presente){
            while (resultset.next()){//Impressão dos dados do produto
                System.out.println("Produto selecionado");
                System.out.println("Código = " + resultset.getInt("Código_Produto"));
                System.out.println("Nome = " + resultset.getString("Nome"));
                System.out.println("Valor = " + resultset.getDouble("Valor"));
                System.out.println("Quantidade = " + resultset.getInt("Qtd_Estoque"));
                
            }
            }else{
                System.out.println("Falha na busca: não há produto com esse código");
            }
            System.out.println("-------------------------");
            
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
        
       
    }

    /**
     * Método que aumenta a quantidade de item de um produto específico no estoque
     * A escolha do produto é feita pelo código do produto
     * @param quantidade a quantidade a ser adicionado ao estoque
     */
    @Override
    public void addItemProduto(int quantidade){
        
        System.out.print("Informe o código do produto o qual você deseja adicionar itens dele no estoque: ");
        Scanner sc = new Scanner(System.in);
        int cdg = sc.nextInt();
        System.out.println("-------------------------------------------------------------------------------");
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        
        conexaoBanco.conectar();
        
        PreparedStatement preparaStatement = null;
        
        String sql = " UPDATE Produtos"
                + " SET "
                + " Qtd_Estoque = ? "
                + " WHERE Código_Produto = ? ;";
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(sql);
            preparaStatement.setInt(1, this.retornaEstoque(cdg) + quantidade );
            preparaStatement.setInt(2, cdg );            
            preparaStatement.executeUpdate();
            //Informa o usuário 
             System.out.println("-------------------------------");
            System.out.println("Estoque desse produto atualizado");
            System.out.println("-------------------------------");
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Falha em desconectar da parte de adição ao estoque");
            }
        }
    }

    /**
     *Método que diminui a quantidade de estoque de um produto específico
     * A escolha do produto é feita pelo código do produto
     * @param quantidade  quantidade a ser diminuida do estoque
     */
    @Override
    public void excluirItemProduto(int quantidade){
        System.out.print("Informe o código do produto o qual você deseja excluir itens dele no estoque: ");
        
        Scanner sc = new Scanner(System.in);
        int cdg = sc.nextInt();
        
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------------");
        conexaoBanco.conectar();
        
        PreparedStatement preparaStatement = null;
        
        String sql = " UPDATE Produtos"
                + " SET "
                + " Qtd_Estoque = ? "
                + " WHERE Código_Produto = ? ;";
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(sql);
            if ((this.retornaEstoque(cdg) - quantidade >= 0)){
                
                preparaStatement.setInt(1, this.retornaEstoque(cdg) - quantidade );
                preparaStatement.setInt(2, cdg );            
                preparaStatement.executeUpdate();
            //Informa o usuário 
                System.out.println("-------------------------------");
                System.out.println("Estoque desse produto atualizado");
                System.out.println("-------------------------------");
            }
            else{
                System.out.println("-------------------------------");
                System.out.println("Verifique a quantidade a retirar pois o estoque ficará negativo com essse valor!!!");                
                System.out.println("Estoque não atualizado");
                System.out.println("-------------------------------");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Falha em desconectar da parte de redução de estoque");
            }
        }
    }
    
    /**
     * Método que verifica se um produto já está cadastrado
     * @param codigoProduto 
     * @return true se há um produto cadastrado para esse código
     * false se não há ainda um produto com esse código
     */
    public boolean verificaProduto(int codigoProduto){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------");
        conexaoBanco.conectar();
        
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
            
        int presenca = 0;
        
        String query = " SELECT *"
                +" FROM Produtos"
                +" WHERE Código_Produto = ?;";
             
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setInt(1,codigoProduto);
            resultset = preparaStatement.executeQuery();
            
            if (resultset.next() == true)//Já há produto com essse código. Entao não pode cadastrar
                presenca = 1;
                           
        }catch(SQLException e){
            
            System.out.println("Erro na verificação de produto");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão em verificação de produto");
            }
           
        } 
        System.out.println("----------------------------");
        if (presenca == 1)
            return true;
        else
            return false;
    }
    /**
     * Método que retorna a quantidade em estoque de um produto
     * @param codigoProduto
     * @return estoque
     */    
    public int retornaEstoque(int codigoProduto){
        
        int estoque = 0;
        System.out.println("----------------------------");
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
            
            if (resultset.next() == false)
                System.out.println("Ainda não há produto com essse código");
            else
                estoque = resultset.getInt("Qtd_Estoque");
            System.out.println("----------------------------");
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
        System.out.println("----------------------------");
        return estoque;       
    }
    /**
     * Método que realiza e cadastra vendas.
     */
    @Override
    public void vender(){
        
        Cliente cliente = new Cliente();
              
        System.out.print("Informe o cpf do cliente: ");
        Scanner sc = new Scanner(System.in);
        String cpf_Cliente = sc.nextLine();
        
        Scanner sc1 = new Scanner(System.in);
        
        
        double valorCompra = 0;
        int contItem = 0;
        int quantidadeItem = 0;
                        
        do{            
            System.out.print("Informe o código de um produto selecionado pelo cliente: ");
            Integer produto = sc.nextInt();
            System.out.println("-------------------------------");
            
            if (super.verificaCadastroProduto(produto)){// Verificar se o código se refere a um produto cadastrado
                
                int estoque = super.retornaEstoqueV(produto);
                System.out.println("--------------------------------");
                System.out.print("Informe a quantidade do produto: ");                
                quantidadeItem = sc.nextInt();
                System.out.println("--------------------------------");
                if (estoque - quantidadeItem >= 0){// Verificar se é possível ou não abater do estoque.               
                
                cliente.addItemNoCarrinho(produto);
                  
                contItem = contItem + quantidadeItem;// abater do estoque do produto
                super.abaterEstoque(produto, quantidadeItem);
                
                valorCompra = valorCompra + super.valorProduto(produto)*quantidadeItem; // a cada iteração atualizar o valor da compra em um contador               
                }                                          
                else{
                    System.out.println("-------------------------");
                    System.out.println("Estoque indisponível");
                    System.out.println("-------------------------");
                }
            }    
            else{
                System.out.println("--------------------------------------");
                System.out.println("Erro na digitação do código do produto");
                System.out.println("--------------------------------------");
            }      
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.print("Há ainda outros itens de produtos a serem adicionados no carrinho de compras do cliente? (s/n): ");
        }while(sc1.nextLine().toLowerCase().equals("s"));
        System.out.println(" ");
        System.out.println("--------------------------------");
        System.out.println("Valor total da venda: " + valorCompra);
        System.out.println("--------------------------------");
        
                
        int codigoVenda = super.gerarCodigoVenda();// Determinar um código pra essa venda.
        boolean invalido = true;
        String tipoVenda = " ";
        
        boolean programaFidelidade = false;
        if (super.verificaCadastroCliente(cpf_Cliente))
                programaFidelidade = this.verificaClienteProgramaFidelidade(cpf_Cliente);
        
        while (invalido){
        System.out.println("-------------------------------");
        System.out.println("Selecione a forma de pagamento");
        System.out.println(" 1 - A VISTA ");
        System.out.println(" 2 - MISTO ");
        System.out.println(" 3 - PARCELADO ");        
        System.out.print("Escolha: ");
        
            switch (sc.nextInt()){// determinar forma de pagamento.
                                  // descontos por valor e quantidade apenas para pagamentos a vista se o cliente estiver no programa fidelidade.
                                  // programa fidelidade: desconto por quantidade de 5% se o cliente comprar a vista 5 ou mais itens de produtos.
                                  // programa fidelidade: desconto de 10% nas compras a vista com valor acima de 49.
                                  // programa fidelidade: Em pagamentos mistos e parcelados não há descontos, há apenas a opção de mais parcelas para os clientes que estão no programa. .
            
                case 1:
                    System.out.println("-------------------------");
                    invalido = false;
                    tipoVenda = "a vista";
                    if (programaFidelidade){
                        if (contItem >= 5){
                            if (valorCompra <= 49){
                                super.cadastrarVenda(codigoVenda, tipoVenda, valorCompra*0.95, cpf_Cliente);
                            }    
                            else
                                super.cadastrarVenda(codigoVenda, tipoVenda, valorCompra*0.95*0.90, cpf_Cliente);
                        }    
                        else
                            super.cadastrarVenda(codigoVenda, tipoVenda, valorCompra*0.90, cpf_Cliente);
                    }    
                    else
                        super.cadastrarVenda(codigoVenda, tipoVenda, valorCompra, cpf_Cliente);                                              
                    break;
                case 2:
                    System.out.println("-------------------------");
                    invalido = false;
                    tipoVenda = "misto";
                    boolean cadastrado = super.verificaCadastroCliente(cpf_Cliente);
                    if (!cadastrado){
                        System.out.println("--------------------------------");
                        System.out.println("O cliente precisa ser cadastrado");
                        this.cadastrarNovoCliente();
                    }
                    System.out.println("Nesta modalidade de venda o cliente tem que dar 50% do valor na primeira parcela");
                    if (programaFidelidade){//Se o cliente estiver no program fidelidade mais parcelas são permitidas.
                        System.out.println("-------------------------------------------------------------------------------------");
                        System.out.println("Esse cliente pode parcelar sua compra em até 4 vezes após pagar metade do valor total");
                        System.out.println("-------------------------------------------------------------------------------------");
                    }
                    else{
                        System.out.println("-------------------------------------------------------------------------------------");
                        System.out.println("Esse cliente pode parcelar sua compra em até 2 vezes após pagar metade do valor total");
                        System.out.println("-------------------------------------------------------------------------------------");
                    }
                    super.cadastrarVenda(codigoVenda, tipoVenda, valorCompra, cpf_Cliente);
                    break;
                case 3:
                    System.out.println("-------------------------");
                    invalido = false;
                    tipoVenda = "parcelado";
                    boolean registrado = super.verificaCadastroCliente(cpf_Cliente);
                    if (!registrado){
                        System.out.println("--------------------------------");
                        System.out.println("O cliente precisa ser cadastrado");
                        this.cadastrarNovoCliente();
                    }
                    
                    if (programaFidelidade){//Se o cliente estiver no program fidelidade mais parcelas são permitidas.
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Esse cliente pode parcelar sua compra em até 10 vezes");
                        System.out.println("-----------------------------------------------------");
                    }
                    else{
                        System.out.println("----------------------------------------------------");
                        System.out.println("Esse cliente pode parcelar sua compra em até 5 vezes");
                        System.out.println("----------------------------------------------------");
                    }
                    super.cadastrarVenda(codigoVenda, tipoVenda, valorCompra, cpf_Cliente);
                    break;
                default:
                System.out.println("Entrada inválida");                
            }          
        }
        cliente.resetCarrinho();
                
    }     
         
        

    /**
     * Método informa se é possível realiza o estorno (compra realizada até sete dias atrás), caso sim,
     * atualiza-se o estoque do referido produto e abate-se o valor do produto devolvido na venda
     * só é possível realizar o estorno de um produto de uma venda por vez.
     * 
     * @param codigoVenda código da venda a que se refere o estorno.
     * 
     */   
    @Override
    public void estorno(int codigoVenda){//Método em construção
        
        String dia,mes,ano,hoje = " ";
        Scanner sc = new Scanner(System.in);
        
        //Método que retorna o tipo da venda
        String tipoVenda = super.retornaTipoVenda(codigoVenda);
        //Método que retorna a data da venda
        String dataVenda = super.retornaDataVenda(codigoVenda);
        
        System.out.println("-------------------------");
        System.out.println("Informe a data de hoje");
        System.out.print("Informe o dia: ");
        dia = sc.nextLine();
        System.out.print("Informe o mês (1 a 12): ");
        mes = sc.nextLine();
        System.out.print("Informe o ano: ");
        ano = sc.nextLine();
        
        hoje = ano+"-"+mes+"-"+dia;
        
        
        try {       
            Date today = super.converteStringDate(hoje);
            Date diaVenda = super.converteStringDate(dataVenda); 
            if ((int)((today.getTime() - diaVenda.getTime())/86400000L) < 8){//Verificar data da venda
                
                int codigo_produto = 0;
                int qtd = 0;
                double devolucao = 0;
                System.out.println("Autorizado o estorno dessa venda");
                
                switch (tipoVenda){ //switch case para selecionar tipo de venda
                    case "a vista":
                        System.out.print("Informe o código do produto a ser devolvido: ");
                        codigo_produto = sc.nextInt();
                        System.out.print("Informe a quantidade do produto a ser devolvida: ");
                        qtd = sc.nextInt();
                        System.out.println("--------------------------------------------");
                        super.addEstoqueEstorno(codigo_produto, qtd);
                        System.out.println("--------------------------------------------");
                        devolucao = qtd*super.valorProduto(codigo_produto);
                        System.out.println("--------------------------------------------");
                        System.out.println("Valor a ser devolvido para o cliente: " + devolucao);
                        System.out.println("--------------------------------------------");
                        super.abaterValorEstornoVenda(codigoVenda, devolucao);
                        System.out.println("--------------------------------------------");
                        System.out.println("Estorno realizado com sucesso");
                        break;
                    case "misto":
                        System.out.print("Informe o código do produto a ser devolvido: ");
                        codigo_produto = sc.nextInt();
                        System.out.print("Informe a quantidade do produto a ser devolvida: ");
                        qtd = sc.nextInt();
                        System.out.println("--------------------------------------------");
                        super.addEstoqueEstorno(codigo_produto, qtd);
                        System.out.println("--------------------------------------------");
                        devolucao = ((qtd*super.valorProduto(codigo_produto))/2);
                        System.out.println("--------------------------------------------");
                        System.out.println("Valor a ser devolvido para o cliente: " + devolucao);
                        System.out.println("--------------------------------------------");
                        super.abaterValorEstornoVenda(codigoVenda,qtd*super.valorProduto(codigo_produto));
                        System.out.println("--------------------------------------------");
                        System.out.println("Estorno realizado com sucesso");
                        break;
                    case "parcelado":
                        System.out.print("Informe o código do produto a ser devolvido: ");
                        codigo_produto = sc.nextInt();
                        System.out.print("Informe a quantidade do produto a ser devolvida: ");
                        qtd = sc.nextInt();
                        System.out.println("--------------------------------------------");
                        super.addEstoqueEstorno(codigo_produto, qtd);
                        System.out.println("--------------------------------------------");
                        devolucao = ((qtd*super.valorProduto(codigo_produto))/(super.retornaNumeroParcelas(codigoVenda)));
                        System.out.println("--------------------------------------------");
                        System.out.println("Valor a ser devolvido para o cliente: " + devolucao);
                         System.out.println("--------------------------------------------");
                        super.abaterValorEstornoVenda(codigoVenda, qtd*super.valorProduto(codigo_produto));
                        System.out.println("--------------------------------------------");
                        System.out.println("Estorno realizado com sucesso");
                        break;
                    default:
                        System.out.println("Tipo de venda não reconhecido");
                }
            }
            else{
                System.out.println("Estorno não autorizado: venda realizada a mais de 7 dias ");               
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(InterfaceUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }      
                
    }
    
    /**
     * Método que imprime o contracheque de um funcionário.
     */
    @Override
    public void contracheque(){
       
        System.out.print("Informe o cpf do funcionario: ");
        Scanner sc = new Scanner(System.in);
        String cpf_funcionario = sc.nextLine();
        System.out.println("----------------------------");
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------");
        conexaoBanco.conectar();
        
        ResultSet resultset = null;
       
        PreparedStatement preparaStatement = null;
        
        
        
        String query = "SELECT * "
                +" FROM Funcionarios"
                +" WHERE CPF = ?;";
        
                
        try{
            Funcionario funcionario = new Funcionario();
                    
            funcionario.setCpf(cpf_funcionario);
            
            RG rg = new RG();
            
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setString(1, cpf_funcionario );
            resultset = preparaStatement.executeQuery();
            
            boolean cadastrado = false;
            while (resultset.next()){
                cadastrado = true;
                funcionario.setNome(resultset.getString("Nome"));
                rg.setNumeroRG(resultset.getString("RG")); 
                funcionario.setRg(rg);
            }
            if (cadastrado){
                System.out.print("Informe a carga horaria mensal trabalhada: ");
                funcionario.gerarContracheque(sc.nextInt());
            }
            else
                System.out.println("Funcionário ainda não está cadastrado");
            System.out.println("----------------------------");    
        }catch(SQLException e){
            // Só pra ver se aconteceu algum erro
            System.out.println("Erro de conexão ao banco em gerar contracheque");
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na deesconexão em gerar contracheque");
            }
           
        }
        System.out.println("----------------------------");
        
    }
    
    /**
     * Método que cadastra um cliente.
     */
    @Override
    public void cadastrarNovoCliente(){
        Scanner sc = new Scanner(System.in);
        ConexaoBanco conexaoBanco = new ConexaoBanco();
     
        Cliente cliente = new Cliente();
        
        System.out.println("Informe os dados do cliente");
        
        System.out.print("Informe o CPF do cliente: ");
        String cpf = sc.nextLine();
        cliente.setCpf(cpf);
        
        Scanner sc1 = new Scanner(System.in);        
        System.out.print("Informe o nome do cliente: ");
        String nome_cliente = sc1.nextLine();
        cliente.setNome(nome_cliente);
        
        RG rg = new RG();
        System.out.print("Informe o RG do cliente: ");
        String ident = sc.nextLine();
        rg.setNumeroRG(ident);
        cliente.setRg(rg);
        
        Endereco endereco = new Endereco();
        System.out.print("Informe o endereço do cliente: ");
        endereco.setEndereco(sc.nextLine());
        cliente.setEndereco(endereco);
        
        System.out.print("Informe o telefone do cliente: ");
        cliente.setTelefone(sc.nextLine());
        System.out.println("----------------------------");
        conexaoBanco.conectar();
            
            String sqlInsert = " INSERT INTO Clientes ("
                + "CPF,"
                + "Nome,"
                + "RG,"
                + "Endereço,"
                + "Telefone,"
                + "Programa_Fidelidade"
                + ") VALUES(?,?,?,?,?,?)"    
                +";";
        
       PreparedStatement preparaStatement = conexaoBanco.criarPreparedStatement(sqlInsert);
            
               try{
                   preparaStatement.setString(1, cliente.getCpf());
                   preparaStatement.setString(2, cliente.getNome());
                   preparaStatement.setString(3, rg.getNumeroRG());
                   preparaStatement.setString(4, endereco.getEndereco() );
                   preparaStatement.setString(5, cliente.getTelefone() );
                   preparaStatement.setString(6, "n" );
                           
                   int resultado = preparaStatement.executeUpdate();
                   
                   if (resultado == 1){
                       System.out.println("-------------------------");
                       System.out.println("Cliente inserido!");
                       System.out.println("-------------------------");
                   }
                   else{
                       System.out.println("-------------------------");
                       System.out.println("Cliente não inserido!");
                       System.out.println("-------------------------");
                   }
        
                }catch(SQLException e){
                    System.out.println("Cliente não inserido, falha de inserção do cliente");
                }finally{
                   if (preparaStatement != null)
                       try {
                           preparaStatement.close();
                   } catch (SQLException ex) {
                       System.out.println("Falha em fechar o prepared statement para");
                       System.out.println("a inserção de registros na tabela clientes");  
                   }
                   conexaoBanco.desconectar();
               }
               System.out.println("----------------------------");
         } 
    
    /**
     *Esse método busca um registro de um cliente a partir do CPF do cliente.
     */
    @Override
    public void buscarCliente(){
     
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------");
        conexaoBanco.conectar();
        System.out.println("----------------------------");
        ResultSet resultset = null;
       
        PreparedStatement preparaStatement = null;
        
        String query = "SELECT * "
                +" FROM Clientes"
                +" WHERE CPF = ?;";
        
        boolean presente = false;
               
        try{
            
            System.out.print("Informe o CPF do cliente: ");
            Scanner sc = new Scanner(System.in);
            String cpf = sc.nextLine();
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setString(1, cpf);            
            resultset = preparaStatement.executeQuery();
            
            while (resultset.next()){
                presente = true;
                System.out.println("---------------------------");
                System.out.println("Cliente selecionado");
                System.out.println("CPF = " + resultset.getString("CPF"));
                System.out.println("Nome = " + resultset.getString("Nome"));
                System.out.println("RG = " + resultset.getString("RG"));
                System.out.println("Endereço = " + resultset.getString("Endereço"));
                System.out.println("Telefone = " + resultset.getString("Telefone"));
                if (resultset.getString("Programa_Fidelidade").toLowerCase().equals("s"))
                    System.out.println("Cliente está no programa fidelidade");
                if (resultset.getString("Programa_Fidelidade").toLowerCase().equals("n"))
                    System.out.println("Cliente não está no programa fidelidade");
                System.out.println("---------------------------");
            }
            if (!presente){
                System.out.println("Não há um cliente cadastrado com esse CPF");
                System.out.println("-----------------------------------------");
            }
        }catch(SQLException e){
            // Só pra ver se aconteceu algum erro
            System.out.println("Erro no acesso ao para a pesquisa por um cliente");
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na deesconexão na parte de pesquisa por um clinete");
            }
           
        }
        System.out.println("----------------------------");
    }
    
    /**
     * Esse método modifica o cpf, nome, rg, endereço e telefone cadastrado do cliente buscando pelo CPF
     * do cliente.
     */
    @Override
    public void atualizarDadosCliente(){// Método em construção
                       
        System.out.print("Informe o CPF do cliente o qual você deseja alterar algum dado: ");
        Scanner sc = new Scanner(System.in);
        String cpf = sc.nextLine();
        
        System.out.println("----------------------");
            
        System.out.println("Opções");
        System.out.println(" 1 - Alterar o CPF");
        System.out.println(" 2 - Alterar nome ");
        System.out.println(" 3 - Alterar RG ");
        System.out.println(" 4 - Alterar o endereço");
        System.out.println(" 5 - Alterar o telefone");
        
        System.out.print("Escolha: ");
        int escolha = sc.nextInt();
             // Apenas escolhendo um dos case é que há necessidade de conexão com o banco
             // por isso, o default não precisa de try catch
             // Cada switch case tem que ter sua propria conexao 
             // e seu proprio prepared statement
            switch (escolha){
                case 1:
                    System.out.println("-------------------------");
                    ConexaoBanco conexaoBanco1 = new ConexaoBanco();
        
                    conexaoBanco1.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement1 = null;
                    String sqlNome = " UPDATE Clientes"
                            + " SET "
                            + " CPF = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement1 = conexaoBanco1.criarPreparedStatement(sqlNome);
                        System.out.print("Informe o novo CPF: "); 
                        Scanner sc1 = new Scanner(System.in);
                        preparaStatement1.setString(1, sc1.nextLine());
                        preparaStatement1.setString(2,cpf);
                        preparaStatement1.executeUpdate();
                        System.out.println("CPF corrigido");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em em corrigir o CPF do cliente");
                    } finally{
                        try{
                            preparaStatement1.close();
                            conexaoBanco1.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de corrigir o CPF do cliente");
                         }
                     }
                     System.out.println("----------------------------");
                    break;
                case 2:
                    System.out.println("-------------------------");
                    ConexaoBanco conexaoBanco2 = new ConexaoBanco();
        
                    conexaoBanco2.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement2 = null;
                    String sqlValor = " UPDATE Clientes"
                            + " SET "
                            + " Nome = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement2 = conexaoBanco2.criarPreparedStatement(sqlValor);
                        System.out.print("Informe o novo nome: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement2.setString(1, sc2.nextLine());
                        preparaStatement2.setString(2, cpf );
                        preparaStatement2.executeUpdate();
                        System.out.println("Clinete atualizado com o novo nome");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o nome do cliente");
                    }finally{
                        try{
                            preparaStatement2.close();
                            conexaoBanco2.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de atualizar o nome do cliente");
                         }
                     }   
                    System.out.println("----------------------------");
                    break;
                case 3:
                    System.out.println("-------------------------");
                    ConexaoBanco conexaoBanco3 = new ConexaoBanco();
        
                    conexaoBanco3.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement3 = null;
                    String sqlRg = " UPDATE Clientes"
                            + " SET "
                            + " RG = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement3 = conexaoBanco3.criarPreparedStatement(sqlRg);
                        System.out.print("Informe o novo RG: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement3.setString(1, sc2.nextLine());
                        preparaStatement3.setString(2, cpf );
                        preparaStatement3.executeUpdate();
                        System.out.println("Clinete atualizado com o novo RG");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o RG do cliente");
                    }finally{
                        try{
                            preparaStatement3.close();
                            conexaoBanco3.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de atualizar o RG do cliente");
                         }
                     }   
                    System.out.println("----------------------------");
                    break;
                case 4:
                    System.out.println("-------------------------");
                    ConexaoBanco conexaoBanco4 = new ConexaoBanco();
        
                    conexaoBanco4.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement4 = null;
                    String sqlEndereco = " UPDATE Clientes"
                            + " SET "
                            + " Endereço = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement4 = conexaoBanco4.criarPreparedStatement(sqlEndereco);
                        System.out.print("Informe o novo endereço: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement4.setString(1, sc2.nextLine());
                        preparaStatement4.setString(2, cpf );
                        preparaStatement4.executeUpdate();
                        System.out.println("Clinete atualizado com o novo endereço");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o endereço do cliente");
                    }finally{
                        try{
                            preparaStatement4.close();
                            conexaoBanco4.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de atualizar o endereço do cliente");
                         }
                     } 
                    System.out.println("----------------------------");
                break;
                case 5:
                    System.out.println("-------------------------");
                    ConexaoBanco conexaoBanco5 = new ConexaoBanco();
        
                    conexaoBanco5.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement5 = null;
                    String sqlTel = " UPDATE Clientes"
                            + " SET "
                            + " Telefone = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement5 = conexaoBanco5.criarPreparedStatement(sqlTel);
                        System.out.print("Informe o novo telefone: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement5.setString(1, sc2.nextLine());
                        preparaStatement5.setString(2, cpf );
                        preparaStatement5.executeUpdate();
                        System.out.println("Clinete atualizado com o novo telefone");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o telefone do cliente");
                    }finally{
                        try{
                            preparaStatement5.close();
                            conexaoBanco5.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de atualizar o telefone do cliente");
                         }
                     }  
                    System.out.println("----------------------------");
                break;
                    
                default:
                    System.out.println("Escolha inválida");                   
            }                         
    }
    
    /**
     *Método que eclui um cliente cadastrado a partir do CPF.
     */
    @Override
    public void excluirCliente(){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------");
        conexaoBanco.conectar();
        
        PreparedStatement preparaStatement = null;
        
        String sql = " DELETE FROM Clientes"
                + " WHERE CPF = ? ;";
        
        System.out.println("----------------------------");
        System.out.println("Atenção só é possível excluir um cliente informando o CPF dele.");
        
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(sql);
            System.out.print("Informe o CPF do cliente: ");
            Scanner sc = new Scanner(System.in);
            preparaStatement.setString(1,sc.nextLine());
            System.out.println("------------------------");
            
            int linhasDeletadas = preparaStatement.executeUpdate();
            //Informa o usuário 
            System.out.println("Foram deletados " + linhasDeletadas + " clientes ");
            if (linhasDeletadas == 0)
                System.out.println("Não há cliente cadastrado com esse CPF!!");
            System.out.println("------------------------");
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Falha em desconectar da parte de deletar um cliente");
            }
        }
        System.out.println("----------------------------");
    }
    
    /**
     *Método que retorna a lista com cpf e nome de todos os clientes do programa fidelidade.
     */
    @Override
    public void listaProgramaFidelidade(){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------");
        conexaoBanco.conectar();
        System.out.println("----------------------------");
        ResultSet resultset = null;
       
        PreparedStatement preparaStatement = null;
        
        String query = "SELECT * "
                +" FROM Clientes"
                +" WHERE Programa_Fidelidade = ?;";
        
        boolean presente = false; // Indica a presença ou não de clientes no programa fidelidade
               
        try{
                        
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setString(1, "s");            
            resultset = preparaStatement.executeQuery();
            
            while (resultset.next()){
                presente = true;
                System.out.println("---------------------------");
                System.out.println("Este cliente selecionado está no programa fidelidade");
                System.out.println("CPF = " + resultset.getString("CPF"));
                System.out.println("Nome = " + resultset.getString("Nome"));
                System.out.println("---------------------------");
            }
            if (!presente){
                System.out.println("---------------------------");
                System.out.println("Não há um cliente cadastrado no programa fidelidade");
                System.out.println("---------------------------");
            }
        }catch(SQLException e){
            // Só pra ver se aconteceu algum erro
            System.out.println("Erro no acesso para gerar a lista com os clientes do programa fidelidade");
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão na parte de gerar a lista com os clientes do programa fidelidade");
            }
           
        }
        System.out.println("----------------------------");
    }
    
    /**
     *Método que cadastra novos funcionários.
     */
    @Override
    public void cadastrarFuncionario(){
        Scanner sc = new Scanner(System.in);
        ConexaoBanco conexaoBanco = new ConexaoBanco();
     
        Funcionario funcionario =  new Funcionario();
        
        System.out.println("Informe os dados do novo funcionário");
        
        System.out.print("Informe o CPF do funcionário: ");
        String cpf = sc.nextLine();
        funcionario.setCpf(cpf);
        
        Scanner sc1 = new Scanner(System.in);        
        System.out.print("Informe o nome do funcionário: ");
        String nome_funcionario = sc1.nextLine();
        funcionario.setNome(nome_funcionario);
        
        RG rg = new RG();
        System.out.print("Informe o RG do funcionário: ");
        String ident = sc.nextLine();
        rg.setNumeroRG(ident);
        funcionario.setRg(rg);
        
        Endereco endereco = new Endereco();
        System.out.print("Informe o endereço do funcionário: ");
        endereco.setEndereco(sc.nextLine());
        funcionario.setEndereco(endereco);
        
        System.out.print("Informe o telefone do funcionário: ");
        funcionario.setTelefone(sc.nextLine());
        System.out.println("----------------------------");
        conexaoBanco.conectar();
            
            String sqlInsert = " INSERT INTO Funcionarios ("
                + "CPF,"
                + "Nome,"
                + "RG,"
                + "Endereço,"
                + "Telefone "
                + ") VALUES(?,?,?,?,?)"    
                +";";
        
       PreparedStatement preparaStatement = conexaoBanco.criarPreparedStatement(sqlInsert);
            
               try{
                   preparaStatement.setString(1, funcionario.getCpf());
                   preparaStatement.setString(2, funcionario.getNome());
                   preparaStatement.setString(3, rg.getNumeroRG());
                   preparaStatement.setString(4, endereco.getEndereco() );
                   preparaStatement.setString(5, funcionario.getTelefone() );
                   
                           
                   int resultado = preparaStatement.executeUpdate();
                   System.out.println("----------------------------");
                   if (resultado == 1)
                       System.out.println("Funcionário cadastrado!");
                   else
                       System.out.println("Funcionário não inserido!");
                   System.out.println("----------------------------");
                }catch(SQLException e){
                    System.out.println("Funcionário não cadstrado, falha de inserção do funcionário");
                }finally{
                   if (preparaStatement != null)
                       try {
                           preparaStatement.close();
                   } catch (SQLException ex) {
                       System.out.println("Falha em fechar o prepared statement para");
                       System.out.println("a inserção de registros na tabela funcionários");  
                   }
                   conexaoBanco.desconectar();
               }
               System.out.println("----------------------------");
    }

    /**
     *Método que busca um funcionário a aprtir do CPF dele.
     */
    @Override
    public void buscarFuncionario(){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------");
        conexaoBanco.conectar();
        System.out.println("----------------------------");
        ResultSet resultset = null;
       
        PreparedStatement preparaStatement = null;
        
        String query = "SELECT * "
                +" FROM Funcionarios"
                +" WHERE CPF = ?;";
        
        boolean presente = false;
               
        try{
            
            System.out.print("Informe o CPF do funcionário: ");
            Scanner sc = new Scanner(System.in);
            String cpf = sc.nextLine();
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setString(1, cpf);            
            resultset = preparaStatement.executeQuery();
                       
            while (resultset.next()){
                presente = true;
                System.out.println("---------------------------");
                System.out.println("Funcionário selecionado");
                System.out.println("CPF = " + resultset.getString("CPF"));
                System.out.println("Nome = " + resultset.getString("Nome"));
                System.out.println("RG = " + resultset.getString("RG"));
                System.out.println("Endereço = " + resultset.getString("Endereço"));
                System.out.println("Telefone = " + resultset.getString("Telefone"));
                System.out.println("---------------------------");
            }
            if (!presente){
                System.out.println("Não há um funcionário cadastrado com esse CPF");
                System.out.println("---------------------------------------------");
            }
        }catch(SQLException e){
            // Só pra ver se aconteceu algum erro
            System.out.println("Erro no acesso durante a busca de um funcionário");
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão na parte de busca por um funcionário");
            }
           
        }
        System.out.println("----------------------------");
    }
    
    /**
     * Esse método modifica o cpf, nome, rg, endereço e telefone cadastrado do funcionário buscando pelo CPF
     * do funcionário.
     */
    @Override
    public void atualizarDadosFuncionario(){
        
        System.out.print("Informe o CPF do funcionário o qual você deseja alterar algum dado: ");
        Scanner sc = new Scanner(System.in);
        String cpf = sc.nextLine();
        
        System.out.println("----------------------");
            
        System.out.println("Opções");
        System.out.println(" 1 - Alterar o CPF");
        System.out.println(" 2 - Alterar nome ");
        System.out.println(" 3 - Alterar RG ");
        System.out.println(" 4 - Alterar o endereço");
        System.out.println(" 5 - Alterar o telefone");
        
        System.out.print("Escolha: ");
        int escolha = sc.nextInt();
             // Apenas escolhendo um dos case é que há necessidade de conexão com o banco
             // por isso, o default não precisa de try catch
             // Cada switch case tem que ter sua propria conexao 
             // e seu proprio prepared statement
            switch (escolha){
                case 1:
                    ConexaoBanco conexaoBanco1 = new ConexaoBanco();
                    System.out.println("----------------------------");
                    conexaoBanco1.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement1 = null;
                    String sqlNome = " UPDATE Funcionarios"
                            + " SET "
                            + " CPF = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement1 = conexaoBanco1.criarPreparedStatement(sqlNome);
                        System.out.print("Informe o novo CPF: "); 
                        Scanner sc1 = new Scanner(System.in);
                        preparaStatement1.setString(1, sc1.nextLine());
                        preparaStatement1.setString(2,cpf);
                        preparaStatement1.executeUpdate();
                        System.out.println("CPF corrigido");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em em corrigir o CPF do funcionario");
                    } finally{
                        try{
                            preparaStatement1.close();
                            conexaoBanco1.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de corrigir o CPF do funcionário");
                         }
                     } 
                    System.out.println("----------------------------");
                    break;
                case 2:
                    ConexaoBanco conexaoBanco2 = new ConexaoBanco();
                    System.out.println("----------------------------");
                    conexaoBanco2.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement2 = null;
                    String sqlValor = " UPDATE Funcionarios"
                            + " SET "
                            + " Nome = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement2 = conexaoBanco2.criarPreparedStatement(sqlValor);
                        System.out.print("Informe o novo nome: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement2.setString(1, sc2.nextLine());
                        preparaStatement2.setString(2, cpf );
                        preparaStatement2.executeUpdate();
                        System.out.println("Funcionário atualizado com o novo nome");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o nome do funcionário");
                    }finally{
                        try{
                            preparaStatement2.close();
                            conexaoBanco2.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de atualizar o nome do funcionário");
                         }
                     }  
                    System.out.println("----------------------------");
                    break;
                case 3:
                    ConexaoBanco conexaoBanco3 = new ConexaoBanco();
                    System.out.println("----------------------------");
                    conexaoBanco3.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement3 = null;
                    String sqlRg = " UPDATE Funcionarios"
                            + " SET "
                            + " RG = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement3 = conexaoBanco3.criarPreparedStatement(sqlRg);
                        System.out.print("Informe o novo RG: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement3.setString(1, sc2.nextLine());
                        preparaStatement3.setString(2, cpf );
                        preparaStatement3.executeUpdate();
                        System.out.println("Funcionário atualizado com o novo RG");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o RG do funcionário");
                    }finally{
                        try{
                            preparaStatement3.close();
                            conexaoBanco3.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de atualizar o RG do funcionário");
                         }
                     }  
                    System.out.println("----------------------------");
                    break;
                case 4:
                    ConexaoBanco conexaoBanco4 = new ConexaoBanco();
                    System.out.println("----------------------------");
                    conexaoBanco4.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement4 = null;
                    String sqlEndereco = " UPDATE Funcionarios"
                            + " SET "
                            + " Endereço = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement4 = conexaoBanco4.criarPreparedStatement(sqlEndereco);
                        System.out.print("Informe o novo endereço: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement4.setString(1, sc2.nextLine());
                        preparaStatement4.setString(2, cpf );
                        preparaStatement4.executeUpdate();
                        System.out.println("Funcionário atualizado com o novo endereço");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o endereço do funcionário");
                    }finally{
                        try{
                            preparaStatement4.close();
                            conexaoBanco4.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de atualizar o endereço do funcionário");
                         }
                     }    
                    System.out.println("----------------------------");
                break;
                case 5:
                    ConexaoBanco conexaoBanco5 = new ConexaoBanco();
                    System.out.println("----------------------------");
                    conexaoBanco5.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement5 = null;
                    String sqlTel = " UPDATE Funcionarios"
                            + " SET "
                            + " Telefone = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement5 = conexaoBanco5.criarPreparedStatement(sqlTel);
                        System.out.print("Informe o novo telefone: ");
                        Scanner sc2 = new Scanner(System.in);
                        preparaStatement5.setString(1, sc2.nextLine());
                        preparaStatement5.setString(2, cpf );
                        preparaStatement5.executeUpdate();
                        System.out.println("Funcionário atualizado com o novo telefone");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em atualizar o telefone do funcionário");
                    }finally{
                        try{
                            preparaStatement5.close();
                            conexaoBanco5.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de atualizar o telefone do funcionário");
                         }
                     }  
                    System.out.println("----------------------------");
                break;
                    
                default:
                    System.out.println("Escolha inválida");                   
            }
    }
    
    /**
     *Método que eclui um funcionário cadastrado a partir do CPF.
     */
    @Override
    public void excluirFuncionario(){
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------");
        conexaoBanco.conectar();
        System.out.println("----------------------------");
        PreparedStatement preparaStatement = null;
        
        String sql = " DELETE FROM Funcionarios"
                + " WHERE CPF = ? ;";
        
        System.out.println("Atenção só é possível excluir um funcionário informando o CPF dele.");
        
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(sql);
            System.out.print("Informe o CPF do funcionário: ");
            Scanner sc = new Scanner(System.in);
            preparaStatement.setString(1,sc.nextLine());
            System.out.println("------------------------");
            
            int linhasDeletadas = preparaStatement.executeUpdate();
            //Informa o usuário 
            System.out.println("Foram deletados " + linhasDeletadas + " funcionários ");
            if (linhasDeletadas == 0)
                System.out.println("Não há funcionário cadastrado com esse CPF!!");
            System.out.println("------------------------");
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch(SQLException ex){
                System.out.println("Falha em desconectar da parte de deletar um funcionário");
            }
        }
        System.out.println("----------------------------");
    }

    /**
     *Método que insere um cliente no programa fidelidade
     * Se o cliente já estiver inserido não acontece nada.
     */
    @Override
    public void inserirClienteProgramaFidelidade(){
        System.out.print("Informe o CPF do cliente: ");
        Scanner sc = new Scanner(System.in);
        String cpf = sc.nextLine();
        
                             
                    ConexaoBanco conexaoBanco1 = new ConexaoBanco();
                    System.out.println("----------------------------");
                    conexaoBanco1.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement1 = null;
                    String sqlNome = " UPDATE Clientes"
                            + " SET "
                            + " Programa_Fidelidade = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement1 = conexaoBanco1.criarPreparedStatement(sqlNome);
                        Scanner sc1 = new Scanner(System.in);
                        preparaStatement1.setString(1, "s");
                        preparaStatement1.setString(2,cpf);
                        preparaStatement1.executeUpdate();
                        System.out.println("Cliente inserido no programa fidelidade");
                        System.out.println("----------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em inserir o cliente no programa fidelidade");
                    } finally{
                        try{
                            preparaStatement1.close();
                            conexaoBanco1.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da inserir cliente no programa fidelidade");
                         }
                     }  
                    System.out.println("----------------------------");
                   
        }
    /**
     *Método que retira um cliente do programa fidelidade
     * Se o cliente não estiver inserido não acontece nada.
     */
    @Override
    public void retirarClienteProgramaFidelidade(){
        
        System.out.print("Informe o CPF do cliente: ");
        Scanner sc = new Scanner(System.in);
        String cpf = sc.nextLine();
        
                             
                    ConexaoBanco conexaoBanco1 = new ConexaoBanco();
                    System.out.println("----------------------------");
                    conexaoBanco1.conectar();
                    System.out.println("----------------------------");
                    PreparedStatement preparaStatement1 = null;
                    String sqlNome = " UPDATE Clientes"
                            + " SET "
                            + " Programa_Fidelidade = ? "
                            + " WHERE CPF = ? ;";
                    try{
                        preparaStatement1 = conexaoBanco1.criarPreparedStatement(sqlNome);
                        Scanner sc1 = new Scanner(System.in);
                        preparaStatement1.setString(1, "n");
                        preparaStatement1.setString(2,cpf);
                        preparaStatement1.executeUpdate();
                        System.out.println("Cliente retirado do programa fidelidade");
                        System.out.println("---------------------------------------");
                    }catch(SQLException e){
                        System.out.println("Falha em retirar o cliente do programa fidelidade");
                    } finally{
                        try{
                            preparaStatement1.close();
                            conexaoBanco1.desconectar();
                        }catch(SQLException ex){
                             System.out.println("Falha em desconectar da parte de inserir cliente no programa fidelidade");
                         }
                     }
                    System.out.println("----------------------------");
    }

    /**
     * Método que verifica se um cliente está no programa fidelidade
     * @param cpf cpf do cliente
     * @return true está no programa fidelidade, false não está
     */
    public boolean verificaClienteProgramaFidelidade(String cpf){
        
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        System.out.println("----------------------------");
        conexaoBanco.conectar();
        System.out.println("----------------------------");
        ResultSet resultset = null;
        
        PreparedStatement preparaStatement = null;
            
        int presenca = 0;
        
        String query = " SELECT Programa_Fidelidade"
                +" FROM Clientes"
                +" WHERE CPF = ?;";
             
        try{
            preparaStatement = conexaoBanco.criarPreparedStatement(query);
            preparaStatement.setString(1,cpf);
            resultset = preparaStatement.executeQuery();
            
            if (resultset.getString("Programa_Fidelidade").equals("s"))
                presenca = 1;
                           
        }catch(SQLException e){
            
            System.out.println("Erro na verificação se um cliente está ou não cadastrado no programa fidelidade");
            
        }finally{
            try{
            resultset.close();
            preparaStatement.close();
            conexaoBanco.desconectar();
            }catch (SQLException ex){
                System.out.println("Falha na desconexão em verificação da parte se um cliente está ou não cadastrado no programa fidelidade");
            }
           
        } 
        System.out.println("----------------------------");
        if (presenca == 1)
            return true;
        else
            return false;
        
    }
    
}
     
    
    



