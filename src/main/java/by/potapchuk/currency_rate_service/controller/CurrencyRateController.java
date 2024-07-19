package by.potapchuk.currency_rate_service.controller;

import by.potapchuk.currency_rate_service.core.dto.CurrencyRateDto;
import by.potapchuk.currency_rate_service.service.CurrencyRateService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class CurrencyRateController {
    private final String DATE_REGEXP = "\\d{4}-\\d{2}-\\d{2}";
    private final CurrencyRateService currencyRateService;

    @GetMapping("/fetch")
    public ResponseEntity<String> fetchRates(
            @RequestParam @NotEmpty @Pattern(regexp = DATE_REGEXP) String date) {
        currencyRateService.fetchAndSaveRates(date);
        return ResponseEntity.ok("Data fetched and saved successfully for date: " + date);
    }

    @GetMapping
    public ResponseEntity<CurrencyRateDto> getRate(
            @RequestParam @NotEmpty @Pattern(regexp = DATE_REGEXP) String date,
            @RequestParam @NotEmpty String code) {
        Optional<CurrencyRateDto> rate = currencyRateService.getRateByDateAndCode(date, code);
        return rate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}