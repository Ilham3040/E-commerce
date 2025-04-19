package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;
import com.example.shoppingapi.repository.UserCartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCartServiceTest {

    @Mock private UserCartRepository cartRepo;
    @InjectMocks private UserCartService service;

    private final ModelHelper<UserCart> helper =
        ModelHelperFactory.getModelHelper(UserCart.class);

    @Test
    void findAll_returnsAllCarts() {
        List<UserCart> list = Arrays.asList(helper.createModel(1), helper.createModel(2));
        when(cartRepo.findAll()).thenReturn(list);

        assertEquals(list, service.findAll());
        verify(cartRepo).findAll();
    }

    @Test
    void findById_found_returnsCart() {
        UserCart cart = helper.createModel(1);
        UserCartId id = cart.getId();
        when(cartRepo.findById(id)).thenReturn(Optional.of(cart));

        assertEquals(cart, service.findById(id));
        verify(cartRepo).findById(id);
    }

    @Test
    void findById_notFound_throws() {
        UserCartId id = new UserCartId(9L, 9L);
        when(cartRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.findById(id)
        );
        assertEquals("UserCart not found with ID: " + id, ex.getMessage());
    }

    @Test
    void saveUserCart_savesAndReturns() {
        UserCart cart = helper.createModel(1);
        when(cartRepo.save(cart)).thenReturn(cart);

        assertEquals(cart, service.saveUserCart(cart));
        verify(cartRepo).save(cart);
    }

    @Test
    void updateUserCart_existing_savesUpdated() {
        UserCart existing = helper.createModel(1);
        UserCartId id = existing.getId();
        when(cartRepo.findById(id)).thenReturn(Optional.of(existing));
        when(cartRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        UserCart updated = existing.toBuilder().quantity(7).build();
        assertEquals(7, service.updateUserCart(id, updated).getQuantity());
        verify(cartRepo).save(updated);
    }

    @Test
    void partialUpdateUserCart_appliesUpdates() {
        UserCart existing = helper.createModel(2);
        UserCartId id = existing.getId();
        when(cartRepo.findById(id)).thenReturn(Optional.of(existing));
        when(cartRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        UserCart result = service.partialUpdateUserCart(id, Map.of("quantity", 10));
        assertEquals(10, result.getQuantity());
        verify(cartRepo).save(existing);
    }

    @Test
    void deleteUserCart_existing_deletes() {
        UserCart existing = helper.createModel(1);
        UserCartId id = existing.getId();
        when(cartRepo.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(cartRepo).deleteById(id);

        service.deleteUserCart(id);
        verify(cartRepo).deleteById(id);
    }

    @Test
    void deleteUserCart_notFound_throws() {
        UserCartId id = new UserCartId(9L,9L);
        when(cartRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> service.deleteUserCart(id)
        );
        assertEquals("UserCart not found with ID: " + id, ex.getMessage());
        verify(cartRepo, never()).deleteById(any());
    }
}
