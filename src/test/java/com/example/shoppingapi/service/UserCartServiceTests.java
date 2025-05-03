package com.example.shoppingapi.service;

import com.example.shoppingapi.dto.create.UserCartCreateDTO;
import com.example.shoppingapi.model.Product;
import com.example.shoppingapi.model.User;
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
    void getAllByUserId_found_returnsProducts() {
        Long userId = 1L;

        Product product1 = Product.builder().productId(1L).productName("Product1").build();
        Product product2 = Product.builder().productId(2L).productName("Product2").build();

        List<Product> products = Arrays.asList(product1, product2);
        when(cartRepo.findProductsByUserId(userId)).thenReturn(products);

        List<Product> result = service.getAllByUserId(userId);

        assertEquals(products, result);
        verify(cartRepo).findProductsByUserId(userId);
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
    void addingUserCart_savesAndReturns() {
        UserCart cart = helper.createModel(1);


        UserCartCreateDTO userCartCreateDTO = new UserCartCreateDTO();
        userCartCreateDTO.setUserId(cart.getUser().getUserId());
        userCartCreateDTO.setProductId(cart.getProduct().getProductId());

        UserCart userCart = UserCart.builder()
                .user(User.builder().userId(cart.getUser().getUserId()).build())
                .product(Product.builder().productId(cart.getProduct().getProductId()).build())
                .build();


        when(cartRepo.save(any(UserCart.class))).thenReturn(userCart);
        UserCart result = service.addingUserCart(userCartCreateDTO);
        assertEquals(userCart.getUser(), result.getUser());
        assertEquals(userCart.getProduct(),result.getProduct());
    }

    @Test
    void deleteById_existing_deletes() {
        UserCart existing = helper.createModel(1);
        UserCartId id = existing.getId();
        when(cartRepo.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(cartRepo).deleteById(id);

        service.deleteById(id);
        verify(cartRepo).deleteById(id);
    }

    @Test
    void deleteById_notFound_throws() {
        UserCartId id = new UserCartId(9L,9L);
        when(cartRepo.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteById(id)
        );
        assertEquals("UserCart not found with ID: " + id, ex.getMessage());
        verify(cartRepo, never()).deleteById(any());
    }
}
