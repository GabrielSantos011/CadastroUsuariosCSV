import br.com.gabriel.arquivos.dominio.Usuario;

private static Usuario[] usuarios;

void main() {
    IO.println("Vamos Começar!");
    separador();

    int escolha = 0;
    do {
        String escolhaString = IO.readln("""
                1 - Cadastrar Usuários Via Arquivo Csv
                2 - Cadastrar Usuário Manualmente
                3 - Ver Usuários Cadastrados
                4 - Exportar Usuários Cadastrados
                9 - Sair do Programa
                Digite sua escolha:""");

        separador();

        if (!escolhaString.matches("\\d+")) {
            /*\\d -> digitos de 0 a 9
            * + -> mais de um dígito*/
            IO.println("Digite Apenas Números");
            separador();

            continue;
        }

        escolha = Integer.parseInt(escolhaString);

        switch (escolha) {
            case 1:
                cadastraUsuariosCsv();
                break;
            case 2:
                cadastroUsuarioManual();
                break;
            case 3:
                visualizaUsuariosCadastrados();
                break;
            case 4:
                exportaUsuariosCsv();
                break;
            case 9:
                IO.println("Até a Próxima!");
                separador();
                break;
            default:
                IO.println("Opção Inválida.");
                separador();
        }
    } while (escolha != 9);
}

void cadastraUsuariosCsv() {
    String caminho;

    do {
        caminho = IO.readln("Digite o Caminho do Arquivo " +
                "(Exemplo: C:/Meu_Usuario/arquivo.csv) ou 9 para Cancelar: ");

        separador();

        if (caminho.equals("9")) {
            IO.println("Operação cancelada!");
            separador();
            break;
        }

        try {
            //javas antigos: BufferedReader br = new BufferedReader(new FileReader("clientes.csv"));
            Path arquivo = Path.of(caminho);

            if (!Files.exists(arquivo)) {
                IO.println("Arquivo selecionado não existe");
                separador();
                continue;
            }

            String conteudo = Files.readString(arquivo);
            String[] linhas = conteudo.split("\n");

            usuarios = new Usuario[linhas.length];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (int i = 0; i < linhas.length; i++) {
                String[] dados = linhas[i].split(";");

                usuarios[i] = new Usuario();
                usuarios[i].setId(Integer.valueOf(dados[0]));
                usuarios[i].setNome(dados[1]);
                usuarios[i].setDataNascimento(formatter.parse(dados[2], LocalDate::from));
                usuarios[i].setSexo(dados[3]);

                IO.println("Usuário " + usuarios[i].getNome() + " cadastrado!");
            }

        } catch (InvalidPathException e) {
            IO.println("Arquivo selecionado não existe");
            separador();
            continue;
        } catch (IOException e) {
            IO.println("Erro ao ler o arquivo.");
            separador();
            continue;
        }

        separador();

        break;
    } while (true);
}

void visualizaUsuariosCadastrados() {
    if (usuarios == null) {
        IO.println("Não Há Usuários Cadastrados.");
    } else {
        IO.println("Usuarios Cadastrados:");
        for (Usuario usuario : usuarios) {
            IO.println(usuario.getNome());
        }
    }
    separador();
}

void exportaUsuariosCsv() {
    if (usuarios == null || usuarios.length == 0) {
        IO.println("Não Há Usuários Cadastrados Para Exportar.");
        separador();
        return;
    }

    Path arquivo = Path.of(
            System.getProperty("user.home"),
            "Downloads",
            "usuarios_cadastrados.csv"
    );

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    StringBuilder conteudo = new StringBuilder();

    for (Usuario usuario : usuarios) {
        conteudo.append(usuario.getId()).append(";")
                .append(usuario.getNome()).append(";")
                .append(usuario.getDataNascimento().format(formatter)).append(";")
                .append(usuario.getSexo()).append("\n");
    }

    try {
        Files.writeString(
                arquivo,
                conteudo.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        IO.println("Arquivo exportado com sucesso!");
        IO.println("Local: " + arquivo);
    } catch (IOException e) {
        IO.println("Erro ao exportar o arquivo.");
        separador();
    }

    separador();
}

void cadastroUsuarioManual() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    Usuario usuario = new Usuario();

    // ID
    while (true) {
        String entrada = IO.readln("ID (ou 9 para cancelar): ");

        if (entrada.equals("9")) {
            IO.println("Operação cancelada!");
            separador();
            return;
        }

        if (!entrada.matches("\\d+")) {
            IO.println("Digite apenas números.");
            separador();
            continue;
        }

        usuario.setId(Integer.parseInt(entrada));

        separador();
        break;
    }

    // Nome
    while (true) {
        String nome = IO.readln("Nome (ou 9 para cancelar): ").trim();

        if (nome.equals("9")) {
            IO.println("Operação cancelada!");
            separador();
            return;
        }

        if (nome.isBlank()) {
            IO.println("O nome não pode ser vazio.");
            separador();
            continue;
        }

        usuario.setNome(nome);

        separador();
        break;
    }

    // Data de nascimento
    while (true) {
        String data = IO.readln("Data de nascimento (dd/MM/yyyy) ou 9 para cancelar: ");

        if (data.equals("9")) {
            IO.println("Operação cancelada!");
            separador();
            return;
        }

        try {
            usuario.setDataNascimento(LocalDate.parse(data, formatter));
            separador();
            break;
        } catch (DateTimeParseException e) {
            IO.println("Data inválida.");
            separador();
        }
    }

    // Sexo
    while (true) {
        String sexo = IO.readln("Sexo (Masculino/Feminino) ou 9 para cancelar: ").trim();

        if (sexo.equals("9")) {
            IO.println("Operação cancelada!");
            separador();
            return;
        }

        if (!sexo.equals("Masculino") && !sexo.equals("Feminino")) {
            IO.println("Digite exatamente 'Masculino' ou 'Feminino'.");
            separador();
            continue;
        }

        usuario.setSexo(sexo);
        separador();
        break;
    }

    // Adiciona ao array
    if (usuarios == null) {
        usuarios = new Usuario[1];
        usuarios[0] = usuario;
    } else {
        Usuario[] novoArray = new Usuario[usuarios.length + 1];

        System.arraycopy(usuarios, 0, novoArray, 0, usuarios.length);

        novoArray[usuarios.length] = usuario;
        usuarios = novoArray;
    }

    IO.println("Usuário " + usuario.getNome() + " cadastrado com sucesso!");
    separador();
}

void separador() {
    IO.println("---------------");
    IO.println();
}