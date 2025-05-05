package com.example.shoppingapi.controller;

import com.example.shoppingapi.dto.create.StoreRoleCreateDTO;
import com.example.shoppingapi.dto.response.ApiResponse;
import com.example.shoppingapi.dto.response.StoreRoleDTO;
import com.example.shoppingapi.model.StoreRole;
import com.example.shoppingapi.model.StoreRoleId;
import com.example.shoppingapi.service.StoreRoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/storeroles/")
@RequiredArgsConstructor
public class StoreRoleController {
    private final StoreRoleService storeRoleService;

    @GetMapping("/store/{storeId}")
    public ApiResponse<List<StoreRoleDTO>> getAllStoreRolesByStoreId(@PathVariable Long storeId) {
        List<StoreRoleDTO> storeRoleDTOs = storeRoleService.getAllStoreRoleByStoreId(storeId)
                .stream()
                .map(role -> new StoreRoleDTO(role.getUser().getUserId(), role.getStore().getStoreId(),role.getRole()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all store roles by store ID", storeRoleDTOs, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<StoreRoleDTO>> getAllStoreRolesByUserId(@PathVariable Long userId) {
        List<StoreRoleDTO> storeRoleDTOs = storeRoleService.getAllStoreRoleByUserId(userId)
                .stream()
                .map(role -> new StoreRoleDTO(role.getUser().getUserId(), role.getStore().getStoreId(),role.getRole()))
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully fetched all store roles by user ID", storeRoleDTOs, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<StoreRoleDTO> createStoreRole(@Validated @RequestBody StoreRoleCreateDTO storeRoleCreateDTO) {
        StoreRole createdStoreRole = storeRoleService.promoteToAdminStoreRole(storeRoleCreateDTO);
        return new ApiResponse<>("Successfully created store role", new StoreRoleDTO(createdStoreRole.getUser().getUserId(), createdStoreRole.getStore().getStoreId(),createdStoreRole.getRole()), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{storeId}/{userId}")
    public ApiResponse<Void> deleteStoreRole(@PathVariable Long storeId, @PathVariable Long userId) {
        storeRoleService.deleteStoreRoleById(new StoreRoleId(storeId, userId));
        return new ApiResponse<>("Successfully deleted store role", null, HttpStatus.NO_CONTENT);
    }
}
