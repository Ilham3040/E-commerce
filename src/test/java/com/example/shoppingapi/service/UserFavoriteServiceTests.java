package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserFavorite;
import com.example.shoppingapi.model.UserFavoriteId;
import com.example.shoppingapi.repository.UserFavoriteRepository;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserFavoriteServiceTests.class)
public class UserFavoriteServiceTests {

    @Mock
    private UserFavoriteRepository userFavoriteRepository;

    @InjectMocks
    private UserFavoriteService userFavoriteService;

    private ModelHelper<UserFavorite> favoriteHelper =
        ModelHelperFactory.getModelHelper(UserFavorite.class);

    @Test
    public void testFindAll() {
        UserFavorite fav1 = favoriteHelper.createModel(1);
        UserFavorite fav2 = favoriteHelper.createModel(2);
        List<UserFavorite> mockList = Arrays.asList(fav1, fav2);

        when(userFavoriteRepository.findAll()).thenReturn(mockList);

        List<UserFavorite> result = userFavoriteService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(fav1, result.get(0));
        assertEquals(fav2, result.get(1));

        verify(userFavoriteRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        UserFavorite fav = favoriteHelper.createModel(1);
        UserFavoriteId id = fav.getId();

        when(userFavoriteRepository.findById(id)).thenReturn(Optional.of(fav));

        Optional<UserFavorite> result = userFavoriteService.findById(id.getUserId(), id.getProductId());

        assertTrue(result.isPresent());
        assertEquals(fav, result.get());

        verify(userFavoriteRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NotFound() {
        UserFavoriteId id = new UserFavoriteId(9L, 9L);

        when(userFavoriteRepository.findById(id)).thenReturn(Optional.empty());

        Optional<UserFavorite> result = userFavoriteService.findById(id.getUserId(), id.getProductId());

        assertFalse(result.isPresent());

        verify(userFavoriteRepository, times(1)).findById(id);
    }

    @Test
    public void testSaveUserFavorite() {
        UserFavorite fav = favoriteHelper.createModel(1);

        when(userFavoriteRepository.save(any(UserFavorite.class))).thenReturn(fav);

        UserFavorite created = userFavoriteService.saveUserFavorite(fav);

        assertNotNull(created);
        assertEquals(fav, created);

        verify(userFavoriteRepository, times(1)).save(fav);
    }

    @Test
    public void testDeleteUserFavorite() {
        UserFavorite fav = favoriteHelper.createModel(1);
        UserFavoriteId id = fav.getId();

        when(userFavoriteRepository.existsById(id)).thenReturn(true);
        doNothing().when(userFavoriteRepository).deleteById(id);

        userFavoriteService.deleteById(id);

        verify(userFavoriteRepository, times(1)).existsById(id);
        verify(userFavoriteRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteUserFavorite_NotFound() {
        UserFavoriteId id = new UserFavoriteId(9L, 9L);

        when(userFavoriteRepository.existsById(id)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> userFavoriteService.deleteById(id)
        );

        assertEquals("UserFavorite not found with ID: " + id, ex.getMessage());

        verify(userFavoriteRepository, times(1)).existsById(id);
        verify(userFavoriteRepository, never()).deleteById(any());
    }
}
