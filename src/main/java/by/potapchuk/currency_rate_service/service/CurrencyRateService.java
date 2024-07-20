package by.potapchuk.currency_rate_service.service;

import by.potapchuk.currency_rate_service.core.dto.CurrencyRateDto;
import by.potapchuk.currency_rate_service.core.entity.CurrencyRate;
import by.potapchuk.currency_rate_service.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing currency rates.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateService {
    private static final String NBRB_API_URL = "https://www.nbrb.by/api/exrates/rates?ondate={date}&periodicity=0";
    private final RestTemplate restTemplate;
    private final CurrencyRateRepository currencyRateRepository;

    /**
     * Fetches currency rates from the NBRB API and saves them to the database.
     *
     * @param date the date for which to fetch the currency rates in the format yyyy-MM-dd
     */
    public void fetchAndSaveRates(String date) {
        try {
            ResponseEntity<CurrencyRateDto[]> response = restTemplate.getForEntity(NBRB_API_URL, CurrencyRateDto[].class, date);
            CurrencyRateDto[] rates = response.getBody();
            if (rates != null) {
                currencyRateRepository.saveAll(Arrays.stream(rates)
                        .map(rateDto -> new CurrencyRate(null, rateDto.getCode(), rateDto.getRate(), rateDto.getCurrency(), date))
                        .collect(Collectors.toList()));
                log.info("Rates for date {} saved successfully.", date);
            } else {
                log.warn("No rates received from API for date {}", date);
            }
        } catch (HttpClientErrorException e) {
            log.error("Error fetching rates: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    /**
     * Gets the currency rate for the specified date and currency code from the database.
     *
     * @param date the date for which to get the currency rate in the format yyyy-MM-dd
     * @param code the currency code
     * @return an optional containing the currency rate DTO if found, otherwise empty
     */
    public Optional<CurrencyRateDto> getRateByDateAndCode(String date, String code) {
        return Optional.ofNullable(currencyRateRepository.findByDateAndCode(date, code))
                .map(rate -> new CurrencyRateDto(rate.getId(), rate.getCode(), rate.getCurrency(), rate.getRate(), rate.getDate()));
    }
}