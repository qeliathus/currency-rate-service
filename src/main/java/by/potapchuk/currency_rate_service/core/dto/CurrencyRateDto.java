package by.potapchuk.currency_rate_service.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRateDto {

    @JsonProperty("Cur_ID")
    private Long id;

    @JsonProperty("Cur_Abbreviation")
    private String code;

    @JsonProperty("Cur_Name")
    private String currency;

    @JsonProperty("Cur_OfficialRate")
    private double rate;

    @JsonProperty("Date")
    private String date;
}