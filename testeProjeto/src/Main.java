import javax.swing.JOptionPane;
import java.util.List;

public class Main {
    private static OperacoesPessoa pessoaDAO = new PessoaDAO();

    public static void main(String[] args) {
        exibirMenu();
    }

    private static void exibirMenu() {
        String[] opcoes = {"Criar Pessoa Física", "Criar Pessoa Jurídica", "Listar Pessoas", "Deletar Pessoa","Atualizar Pessoa", "Sair"};
        while (true) {
            String escolha = (String) JOptionPane.showInputDialog(null, "Selecione uma opção:", "Menu", JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);
            if (escolha == null) {
                encerrarPrograma();
                break;
            }

            switch (escolha) {
                case "Criar Pessoa Física":
                    criarPessoaFisica();
                    break;
                case "Criar Pessoa Jurídica":
                    criarPessoaJuridica();
                    break;
                case "Listar Pessoas":
                    listarPessoas();
                    break;
                case "Deletar Pessoa":
                    deletarPessoa();
                    break;
                case "Atualizar Pessoa":
                    atualizarPessoa();
                    break;
                case "Sair":
                    encerrarPrograma();
                    break;
            }
        }
    }

    private static void atualizarPessoa() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID da pessoa a ser atualizada:"));
        Pessoa pessoa = null;

        // Verifica se a pessoa existe antes de atualizá-la
        for (Pessoa p : pessoaDAO.listarPessoas()) {
            if (p.getId() == id) {
                pessoa = p;
                break;
            }
        }

        if (pessoa != null) {
            String nome = JOptionPane.showInputDialog("Digite o novo nome da pessoa:");
            String endereco = JOptionPane.showInputDialog("Digite o novo endereço da pessoa:");
            String telefone = JOptionPane.showInputDialog("Digite o novo telefone da pessoa:");

            pessoa.setNome(nome);
            pessoa.setEndereco(endereco);
            pessoa.setTelefone(telefone);

            pessoaDAO.atualizarPessoa(pessoa);
            JOptionPane.showMessageDialog(null, "Pessoa atualizada com sucesso.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Pessoa não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void criarPessoaFisica() {
        String nome = JOptionPane.showInputDialog("Digite o nome da pessoa física:");
        String endereco = JOptionPane.showInputDialog("Digite o endereço da pessoa física:");
        String telefone = JOptionPane.showInputDialog("Digite o telefone da pessoa física:");
        String cpf = JOptionPane.showInputDialog("Digite o CPF da pessoa física:");

        Pessoa pessoa = new PessoaFisica(nome, endereco, telefone, cpf);
        pessoaDAO.criarPessoa(pessoa);
        JOptionPane.showMessageDialog(null, "Pessoa física criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void criarPessoaJuridica() {
        String nome = JOptionPane.showInputDialog("Digite o nome da pessoa jurídica:");
        String endereco = JOptionPane.showInputDialog("Digite o endereço da pessoa jurídica:");
        String telefone = JOptionPane.showInputDialog("Digite o telefone da pessoa jurídica:");
        String cnpj = JOptionPane.showInputDialog("Digite o CNPJ da pessoa jurídica:");

        Pessoa pessoa = new PessoaJuridica(nome, endereco, telefone, cnpj);
        pessoaDAO.criarPessoa(pessoa);
        JOptionPane.showMessageDialog(null, "Pessoa jurídica criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void listarPessoas() {
        List<Pessoa> pessoas = pessoaDAO.listarPessoas();
        if (!pessoas.isEmpty()) {
            StringBuilder mensagem = new StringBuilder("Lista de Pessoas:\n\n");
            for (Pessoa pessoa : pessoas) {
                mensagem.append("Detalhes da Pessoa:\n");
                mensagem.append("ID: ").append(pessoa.getId()).append("\n");
                mensagem.append("Nome: ").append(pessoa.getNome()).append("\n");
                mensagem.append("Endereço: ").append(pessoa.getEndereco()).append("\n");
                mensagem.append("Telefone: ").append(pessoa.getTelefone()).append("\n");

                if (pessoa instanceof PessoaFisica) {
                    mensagem.append("CPF: ").append(((PessoaFisica) pessoa).getCpf()).append("\n");
                } else if (pessoa instanceof PessoaJuridica) {
                    mensagem.append("CNPJ: ").append(((PessoaJuridica) pessoa).getCnpj()).append("\n");
                }

                mensagem.append("----------------\n");
            }
            JOptionPane.showMessageDialog(null, mensagem.toString(), "Lista de Pessoas", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma pessoa encontrada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private static void deletarPessoa() {
        List<Pessoa> pessoas = pessoaDAO.listarPessoas();
        if (pessoas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma pessoa encontrada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opcoes = new String[pessoas.size()];
        for (int i = 0; i < pessoas.size(); i++) {
            opcoes[i] = pessoas.get(i).getNome();
        }

        String escolha = (String) JOptionPane.showInputDialog(null, "Selecione a pessoa que deseja deletar:", "Deletar Pessoa", JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha != null) {
            Pessoa pessoaSelecionada = null;
            for (Pessoa pessoa : pessoas) {
                if (pessoa.getNome().equals(escolha)) {
                    pessoaSelecionada = pessoa;
                    break;
                }
            }

            if (pessoaSelecionada != null) {
                int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar a pessoa?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    pessoaDAO.deletarPessoa(pessoaSelecionada.getId());
                    JOptionPane.showMessageDialog(null, "Pessoa deletada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pessoa não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void encerrarPrograma() {
        JOptionPane.showMessageDialog(null, "Programa encerrado. Obrigado!", "Fim", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}
