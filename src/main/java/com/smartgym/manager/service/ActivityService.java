package com.smartgym.manager.service;

import com.smartgym.manager.domain.Activity;
import com.smartgym.manager.repository.ActivityRepository;
import com.smartgym.manager.service.dto.ActivityDTO;
import com.smartgym.manager.service.mapper.ActivityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.smartgym.manager.domain.Activity}.
 */
@Service
@Transactional
public class ActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    public ActivityService(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save.
     * @return the persisted entity.
     */
    public ActivityDTO save(ActivityDTO activityDTO) {
        LOG.debug("Request to save Activity : {}", activityDTO);
        Activity activity = activityMapper.toEntity(activityDTO);
        activity = activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }

    /**
     * Update a activity.
     *
     * @param activityDTO the entity to save.
     * @return the persisted entity.
     */
    public ActivityDTO update(ActivityDTO activityDTO) {
        LOG.debug("Request to update Activity : {}", activityDTO);
        Activity activity = activityMapper.toEntity(activityDTO);
        activity = activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }

    /**
     * Partially update a activity.
     *
     * @param activityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ActivityDTO> partialUpdate(ActivityDTO activityDTO) {
        LOG.debug("Request to partially update Activity : {}", activityDTO);

        return activityRepository
            .findById(activityDTO.getId())
            .map(existingActivity -> {
                activityMapper.partialUpdate(existingActivity, activityDTO);

                return existingActivity;
            })
            .map(activityRepository::save)
            .map(activityMapper::toDto);
    }

    /**
     * Get all the activities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ActivityDTO> findAll() {
        LOG.debug("Request to get all Activities");
        return activityRepository.findAll().stream().map(activityMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one activity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActivityDTO> findOne(Long id) {
        LOG.debug("Request to get Activity : {}", id);
        return activityRepository.findById(id).map(activityMapper::toDto);
    }

    /**
     * Delete the activity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Activity : {}", id);
        activityRepository.deleteById(id);
    }
}
