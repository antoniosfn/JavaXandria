import java.io.Serializable;
import java.util.Date;

// Associação/Composição: relaciona Usuario e ItemDeAcervo
public class Emprestimo implements Serializable {

    // Encapsulamento: atributos privados do empréstimo
    private String idEmprestimo;
    private Usuario usuario;
    private ItemDeAcervo item;
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoReal;
    private double multaCobrada;

    // Encapsulamento: getters e setters públicos

    public String getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(String idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ItemDeAcervo getItem() {
        return item;
    }

    public void setItem(ItemDeAcervo item) {
        this.item = item;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Date getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(Date dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public double getMultaCobrada() {
        return multaCobrada;
    }

    public void setMultaCobrada(double multaCobrada) {
        this.multaCobrada = multaCobrada;
    }

    // Regra de negócio RN3: cálculo de multa R$ 1,00 por dia de atraso
    public double calcularMulta(Date dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;

        if (dataDevolucaoPrevista == null || dataDevolucaoReal == null) {
            this.multaCobrada = 0.0;
            return this.multaCobrada;
        }

        long diferencaMillis = dataDevolucaoReal.getTime() - dataDevolucaoPrevista.getTime();

        if (diferencaMillis <= 0) {
            this.multaCobrada = 0.0;
        } else {
            long diasAtraso = diferencaMillis / (1000 * 60 * 60 * 24);
            this.multaCobrada = diasAtraso * 1.0;
        }

        return this.multaCobrada;
    }

    // Regra de negócio RF3/RF4: finalizar empréstimo e atualizar item
    public void finalizarEmprestimo(Date dataDevolucaoReal) {
        calcularMulta(dataDevolucaoReal);
        if (item != null) {
            item.devolver();
        }
    }
}
