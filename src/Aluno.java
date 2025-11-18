import java.util.Calendar;
import java.util.Date;

// Herança: Aluno estende Usuario
public class Aluno extends Usuario {

    // Encapsulamento: atributos privados específicos de Aluno
    private String matricula;
    private String curso;
    private int limiteEmprestimo = 3;

    public Aluno() {
        super();
    }


    public Aluno(String id, String nome, String endereco, String matricula, String curso) {
        super(); // chama Usuario()
        setId(id);
        setNome(nome);
        setEndereco(endereco);
        setStatus("Ativo");
        this.matricula = matricula;
        this.curso = curso;
        this.limiteEmprestimo = 3;
    }

    // Getters e Setters (POO: Encapsulamento)
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getLimiteEmprestimo() {
        return limiteEmprestimo;
    }

    public void setLimiteEmprestimo(int limiteEmprestimo) {
        this.limiteEmprestimo = limiteEmprestimo;
    }

    // Polimorfismo (Sobrescrita): prazo de devolução para aluno = 7 dias
    @Override
    public Date calculaPrazoDevolucao() {
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, 7);
        return calendario.getTime();
    }

    // Polimorfismo (Sobrescrita): verifica limite antes de emprestar
    @Override
    public boolean isAptoParaEmprestimo() {
        if (!super.isAptoParaEmprestimo()) {
            return false;
        }
        return getQuantidadeEmprestimosAtivos() < limiteEmprestimo;
    }
}
