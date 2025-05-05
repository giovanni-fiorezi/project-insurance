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
import br.com.project.insurance.service.exception.ApoliceNaoEncontradaException;
import br.com.project.insurance.service.exception.ProcessamentoArquivoException;
import br.com.project.insurance.service.exception.RequisicaoInvalidaException;
import br.com.project.insurance.service.impl.ApoliceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		apoliceService = new ApoliceServiceImpl(apoliceRepository);
	}

	@Test
	void criarApoliceComSucesso() {
		ParcelaRequest parcelaRequest = buildParcela();
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
		ParcelaRequest parcelaRequest = buildParcela();

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

		ApoliceResponse response = buildApoliceResponse(parcelaResponses);

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
		apolice1.setId(1);
		apolice1.setDescricao("APOLICE TESTE 1");
		apolice1.setCpf(12345556);
		apolice1.setSituacao(SituacaoApolice.ATIVA);
		apolice1.setPremioTotal(new BigDecimal("340.00"));
		apolice1.setDataCriacaoRegistro(LocalDate.now());

		Apolice apolice2 = new Apolice();
		apolice1.setId(2);
		apolice1.setDescricao("APOLICE TESTE 2");
		apolice1.setCpf(55545556);
		apolice1.setSituacao(SituacaoApolice.ATIVA);
		apolice1.setPremioTotal(new BigDecimal("1400.00"));
		apolice1.setDataCriacaoRegistro(LocalDate.now());

		ApoliceResponse response1 = buildApoliceResponse(null);
		ApoliceResponse response2 = buildApoliceResponse(null);

		List<Apolice> apolices = List.of(apolice1, apolice2);

		ApoliceResponse mapperResponse1 = mapper.toResponse(apolice1);
		ApoliceResponse mapperResponse2 = mapper.toResponse(apolice2);

		Mockito.when(apoliceRepository.findAll()).thenReturn(apolices);

		List<ApoliceResponse> result = apoliceService.buscaApolicePorIdOuTodas(null);

		assertEquals(2, result.size());
		Mockito.verify(apoliceRepository).findAll();
	}

	@Test
	void deveLancarExcecaoQuandoIdDeApoliceNaoExistir() {
		Integer idInexistente = 999;

		Mockito.when(apoliceRepository.findById(idInexistente)).thenReturn(Optional.empty());

		Assertions.assertThrows(
				ApoliceNaoEncontradaException.class,
				() -> apoliceService.buscaApolicePorIdOuTodas(idInexistente),
				"Id de apolice não existe"
		);

		Mockito.verify(apoliceRepository).findById(idInexistente);
	}

	@Test
	void deveAtualizarUmaApolice () {
		Integer apoliceId = 1;
		Integer usuarioId = 10;

		// Criar request mockado com valores simulados
		ApoliceRequest request = new ApoliceRequest(
				apoliceId,
				"APOLICE ",
				1234567890,
				SituacaoApolice.ATIVA,
				new BigDecimal("999.99"),
				List.of()
		);

		Apolice apoliceExistente = new Apolice();
		apoliceExistente.setId(apoliceId);

		Mockito.when(apoliceRepository.findById(apoliceId))
				.thenReturn(Optional.of(apoliceExistente));

		Mockito.when(apoliceRepository.save(Mockito.any(Apolice.class)))
				.thenReturn(apoliceExistente);

		assertDoesNotThrow(() -> apoliceService.atualizarApolice(request, usuarioId));

		Mockito.verify(apoliceRepository).save(Mockito.any(Apolice.class));
	}

	@Test
	void deveLancarExcecaoQuandoIdDaRequestForNull() {
		ApoliceRequest request = Mockito.mock(ApoliceRequest.class);
		Mockito.when(request.id()).thenReturn(null);

		assertThrows(RequisicaoInvalidaException.class, () -> {
			apoliceService.atualizarApolice(request, 1);
		});
	}

	@Test
	void deveLancarExcecaoQuandoApoliceNaoExistir() {
		Integer apoliceId = 10;
		ApoliceRequest request = Mockito.mock(ApoliceRequest.class);
		Mockito.when(request.id()).thenReturn(apoliceId);

		Mockito.when(apoliceRepository.findById(apoliceId)).thenReturn(Optional.empty());

		assertThrows(ApoliceNaoEncontradaException.class, () -> {
			apoliceService.atualizarApolice(request, 1);
		});

		Mockito.verify(apoliceRepository).findById(apoliceId);
	}

	@Test
	void deveSalvarArquivoComSucesso() throws IOException {
		String conteudo = "id,nome,test";
		byte[] bytes = conteudo.getBytes();

		MockMultipartFile mockFile = new MockMultipartFile(
				"file", "test.csv", "text/csv", new ByteArrayInputStream(bytes)
		);

		String resultado = apoliceService.uploadCsv(mockFile);
		assertEquals("Arquivo salvo com sucesso.", resultado);

		Path filePath = Paths.get("src/main/resources/test.csv");
		assertTrue(Files.exists(filePath));

		Files.deleteIfExists(filePath);
	}

	@Test
	void deveLancarExcecaoAoFalharUpload() throws IOException {
		MultipartFile fileMock = Mockito.mock(MultipartFile.class);
		Mockito.when(fileMock.getOriginalFilename()).thenReturn("test.csv");
		Mockito.when(fileMock.getInputStream()).thenThrow(new IOException("Falha no stream"));

		assertThrows(ProcessamentoArquivoException.class, () -> {
			apoliceService.uploadCsv(fileMock);
		});
	}

	private static ParcelaRequest buildParcela() {
        return new ParcelaRequest(1, new BigDecimal("1500.00"), FormaPagamento.CARTAO,
				LocalDate.now());
	}

	private static ApoliceResponse buildApoliceResponse(List<ParcelaResponse> parcelaResponses) {
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
		return response;
	}

}
