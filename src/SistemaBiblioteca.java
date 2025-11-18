import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



// Agregação/Composição: gerencia listas de usuários, acervo e empréstimos
public class SistemaBiblioteca implements Serializable {

    // Encapsulamento: listas internas de objetos
    private List<Usuario> listaUsuarios;
    private List<ItemDeAcervo> acervo;
    private List<Emprestimo> historicoEmprestimos;

    // Encapsulamento: construtor inicializando listas
    public SistemaBiblioteca() {
        this.listaUsuarios = new ArrayList<Usuario>();
        this.acervo = new ArrayList<ItemDeAcervo>();
        this.historicoEmprestimos = new ArrayList<Emprestimo>();
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public List<ItemDeAcervo> getAcervo() {
        return acervo;
    }

    public List<Emprestimo> getHistoricoEmprestimos() {
        return historicoEmprestimos;
    }

    // Funcionalidade: cadastro de novo usuário
    public void cadastrarUsuario(Usuario usuario) {
        if (usuario != null) {
            this.listaUsuarios.add(usuario);
        }
    }

    // Funcionalidade: cadastro de novo item no acervo
    public void cadastrarItem(ItemDeAcervo item) {
        if (item != null) {
            this.acervo.add(item);
        }
    }

    // Encapsulamento: cadastro de usuário
    public void adicionarUsuario(Usuario usuario) {
        cadastrarUsuario(usuario);
    }

    // Encapsulamento:cadastro de item
    public void adicionarItem(ItemDeAcervo item) {
        cadastrarItem(item);
    }

    // Funcionalidade: busca por acervo pelo título
    public List<ItemDeAcervo> buscarPorTitulo(String titulo) {
        List<ItemDeAcervo> resultado = new ArrayList<ItemDeAcervo>();
        if (titulo == null) {
            return resultado;
        }

        for (ItemDeAcervo item : acervo) {
            if (item.getTitulo() != null &&
                    item.getTitulo().equalsIgnoreCase(titulo)) {
                resultado.add(item);
            }
        }

        return resultado;
    }

    // nome usado no Main
    public List<ItemDeAcervo> buscaPorTitulo(String titulo) {
        return buscarPorTitulo(titulo);
    }

    // Funcionalidade: busca por acervo por ISBN ou ISSN (primeiro encontrado)
    public ItemDeAcervo buscarPorIsbnOuIssn(String codigo) {
        if (codigo == null) {
            return null;
        }

        for (ItemDeAcervo item : acervo) {
            if (item instanceof Livro livro) {
                if (livro.getIsbn() != null &&
                        livro.getIsbn().equalsIgnoreCase(codigo)) {
                    return livro;
                }
            } else if (item instanceof Revista revista) {
                if (revista.getIssn() != null &&
                        revista.getIssn().equalsIgnoreCase(codigo)) {
                    return revista;
                }
            }
        }
        return null;
    }

    // Busca apenas livros por ISBN (lista) - usado no Main
    public List<Livro> buscaPorIsbn(String isbn) {
        List<Livro> resultado = new ArrayList<Livro>();
        if (isbn == null) {
            return resultado;
        }

        for (ItemDeAcervo item : acervo) {
            if (item instanceof Livro) {
                Livro livro = (Livro) item;
                if (livro.getIsbn() != null &&
                        livro.getIsbn().equalsIgnoreCase(isbn)) {
                    resultado.add(livro);
                }
            }
        }
        return resultado;
    }

    // Busca apenas revistas por ISSN (lista) - usado no Main
    public List<Revista> buscaPorIssn(String issn) {
        List<Revista> resultado = new ArrayList<Revista>();
        if (issn == null) {
            return resultado;
        }

        for (ItemDeAcervo item : acervo) {
            if (item instanceof Revista) {
                Revista revista = (Revista) item;
                if (revista.getIssn() != null &&
                        revista.getIssn().equalsIgnoreCase(issn)) {
                    resultado.add(revista);
                }
            }
        }
        return resultado;
    }

    // Regra de negócio: realizar empréstimo (RF1, RN1, RN2, RN4)
    public Emprestimo realizarEmprestimo(String idUsuario, String codItem) throws Exception {

        Usuario usuarioSelecionado = null;
        ItemDeAcervo itemSelecionado = null;

        for (Usuario u : listaUsuarios) {
            if (u.getId().equals(idUsuario)) {
                usuarioSelecionado = u;
                break;
            }
        }

        for (ItemDeAcervo item : acervo) {
            if (item.getCodigo().equals(codItem)) {
                itemSelecionado = item;
                break;
            }
        }

        if (usuarioSelecionado == null || itemSelecionado == null) {
            throw new Exception("Usuário ou item não encontrado.");
        }

        // RN1 - Disponibilidade
        if (itemSelecionado.isEmprestado()) {
            throw new Exception("Item indisponível para empréstimo (RN1).");
        }

        // RN2 e RN4 - verificação pelas regras da classe Usuario
        if (!usuarioSelecionado.isAptoParaEmprestimo()) {
            throw new Exception("Usuário não apto para empréstimo (RN2/RN4).");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuarioSelecionado);
        emprestimo.setItem(itemSelecionado);

        Date dataEmprestimo = new Date();
        emprestimo.setDataEmprestimo(dataEmprestimo);

        Date dataPrevista = usuarioSelecionado.calculaPrazoDevolucao();
        emprestimo.setDataDevolucaoPrevista(dataPrevista);

        // Gera ID simples de empréstimo
        String novoId = "E" + (historicoEmprestimos.size() + 1);
        emprestimo.setIdEmprestimo(novoId);

        itemSelecionado.emprestar();

        historicoEmprestimos.add(emprestimo);
        usuarioSelecionado.adicionarEmprestimo(emprestimo);

        return emprestimo;
    }

    // Busca de empréstimo por ID - usado no Main
    public Emprestimo buscarEmprestimo(String idEmprestimo) {
        if (idEmprestimo == null) {
            return null;
        }
        for (Emprestimo e : historicoEmprestimos) {
            if (idEmprestimo.equals(e.getIdEmprestimo())) {
                return e;
            }
        }
        return null;
    }

    // Regra de negócio: devolução de item (RF3, RN3)
    public void realizarDevolucao(String idEmprestimo) {
        Emprestimo emp = buscarEmprestimo(idEmprestimo);
        if (emp != null) {
            Date dataDevolucaoReal = new Date();
            emp.finalizarEmprestimo(dataDevolucaoReal);
        }
    }

    // Persistência: salvar dados em arquivo texto
    public void salvarDados() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/dados_biblioteca.txt"));

            writer.write("[USUARIOS]");
            writer.newLine();
            for (Usuario u : listaUsuarios) {
                if (u instanceof Aluno) {
                    Aluno a = (Aluno) u;
                    writer.write("ALUNO;" + a.getId() + ";" + a.getNome() + ";" + a.getEndereco() + ";" +
                            a.getStatus() + ";" + a.getMatricula() + ";" + a.getCurso());
                } else if (u instanceof Professor) {
                    Professor p = (Professor) u;
                    writer.write("PROFESSOR;" + p.getId() + ";" + p.getNome() + ";" + p.getEndereco() + ";" +
                            p.getStatus() + ";" + p.getSiape() + ";" + p.getDepartamento());
                }
                writer.newLine();
            }

            writer.newLine();
            writer.write("[ACERVO]");
            writer.newLine();
            for (ItemDeAcervo item : acervo) {
                if (item instanceof Livro) {
                    Livro l = (Livro) item;
                    writer.write("LIVRO;" + l.getCodigo() + ";" + l.getTitulo() + ";" + l.getAnoPublicacao() + ";" +
                            l.isEmprestado() + ";" + l.getAutor() + ";" + l.getIsbn() + ";" + l.getEdicao());
                } else if (item instanceof Revista) {
                    Revista r = (Revista) item;
                    writer.write("REVISTA;" + r.getCodigo() + ";" + r.getTitulo() + ";" + r.getAnoPublicacao() + ";" +
                            r.isEmprestado() + ";" + r.getEditora() + ";" + r.getVolume() + ";" + r.getIssn());
                }
                writer.newLine();
            }

            writer.newLine();
            writer.write("[EMPRESTIMOS]");
            writer.newLine();
            for (Emprestimo e : historicoEmprestimos) {
                writer.write(
                        e.getIdEmprestimo() + ";" +
                                e.getUsuario().getId() + ";" +
                                e.getItem().getCodigo() + ";" +
                                e.getDataEmprestimo() + ";" +
                                e.getDataDevolucaoPrevista() + ";" +
                                e.getDataDevolucaoReal() + ";" +
                                e.getMultaCobrada()
                );
                writer.newLine();
            }

            writer.close();
            System.out.println("Dados salvos no arquivo dados_biblioteca.txt");

        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    // Persistência: leitura do arquivo e recriação dos objetos em memória
    public void carregarDados() {
        BufferedReader reader = null;

        // limpa listas atuais para não duplicar ao carregar
        listaUsuarios = new ArrayList<Usuario>();
        acervo = new ArrayList<ItemDeAcervo>();
        historicoEmprestimos = new ArrayList<Emprestimo>();

        try {
            reader = new BufferedReader(new FileReader("src/dados_biblioteca.txt"));
            String linha = reader.readLine();
            String secaoAtual = "";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");


            while (linha != null) {

                linha = linha.trim();

                // ignora linhas vazias
                if (linha.isEmpty()) {
                    linha = reader.readLine();
                    continue;
                }

                // identifica seção
                if (linha.equalsIgnoreCase("[USUARIOS]")) {
                    secaoAtual = "USUARIOS";
                    linha = reader.readLine();
                    continue;
                } else if (linha.equalsIgnoreCase("[ACERVO]")) {
                    secaoAtual = "ACERVO";
                    linha = reader.readLine();
                    continue;
                } else if (linha.equalsIgnoreCase("[EMPRESTIMOS]")) {
                    secaoAtual = "EMPRESTIMOS";
                    linha = reader.readLine();
                    continue;
                }

                String[] partes = linha.split(";");
                if (secaoAtual.equals("USUARIOS")) {

                    String tipo = partes[0];

                    if ("ALUNO".equalsIgnoreCase(tipo)) {
                        // ALUNO;id;nome;endereco;status;matricula;curso
                        Aluno a = new Aluno();
                        a.setId(partes[1]);
                        a.setNome(partes[2]);
                        a.setEndereco(partes[3]);
                        a.setStatus(partes[4]);
                        a.setMatricula(partes[5]);
                        a.setCurso(partes[6]);
                        listaUsuarios.add(a);

                    } else if ("PROFESSOR".equalsIgnoreCase(tipo)) {
                        // PROFESSOR;id;nome;endereco;status;siape;departamento
                        Professor p = new Professor();
                        p.setId(partes[1]);
                        p.setNome(partes[2]);
                        p.setEndereco(partes[3]);
                        p.setStatus(partes[4]);
                        p.setSiape(partes[5]);
                        p.setDepartamento(partes[6]);
                        listaUsuarios.add(p);
                    }

                } else if (secaoAtual.equals("ACERVO")) {

                    String tipo = partes[0];

                    if ("LIVRO".equalsIgnoreCase(tipo)) {
                        // LIVRO;codigo;titulo;ano;isEmprestado;autor;isbn;edicao
                        Livro l = new Livro();
                        l.setCodigo(partes[1]);
                        l.setTitulo(partes[2]);
                        l.setAnoPublicacao(Integer.parseInt(partes[3]));
                        l.setEmprestado(Boolean.parseBoolean(partes[4]));
                        l.setAutor(partes[5]);
                        l.setIsbn(partes[6]);
                        l.setEdicao(Integer.parseInt(partes[7]));
                        acervo.add(l);

                    } else if ("REVISTA".equalsIgnoreCase(tipo)) {
                        // REVISTA;codigo;titulo;ano;isEmprestado;editora;volume;issn
                        Revista r = new Revista();
                        r.setCodigo(partes[1]);
                        r.setTitulo(partes[2]);
                        r.setAnoPublicacao(Integer.parseInt(partes[3]));
                        r.setEmprestado(Boolean.parseBoolean(partes[4]));
                        r.setEditora(partes[5]);
                        r.setVolume(Integer.parseInt(partes[6]));
                        r.setIssn(partes[7]);
                        acervo.add(r);
                    }

                } else if (secaoAtual.equals("EMPRESTIMOS")) {
                    // E1;idUsuario;codItem;dataEmp;dataPrev;dataReal;multa

                    Emprestimo e = new Emprestimo();

                    String idEmprestimo = partes[0];
                    String idUsuario = partes[1];
                    String codItem = partes[2];
                    String dataEmpStr = partes[3];
                    String dataPrevStr = partes[4];
                    String dataRealStr = partes[5];
                    String multaStr = partes[6];

                    e.setIdEmprestimo(idEmprestimo);

                    // associa usuário
                    Usuario usuario = null;
                    for (Usuario u : listaUsuarios) {
                        if (u.getId().equals(idUsuario)) {
                            usuario = u;
                            break;
                        }
                    }
                    e.setUsuario(usuario);

                    // associa item
                    ItemDeAcervo itemEmp = null;
                    for (ItemDeAcervo it : acervo) {
                        if (it.getCodigo().equals(codItem)) {
                            itemEmp = it;
                            break;
                        }
                    }
                    e.setItem(itemEmp);

                    // datas
                    try {
                        if (!"null".equalsIgnoreCase(dataEmpStr) && !dataEmpStr.isEmpty()) {
                            e.setDataEmprestimo(sdf.parse(dataEmpStr));
                        }
                        if (!"null".equalsIgnoreCase(dataPrevStr) && !dataPrevStr.isEmpty()) {
                            e.setDataDevolucaoPrevista(sdf.parse(dataPrevStr));
                        }
                        if (!"null".equalsIgnoreCase(dataRealStr) && !dataRealStr.isEmpty()) {
                            e.setDataDevolucaoReal(sdf.parse(dataRealStr));
                        }
                    } catch (ParseException pe) {
                        // Se der erro ao parsear, deixa datas como null
                    }

                    // multa
                    try {
                        e.setMultaCobrada(Double.parseDouble(multaStr));
                    } catch (NumberFormatException ne) {
                        e.setMultaCobrada(0.0);
                    }

                    historicoEmprestimos.add(e);

                    // adiciona empréstimo à lista do usuário (associação)
                    if (usuario != null) {
                        usuario.adicionarEmprestimo(e);
                    }
                }

                linha = reader.readLine();
            }

            System.out.println("Dados carregados do arquivo e objetos recriados na memória.");

        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                // ignora erro de fechamento
            }
        }
    }


}
