public class PessoaJuridica extends Pessoa {
    private String cnpj;

    public PessoaJuridica(String nome, String endereco, String telefone, String cnpj) {
        super(nome, endereco, telefone);
        this.cnpj = cnpj;
    }

    @Override
    public void imprimirDetalhes() {
        System.out.println("Detalhes da Pessoa Jurídica:");
        System.out.println("ID: " + getId());
        System.out.println("Nome: " + getNome());
        System.out.println("Endereço: " + getEndereco());
        System.out.println("Telefone: " + getTelefone());
        System.out.println("CNPJ: " + cnpj);
        System.out.println("----------------");
    }

    @Override
    public void realizarAcaoEspecifica() {
        System.out.println("Ação específica para Pessoa Jurídica");
    }

    // getter e setter para cnpj

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
