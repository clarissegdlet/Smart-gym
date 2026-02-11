package com.smartgym.manager.web.rest;

import static com.smartgym.manager.domain.StravaAccountAsserts.*;
import static com.smartgym.manager.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgym.manager.IntegrationTest;
import com.smartgym.manager.domain.StravaAccount;
import com.smartgym.manager.repository.StravaAccountRepository;
import com.smartgym.manager.service.dto.StravaAccountDTO;
import com.smartgym.manager.service.mapper.StravaAccountMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StravaAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StravaAccountResourceIT {

    private static final String DEFAULT_STRAVA_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_STRAVA_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/strava-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StravaAccountRepository stravaAccountRepository;

    @Autowired
    private StravaAccountMapper stravaAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStravaAccountMockMvc;

    private StravaAccount stravaAccount;

    private StravaAccount insertedStravaAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StravaAccount createEntity() {
        return new StravaAccount().stravaUserId(DEFAULT_STRAVA_USER_ID).accessToken(DEFAULT_ACCESS_TOKEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StravaAccount createUpdatedEntity() {
        return new StravaAccount().stravaUserId(UPDATED_STRAVA_USER_ID).accessToken(UPDATED_ACCESS_TOKEN);
    }

    @BeforeEach
    void initTest() {
        stravaAccount = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStravaAccount != null) {
            stravaAccountRepository.delete(insertedStravaAccount);
            insertedStravaAccount = null;
        }
    }

    @Test
    @Transactional
    void createStravaAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StravaAccount
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);
        var returnedStravaAccountDTO = om.readValue(
            restStravaAccountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stravaAccountDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StravaAccountDTO.class
        );

        // Validate the StravaAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStravaAccount = stravaAccountMapper.toEntity(returnedStravaAccountDTO);
        assertStravaAccountUpdatableFieldsEquals(returnedStravaAccount, getPersistedStravaAccount(returnedStravaAccount));

        insertedStravaAccount = returnedStravaAccount;
    }

    @Test
    @Transactional
    void createStravaAccountWithExistingId() throws Exception {
        // Create the StravaAccount with an existing ID
        stravaAccount.setId(1L);
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStravaAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stravaAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StravaAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStravaUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stravaAccount.setStravaUserId(null);

        // Create the StravaAccount, which fails.
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        restStravaAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stravaAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccessTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stravaAccount.setAccessToken(null);

        // Create the StravaAccount, which fails.
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        restStravaAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stravaAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStravaAccounts() throws Exception {
        // Initialize the database
        insertedStravaAccount = stravaAccountRepository.saveAndFlush(stravaAccount);

        // Get all the stravaAccountList
        restStravaAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stravaAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].stravaUserId").value(hasItem(DEFAULT_STRAVA_USER_ID)))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN)));
    }

    @Test
    @Transactional
    void getStravaAccount() throws Exception {
        // Initialize the database
        insertedStravaAccount = stravaAccountRepository.saveAndFlush(stravaAccount);

        // Get the stravaAccount
        restStravaAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, stravaAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stravaAccount.getId().intValue()))
            .andExpect(jsonPath("$.stravaUserId").value(DEFAULT_STRAVA_USER_ID))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN));
    }

    @Test
    @Transactional
    void getNonExistingStravaAccount() throws Exception {
        // Get the stravaAccount
        restStravaAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStravaAccount() throws Exception {
        // Initialize the database
        insertedStravaAccount = stravaAccountRepository.saveAndFlush(stravaAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stravaAccount
        StravaAccount updatedStravaAccount = stravaAccountRepository.findById(stravaAccount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStravaAccount are not directly saved in db
        em.detach(updatedStravaAccount);
        updatedStravaAccount.stravaUserId(UPDATED_STRAVA_USER_ID).accessToken(UPDATED_ACCESS_TOKEN);
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(updatedStravaAccount);

        restStravaAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stravaAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stravaAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the StravaAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStravaAccountToMatchAllProperties(updatedStravaAccount);
    }

    @Test
    @Transactional
    void putNonExistingStravaAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stravaAccount.setId(longCount.incrementAndGet());

        // Create the StravaAccount
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStravaAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stravaAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stravaAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StravaAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStravaAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stravaAccount.setId(longCount.incrementAndGet());

        // Create the StravaAccount
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStravaAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stravaAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StravaAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStravaAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stravaAccount.setId(longCount.incrementAndGet());

        // Create the StravaAccount
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStravaAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stravaAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StravaAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStravaAccountWithPatch() throws Exception {
        // Initialize the database
        insertedStravaAccount = stravaAccountRepository.saveAndFlush(stravaAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stravaAccount using partial update
        StravaAccount partialUpdatedStravaAccount = new StravaAccount();
        partialUpdatedStravaAccount.setId(stravaAccount.getId());

        restStravaAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStravaAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStravaAccount))
            )
            .andExpect(status().isOk());

        // Validate the StravaAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStravaAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStravaAccount, stravaAccount),
            getPersistedStravaAccount(stravaAccount)
        );
    }

    @Test
    @Transactional
    void fullUpdateStravaAccountWithPatch() throws Exception {
        // Initialize the database
        insertedStravaAccount = stravaAccountRepository.saveAndFlush(stravaAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stravaAccount using partial update
        StravaAccount partialUpdatedStravaAccount = new StravaAccount();
        partialUpdatedStravaAccount.setId(stravaAccount.getId());

        partialUpdatedStravaAccount.stravaUserId(UPDATED_STRAVA_USER_ID).accessToken(UPDATED_ACCESS_TOKEN);

        restStravaAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStravaAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStravaAccount))
            )
            .andExpect(status().isOk());

        // Validate the StravaAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStravaAccountUpdatableFieldsEquals(partialUpdatedStravaAccount, getPersistedStravaAccount(partialUpdatedStravaAccount));
    }

    @Test
    @Transactional
    void patchNonExistingStravaAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stravaAccount.setId(longCount.incrementAndGet());

        // Create the StravaAccount
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStravaAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stravaAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stravaAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StravaAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStravaAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stravaAccount.setId(longCount.incrementAndGet());

        // Create the StravaAccount
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStravaAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stravaAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StravaAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStravaAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stravaAccount.setId(longCount.incrementAndGet());

        // Create the StravaAccount
        StravaAccountDTO stravaAccountDTO = stravaAccountMapper.toDto(stravaAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStravaAccountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stravaAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StravaAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStravaAccount() throws Exception {
        // Initialize the database
        insertedStravaAccount = stravaAccountRepository.saveAndFlush(stravaAccount);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stravaAccount
        restStravaAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, stravaAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stravaAccountRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected StravaAccount getPersistedStravaAccount(StravaAccount stravaAccount) {
        return stravaAccountRepository.findById(stravaAccount.getId()).orElseThrow();
    }

    protected void assertPersistedStravaAccountToMatchAllProperties(StravaAccount expectedStravaAccount) {
        assertStravaAccountAllPropertiesEquals(expectedStravaAccount, getPersistedStravaAccount(expectedStravaAccount));
    }

    protected void assertPersistedStravaAccountToMatchUpdatableProperties(StravaAccount expectedStravaAccount) {
        assertStravaAccountAllUpdatablePropertiesEquals(expectedStravaAccount, getPersistedStravaAccount(expectedStravaAccount));
    }
}
