import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO implements OperacoesPessoa {
    private static final String INSERIR_PESSOA = "INSERT INTO pessoa (nome, endereco, telefone, tipo) VALUES (?, ?, ?, ?)";
    private static final String INSERIR_PESSOA_FISICA = "INSERT INTO pessoa_fisica (id, cpf) VALUES (?, ?)";
    private static final String INSERIR_PESSOA_JURIDICA = "INSERT INTO pessoa_juridica (id, cnpj) VALUES (?, ?)";
    private static final String ATUALIZAR_PESSOA = "UPDATE pessoa SET nome = ?, endereco = ?, telefone = ? WHERE id = ?";
    private static final String DELETAR_PESSOA = "DELETE FROM pessoa WHERE id = ?";
    private static final String LISTAR_PESSOAS = "SELECT * FROM pessoa";

    @Override
    public void criarPessoa(Pessoa pessoa) {
        Connection connection = null;
        PreparedStatement stmtPessoa = null;
        PreparedStatement stmtPessoaEspecifica = null;

        try {
            connection = Conexao.getConnection();
            connection.setAutoCommit(false);

            stmtPessoa = connection.prepareStatement(INSERIR_PESSOA, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtPessoa.setString(1, pessoa.getNome());
            stmtPessoa.setString(2, pessoa.getEndereco());
            stmtPessoa.setString(3, pessoa.getTelefone());
            stmtPessoa.setString(4, pessoa instanceof PessoaFisica ? "FISICA" : "JURIDICA");
            stmtPessoa.executeUpdate();

            ResultSet generatedKeys = stmtPessoa.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                pessoa.setId(id);

                if (pessoa instanceof PessoaFisica) {
                    stmtPessoaEspecifica = connection.prepareStatement(INSERIR_PESSOA_FISICA);
                    stmtPessoaEspecifica.setInt(1, id);
                    stmtPessoaEspecifica.setString(2, ((PessoaFisica) pessoa).getCpf());
                    stmtPessoaEspecifica.executeUpdate();
                } else if (pessoa instanceof PessoaJuridica) {
                    stmtPessoaEspecifica = connection.prepareStatement(INSERIR_PESSOA_JURIDICA);
                    stmtPessoaEspecifica.setInt(1, id);
                    stmtPessoaEspecifica.setString(2, ((PessoaJuridica) pessoa).getCnpj());
                    stmtPessoaEspecifica.executeUpdate();
                }

                connection.commit();
            } else {
                throw new SQLException("Falha ao obter o ID gerado para a pessoa.");
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (stmtPessoaEspecifica != null) {
                try {
                    stmtPessoaEspecifica.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmtPessoa != null) {
                try {
                    stmtPessoa.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void atualizarPessoa(Pessoa pessoa) {
        Connection connection = null;
        PreparedStatement stmtPessoa = null;

        try {
            connection = Conexao.getConnection();
            stmtPessoa = connection.prepareStatement(ATUALIZAR_PESSOA);
            stmtPessoa.setString(1, pessoa.getNome());
            stmtPessoa.setString(2, pessoa.getEndereco());
            stmtPessoa.setString(3, pessoa.getTelefone());
            stmtPessoa.setInt(4, pessoa.getId());
            stmtPessoa.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmtPessoa != null) {
                try {
                    stmtPessoa.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deletarPessoa(int id) {
        Connection connection = null;
        PreparedStatement stmtPessoa = null;

        try {
            connection = Conexao.getConnection();
            stmtPessoa = connection.prepareStatement(DELETAR_PESSOA);
            stmtPessoa.setInt(1, id);
            stmtPessoa.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmtPessoa != null) {
                try {
                    stmtPessoa.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Pessoa> listarPessoas() {
        List<Pessoa> pessoas = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmtPessoas = null;
        ResultSet rsPessoas = null;

        try {
            connection = Conexao.getConnection();
            stmtPessoas = connection.prepareStatement(LISTAR_PESSOAS);
            rsPessoas = stmtPessoas.executeQuery();

            while (rsPessoas.next()) {
                int id = rsPessoas.getInt("id");
                String nome = rsPessoas.getString("nome");
                String endereco = rsPessoas.getString("endereco");
                String telefone = rsPessoas.getString("telefone");
                String tipo = rsPessoas.getString("tipo");

                if (tipo.equals("FISICA")) {
                    String cpf = obterCPFPessoaFisica(connection, id);
                    PessoaFisica pessoaFisica = new PessoaFisica(nome, endereco, telefone, cpf);
                    pessoaFisica.setId(id);
                    pessoas.add(pessoaFisica);
                } else {
                    String cnpj = obterCNPJPessoaJuridica(connection, id);
                    PessoaJuridica pessoaJuridica = new PessoaJuridica(nome, endereco, telefone, cnpj);
                    pessoaJuridica.setId(id);
                    pessoas.add(pessoaJuridica);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rsPessoas != null) {
                try {
                    rsPessoas.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmtPessoas != null) {
                try {
                    stmtPessoas.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return pessoas;
    }

    private String obterCPFPessoaFisica(Connection connection, int id) throws SQLException {
        String cpf = null;
        PreparedStatement stmtPessoaFisica = null;
        ResultSet rsPessoaFisica = null;

        try {
            stmtPessoaFisica = connection.prepareStatement("SELECT cpf FROM pessoa_fisica WHERE id = ?");
            stmtPessoaFisica.setInt(1, id);
            rsPessoaFisica = stmtPessoaFisica.executeQuery();
            if (rsPessoaFisica.next()) {
                cpf = rsPessoaFisica.getString("cpf");
            }
        } finally {
            if (rsPessoaFisica != null) {
                rsPessoaFisica.close();
            }
            if (stmtPessoaFisica != null) {
                stmtPessoaFisica.close();
            }
        }

        return cpf;
    }

    private String obterCNPJPessoaJuridica(Connection connection, int id) throws SQLException {
        String cnpj = null;
        PreparedStatement stmtPessoaJuridica = null;
        ResultSet rsPessoaJuridica = null;

        try {
            stmtPessoaJuridica = connection.prepareStatement("SELECT cnpj FROM pessoa_juridica WHERE id = ?");
            stmtPessoaJuridica.setInt(1, id);
            rsPessoaJuridica = stmtPessoaJuridica.executeQuery();
            if (rsPessoaJuridica.next()) {
                cnpj = rsPessoaJuridica.getString("cnpj");
            }
        } finally {
            if (rsPessoaJuridica != null) {
                rsPessoaJuridica.close();
            }
            if (stmtPessoaJuridica != null) {
                stmtPessoaJuridica.close();
            }
        }

        return cnpj;
    }
}
