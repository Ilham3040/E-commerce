package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.UserFavoriteCreateDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.User;
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
    void getAllByUserId_found_returnsProducts() {
        Long userId = 1L;

        UserFavorite userFavorite1 = helper.createModel(1);
        UserFavorite userFavorite2 = helper.createModel(2);

        // Mocking the response from the repository
        List<UserFavorite> products = Arrays.asList(userFavorite1, userFavorite2);
        when(favRepo.findItemsByUserId(userId)).thenReturn(products);

        List<UserFavorite> result = service.getAllByUserId(userId);

        assertEquals(products, result);
        verify(favRepo).findItemsByUserId(userId);
    }

    @Test
    void findById_found_returnsFavorite() {
        UserFavorite fav = helper.createModel(1);
        UserFavoriteId id = fav.getId();
        when(favRepo.findById(id)).thenReturn(Optional.of(fav));

        assertEquals(fav, service.findById(id));
        verify(favRepo).findById(id);
    }

    @Test
    void findById_notFound_throws() {
        UserFavoriteId id = new UserFavoriteId(9L, 9L);
        when(favRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(id)
        );
        assertEquals("UserFavorite not found with ID: " + id, ex.getMessage());
    }

    @Test
    void saveUserFavorite_savesAndReturns() {
        UserFavorite fav = helper.createModel(1);


        UserFavoriteCreateDTO userFavoriteCreateDTO = new UserFavoriteCreateDTO();
        userFavoriteCreateDTO.setUserId(fav.getUser().getUserId());
        userFavoriteCreateDTO.setProductId(fav.getProduct().getProductId());

        UserFavoriteId userFavoriteId = new UserFavoriteId(fav.getUser().getUserId(), fav.getProduct().getProductId());

        UserFavorite userFavorite = new UserFavorite(userFavoriteId,
                User.builder().userId(fav.getUser().getUserId()).build(),
                Product.builder().productId(fav.getProduct().getProductId()).build());

        when(favRepo.save(any(UserFavorite.class))).thenReturn(userFavorite);

        UserFavorite result = service.addingUserFavorite(userFavoriteCreateDTO);

        assertEquals(userFavorite.getId(),result.getId());
        assertEquals(userFavorite.getUser(),result.getUser());
        assertEquals(userFavorite.getProduct(),result.getProduct());
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
