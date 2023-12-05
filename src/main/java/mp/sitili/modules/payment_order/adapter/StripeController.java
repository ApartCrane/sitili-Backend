package mp.sitili.modules.payment_order.adapter;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import mp.sitili.modules.payment_order.use_cases.http.PaymentIntentDto;
import mp.sitili.modules.payment_order.use_cases.service.PaymentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    PaymentService paymentService;

    /*
     * @PostMapping("/paymentintent")
     * public ResponseEntity<String> payment(@RequestBody PaymentIntentDto
     * paymentIntentDto) throws StripeException {
     * PaymentIntent paymentIntent = paymentService.paymentIntent(paymentIntentDto);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */
    @PostMapping("/paymentintent")
    public ResponseEntity<String> payment(@RequestBody List<PaymentIntentDto> paymentIntentDtoList)
            throws StripeException {
        List<PaymentIntent> paymentIntents = paymentService.paymentIntents(paymentIntentDtoList);
        List<String> paymentStrList = paymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }

    /*
     * @PostMapping("/confirm/{id}")
     * public ResponseEntity<String> confirm(@PathVariable("id") String id) throws
     * StripeException {
     * PaymentIntent paymentIntent = paymentService.confirm(id);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */
    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestBody List<String> paymentIntentIds) throws StripeException {
        List<PaymentIntent> confirmedPaymentIntents = paymentService.confirmPaymentIntents(paymentIntentIds);
        List<String> paymentStrList = confirmedPaymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }

    /*
     * @PostMapping("/cancel/{id}")
     * public ResponseEntity<String> cancel(@PathVariable("id") String id) throws
     * StripeException {
     * PaymentIntent paymentIntent = paymentService.cancel(id);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */
    @PostMapping("/cancel")
    public ResponseEntity<String> cancel(@RequestBody List<String> paymentIntentIds) throws StripeException {
        List<PaymentIntent> cancelledPaymentIntents = paymentService.cancelPaymentIntents(paymentIntentIds);
        List<String> paymentStrList = cancelledPaymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }
    

}
