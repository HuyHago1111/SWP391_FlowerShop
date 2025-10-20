package com.flowerShop1.controller.client;

import com.flowerShop1.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class VNPayController {
    @Value("${vnp_TmnCode}")
    private String vnpay_TmnCode;
    @Value("${vnp_HashSecret}")
    private String vnpay_HashSecret;
    @Value("${vnp_Url}")
    private String vnpay_Url;
    @Value("${vnp_ReturnUrl}")
    private String vnpay_Returnurl;
    @PostMapping ("/api/payment/create")
    @ResponseBody
    public Map<String, Object> createPayment(HttpServletRequest request, @RequestParam("amount") long amount) throws Exception {
        Map<String,String> vnp_Params =  new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpay_TmnCode);
        vnp_Params.put("vnp_Amount",String.valueOf(amount*100));
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang");
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", vnpay_Returnurl);
        vnp_Params.put("vnp_ExpireDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date(System.currentTimeMillis() + 15 * 60 * 1000)));
        vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder query = new StringBuilder();
        StringBuilder hashData = new StringBuilder();
        for (String name : fieldNames){
            String value = vnp_Params.get(name);
            if((value!=null) && (value.length() > 0)){
                hashData.append(name).append('=').append(java.net.URLEncoder.encode(value,"UTF-8"));
                query.append(java.net.URLEncoder.encode(name,"UTF-8")).append('=').append(java.net.URLEncoder.encode(value,"UTF-8"));
                if(!name.equals(fieldNames.get(fieldNames.size() -1))){
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnpay_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = vnpay_Url + "?" + query.toString();
        Map<String, Object> response = new HashMap<>();
        response.put("code", "00");
        response.put("message", "success");
        response.put("data", paymentUrl);
        return response;


    }

}
