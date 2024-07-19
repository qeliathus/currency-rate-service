package by.potapchuk.currency_rate_service.repository;

import by.potapchuk.currency_rate_service.core.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    CurrencyRate findByDateAndCode(String date, String code);
}