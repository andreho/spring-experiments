package net.andreho.spring.experiments.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RestController("/api/v1/in-memory")
public class InMemoryRestController {

    private final Map<String, Object> database = new ConcurrentHashMap<>();

    @ResponseBody
    @GetMapping("/{key}")
    Mono<Object> get(@NotBlank @PathVariable("key") String key) {
        return Mono.just(database.getOrDefault(key, "undefined"));
    }

    @ResponseBody
    @PostMapping("/{key}")
    Mono<Object> set(@NotBlank @PathVariable("key") String key, @RequestBody Object value) {
        return Mono.justOrEmpty(database.putIfAbsent(key, value));
    }

    @ResponseBody
    @PutMapping("/{key}")
    Mono<Object> put(@NotBlank @PathVariable("key") String key, @RequestBody Object value) {
        return Mono.justOrEmpty(database.put(key, value));
    }

    @ResponseBody
    @DeleteMapping("/{key}")
    Mono<Object> delete(@NotBlank @PathVariable("key") String key) {
        return Mono.justOrEmpty(database.remove(key));
    }
}
