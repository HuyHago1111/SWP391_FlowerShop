package com.flowerShop1.service.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowerShop1.dto.product.CartItermDTO;
import com.flowerShop1.service.cookie.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private ObjectMapper objectMapper;

    public CartService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;

    }
    public List<CartItermDTO> addToCart(CartItermDTO cartItermDTO, HttpServletRequest request, HttpServletResponse response){
        List<CartItermDTO> lsCart = getlsCart(request);
        Optional<CartItermDTO> existingItem = lsCart.stream()
                .filter(item -> item.getProductId() == cartItermDTO.getProductId())
                .findFirst();
        if(existingItem.isPresent()) {
            CartItermDTO item = existingItem.get();
            item.setQuantityCart(cartItermDTO.getQuantityCart());
        } else {

            lsCart.add(cartItermDTO);

        }
        saveLsCart(lsCart, response);
        return lsCart;
    }

    public List<CartItermDTO> getlsCart(HttpServletRequest request){
        try {
            String lsCartJson = CookieService.getCookieValue(request, "lscart");
            if(lsCartJson == null || lsCartJson.isEmpty()){
                return new ArrayList<>();
            }
            if(lsCartJson != null){
                String decodeJson = java.net.URLDecoder.decode(lsCartJson, "UTF-8");
                List<CartItermDTO> lsCart = objectMapper.readValue(decodeJson, objectMapper.getTypeFactory().constructCollectionType(List.class, CartItermDTO.class));
                return lsCart;
            }
        }catch (Exception e){

        }
        return null;
    }
    public void saveLsCart(List<CartItermDTO> lsCart, HttpServletResponse response){
        try {
            String json = objectMapper.writeValueAsString(lsCart);
            String encodeJson = java.net.URLEncoder.encode(json, "UTF-8");
            CookieService.setCookie(response, "lscart", encodeJson, 7*24*60*60);


        }catch (Exception e){
            e.printStackTrace();


        }

    }
    public void updateQuantity(int productId,int quantity ,HttpServletRequest request,HttpServletResponse response){
        List<CartItermDTO> lsCart = getlsCart(request);
        Optional<CartItermDTO> existingItem = lsCart.stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst();
        if(existingItem.isPresent()) {
            CartItermDTO item = existingItem.get();
            item.setQuantityCart(quantity);
        }
        saveLsCart(lsCart,response);
    }
    public void removeCartItem(int productId,HttpServletRequest request,HttpServletResponse response){
        List<CartItermDTO> lsCart = getlsCart(request);
        lsCart.removeIf(item -> item.getProductId() == productId);
        saveLsCart(lsCart,response);
    }
}
