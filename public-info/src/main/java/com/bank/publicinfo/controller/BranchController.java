package com.bank.publicinfo.controller;

import com.bank.publicinfo.service.BranchService;
import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;
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
 * Контроллер для {@link BranchDto}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
@Tag(name = "Филиалы", description = "Позволяет управлять информацией о филиалах банка")
public class BranchController {

    private final BranchService service;

    /**
     * @param id технический идентификатор {@link BranchEntity}
     * @return {@link ResponseEntity}, {@link BankDetailsDto} и HttpStatus.OK
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Чтение записи",
            description = "Позволяет получить информацию о филиале по его id"
    )
    public ResponseEntity<BranchDto> readById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    /**
     * @param ids лист технических идентификаторов {@link BranchEntity}
     * @return {@link ResponseEntity}, лист {@link BranchDto} и HttpStatus.OK
     */
    @GetMapping("/read/all")
    @Operation(
            summary = "Чтение набора записей",
            description = "Позволяет получить список филиалов по списку идентификаторов ids"
    )
    public ResponseEntity<List<BranchDto>> readAllById(@RequestParam List<Long> ids) {
        return ResponseEntity.ok().body(service.findAllById(ids));
    }

    /**
     * @param branch {@link BranchDto}
     * @return {@link ResponseEntity}, {@link BranchDto} и HttpStatus.OK
     */
    @PostMapping("/create")
    @Operation(
            summary = "Создание записи",
            description = "Позволяет создать новую запись о филиале"
    )
    public ResponseEntity<BranchDto> create(@RequestBody BranchDto branch) {
        return ResponseEntity.ok().body(service.create(branch));
    }

    /**
     * @param id     технический идентификатор {@link BranchEntity}
     * @param branch {@link BranchDto}
     * @return {@link ResponseEntity}, {@link BranchDto} и HttpStatus.OK
     */
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Обновление записи",
            description = "Позволяет обновить информацию о филиале по его id"
    )
    public ResponseEntity<BranchDto> update(@PathVariable("id") Long id,
                                             @RequestBody BranchDto branch) {
        return ResponseEntity.ok().body(service.update(id, branch));
    }
}
