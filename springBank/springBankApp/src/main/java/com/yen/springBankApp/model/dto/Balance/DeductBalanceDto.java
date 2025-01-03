package com.yen.springBankApp.model.dto.Balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeductBalanceDto {

    private Integer userId;
    private Integer amount;
}
