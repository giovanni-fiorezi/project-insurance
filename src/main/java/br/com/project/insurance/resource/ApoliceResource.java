package br.com.project.insurance.resource;

import br.com.project.insurance.dto.request.ApoliceRequest;
import br.com.project.insurance.service.ApoliceService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apolice")
public class ApoliceResource {

    private final ApoliceService apoliceService;

    public ApoliceResource(ApoliceService apoliceService) {
        this.apoliceService = apoliceService;
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<Void> criarApolice(@RequestBody ApoliceRequest dto, @PathParam("usuarioId") Integer usuarioId) {
        apoliceService.criarApolice(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Void> atualizarApolice(@RequestBody ApoliceRequest dto, @PathParam("usuarioId") Integer usuarioId) {
        apoliceService.atualizarApolice(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
