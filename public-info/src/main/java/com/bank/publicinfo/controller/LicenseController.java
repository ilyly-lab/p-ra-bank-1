package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.LicenseEntity;
import com.bank.publicinfo.service.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для {@link LicenseDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/license")
@Tag(name = "Лицензии", description = "Позволяет управлять информацией о лицензиях")
public class LicenseController {

    private final LicenseService service;

    /**
     * @param id технический идентификатор {@link LicenseEntity}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Чтение записи",
            description = "Позволяет получить информацию о лицензии по ее id"
    )
    public ResponseEntity<LicenseDto> readById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    /**
     * @param ids лист технических идентификаторов {@link LicenseEntity}
     * @return {@link ResponseEntity}, лист {@link LicenseDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(
            summary = "Чтение набора записей",
            description = "Позволяет получить список лицензий по списку идентификаторов ids"
    )
    public ResponseEntity<List<LicenseDto>> readAllById(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.findAllById(ids));
    }

    /**
     * @param license {@link LicenseDto}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(
            summary = "Создание записи",
            description = "Позволяет создать новую запись о лицензии"
    )
    public ResponseEntity<LicenseDto> create(@RequestBody LicenseDto license) {
        return ResponseEntity.ok().body(service.create(license));
    }

    /**
     * @param id      технический идентификатор {@link LicenseEntity}
     * @param license {@link LicenseDto}
     * @return {@link ResponseEntity}, {@link LicenseDto} и HttpStatus.OK
     */
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Обновление записи",
            description = "Позволяет обновить информацию о лицензии по ее id"
    )
    public ResponseEntity<LicenseDto> update(@PathVariable("id") Long id,
                                              @RequestBody LicenseDto license) {
        return ResponseEntity.ok().body(service.update(id, license));
    }
}
