package com.flowerShop1.util;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class VNPayUtil {
    public static String hmacSHA512(String key, String data) {
        try{
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for(byte b : bytes){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
