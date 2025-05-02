package br.com.project.insurance.resource;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.dto.response.ApoliceResponse;
import br.com.project.insurance.service.ApoliceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/apolice")
public class ApoliceResource {

    private final ApoliceService apoliceService;

    public ApoliceResource(ApoliceService apoliceService) {
        this.apoliceService = apoliceService;
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<Void> criarApolice(@RequestBody ApoliceRequest dto, @PathVariable("usuarioId") Integer usuarioId) {
        apoliceService.criarApolice(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Void> atualizarApolice(@RequestBody ApoliceRequest dto, @PathVariable("usuarioId") Integer usuarioId) {
        apoliceService.atualizarApolice(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{apoliceId}")
    public ResponseEntity<List<ApoliceResponse>> buscaApolicePorIdOuTodas (@PathVariable(value = "apoliceId", required = false) Integer apoliceId) {
        List<ApoliceResponse> response = apoliceService.buscaApolicePorIdOuTodas(apoliceId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<ApoliceResponse>> buscarTodasAsApolices () {
        List<ApoliceResponse> response = apoliceService.buscaApolicePorIdOuTodas(null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file")MultipartFile file) {
        apoliceService.uploadCsv(file);
        return ResponseEntity.status(HttpStatus.OK).body("Arquivo processado com sucesso");
    }
}
