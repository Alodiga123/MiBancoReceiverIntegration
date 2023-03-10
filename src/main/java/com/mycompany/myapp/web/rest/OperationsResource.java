package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Operations;
import com.mycompany.myapp.repository.OperationsRepository;
import com.mycompany.myapp.response.Response;
import com.mycompany.myapp.service.dto.OperationDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Operations}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OperationsResource {

    private final Logger log = LoggerFactory.getLogger(OperationsResource.class);

    private static final String ENTITY_NAME = "operations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperationsRepository operationsRepository;

    public OperationsResource(OperationsRepository operationsRepository) {
        this.operationsRepository = operationsRepository;
    }

    /**
     * {@code POST  /operations} : Create a new operations.
     *
     * @param operations the operations to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operations, or with status {@code 400 (Bad Request)} if the operations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operations")
    public ResponseEntity<Response> createOperations(@RequestBody OperationDTO operations) throws URISyntaxException {
        log.debug("REST request to save Operations : {}", operations);
        Response response = new Response();
        boolean valid = Validar(operations);
        if (!valid) {
            return ResponseEntity.badRequest().body(response);
        }
        Operations operation = new Operations();
        operation.setCedulaBeneficiario(operations.getCedulaBeneficiario());
        operation.setBancoEmisor(operations.getCedulaBeneficiario());
        operation.setTelefonoEmisor(operations.getTelefonoEmisor());
        operation.setTelefonoBeneficiario(operations.getTelefonoBeneficiario());
        operation.setMonto(operations.getMonto());
        operation.setBancoEmisor(operations.getBancoEmisor());
        operation.setConcepto(operations.getConcepto());
        operation.setReferencia(operations.getReferencia());
        operation.setFechaHora(operations.getFechaHora());
        Operations result = operationsRepository.save(operation);
        response.setSuccess(true);
        return ResponseEntity.ok().body(response);
    }

    /**
     * {@code PUT  /operations/:id} : Updates an existing operations.
     *
     * @param id the id of the operations to save.
     * @param operations the operations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operations,
     * or with status {@code 400 (Bad Request)} if the operations is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operations/{id}")
    public ResponseEntity<Operations> updateOperations(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Operations operations
    ) throws URISyntaxException {
        log.debug("REST request to update Operations : {}, {}", id, operations);
        if (operations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Operations result = operationsRepository.save(operations);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operations.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operations/:id} : Partial updates given fields of an existing operations, field will ignore if it is null
     *
     * @param id the id of the operations to save.
     * @param operations the operations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operations,
     * or with status {@code 400 (Bad Request)} if the operations is not valid,
     * or with status {@code 404 (Not Found)} if the operations is not found,
     * or with status {@code 500 (Internal Server Error)} if the operations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Operations> partialUpdateOperations(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Operations operations
    ) throws URISyntaxException {
        log.debug("REST request to partial update Operations partially : {}, {}", id, operations);
        if (operations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Operations> result = operationsRepository
            .findById(operations.getId())
            .map(existingOperations -> {
                if (operations.getCedulaBeneficiario() != null) {
                    existingOperations.setCedulaBeneficiario(operations.getCedulaBeneficiario());
                }
                if (operations.getTelefonoEmisor() != null) {
                    existingOperations.setTelefonoEmisor(operations.getTelefonoEmisor());
                }
                if (operations.getTelefonoBeneficiario() != null) {
                    existingOperations.setTelefonoBeneficiario(operations.getTelefonoBeneficiario());
                }
                if (operations.getMonto() != null) {
                    existingOperations.setMonto(operations.getMonto());
                }
                if (operations.getBancoEmisor() != null) {
                    existingOperations.setBancoEmisor(operations.getBancoEmisor());
                }
                if (operations.getConcepto() != null) {
                    existingOperations.setConcepto(operations.getConcepto());
                }
                if (operations.getReferencia() != null) {
                    existingOperations.setReferencia(operations.getReferencia());
                }
                if (operations.getFechaHora() != null) {
                    existingOperations.setFechaHora(operations.getFechaHora());
                }

                return existingOperations;
            })
            .map(operationsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operations.getId().toString())
        );
    }

    /**
     * {@code GET  /operations} : get all the operations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operations in body.
     */
    @GetMapping("/operations")
    public ResponseEntity<List<Operations>> getAllOperations(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Operations");
        Page<Operations> page = operationsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /operations/:id} : get the "id" operations.
     *
     * @param id the id of the operations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operations/{id}")
    public ResponseEntity<Operations> getOperations(@PathVariable Long id) {
        log.debug("REST request to get Operations : {}", id);
        Optional<Operations> operations = operationsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(operations);
    }

    /**
     * {@code DELETE  /operations/:id} : delete the "id" operations.
     *
     * @param id the id of the operations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operations/{id}")
    public ResponseEntity<Void> deleteOperations(@PathVariable Long id) {
        log.debug("REST request to delete Operations : {}", id);
        operationsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private boolean Validar(OperationDTO operations) {
        if (
            operations.getCedulaBeneficiario() == null ||
            " ".equals(operations.getCedulaBeneficiario()) ||
            operations.getTelefonoEmisor() == null ||
            " ".equals(operations.getTelefonoEmisor()) ||
            operations.getTelefonoBeneficiario() == null ||
            " ".equals(operations.getTelefonoBeneficiario()) ||
            operations.getMonto() == null ||
            " ".equals(operations.getMonto()) ||
            operations.getBancoEmisor() == null ||
            " ".equals(operations.getBancoEmisor()) ||
            operations.getConcepto() == null ||
            " ".equals(operations.getConcepto()) ||
            operations.getReferencia() == null ||
            " ".equals(operations.getReferencia()) ||
            operations.getFechaHora() == null ||
            " ".equals(operations.getFechaHora())
        ) {
            return false;
        }

        return true;
    }
}
