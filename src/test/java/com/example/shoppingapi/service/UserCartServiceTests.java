// src/test/java/com/example/shoppingapi/service/UserCartServiceTests.java
package com.example.shoppingapi.service;

import com.example.shoppingapi.model.UserCart;
import com.example.shoppingapi.model.UserCartId;
import com.example.shoppingapi.repository.UserCartRepository;
import com.example.shoppingapi.modelhelper.ModelHelper;
import com.example.shoppingapi.modelhelper.ModelHelperFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserCartServiceTests.class)
public class UserCartServiceTests {

    @Mock
    private UserCartRepository userCartRepository;

    @InjectMocks
    private UserCartService userCartService;

    private ModelHelper<UserCart> cartHelper =
        ModelHelperFactory.getModelHelper(UserCart.class);

    @Test
    public void testFindAll() {
        UserCart cart1 = cartHelper.createModel(1);
        UserCart cart2 = cartHelper.createModel(2);

        List<UserCart> mockList = Arrays.asList(cart1, cart2);

        when(userCartRepository.findAll()).thenReturn(mockList);

        List<UserCart> result = userCartService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(cart1, result.get(0));
        assertEquals(cart2, result.get(1));

        verify(userCartRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        UserCart cart = cartHelper.createModel(1);
        when(userCartRepository.findById(cart.getId()))
            .thenReturn(Optional.of(cart));

        UserCart result = userCartService.findById(cart.getId()).orElseThrow(() -> new AssertionError("Cart Item doesn't exist"));
        assertNotNull(result);
        assertEquals(cart, result);

        verify(userCartRepository, times(1)).findById(cart.getId());
    }

    @Test
    public void testFindById_NotFound() {
        UserCartId missingId = new UserCartId(9L, 9L);

        when(userCartRepository.findById(missingId)).thenReturn(Optional.empty());

        Optional<UserCart> result = userCartService.findById(missingId);

        assertFalse(result.isPresent());

        verify(userCartRepository, times(1)).findById(missingId);
    }

    @Test
    public void testSaveUserCart() {
        UserCart cart = cartHelper.createModel(1);

        when(userCartRepository.save(any(UserCart.class))).thenReturn(cart);

        UserCart created = userCartService.saveUserCart(cart);

        assertNotNull(created);
        assertEquals(cart.getId(), created.getId());
        assertEquals(cart.getQuantity(), created.getQuantity());

        verify(userCartRepository, times(1)).save(cart);
    }

    @Test
    public void testUpdateUserCart() {
        UserCart existing = cartHelper.createModel(1);

        when(userCartRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(userCartRepository.save(any(UserCart.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
        
        UserCart updated  = existing.toBuilder().quantity(7).build();
        UserCart result = userCartService.updateUserCart(existing.getId(), updated);

        assertNotNull(result);
        assertEquals(7, result.getQuantity());

        verify(userCartRepository, times(1)).findById(existing.getId());
        verify(userCartRepository, times(1)).save(updated);
    }

    @Test
    public void testPartialUpdateUserCart() {
        UserCart cart = cartHelper.createModel(2);
        UserCartId id = cart.getId();

        when(userCartRepository.findById(id)).thenReturn(Optional.of(cart));
        when(userCartRepository.save(any(UserCart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> updates = Map.of("quantity", 10);
        UserCart result = userCartService.partialUpdateUserCart(id, updates);

        assertNotNull(result);
        assertEquals(10, result.getQuantity());

        verify(userCartRepository, times(1)).findById(id);
        verify(userCartRepository, times(1)).save(cart);
    }
}
