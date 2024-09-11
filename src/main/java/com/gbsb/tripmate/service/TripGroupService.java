package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.UpdateTripGroup;
import com.gbsb.tripmate.entity.TripGroup;
import com.gbsb.tripmate.repository.TripGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TripGroupService {
    private final TripGroupRepository tripGroupRepository;

    public TripGroupService(TripGroupRepository tripGroupRepository) {
        this.tripGroupRepository = tripGroupRepository;
    }

    public void updateTripGroup(Long groupId, UpdateTripGroup.Request request) {
        TripGroup tripGroup =  tripGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("groupId가 없습니다."));

        tripGroup.setTripGroup(request);
        tripGroupRepository.save(tripGroup);
    }
    
}