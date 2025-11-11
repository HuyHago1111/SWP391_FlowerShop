package com.flowerShop1.service.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowerShop1.dto.product.CartItermDTO;
import com.flowerShop1.entity.Product;
// ❌ KHÔNG CẦN IMPORT LỖI MỚI
import com.flowerShop1.service.cookie.CookieService;
import com.flowerShop1.service.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public CartService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Sửa đổi: Ném lỗi với thông báo "Số lượng trong kho không đủ"
     */
    public List<CartItermDTO> addToCart(CartItermDTO cartItermDTO, HttpServletRequest request, HttpServletResponse response) {

        Product product;
        try {
            product = productService.getProductById(cartItermDTO.getProductId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại hoặc đã bị xóa.");
        }

        int stockQuantity = product.getStockQuantity();
        if (stockQuantity <= 0) {
            // ✅ SỬA LỖI 1: Thông báo hết hàng
            throw new IllegalArgumentException("Sản phẩm này hiện đã hết hàng.");
        }

        int quantityToAdd = cartItermDTO.getQuantityCart();
        if (quantityToAdd <= 0) {
            quantityToAdd = 1;
        }

        // ✅ SỬA LỖI 2: Thông báo khi thêm mới vượt kho
        if (quantityToAdd > stockQuantity) {
            // Thông báo này xuất hiện khi giỏ hàng rỗng, nhưng thêm 20 (tồn kho 10)
            throw new IllegalArgumentException("Số lượng trong kho không đủ (Chỉ còn " + stockQuantity + ").");
        }

        List<CartItermDTO> lsCart = getlsCart(request);
        Optional<CartItermDTO> existingItem = lsCart.stream()
                .filter(item -> item.getProductId() == cartItermDTO.getProductId())
                .findFirst();

        if (existingItem.isPresent()) {
            CartItermDTO item = existingItem.get();
            int newQuantity = item.getQuantityCart() + quantityToAdd;

            // ✅ SỬA LỖI 3: Thông báo khi cộng dồn vượt kho
            // (Đây là trường hợp của bạn: có 9, thêm 20, tồn kho 10)
            if (newQuantity > stockQuantity) {
                throw new IllegalArgumentException("Số lượng trong kho không đủ (Đã có " + item.getQuantityCart() + " trong giỏ.)");
            }
            item.setQuantityCart(newQuantity);

        } else {
            cartItermDTO.setQuantityCart(quantityToAdd);
            lsCart.add(cartItermDTO);
        }

        saveLsCart(lsCart, response);
        return lsCart;
    }

    // ... (Toàn bộ các hàm còn lại: updateQuantity, getlsCart, saveLsCart...
    //      bạn giữ nguyên như phiên bản trước) ...
    // ...
    public void updateQuantity(int productId, int quantity, HttpServletRequest request, HttpServletResponse response) {

        Product product;
        try {
            product = productService.getProductById(productId);
        } catch (Exception e) {
            removeCartItem(productId, request, response);
            throw new IllegalArgumentException("Sản phẩm không tồn tại và đã được xóa khỏi giỏ.");
        }

        int stockQuantity = product.getStockQuantity();
        if (stockQuantity <= 0) {
            removeCartItem(productId, request, response);
            throw new IllegalArgumentException("Sản phẩm đã hết hàng và được xóa khỏi giỏ.");
        }

        if (quantity <= 0) {
            quantity = 1;
        }

        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Số lượng trong kho không đủ (Chỉ còn " + stockQuantity + ").");
        }

        List<CartItermDTO> lsCart = getlsCart(request);
        Optional<CartItermDTO> existingItem = lsCart.stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst();

        if (existingItem.isPresent()) {
            CartItermDTO item = existingItem.get();
            item.setQuantityCart(quantity);
        }

        saveLsCart(lsCart, response);
    }

    public List<CartItermDTO> getlsCart(HttpServletRequest request) {
        try {
            String lsCartJson = CookieService.getCookieValue(request, "lscart");
            if (lsCartJson == null || lsCartJson.isEmpty()) {
                return new ArrayList<>();
            }
            String decodeJson = java.net.URLDecoder.decode(lsCartJson, "UTF-8");
            List<CartItermDTO> lsCart = objectMapper.readValue(decodeJson, objectMapper.getTypeFactory().constructCollectionType(List.class, CartItermDTO.class));
            return (lsCart != null) ? lsCart : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Lỗi giải mã cookie giỏ hàng. Trả về giỏ hàng rỗng.", e);
            return new ArrayList<>();
        }
    }

    public void saveLsCart(List<CartItermDTO> lsCart, HttpServletResponse response) {
        try {
            String json = objectMapper.writeValueAsString(lsCart);
            String encodeJson = java.net.URLEncoder.encode(json, "UTF-8");
            CookieService.setCookie(response, "lscart", encodeJson, 7 * 24 * 60 * 60);
        } catch (Exception e) {
            logger.error("Lỗi mã hóa và lưu cookie giỏ hàng.", e);
        }
    }

    public void removeCartItem(int productId, HttpServletRequest request, HttpServletResponse response) {
        List<CartItermDTO> lsCart = getlsCart(request);
        lsCart.removeIf(item -> item.getProductId() == productId);
        saveLsCart(lsCart, response);
    }

    public void clearCart(HttpServletResponse response) {
        CookieService.setCookie(response, "lscart", "", 0);
    }
}