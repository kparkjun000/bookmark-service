package com.shopping.service;

import com.shopping.dto.CartItemRequest;
import com.shopping.dto.CartItemResponse;
import com.shopping.dto.CartResponse;
import com.shopping.entity.Cart;
import com.shopping.entity.CartItem;
import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.exception.BadRequestException;
import com.shopping.exception.InsufficientStockException;
import com.shopping.exception.ResourceNotFoundException;
import com.shopping.repository.CartItemRepository;
import com.shopping.repository.CartRepository;
import com.shopping.repository.ProductRepository;
import com.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartResponse getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(user));

        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse addItemToCart(Long userId, CartItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(user));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        if (!product.getIsActive()) {
            throw new BadRequestException("Product is not available");
        }

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
            cartItem.setQuantity(newQuantity);
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            cart.getItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to user");
        }

        if (cartItem.getProduct().getStockQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + cartItem.getProduct().getName());
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to user");
        }

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return mapToResponse(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        cart.getItems().clear();
        cartItemRepository.deleteAll(cart.getItems());
    }

    @Transactional
    protected Cart createCartForUser(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .build();
        return cartRepository.save(cart);
    }

    private CartResponse mapToResponse(Cart cart) {
        var items = cart.getItems().stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());

        BigDecimal totalAmount = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .totalAmount(totalAmount)
                .build();
    }

    private CartItemResponse mapToCartItemResponse(CartItem item) {
        BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }
}

