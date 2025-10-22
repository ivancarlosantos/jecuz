# 🗃️ Modelo Conceptual — Sistema de Diaristas

## 👨‍👩‍👧‍👦 Entidades Principais

### 🧑‍🧹 1. Diarista

**Campos principais:**

- `id` (PK) - Identificador único
- `nomeCompleto` - Nome completo da diarista
- `dataNascimento` - Data de nascimento
- `telefone` - Contacto telefónico
- `email` - E-mail para contacto
- `bi` - Número do Bilhete de Identidade
- `endereco` - Morada completa
- `fotoPerfil` - Fotografia para perfil
- `nivelExperiencia` (iniciante, médio, avançado)
- `avaliacaoMedia` - Média das avaliações
- `estadoConta` (activo, suspenso, pendente)
- `dataRegisto` - Data de registo na plataforma

**Relacionamentos:**

- 1 diarista → N ServiçosPrestados
- 1 diarista → N Avaliações
- 1 diarista → N Disponibilidades
- 1 diarista → 1 ContaFinanceira

### 👤 2. Cliente

**Campos principais:**

- `id` (PK)
- `nomeCompleto`
- `telefone`
- `email`
- `endereco`
- `dataRegisto`

**Relacionamentos:**

- 1 cliente → N Reservas
- 1 cliente → N Avaliações

### 🧾 3. Serviço

**Campos principais:**

- `id` (PK)
- `nome` (Limpeza geral, Passar roupa, Cozinhar, etc.)
- `descricao`
- `precoBase`
- `duracaoPadrao`
- `ativo` (boolean)

**Relacionamentos:**

- 1 serviço → N Reservas
- 1 serviço → N ServiçosPrestados

### 📅 4. Reserva

**Campos principais:**

- `id` (PK)
- `cliente_id` (FK → Cliente)
- `diarista_id` (FK → Diarista)
- `servico_id` (FK → Serviço)
- `dataReserva`
- `horaInicio`
- `horaFim`
- `enderecoExecucao`
- `estado` (pendente, confirmado, concluído, cancelado)
- `precoFinal`

**Relacionamentos:**

- 1 reserva → 1 Pagamento
- 1 reserva → N Avaliações

### 💸 5. Pagamento

**Campos principais:**

- `id` (PK)
- `reserva_id` (FK → Reserva)
- `metodoPagamento` (Multicaixa, referência bancária, PayPay, etc.)
- `valor`
- `dataPagamento`
- `estado` (pendente, confirmado, devolvido)
- `comissaoPlataforma`
- `valorDiarista`

### ⭐ 6. Avaliação

**Campos principais:**

- `id` (PK)
- `cliente_id` (FK → Cliente)
- `diarista_id` (FK → Diarista)
- `reserva_id` (FK → Reserva)
- `pontuacao` (1 a 5)
- `comentario`
- `dataAvaliacao`

### 📆 7. Disponibilidade

**Campos principais:**

- `id` (PK)
- `diarista_id` (FK → Diarista)
- `diaSemana` (Segunda, Terça, etc.)
- `horaInicio`
- `horaFim`

### 🏦 8. ContaFinanceira

**Campos principais:**

- `id` (PK)
- `diarista_id` (FK → Diarista)
- `banco`
- `iban`
- `saldoDisponivel`
- `dataAtualizacao`

### 📍 9. Localização

**Campos principais:**

- `id` (PK)
- `diarista_id` (FK → Diarista)
- `latitude`
- `longitude`
- `municipio`
- `bairro`

## 🔗 Relacionamentos Principais

| Entidade Principal | Relaciona-se com                                             | Tipo de Relação |
| ------------------ | ------------------------------------------------------------ | --------------- |
| **Diarista**       | ServiçoPrestado, Avaliação, Disponibilidade, ContaFinanceira | 1:N / 1:1       |
| **Cliente**        | Reserva, Avaliação                                           | 1:N             |
| **Reserva**        | Pagamento, Avaliação                                         | 1:1 / 1:N       |
| **Serviço**        | Reserva                                                      | 1:N             |

---
