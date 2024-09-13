package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.GroupCreateRequest;
import com.gbsb.tripmate.entity.Group;
import com.gbsb.tripmate.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody GroupCreateRequest request, @AuthenticationPrincipal User user) {
        Group newGroup = groupService.createGroup(user.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Group> deleteGroup(@PathVariable long groupId, @AuthenticationPrincipal User user) {
        groupService.deleteGroup(user.getUserId(), groupId);
        return ResponseEntity.noContent().build();
    }
}
