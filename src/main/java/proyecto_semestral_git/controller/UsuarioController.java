
package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.UsuarioModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final ConcurrentMap<Integer, UsuarioModel> repo = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @GetMapping
    public List<UsuarioModel> listar() {
        return new ArrayList<>(repo.values());
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<UsuarioModel> obtener(@PathVariable int id) {
        UsuarioModel p = repo.get(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PostMapping("/crear")
    public ResponseEntity<UsuarioModel> crear(@RequestBody UsuarioModel usuario) {
        int id = idGenerator.getAndIncrement();
        usuario.setId(id);
        repo.put(id, usuario);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/usuarios/" + id));
        return new ResponseEntity<>(usuario, headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioModel> actualizar(@PathVariable int id, @RequestBody UsuarioModel usuario) {
        UsuarioModel existente = repo.get(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setNombre(usuario.getNombre());
        existente.setEdad(usuario.getEdad());
        existente.setCorreo(usuario.getCorreo());
        repo.put(id, existente);
        return ResponseEntity.ok(existente);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        UsuarioModel removed = repo.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
