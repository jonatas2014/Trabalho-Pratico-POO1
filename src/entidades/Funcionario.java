
package entidades;

/**
 *
 * @author jonatas
 */
public class Funcionario extends Pessoa {
    
    private final int CARGA_HORARIA_MENSAL = 160;
    
    public Funcionario(){
        
    }

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
     * Método que gera conrtracheque para um funcionário, baseado na quantidade de hoaras trabalhas no mês.
     * @param carga_Horaria_Trabalhada carga horária trabalhada no mês
     */
   
    public void gerarContracheque(int carga_Horaria_Trabalhada){
             
        int salarioMensal = (3000*carga_Horaria_Trabalhada)/(CARGA_HORARIA_MENSAL);
        System.out.println("-----------------------");
        System.out.println("|     Contracheque     |");
        System.out.println("-----------------------");
        System.out.println("| Nome: " + this.getNome() + " |");
        System.out.println("| CPF: " + this.getCpf() + " |");
        System.out.println("|  " + this.getRg() + " |");
        System.out.println("| Salario: " + salarioMensal + " |");
    }
}
