import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Classe usuário - Abstração e Herança
public class Usuario {

    // Encapsulamento: atributos privados com acesso por getters e setters
    private String id;
    private String nome;
    private String endereco;
    private String status; // ex: "Ativo", "Bloqueado"
    private List<Emprestimo> itensEmprestados;

    // Encapsulamento: construtor inicializando lista
    public Usuario() {
        this.itensEmprestados = new ArrayList<Emprestimo>();
    }

    // Encapsulamento: getters e setters públicos

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Emprestimo> getItensEmprestados() {
        return itensEmprestados;
    }

    public void setItensEmprestados(List<Emprestimo> itensEmprestados) {
        this.itensEmprestados = itensEmprestados;
    }

    // Associação/Composição: usuário mantém relação com objetos Emprestimo
    public void adicionarEmprestimo(Emprestimo emprestimo) {
        if (this.itensEmprestados == null) {
            this.itensEmprestados = new ArrayList<Emprestimo>();
        }
        this.itensEmprestados.add(emprestimo);
    }

    // Encapsulamento: obtém quantidade de empréstimos ainda ativos
    public int getQuantidadeEmprestimosAtivos() {
        int contador = 0;
        if (itensEmprestados != null) {
            for (Emprestimo e : itensEmprestados) {
                if (e.getDataDevolucaoReal() == null) {
                    contador++;
                }
            }
        }
        return contador;
    }

    // Encapsulamento: verifica se existe multa pendente
    public boolean temMultaPendente() {
        if (itensEmprestados != null) {
            for (Emprestimo e : itensEmprestados) {
                if (e.getMultaCobrada() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // Encapsulamento: verifica se existe item atrasado ainda não devolvido
    public boolean temItemAtrasado() {
        Date hoje = new Date();
        if (itensEmprestados != null) {
            for (Emprestimo e : itensEmprestados) {
                if (e.getDataDevolucaoReal() == null &&
                        e.getDataDevolucaoPrevista() != null &&
                        e.getDataDevolucaoPrevista().before(hoje)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Polimorfismo: (RN4 - bloqueio por multa/atraso)
    public boolean isAptoParaEmprestimo() {
        if (!"Ativo".equalsIgnoreCase(this.status)) {
            return false;
        }
        if (temMultaPendente()) {
            return false;
        }
        if (temItemAtrasado()) {
            return false;
        }
        return true;
    }

    // Polimorfismo: será sobrescrito em Aluno e Professor para prazos diferentes
    public Date calculaPrazoDevolucao() {
        return null;
    }
}
