package ao.tcc.projetofinal.jecuz.dto.cliente;

public class ClientePattern {

    private String nome;

    private String email;

    public ClientePattern() {}

    public ClientePattern(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public static ClientePatternBuilder builder() {
        return new ClientePatternBuilder();
    }

    public static class ClientePatternBuilder {

        private String nome;

        private String email;

        public ClientePatternBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public ClientePatternBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ClientePattern build() {
            return new ClientePattern(nome, email);
        }
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
