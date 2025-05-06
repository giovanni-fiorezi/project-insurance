package br.com.project.insurance.repository;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.dto.request.ParcelaRequest;
import br.com.project.insurance.entity.Apolice;
import br.com.project.insurance.entity.enums.FormaPagamento;
import br.com.project.insurance.entity.enums.SituacaoApolice;
import br.com.project.insurance.mapper.ApoliceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class ApoliceRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testedb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ApoliceRepository repository;

    @Test
    void deveSalvarApolice() {
        ParcelaRequest parcelaRequest = new ParcelaRequest(1, new BigDecimal("1500.00"), FormaPagamento.CARTAO,
                LocalDate.now());

        Integer usuarioId = 1;

        List<ParcelaRequest> parcelaRequestList = new ArrayList<>();
        parcelaRequestList.add(parcelaRequest);

        ApoliceRequest apoliceRequest = new ApoliceRequest(1, "APOLICE TESTE 001", 430292922,
                SituacaoApolice.ATIVA, new BigDecimal("3000.00"), parcelaRequestList);

        Apolice apolice = ApoliceMapper.toEntity(apoliceRequest, usuarioId);

        repository.save(apolice);
        assertThat(repository.findById(1)).isPresent();
    }

}