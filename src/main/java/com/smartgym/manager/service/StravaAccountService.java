package com.smartgym.manager.service;

import com.smartgym.manager.domain.StravaAccount;
import com.smartgym.manager.repository.StravaAccountRepository;
import com.smartgym.manager.service.dto.StravaAccountDTO;
import com.smartgym.manager.service.mapper.StravaAccountMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.smartgym.manager.domain.StravaAccount}.
 */
@Service
@Transactional
public class StravaAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(StravaAccountService.class);

    private final StravaAccountRepository stravaAccountRepository;

    private final StravaAccountMapper stravaAccountMapper;

    public StravaAccountService(StravaAccountRepository stravaAccountRepository, StravaAccountMapper stravaAccountMapper) {
        this.stravaAccountRepository = stravaAccountRepository;
        this.stravaAccountMapper = stravaAccountMapper;
    }

    /**
     * Save a stravaAccount.
     *
     * @param stravaAccountDTO the entity to save.
     * @return the persisted entity.
     */
    public StravaAccountDTO save(StravaAccountDTO stravaAccountDTO) {
        LOG.debug("Request to save StravaAccount : {}", stravaAccountDTO);
        StravaAccount stravaAccount = stravaAccountMapper.toEntity(stravaAccountDTO);
        stravaAccount = stravaAccountRepository.save(stravaAccount);
        return stravaAccountMapper.toDto(stravaAccount);
    }

    /**
     * Update a stravaAccount.
     *
     * @param stravaAccountDTO the entity to save.
     * @return the persisted entity.
     */
    public StravaAccountDTO update(StravaAccountDTO stravaAccountDTO) {
        LOG.debug("Request to update StravaAccount : {}", stravaAccountDTO);
        StravaAccount stravaAccount = stravaAccountMapper.toEntity(stravaAccountDTO);
        stravaAccount = stravaAccountRepository.save(stravaAccount);
        return stravaAccountMapper.toDto(stravaAccount);
    }

    /**
     * Partially update a stravaAccount.
     *
     * @param stravaAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StravaAccountDTO> partialUpdate(StravaAccountDTO stravaAccountDTO) {
        LOG.debug("Request to partially update StravaAccount : {}", stravaAccountDTO);

        return stravaAccountRepository
            .findById(stravaAccountDTO.getId())
            .map(existingStravaAccount -> {
                stravaAccountMapper.partialUpdate(existingStravaAccount, stravaAccountDTO);

                return existingStravaAccount;
            })
            .map(stravaAccountRepository::save)
            .map(stravaAccountMapper::toDto);
    }

    /**
     * Get all the stravaAccounts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StravaAccountDTO> findAll() {
        LOG.debug("Request to get all StravaAccounts");
        return stravaAccountRepository.findAll().stream().map(stravaAccountMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the stravaAccounts where Member is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StravaAccountDTO> findAllWhereMemberIsNull() {
        LOG.debug("Request to get all stravaAccounts where Member is null");
        return StreamSupport.stream(stravaAccountRepository.findAll().spliterator(), false)
            .filter(stravaAccount -> stravaAccount.getMember() == null)
            .map(stravaAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one stravaAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StravaAccountDTO> findOne(Long id) {
        LOG.debug("Request to get StravaAccount : {}", id);
        return stravaAccountRepository.findById(id).map(stravaAccountMapper::toDto);
    }

    /**
     * Delete the stravaAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete StravaAccount : {}", id);
        stravaAccountRepository.deleteById(id);
    }
}
