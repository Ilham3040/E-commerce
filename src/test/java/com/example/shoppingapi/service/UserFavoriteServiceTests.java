package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.UserFavoriteId;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.UserFavoriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFavoriteServiceTest {

    @Mock private UserFavoriteRepository favRepo;
    @InjectMocks private UserFavoriteService service;

    private final ModelHelper<UserFavorite> helper =
        ModelHelperFactory.getModelHelper(UserFavorite.class);

    @Test
    void findAll_returnsAllFavorites() {
        List<UserFavorite> list = Arrays.asList(helper.createModel(1), helper.createModel(2));
        when(favRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(favRepo).findAll();
    }

    @Test
    void findById_found_returnsFavorite() {
        UserFavorite fav = helper.createModel(1);
        UserFavoriteId id = fav.getId();
        when(favRepo.findById(id)).thenReturn(Optional.of(fav));

        assertEquals(fav, service.findById(id.getUserId(), id.getProductId()));
        verify(favRepo).findById(id);
    }

    @Test
    void findById_notFound_throws() {
        UserFavoriteId id = new UserFavoriteId(9L, 9L);
        when(favRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(id.getUserId(), id.getProductId())
        );
        assertEquals("UserFavorite not found with ID: " + id, ex.getMessage());
    }

    @Test
    void saveUserFavorite_savesAndReturns() {
        UserFavorite fav = helper.createModel(1);
        when(favRepo.save(fav)).thenReturn(fav);

        assertEquals(fav, service.saveUserFavorite(fav));
        verify(favRepo).save(fav);
    }

    @Test
    void deleteById_existing_deletes() {
        UserFavorite fav = helper.createModel(1);
        UserFavoriteId id = fav.getId();
        when(favRepo.findById(id)).thenReturn(Optional.of(fav));
        doNothing().when(favRepo).deleteById(id);

        service.deleteById(id);
        verify(favRepo).deleteById(id);
    }

    @Test
    void deleteById_notFound_throws() {
        UserFavoriteId id = new UserFavoriteId(9L,9L);
        when(favRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.deleteById(id)
        );
        assertEquals("UserFavorite not found with ID: " + id, ex.getMessage());
        verify(favRepo, never()).deleteById(any());
    }
}
