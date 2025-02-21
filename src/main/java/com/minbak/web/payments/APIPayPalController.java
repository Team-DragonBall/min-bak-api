package com.minbak.web.payments;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
public class APIPayPalController {

    @Value("${server.url}")
    private String serverUrl;

    private final PayPalService payPalService;

    public APIPayPalController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @GetMapping("/pay")
    public ResponseEntity<?> payment() {
        try {
            String redirectUrl = payPalService.createPayment(20.00, "USD", "paypal",
                    "sale", "Payment Description",
                    serverUrl+"/api/paypal/cancel",
                    serverUrl+"/api/paypal/success");
            // 성공 시 redirect, 실패 시 JSON 응답을 반환
            // HttpHeaders를 사용하여 리다이렉트 URL을 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(redirectUrl));
            return ResponseEntity.status(HttpStatus.FOUND)  // 302 Redirect
                    .headers(headers)
                    .build();
        } catch (PayPalRESTException e) {
            // TODO 로그 남기기
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment creation failed."));
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);

            // {"paymentId":"PAYID-M636YWQ9LK93425FJ417642Y","payerId":"GNAJCMU8Q7YWY","state":"approved","status":"success"}
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("paymentId", payment.getId());
            response.put("payerId", payment.getPayer().getPayerInfo().getPayerId());
            response.put("state", payment.getState());

            return ResponseEntity.ok().body(response);
        } catch (PayPalRESTException e) {
            // TODO 로그 남기기
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment execution failed."));
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> cancel() {
        return ResponseEntity.ok().body(Map.of("status", "cancel", "message", "Payment was cancelled."));
    }
}