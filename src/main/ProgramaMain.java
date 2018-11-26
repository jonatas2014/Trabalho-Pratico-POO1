
package main;


import interfaces.InterfaceUsuario;
import java.util.Scanner;
/**
 *
 * @author jonatas
 */
public class ProgramaMain {
                
    public static void main(String[] args)  {
        Scanner sc = new Scanner(System.in);
        InterfaceUsuario interfaceU = new InterfaceUsuario();
	
        while (true){
            System.out.println("Bem vindo ao sistema de gerenciamento da loja de departamento");
            System.out.println("-------------------------------------------------------------");
            System.out.println("Escolha uma das opções abaixo:");
            System.out.println(" 1 - Gestão de produtos e itens de produtos");
            System.out.println(" 2 - Gestão de vendas");
            System.out.println(" 3 - Gestão de clientes");
            System.out.println(" 4 - Gestão de funcionários");
            System.out.print("Escolha: ");
        
             int escolha = sc.nextInt();
        
            switch (escolha){// swictch case do menu principal
            
                case 1:
                    while (true){
                    System.out.println("Gestão de Produtos e itens");
                    System.out.println("Escolha uma das opções abaixo:");
                    System.out.println(" 1 - Adicionar um cadastro de produto ");
                    System.out.println(" 2 - Excluir um cadastro de produto");
                    System.out.println(" 3 - Alterar um cadatro de produto");
                    System.out.println(" 4 - Buscar um produto");                
                    System.out.println(" 5 - Adicionar um item");                    
                    System.out.println(" 6 - Excluir um item");                                  
                    System.out.print("Escolha: ");
                
                    int escolha1 = sc.nextInt();
                
                    switch (escolha1){// switch case de gestão de produtos
                        case 1:
                            System.out.println("------------------------");
                            interfaceU.cadastrarProduto();
                            break;
                        case 2:
                            System.out.println("------------------------");
                            interfaceU.excluirProduto();
                            break;  
                        case 3:
                             System.out.println("------------------------");
                             interfaceU.atualizarProduto();                                                                  
                            break;
                        case 4:
                            System.out.println("------------------------");
                            interfaceU.buscarProduto();                                    
                            break;                                      
                        case 5:
                            System.out.println("------------------------");
                            System.out.print("Informe a quantidade a ser adicionada: ");                            
                            interfaceU.addItemProduto(sc.nextInt());                        
                            break;   
                        case 6:
                            System.out.println("------------------------");
                            System.out.print("Informe a quantidade a ser retirada: ");
                            interfaceU.excluirItemProduto(sc.nextInt());
                            break;
                        default:   
                            System.out.println("Escolha inválida");
                    }
                        System.out.print(":: Quer continuar a gerenciar os produtos? (s/n) ::");
                                    if (sc.next().toLowerCase().equals("n"))
                                        break; 
                                    
                    }
                    
            
                
                    break;
                case 2:
                    while(true){
                        System.out.println("Gestão de vendas");
                        System.out.println("-------------------------");
                        System.out.println("Escolha uma das opções abaixo:");
                        System.out.println(" 1 - Realizar uma venda ");
                        System.out.println(" 2 - Estorno de venda ");
                        System.out.print("Escolha: ");
                        
                        switch (sc.nextInt()){// switch case de gestão de vendas
                            case 1:
                                interfaceU.vender();
                                break;
                            case 2:
                                System.out.print("Informe o código da venda: ");
                                interfaceU.estorno(sc.nextInt());
                                break;
                                
                            default: 
                                System.out.println("Escolha inválida");
                        }                        
                        System.out.print(":: Quer continuar a gerenciar alguma venda? (s/n) ::");
                                    if (sc.next().toLowerCase().equals("n")) 
                                        break; 
                    }
                    break;
                case 3: 
                    while (true){
                        System.out.println("Gestão de clientes");
                        System.out.println("Escolha uma das opções abaixo");
                        System.out.println(" 1 - Cadastrar novo cliente ");
                        System.out.println(" 2 - Pesquisar cliente ");
                        System.out.println(" 3 - Atualizar dados de cliente ");
                        System.out.println(" 4 - Excluir um cadastro de cliente ");
                        System.out.println(" 5 - inserir cliente no programa fidelidade ");
                        System.out.println(" 6 - retirar cliente no programa fidelidade ");
                        System.out.println(" 7 - Relação de clientes do programa fidelidade ");                        
                        System.out.print("Escolha: ");
                        int escolha3 = sc.nextInt();
                        
                        switch (escolha3){// switch case de gestão de clientes
                            case 1:
                                interfaceU.cadastrarNovoCliente();
                                break;
                            case 2:
                                interfaceU.buscarCliente();
                                break;
                            case 3:
                                interfaceU.atualizarDadosCliente();
                                break;
                            case 4:
                                interfaceU.excluirCliente();
                                break;
                            case 5:
                                interfaceU.inserirClienteProgramaFidelidade();
                                break;
                            case 6:
                                interfaceU.retirarClienteProgramaFidelidade();
                                break;
                            case 7:
                                interfaceU.listaProgramaFidelidade();
                                break;
                            default:  
                                System.out.println("Escolha inválida");
                        }                        
                         System.out.print(":: Quer continuar a gerenciar algum cliente? (s/n) ::");
                                    if (sc.next().toLowerCase().equals("n")) 
                                        break; 
                    }
                     break;
                case 4:
                    while (true){// switch case de gestão de funcionários
                        System.out.println("Gestão de funcionários");
                        System.out.println("Escolha uma das opções abaixo");
                        System.out.println(" 1 - Cadastrar novo funcionário ");
                        System.out.println(" 2 - Pesquisar funcionário ");
                        System.out.println(" 3 - Atualizar dados de funcionário ");
                        System.out.println(" 4 - Excluir cadastro de funcionário ");
                        System.out.println(" 5 - Gerar contracheque de um funcionário ");
                        System.out.print("Escolha: ");
                        int escolha4 = sc.nextInt();
                        
                        switch (escolha4){
                            case 1:
                                interfaceU.cadastrarFuncionario();
                                break;
                            case 2:
                                interfaceU.buscarFuncionario();
                                break;
                            case 3:
                                interfaceU.atualizarDadosFuncionario();
                                break;
                            case 4:
                                interfaceU.excluirFuncionario();
                                break;
                            case 5:
                                interfaceU.contracheque();
                                break;                                                          
                            default:  
                                System.out.println("Escolha inválida");
                        }                        
                         System.out.print(":: Quer continuar a gerenciar algum funcionario? (s/n) ::");
                                    if (sc.next().toLowerCase().equals("n")) 
                                        break; 
                    }
                    break;
                default: 
                    System.out.println("Entrada inválida");
                }    
                    System.out.print(":: Quer continuar a fazer operações no sistema? (s/n) ::");
                                   if (sc.next().toLowerCase().equals("n"))                                                          
                                        break;
                                    
            
            
        }
        sc.close();
       
       
        
        
    }
  
    
}

