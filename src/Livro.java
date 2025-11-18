// Herança: Livro estende ItemDeAcervo
public class Livro extends ItemDeAcervo {

    // Encapsulamento: atributos privados específicos de Livro
    private String autor;
    private String isbn;
    private int edicao;

    // Construtor
    public Livro() {
        super();
    }

    // Construtor
    public Livro(String codigo, String titulo, int anoPublicacao,
                 String autor, String isbn, int edicao) {

        setCodigo(codigo);
        setTitulo(titulo);
        setAnoPublicacao(anoPublicacao);
        setEmprestado(false); // item recém-cadastrado nunca começa emprestado

        this.autor = autor;
        this.isbn = isbn;
        this.edicao = edicao;
    }

    // Encapsulamento: getters e setters
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getEdicao() {
        return edicao;
    }

    public void setEdicao(int edicao) {
        this.edicao = edicao;
    }
}
