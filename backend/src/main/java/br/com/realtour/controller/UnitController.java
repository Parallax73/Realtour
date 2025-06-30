package br.com.realtour.controller;

import br.com.realtour.entity.Unit;
import br.com.realtour.service.UnitService;
import br.com.realtour.util.CreateUnitDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/units")
public class UnitController {

    @Autowired
    UnitService service;

    @PostMapping("/create")
    public Mono<Unit> createUnit(
            @CookieValue("SecureJWT") String token
           // @RequestBody CreateUnitDTO dto
    ) {
        log.info("Token is " + token);
        CreateUnitDTO dto = new CreateUnitDTO((float) 0.0, (float) 0.0, (float) 0.0, "123") ;

        return service.createUnit(token, dto);
    }

    @PostMapping("/client/add/{unitId}")
    public Mono<Void> saveUnitToClient(
            @CookieValue("SecureJWT") String token,
            @PathVariable String unitId
    ) {
        log.info("Token is " + token);
        return service.saveUnitToClient(token, unitId);
    }

    @PostMapping("/realtor/add/{unitId}")
    public Mono<Void> saveUnitToRealtor(
            @CookieValue("SecureJWT") String token,
            @PathVariable String unitId
    ) {
        log.info("Token is " + token);
        return service.saveUnitToRealtor(token, unitId);
    }

    @GetMapping("/{id}")
    public Mono<Unit> getUnit(@PathVariable("id") String id ){
        return service.getUnitInfo(id);
    }
}