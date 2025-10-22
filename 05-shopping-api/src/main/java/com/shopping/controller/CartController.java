package com.shopping.controller;

import com.shopping.dto.CartItemRequest;
import com.shopping.dto.CartResponse;
import com.shopping.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Shopping cart management APIs")
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user cart", description = "Retrieve the shopping cart for a specific user")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/user/{userId}/items")
    @Operation(summary = "Add item to cart", description = "Add a product to the user's shopping cart")
    public ResponseEntity<CartResponse> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, request));
    }

    @PutMapping("/user/{userId}/items/{cartItemId}")
    @Operation(summary = "Update cart item", description = "Update the quantity of an item in the cart")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(userId, cartItemId, quantity));
    }

    @DeleteMapping("/user/{userId}/items/{cartItemId}")
    @Operation(summary = "Remove item from cart", description = "Remove an item from the shopping cart")
    public ResponseEntity<CartResponse> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, cartItemId));
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = "Clear cart", description = "Remove all items from the shopping cart")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}

