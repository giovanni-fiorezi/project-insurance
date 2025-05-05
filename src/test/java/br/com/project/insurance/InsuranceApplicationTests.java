package br.com.project.insurance;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.dto.request.ParcelaRequest;
import br.com.project.insurance.dto.response.ApoliceResponse;
import br.com.project.insurance.dto.response.ParcelaResponse;
import br.com.project.insurance.entity.Apolice;
import br.com.project.insurance.entity.enums.FormaPagamento;
import br.com.project.insurance.entity.enums.SituacaoApolice;
import br.com.project.insurance.mapper.ApoliceMapper;
import br.com.project.insurance.repository.ApoliceRepository;
import br.com.project.insurance.service.exception.RequisicaoInvalidaException;
import br.com.project.insurance.service.impl.ApoliceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InsuranceApplicationTests {

	@Mock
	private ApoliceRepository apoliceRepository;

	private ApoliceMapper mapper = new ApoliceMapper();

	@InjectMocks
	private ApoliceServiceImpl apoliceService;

	@BeforeEach
	void setUp() {
		apoliceService = new ApoliceServiceImpl(apoliceRepository, mapper);
	}

	@Test
	void criarApoliceComSucesso() {

		ParcelaRequest parcelaRequest = new ParcelaRequest(1, new BigDecimal("1500.00"), FormaPagamento.CARTAO,
				LocalDate.now());
		Integer usuarioId = 1;

		List<ParcelaRequest> parcelaRequestList = new ArrayList<>();
		parcelaRequestList.add(parcelaRequest);

		ApoliceRequest apoliceRequest = new ApoliceRequest(1, "APOLICE TESTE 001", 430292922,
				SituacaoApolice.ATIVA, new BigDecimal("3000.00"), parcelaRequestList);

		ArgumentCaptor<Apolice> apoliceCaptor = ArgumentCaptor.forClass(Apolice.class);

		apoliceService.criarApolice(apoliceRequest, usuarioId);

		Mockito.verify(apoliceRepository).save(apoliceCaptor.capture());

		Apolice apoliceSalva = apoliceCaptor.getValue();

		assertEquals("APOLICE TESTE 001", apoliceSalva.getDescricao());
		assertEquals(430292922, apoliceSalva.getCpf());
		assertEquals(SituacaoApolice.ATIVA, apoliceSalva.getSituacao());
		assertEquals(new BigDecimal("3000.00"), apoliceSalva.getPremioTotal());
		assertEquals(usuarioId, apoliceSalva.getUsuarioCriacaoRegistro());
		
		assertNotNull(apoliceSalva.getParcelas());
		assertEquals(1, apoliceSalva.getParcelas().size());
		assertEquals(new BigDecimal("1500.00"), apoliceSalva.getParcelas().get(0).getPremio());
		assertEquals(FormaPagamento.CARTAO, apoliceSalva.getParcelas().get(0).getFormaPagamento());
	}

	@Test
	void criarApoliceQuandoDadosInvalidosEntaoLancaRequisicaoInvalidaException() {
		ParcelaRequest parcelaRequest = new ParcelaRequest(1, new BigDecimal("1500.00"), FormaPagamento.CARTAO,
				LocalDate.now());

		List<ParcelaRequest> parcelaRequestList = new ArrayList<>();
		parcelaRequestList.add(parcelaRequest);
		ApoliceRequest apoliceRequest = new ApoliceRequest(null, "APOLICE TESTE 001", null,
				SituacaoApolice.ATIVA, new BigDecimal("3000.00"), parcelaRequestList);

		Apolice apolice = new Apolice();

		Mockito.doThrow(DataIntegrityViolationException.class)
				.when(apoliceRepository)
				.save(Mockito.any(Apolice.class));

		assertThrows(RequisicaoInvalidaException.class, () ->
				apoliceService.criarApolice(apoliceRequest, null));
	}

	@Test
	void deveRetornarApoliceQuandoIdInformadoEValido() {
		List<ParcelaResponse> parcelaResponses = new ArrayList<>();

		Integer apoliceId = 1;

		Apolice apolice = new Apolice();
		apolice.setId(1);
		apolice.setDescricao("APOLICE TESTE");
		apolice.setCpf(123456789);
		apolice.setSituacao(SituacaoApolice.ATIVA);
		apolice.setPremioTotal(new BigDecimal("2500.00"));
		apolice.setDataCriacaoRegistro(LocalDate.now());
		apolice.setUsuarioCriacaoRegistro(1);
		apolice.setParcelas(new ArrayList<>());

		ApoliceResponse response = new ApoliceResponse(
				1,
				"APOLICE TESTE",
				123456789,
				SituacaoApolice.ATIVA,
				new BigDecimal("2500.00"),
				LocalDate.now(),
				null,
				1,
				null,
				parcelaResponses
		);

		Mockito.when(apoliceRepository.findById(apoliceId)).thenReturn(Optional.of(apolice));
		List<ApoliceResponse> result = apoliceService.buscaApolicePorIdOuTodas(1);

		assertEquals(1, result.size());

		ApoliceResponse apoliceResponse = result.get(0);
		assertEquals(1, response.id());
		assertEquals("APOLICE TESTE", response.descricao());
		assertEquals(123456789, response.cpf());
		assertEquals(SituacaoApolice.ATIVA, response.situacao());
		assertEquals(new BigDecimal("2500.00"), response.premioTotal());
		assertEquals(LocalDate.now(), response.dataCriacao());
		assertEquals(1, response.usuarioCriacao());
		assertTrue(response.parcelas().isEmpty());

		Mockito.verify(apoliceRepository).findById(1);
	}

	@Test
	void deveRetornarTodasAsApoliceIdNaoInformado() {
		Apolice apolice1 = new Apolice();
		Apolice apolice2 = new Apolice();

		ApoliceResponse response1 = new ApoliceResponse();
		ApoliceResponse response2 = new ApoliceResponse();

		List<Apolice> apolices = List.of(apolice1, apolice2);
		Mockito.when(apoliceRepository.findAll()).thenReturn(apolices);
		Mockito.when(mapper.toResponse(apolice1)).thenReturn(response1);
		Mockito.when(mapper.toResponse(apolice2)).thenReturn(response2);

		List<ApoliceResponse> result = apoliceService.buscaApolicePorIdOuTodas(null);

		assertEquals(2, result.size());
		assertTrue(result.containsAll(List.of(response1, response2)));
		Mockito.verify(apoliceRepository).findAll();
	}

}
