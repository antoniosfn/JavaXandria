import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Associação: Main utiliza a classe de controle SistemaBiblioteca
        SistemaBiblioteca sistema = new SistemaBiblioteca();

        // Persistência: carregar dados do arquivo texto no início da execução
        sistema.carregarDados();

        int opcao;

        do {
            System.out.println("\n=== SISTEMA DE GESTÃO DE BIBLIOTECA UNIVERSITÁRIA ===");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Cadastrar item");
            System.out.println("3. Buscar item");
            System.out.println("4. Realizar empréstimo");
            System.out.println("5. Realizar devolução");
            System.out.println("6. Listar usuários cadastrados");
            System.out.println("7. Listar acervo cadastrados");
            System.out.println("8. Ver itens emprestados");
            System.out.println("9. Ver empréstimos atrasados (com multa)");
            System.out.println("10. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    cadastrarUsuario(scanner, sistema);
                    break;
                case 2:
                    cadastrarItem(scanner, sistema);
                    break;
                case 3:
                    buscarItem(scanner, sistema);
                    break;
                case 4:
                    realizarEmprestimo(scanner, sistema);
                    break;
                case 5:
                    realizarDevolucao(scanner, sistema);
                    break;
                case 6:
                    listarUsuarios(sistema);
                    break;
                case 7:
                    listarLivros(sistema);
                    break;
                case 8:
                    listarItensEmprestados(sistema);
                    break;
                case 9:
                    listarEmprestimosAtrasados(sistema);
                    break;
                case 10:
                    System.out.println("Saindo do sistema. Salvando dados...");
                    sistema.salvarDados();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 10);

        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    // Interface de cadastro: cria objetos Aluno/Professor - Encapsulamento e Herança
    private static void cadastrarUsuario(Scanner scanner, SistemaBiblioteca sistema) {
        System.out.println("\n=== CADASTRAR USUÁRIO ===");
        System.out.println("1. Aluno");
        System.out.println("2. Professor");
        System.out.print("Tipo de usuário: ");

        int tipo = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Status (ex: Ativo, Bloqueado): ");
        String status = scanner.nextLine();

        if (tipo == 1) {
            System.out.print("Matrícula: ");
            String matricula = scanner.nextLine();
            System.out.print("Curso: ");
            String curso = scanner.nextLine();

            Aluno aluno = new Aluno();
            aluno.setId(id);
            aluno.setNome(nome);
            aluno.setEndereco(endereco);
            aluno.setStatus(status);
            aluno.setMatricula(matricula);
            aluno.setCurso(curso);

            sistema.cadastrarUsuario(aluno);
            System.out.println("Aluno cadastrado com sucesso!");

        } else if (tipo == 2) {
            System.out.print("SIAPE: ");
            String siape = scanner.nextLine();
            System.out.print("Departamento: ");
            String departamento = scanner.nextLine();

            Professor professor = new Professor();
            professor.setId(id);
            professor.setNome(nome);
            professor.setEndereco(endereco);
            professor.setStatus(status);
            professor.setSiape(siape);
            professor.setDepartamento(departamento);

            sistema.cadastrarUsuario(professor);
            System.out.println("Professor cadastrado com sucesso!");

        } else {
            System.out.println("Tipo de usuário inválido!");
        }
    }

    // Interface de cadastro: cria objetos Livro/Revista - Encapsulamento e Herança
    private static void cadastrarItem(Scanner scanner, SistemaBiblioteca sistema) {
        System.out.println("\n=== CADASTRAR ITEM ===");
        System.out.println("1. Livro");
        System.out.println("2. Revista");
        System.out.print("Tipo de item: ");

        int tipo = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Ano de publicação: ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        if (tipo == 1) {
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();
            System.out.print("Edição: ");
            int edicao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            Livro livro = new Livro();
            livro.setCodigo(codigo);
            livro.setTitulo(titulo);
            livro.setAnoPublicacao(ano);
            livro.setAutor(autor);
            livro.setIsbn(isbn);
            livro.setEdicao(edicao);

            sistema.cadastrarItem(livro);
            System.out.println("Livro cadastrado com sucesso!");

        } else if (tipo == 2) {
            System.out.print("Editora: ");
            String editora = scanner.nextLine();
            System.out.print("Volume: ");
            int volume = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
            System.out.print("ISSN: ");
            String issn = scanner.nextLine();

            Revista revista = new Revista();
            revista.setCodigo(codigo);
            revista.setTitulo(titulo);
            revista.setAnoPublicacao(ano);
            revista.setEditora(editora);
            revista.setVolume(volume);
            revista.setIssn(issn);

            sistema.cadastrarItem(revista);
            System.out.println("Revista cadastrada com sucesso!");

        } else {
            System.out.println("Tipo de item inválido!");
        }
    }

    // Interface de consulta: usa busca por título / ISBN / ISSN - Associação e Polimorfismo
    private static void buscarItem(Scanner scanner, SistemaBiblioteca sistema) {
        System.out.println("\n=== BUSCAR ITEM ===");
        System.out.println("1. Por título");
        System.out.println("2. Por ISBN (livro)");
        System.out.println("3. Por ISSN (revista)");
        System.out.print("Tipo de busca: ");

        int tipo = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        if (tipo == 1) {
            System.out.print("Título: ");
            String titulo = scanner.nextLine();

            List<ItemDeAcervo> resultados = sistema.buscarPorTitulo(titulo);
            System.out.println("\nItens encontrados: " + resultados.size());
            for (ItemDeAcervo item : resultados) {
                System.out.println("- " + item.getCodigo() + " | " + item.getTitulo());
            }

        } else if (tipo == 2) {
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();

            ItemDeAcervo item = sistema.buscarPorIsbnOuIssn(isbn);
            if (item instanceof Livro) {
                Livro livro = (Livro) item;
                System.out.println("\nLivro encontrado:");
                System.out.println("Código: " + livro.getCodigo());
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + livro.getAutor());
            } else {
                System.out.println("Nenhum livro encontrado com esse ISBN.");
            }

        } else if (tipo == 3) {
            System.out.print("ISSN: ");
            String issn = scanner.nextLine();

            ItemDeAcervo item = sistema.buscarPorIsbnOuIssn(issn);
            if (item instanceof Revista revista) {
                System.out.println("\nRevista encontrada:");
                System.out.println("Código: " + revista.getCodigo());
                System.out.println("Título: " + revista.getTitulo());
                System.out.println("Editora: " + revista.getEditora());
            } else {
                System.out.println("Nenhuma revista encontrada com esse ISSN.");
            }

        } else {
            System.out.println("Tipo de busca inválido!");
        }
    }

    // Interface de empréstimo
    private static void realizarEmprestimo(Scanner scanner, SistemaBiblioteca sistema) {
        System.out.println("\n=== REALIZAR EMPRÉSTIMO ===");
        System.out.print("ID do usuário: ");
        String idUsuario = scanner.nextLine();
        System.out.print("Código do item: ");
        String codItem = scanner.nextLine();

        try {
            Emprestimo emprestimo = sistema.realizarEmprestimo(idUsuario, codItem);

            System.out.println("\nEmpréstimo realizado com sucesso!");
            System.out.println("Usuário: " + emprestimo.getUsuario().getNome());
            System.out.println("Item: " + emprestimo.getItem().getTitulo());
            System.out.println("Data de empréstimo: " + emprestimo.getDataEmprestimo());
            System.out.println("Data de devolução prevista: " + emprestimo.getDataDevolucaoPrevista());

        } catch (Exception e) {
            System.out.println("\nNão foi possível realizar o empréstimo:");
            System.out.println(e.getMessage());
        }
    }

    // Interface de devolução
    private static void realizarDevolucao(Scanner scanner, SistemaBiblioteca sistema) {
        System.out.println("\n=== REALIZAR DEVOLUÇÃO ===");
        System.out.print("ID do empréstimo: ");
        String idEmprestimo = scanner.nextLine();

        sistema.realizarDevolucao(idEmprestimo);

        System.out.println("Devolução registrada. Multa (se houver) foi calculada pela regra de negócio.");
    }

    // Listagem de usuários cadastrados
    private static void listarUsuarios(SistemaBiblioteca sistema) {
        System.out.println("\n=== USUÁRIOS CADASTRADOS ===");

        List<Usuario> usuarios = sistema.getListaUsuarios();

        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        for (Usuario u : usuarios) {
            String tipo;
            if (u instanceof Aluno) {
                tipo = "Aluno";
            } else if (u instanceof Professor) {
                tipo = "Professor";
            } else {
                tipo = "Usuário";
            }

            System.out.println("[" + tipo + "] ID: " + u.getId() + " | Nome: " + u.getNome());
        }
    }

    // Listagem de livros cadastrados
    private static void listarLivros(SistemaBiblioteca sistema) {
        System.out.println("\n=== LIVROS CADASTRADOS ===");

        List<ItemDeAcervo> itens = sistema.getAcervo();
        boolean encontrou = false;

        if (itens == null || itens.isEmpty()) {
            System.out.println("Nenhum item cadastrado no acervo.");
            return;
        }

        for (ItemDeAcervo item : itens) {
            if (item instanceof Livro livro) {
                encontrou = true;
                System.out.println("Código: " + livro.getCodigo()
                        + " | Título: " + livro.getTitulo()
                        + " | Autor: " + livro.getAutor()
                        + " | Ano: " + livro.getAnoPublicacao());
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum livro cadastrado no acervo.");
        }
    }

    // Listagem de itens emprestados
    private static void listarItensEmprestados(SistemaBiblioteca sistema) {
        System.out.println("\n=== ITENS EMPRESTADOS ===");

        List<Emprestimo> emprestimos = sistema.getHistoricoEmprestimos();
        boolean encontrou = false;

        if (emprestimos == null || emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
            return;
        }

        for (Emprestimo e : emprestimos) {
            if (e.getDataDevolucaoReal() == null) {
                encontrou = true;
                System.out.println(
                        "Item: " + e.getItem().getTitulo() +
                                " | Código: " + e.getItem().getCodigo() +
                                " | Usuário: " + e.getUsuario().getNome() +
                                " | Devolução prevista: " + e.getDataDevolucaoPrevista()
                );
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum item está emprestado no momento.");
        }
    }

    // Listagem de empréstimos atrasados com cálculo de multa (R$ 1,00 por dia)
    private static void listarEmprestimosAtrasados(SistemaBiblioteca sistema) {
        System.out.println("\n=== EMPRÉSTIMOS ATRASADOS ===");

        List<Emprestimo> emprestimos = sistema.getHistoricoEmprestimos();
        if (emprestimos == null || emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
            return;
        }

        Date hoje = new Date();
        boolean encontrou = false;

        for (Emprestimo e : emprestimos) {
            if (e.getDataDevolucaoPrevista() != null &&
                    e.getDataDevolucaoReal() == null &&
                    hoje.after(e.getDataDevolucaoPrevista())) {

                long diffMillis = hoje.getTime() - e.getDataDevolucaoPrevista().getTime();
                long diasAtraso = diffMillis / (1000L * 60 * 60 * 24);

                if (diasAtraso < 1) {
                    diasAtraso = 1;
                }

                double multa = diasAtraso * 1.0; // R$ 1,00 por dia

                encontrou = true;

                System.out.println(
                        "Item: " + e.getItem().getTitulo() +
                                " | Usuário: " + e.getUsuario().getNome() +
                                " | Previsto para: " + e.getDataDevolucaoPrevista() +
                                " | Dias de atraso: " + diasAtraso +
                                " | Multa atual: R$ " + String.format("%.2f", multa)
                );
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum empréstimo está atrasado no momento.");
        }
    }

}