import java.util.List;

public interface OperacoesPessoa {
    void criarPessoa(Pessoa pessoa);
    void atualizarPessoa(Pessoa pessoa);
    List<Pessoa> listarPessoas();
    void deletarPessoa(int id);


}
