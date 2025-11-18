import java.io.Serializable;

// Classe base abstrata do acervo - Abstração e Polimorfismo
public abstract class ItemDeAcervo implements Serializable {

    // Encapsulamento: atributos privados
    private String codigo;
    private String titulo;
    private int anoPublicacao;
    private boolean isEmprestado;

    // Encapsulamento: getters e setters públicos

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public boolean isEmprestado() {
        return isEmprestado;
    }

    public void setEmprestado(boolean isEmprestado) {
        this.isEmprestado = isEmprestado;
    }

    // Polimorfismo: comportamento comum para emprestar item
    public void emprestar() {
        this.isEmprestado = true;
    }

    // Polimorfismo: comportamento comum para devolver item
    public void devolver() {
        this.isEmprestado = false;
    }
}
