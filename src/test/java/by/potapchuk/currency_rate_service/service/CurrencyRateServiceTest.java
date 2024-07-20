package by.potapchuk.currency_rate_service.service;

import by.potapchuk.currency_rate_service.core.dto.CurrencyRateDto;
import by.potapchuk.currency_rate_service.core.entity.CurrencyRate;
import by.potapchuk.currency_rate_service.repository.CurrencyRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CurrencyRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    private CurrencyRateService currencyRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchAndSaveRates() {
        String date = "2023-07-20";
        CurrencyRateDto[] rates = {
                new CurrencyRateDto(1L, "USD", "US Dollar", 2.6, date)
        };

        when(restTemplate.getForEntity(anyString(), eq(CurrencyRateDto[].class), eq(date)))
                .thenReturn(new ResponseEntity<>(rates, HttpStatus.OK));

        currencyRateService.fetchAndSaveRates(date);

        verify(currencyRateRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testGetRateByDateAndCode() {
        String date = "2023-07-20";
        String code = "USD";
        CurrencyRate currencyRate = new CurrencyRate(1L, code, 2.6, "US Dollar", date);

        when(currencyRateRepository.findByDateAndCode(date, code)).thenReturn(currencyRate);

        Optional<CurrencyRateDto> result = currencyRateService.getRateByDateAndCode(date, code);

        assertTrue(result.isPresent());
        assertEquals("USD", result.get().getCode());
        assertEquals(2.6, result.get().getRate());
    }
}