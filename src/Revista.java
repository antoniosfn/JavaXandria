// Herança: Revista estende ItemDeAcervo
public class Revista extends ItemDeAcervo {

    // Encapsulamento: atributos privados específicos de Revista
    private String editora;
    private int volume;
    private String issn;

    // Construtor
    public Revista() {
        super();
    }

    // Construtor
    public Revista(String codigo, String titulo, int anoPublicacao,
                   String editora, int volume, String issn) {
        setCodigo(codigo);
        setTitulo(titulo);
        setAnoPublicacao(anoPublicacao);
        setEmprestado(false); // revista cadastrada começa disponível

        this.editora = editora;
        this.volume = volume;
        this.issn = issn;
    }

    // Encapsulamento: getters e setters
    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }
}
