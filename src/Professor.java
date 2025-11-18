import java.util.Calendar;
import java.util.Date;

// Herança: Professor estende Usuario
public class Professor extends Usuario {

    // Encapsulamento: atributos privados
    private String siape;
    private String departamento;
    private int limiteEmprestimo = 5;

    // Construtor
    public Professor() {
        super();
    }

    // Construtor
    public Professor(String id, String nome, String endereco, String siape, String departamento) {
        super();
        setId(id);
        setNome(nome);
        setEndereco(endereco);
        setStatus("Ativo");
        this.siape = siape;
        this.departamento = departamento;
        this.limiteEmprestimo = 5;
    }

    // Encapsulamento: getters e setters
    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public int getLimiteEmprestimo() {
        return limiteEmprestimo;
    }

    public void setLimiteEmprestimo(int limiteEmprestimo) {
        this.limiteEmprestimo = limiteEmprestimo;
    }

    // Polimorfismo: prazo de devolução para professor = 15 dias
    @Override
    public Date calculaPrazoDevolucao() {
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, 15);
        return calendario.getTime();
    }

    // Polimorfismo: verifica limite antes de emprestar
    @Override
    public boolean isAptoParaEmprestimo() {
        if (!super.isAptoParaEmprestimo()) {
            return false;
        }
        return getQuantidadeEmprestimosAtivos() < limiteEmprestimo;
    }
}
