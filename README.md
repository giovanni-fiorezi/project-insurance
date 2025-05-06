# 🛡️ Insurance API

API RESTful para gerenciamento de **apólices de seguro** e **parcelas**, com processamento de eventos assíncronos via RabbitMQ e persistência em PostgreSQL.

---

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.4**
- **Spring Data JPA**
- **RabbitMQ** (mensageria)
- **PostgreSQL**
- **Testcontainers** (para testes de integração)
- **JUnit 5 + Mockito** (testes unitários)
- **Maven**

---

## 📁 Módulos principais

### 🧾 Apólice (`Apolice`)
- CRUD completo.
- Associação com múltiplas parcelas.
- Upload de CSV para cadastro em lote.

### 💳 Parcela (`Parcela`)
- Processamento de pagamento com atualização automática de:
    - Data de pagamento
    - Situação (`PAGO`, `PENDENTE`, etc)
    - Juros de atraso por tipo de pagamento (cartão, boleto, dinheiro)

### 📩 Mensageria (`PagamentoParcelaListener`)
- Consumidor de mensagens RabbitMQ para pagamento de parcelas.
- Listener: `PagamentoParcelaListener` consome eventos do tipo `PagamentoApoliceParcelaEvent`.

---

## 🧪 Testes

### ✅ Testes unitários
- Cobrem lógica de negócio nos services e utilitários com JUnit e Mockito.

### 🧪 Testes de integração
- Utiliza **Testcontainers** para subir PostgreSQL real em ambiente de teste.
- Exemplo de teste com persistência real (`ApoliceRepositoryTest`).

---

## 🧵 Exemplo de fluxo: Pagamento via evento

1. Sistema envia evento `PagamentoApoliceParcelaEvent` para fila `PAGAMENTO_PARCELA_QUEUE`.
2. O listener consome o evento.
3. Para cada `PagamentoParcelaItemEvent`:
    - A parcela é buscada no banco.
    - Atualizada com a forma de pagamento, data e juros.
    - Persistida como `PAGO`.

---

## ⚙️ Como rodar localmente

1. **Pré-requisitos:**
    - Java 17+
    - Docker e Docker Compose (para subir RabbitMQ e PostgreSQL)
    - Maven

2. **Subir infraestrutura (RabbitMQ + PostgreSQL):**
   ```bash
   docker-compose up -d
