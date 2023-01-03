package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Operations;
import com.mycompany.myapp.repository.OperationsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OperationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperationsResourceIT {

    private static final String DEFAULT_CEDULA_BENEFICIARIO = "AAAAAAAAAA";
    private static final String UPDATED_CEDULA_BENEFICIARIO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_EMISOR = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_EMISOR = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_BENEFICIARIO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_BENEFICIARIO = "BBBBBBBBBB";

    private static final String DEFAULT_MONTO = "AAAAAAAAAA";
    private static final String UPDATED_MONTO = "BBBBBBBBBB";

    private static final String DEFAULT_BANCO_EMISOR = "AAAA";
    private static final String UPDATED_BANCO_EMISOR = "BBBB";

    private static final String DEFAULT_CONCEPTO = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPTO = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_FECHA_HORA = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_HORA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperationsRepository operationsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperationsMockMvc;

    private Operations operations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operations createEntity(EntityManager em) {
        Operations operations = new Operations()
            .cedulaBeneficiario(DEFAULT_CEDULA_BENEFICIARIO)
            .telefonoEmisor(DEFAULT_TELEFONO_EMISOR)
            .telefonoBeneficiario(DEFAULT_TELEFONO_BENEFICIARIO)
            .monto(DEFAULT_MONTO)
            .bancoEmisor(DEFAULT_BANCO_EMISOR)
            .concepto(DEFAULT_CONCEPTO)
            .referencia(DEFAULT_REFERENCIA)
            .fechaHora(DEFAULT_FECHA_HORA);
        return operations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operations createUpdatedEntity(EntityManager em) {
        Operations operations = new Operations()
            .cedulaBeneficiario(UPDATED_CEDULA_BENEFICIARIO)
            .telefonoEmisor(UPDATED_TELEFONO_EMISOR)
            .telefonoBeneficiario(UPDATED_TELEFONO_BENEFICIARIO)
            .monto(UPDATED_MONTO)
            .bancoEmisor(UPDATED_BANCO_EMISOR)
            .concepto(UPDATED_CONCEPTO)
            .referencia(UPDATED_REFERENCIA)
            .fechaHora(UPDATED_FECHA_HORA);
        return operations;
    }

    @BeforeEach
    public void initTest() {
        operations = createEntity(em);
    }

    @Test
    @Transactional
    void createOperations() throws Exception {
        int databaseSizeBeforeCreate = operationsRepository.findAll().size();
        // Create the Operations
        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isCreated());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeCreate + 1);
        Operations testOperations = operationsList.get(operationsList.size() - 1);
        assertThat(testOperations.getCedulaBeneficiario()).isEqualTo(DEFAULT_CEDULA_BENEFICIARIO);
        assertThat(testOperations.getTelefonoEmisor()).isEqualTo(DEFAULT_TELEFONO_EMISOR);
        assertThat(testOperations.getTelefonoBeneficiario()).isEqualTo(DEFAULT_TELEFONO_BENEFICIARIO);
        assertThat(testOperations.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testOperations.getBancoEmisor()).isEqualTo(DEFAULT_BANCO_EMISOR);
        assertThat(testOperations.getConcepto()).isEqualTo(DEFAULT_CONCEPTO);
        assertThat(testOperations.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testOperations.getFechaHora()).isEqualTo(DEFAULT_FECHA_HORA);
    }

    @Test
    @Transactional
    void createOperationsWithExistingId() throws Exception {
        // Create the Operations with an existing ID
        operations.setId(1L);

        int databaseSizeBeforeCreate = operationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCedulaBeneficiarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationsRepository.findAll().size();
        // set the field null
        operations.setCedulaBeneficiario(null);

        // Create the Operations, which fails.

        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoEmisorIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationsRepository.findAll().size();
        // set the field null
        operations.setTelefonoEmisor(null);

        // Create the Operations, which fails.

        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoBeneficiarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationsRepository.findAll().size();
        // set the field null
        operations.setTelefonoBeneficiario(null);

        // Create the Operations, which fails.

        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationsRepository.findAll().size();
        // set the field null
        operations.setMonto(null);

        // Create the Operations, which fails.

        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBancoEmisorIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationsRepository.findAll().size();
        // set the field null
        operations.setBancoEmisor(null);

        // Create the Operations, which fails.

        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConceptoIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationsRepository.findAll().size();
        // set the field null
        operations.setConcepto(null);

        // Create the Operations, which fails.

        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReferenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationsRepository.findAll().size();
        // set the field null
        operations.setReferencia(null);

        // Create the Operations, which fails.

        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaHoraIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationsRepository.findAll().size();
        // set the field null
        operations.setFechaHora(null);

        // Create the Operations, which fails.

        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperations() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        // Get all the operationsList
        restOperationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operations.getId().intValue())))
            .andExpect(jsonPath("$.[*].cedulaBeneficiario").value(hasItem(DEFAULT_CEDULA_BENEFICIARIO)))
            .andExpect(jsonPath("$.[*].telefonoEmisor").value(hasItem(DEFAULT_TELEFONO_EMISOR)))
            .andExpect(jsonPath("$.[*].telefonoBeneficiario").value(hasItem(DEFAULT_TELEFONO_BENEFICIARIO)))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO)))
            .andExpect(jsonPath("$.[*].bancoEmisor").value(hasItem(DEFAULT_BANCO_EMISOR)))
            .andExpect(jsonPath("$.[*].concepto").value(hasItem(DEFAULT_CONCEPTO)))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)))
            .andExpect(jsonPath("$.[*].fechaHora").value(hasItem(DEFAULT_FECHA_HORA)));
    }

    @Test
    @Transactional
    void getOperations() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        // Get the operations
        restOperationsMockMvc
            .perform(get(ENTITY_API_URL_ID, operations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operations.getId().intValue()))
            .andExpect(jsonPath("$.cedulaBeneficiario").value(DEFAULT_CEDULA_BENEFICIARIO))
            .andExpect(jsonPath("$.telefonoEmisor").value(DEFAULT_TELEFONO_EMISOR))
            .andExpect(jsonPath("$.telefonoBeneficiario").value(DEFAULT_TELEFONO_BENEFICIARIO))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO))
            .andExpect(jsonPath("$.bancoEmisor").value(DEFAULT_BANCO_EMISOR))
            .andExpect(jsonPath("$.concepto").value(DEFAULT_CONCEPTO))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA))
            .andExpect(jsonPath("$.fechaHora").value(DEFAULT_FECHA_HORA));
    }

    @Test
    @Transactional
    void getNonExistingOperations() throws Exception {
        // Get the operations
        restOperationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperations() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();

        // Update the operations
        Operations updatedOperations = operationsRepository.findById(operations.getId()).get();
        // Disconnect from session so that the updates on updatedOperations are not directly saved in db
        em.detach(updatedOperations);
        updatedOperations
            .cedulaBeneficiario(UPDATED_CEDULA_BENEFICIARIO)
            .telefonoEmisor(UPDATED_TELEFONO_EMISOR)
            .telefonoBeneficiario(UPDATED_TELEFONO_BENEFICIARIO)
            .monto(UPDATED_MONTO)
            .bancoEmisor(UPDATED_BANCO_EMISOR)
            .concepto(UPDATED_CONCEPTO)
            .referencia(UPDATED_REFERENCIA)
            .fechaHora(UPDATED_FECHA_HORA);

        restOperationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOperations.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOperations))
            )
            .andExpect(status().isOk());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
        Operations testOperations = operationsList.get(operationsList.size() - 1);
        assertThat(testOperations.getCedulaBeneficiario()).isEqualTo(UPDATED_CEDULA_BENEFICIARIO);
        assertThat(testOperations.getTelefonoEmisor()).isEqualTo(UPDATED_TELEFONO_EMISOR);
        assertThat(testOperations.getTelefonoBeneficiario()).isEqualTo(UPDATED_TELEFONO_BENEFICIARIO);
        assertThat(testOperations.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testOperations.getBancoEmisor()).isEqualTo(UPDATED_BANCO_EMISOR);
        assertThat(testOperations.getConcepto()).isEqualTo(UPDATED_CONCEPTO);
        assertThat(testOperations.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testOperations.getFechaHora()).isEqualTo(UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void putNonExistingOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operations.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperationsWithPatch() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();

        // Update the operations using partial update
        Operations partialUpdatedOperations = new Operations();
        partialUpdatedOperations.setId(operations.getId());

        partialUpdatedOperations.telefonoEmisor(UPDATED_TELEFONO_EMISOR).bancoEmisor(UPDATED_BANCO_EMISOR);

        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperations))
            )
            .andExpect(status().isOk());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
        Operations testOperations = operationsList.get(operationsList.size() - 1);
        assertThat(testOperations.getCedulaBeneficiario()).isEqualTo(DEFAULT_CEDULA_BENEFICIARIO);
        assertThat(testOperations.getTelefonoEmisor()).isEqualTo(UPDATED_TELEFONO_EMISOR);
        assertThat(testOperations.getTelefonoBeneficiario()).isEqualTo(DEFAULT_TELEFONO_BENEFICIARIO);
        assertThat(testOperations.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testOperations.getBancoEmisor()).isEqualTo(UPDATED_BANCO_EMISOR);
        assertThat(testOperations.getConcepto()).isEqualTo(DEFAULT_CONCEPTO);
        assertThat(testOperations.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testOperations.getFechaHora()).isEqualTo(DEFAULT_FECHA_HORA);
    }

    @Test
    @Transactional
    void fullUpdateOperationsWithPatch() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();

        // Update the operations using partial update
        Operations partialUpdatedOperations = new Operations();
        partialUpdatedOperations.setId(operations.getId());

        partialUpdatedOperations
            .cedulaBeneficiario(UPDATED_CEDULA_BENEFICIARIO)
            .telefonoEmisor(UPDATED_TELEFONO_EMISOR)
            .telefonoBeneficiario(UPDATED_TELEFONO_BENEFICIARIO)
            .monto(UPDATED_MONTO)
            .bancoEmisor(UPDATED_BANCO_EMISOR)
            .concepto(UPDATED_CONCEPTO)
            .referencia(UPDATED_REFERENCIA)
            .fechaHora(UPDATED_FECHA_HORA);

        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperations))
            )
            .andExpect(status().isOk());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
        Operations testOperations = operationsList.get(operationsList.size() - 1);
        assertThat(testOperations.getCedulaBeneficiario()).isEqualTo(UPDATED_CEDULA_BENEFICIARIO);
        assertThat(testOperations.getTelefonoEmisor()).isEqualTo(UPDATED_TELEFONO_EMISOR);
        assertThat(testOperations.getTelefonoBeneficiario()).isEqualTo(UPDATED_TELEFONO_BENEFICIARIO);
        assertThat(testOperations.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testOperations.getBancoEmisor()).isEqualTo(UPDATED_BANCO_EMISOR);
        assertThat(testOperations.getConcepto()).isEqualTo(UPDATED_CONCEPTO);
        assertThat(testOperations.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testOperations.getFechaHora()).isEqualTo(UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void patchNonExistingOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperations() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        int databaseSizeBeforeDelete = operationsRepository.findAll().size();

        // Delete the operations
        restOperationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, operations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
