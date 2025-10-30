package com.vovgoo.BankSystem.config.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "transaction")
public class TransactionProperties {

    @Min(0)
    @Max(100)
    private double transferCommissionPercent;
}