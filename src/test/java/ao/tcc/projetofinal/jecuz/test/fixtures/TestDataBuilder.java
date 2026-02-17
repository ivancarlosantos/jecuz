package ao.tcc.projetofinal.jecuz.test.fixtures;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Construtor fluente de dados para testes.
 * Utiliza Faker para gerar dados realistas.
 */
public class TestDataBuilder {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    // ==================== Cliente ====================

    public static ClienteBuilder clienteBuilder() {
        return new ClienteBuilder();
    }

    public static class ClienteBuilder {
        private Long id;
        private String nome;
        private LocalDate nascimento;
        private String telefone;
        private String numeroBi;
        private String email;
        private LocalDateTime dataRegistro;
        private ClienteStatus status;

        public ClienteBuilder() {
            this.nome = faker.name().fullName();
            this.nascimento = LocalDate.of(1980 + faker.random().nextInt(40),
                    faker.random().nextInt(12) + 1,
                    faker.random().nextInt(28) + 1);
            this.telefone = faker.phoneNumber().cellPhone();
            this.numeroBi = faker.bothify("0002505BA012");
            this.email = faker.internet().emailAddress();
            this.dataRegistro = LocalDateTime.now();
            this.status = ClienteStatus.ATIVO;
        }

        public ClienteBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ClienteBuilder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        public ClienteBuilder withNascimento(LocalDate nascimento) {
            this.nascimento = nascimento;
            return this;
        }

        public ClienteBuilder withTelefone(String telefone) {
            this.telefone = telefone;
            return this;
        }

        public ClienteBuilder withNumeroBi(String numeroBi) {
            this.numeroBi = numeroBi;
            return this;
        }

        public ClienteBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ClienteBuilder withStatus(ClienteStatus status) {
            this.status = status;
            return this;
        }

        public Cliente buildEntity() {
            return Cliente.builder()
                    .id(id)
                    .nome(nome)
                    .nascimento(nascimento)
                    .telefone(telefone)
                    .numeroBi(numeroBi)
                    .email(email)
                    .dataRegistro(dataRegistro)
                    .status(status)
                    .build();
        }

        public ClienteRequest buildRequest() {
            return ClienteRequest.builder()
                    .nome(nome)
                    .nascimento(nascimento.toString())
                    .telefone(telefone)
                    .numeroBi(numeroBi)
                    .email(email)
                    .build();
        }

        public ClienteResponse buildResponse() {
            return ClienteResponse.builder()
                    .nome(nome)
                    .nascimento(nascimento.toString())
                    .telefone(telefone)
                    .numeroBi(numeroBi)
                    .email(email)
                    .build();
        }
    }

    // ==================== Diarista ====================

    public static DiaristaBuilder diaristaBuilder() {
        return new DiaristaBuilder();
    }

    public static class DiaristaBuilder {
        private Long id;
        private String nome;
        private LocalDate nascimento;
        private String telefone;
        private String numeroBi;
        private String email;
        private Double taxaDiaria;
        private LocalDateTime dataRegistro;

        public DiaristaBuilder() {
            this.nome = faker.name().fullName();
            this.nascimento = LocalDate.of(1990 + faker.random().nextInt(30),
                                                 faker.random().nextInt(12) + 1,
                                             faker.random().nextInt(28) + 1);
            this.telefone = faker.phoneNumber().cellPhone();
            this.numeroBi = faker.bothify("0002505BA012");
            this.email = faker.internet().emailAddress();
            this.taxaDiaria = 50.0 + faker.random().nextDouble() * 100;
            this.dataRegistro = LocalDateTime.now();
        }

        public DiaristaBuilder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        public DiaristaBuilder withTaxaDiaria(Double taxaDiaria) {
            this.taxaDiaria = taxaDiaria;
            return this;
        }

        public Diarista buildEntity() {
            return Diarista.builder()
                    .id(id)
                    .nome(nome)
                    .nascimento(nascimento.toString())
                    .telefone(telefone)
                    .numeroBi(numeroBi)
                    .email(email)
                    .taxaDiaria(taxaDiaria)
                    .dataRegistro(dataRegistro)
                    .build();
        }

        public DiaristaRequest buildRequest() {
            return DiaristaRequest.builder()
                    .nome(nome)
                    .nascimento(nascimento.toString())
                    .telefone(telefone)
                    .numeroBi(numeroBi)
                    .email(email)
                    .build();
        }
    }
}
