package com.smartgym.manager.web.rest;

import com.smartgym.manager.repository.StravaAccountRepository;
import com.smartgym.manager.service.StravaAccountService;
import com.smartgym.manager.service.dto.StravaAccountDTO;
import com.smartgym.manager.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.smartgym.manager.domain.StravaAccount}.
 */
@RestController
@RequestMapping("/api/strava-accounts")
public class StravaAccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(StravaAccountResource.class);

    private static final String ENTITY_NAME = "stravaAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StravaAccountService stravaAccountService;

    private final StravaAccountRepository stravaAccountRepository;

    public StravaAccountResource(StravaAccountService stravaAccountService, StravaAccountRepository stravaAccountRepository) {
        this.stravaAccountService = stravaAccountService;
        this.stravaAccountRepository = stravaAccountRepository;
    }

    /**
     * {@code POST  /strava-accounts} : Create a new stravaAccount.
     *
     * @param stravaAccountDTO the stravaAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stravaAccountDTO, or with status {@code 400 (Bad Request)} if the stravaAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StravaAccountDTO> createStravaAccount(@Valid @RequestBody StravaAccountDTO stravaAccountDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StravaAccount : {}", stravaAccountDTO);
        if (stravaAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new stravaAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stravaAccountDTO = stravaAccountService.save(stravaAccountDTO);
        return ResponseEntity.created(new URI("/api/strava-accounts/" + stravaAccountDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, stravaAccountDTO.getId().toString()))
            .body(stravaAccountDTO);
    }

    /**
     * {@code PUT  /strava-accounts/:id} : Updates an existing stravaAccount.
     *
     * @param id the id of the stravaAccountDTO to save.
     * @param stravaAccountDTO the stravaAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stravaAccountDTO,
     * or with status {@code 400 (Bad Request)} if the stravaAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stravaAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StravaAccountDTO> updateStravaAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StravaAccountDTO stravaAccountDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StravaAccount : {}, {}", id, stravaAccountDTO);
        if (stravaAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stravaAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stravaAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stravaAccountDTO = stravaAccountService.update(stravaAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stravaAccountDTO.getId().toString()))
            .body(stravaAccountDTO);
    }

    /**
     * {@code PATCH  /strava-accounts/:id} : Partial updates given fields of an existing stravaAccount, field will ignore if it is null
     *
     * @param id the id of the stravaAccountDTO to save.
     * @param stravaAccountDTO the stravaAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stravaAccountDTO,
     * or with status {@code 400 (Bad Request)} if the stravaAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stravaAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stravaAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StravaAccountDTO> partialUpdateStravaAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StravaAccountDTO stravaAccountDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StravaAccount partially : {}, {}", id, stravaAccountDTO);
        if (stravaAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stravaAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stravaAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StravaAccountDTO> result = stravaAccountService.partialUpdate(stravaAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stravaAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /strava-accounts} : get all the stravaAccounts.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stravaAccounts in body.
     */
    @GetMapping("")
    public List<StravaAccountDTO> getAllStravaAccounts(@RequestParam(name = "filter", required = false) String filter) {
        if ("member-is-null".equals(filter)) {
            LOG.debug("REST request to get all StravaAccounts where member is null");
            return stravaAccountService.findAllWhereMemberIsNull();
        }
        LOG.debug("REST request to get all StravaAccounts");
        return stravaAccountService.findAll();
    }

    /**
     * {@code GET  /strava-accounts/:id} : get the "id" stravaAccount.
     *
     * @param id the id of the stravaAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stravaAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StravaAccountDTO> getStravaAccount(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StravaAccount : {}", id);
        Optional<StravaAccountDTO> stravaAccountDTO = stravaAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stravaAccountDTO);
    }

    /**
     * {@code DELETE  /strava-accounts/:id} : delete the "id" stravaAccount.
     *
     * @param id the id of the stravaAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStravaAccount(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StravaAccount : {}", id);
        stravaAccountService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
