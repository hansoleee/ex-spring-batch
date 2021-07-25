package com.hansoleee.exspringbatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinMarketDTO {

    private String market;
    private String koreanName;
    private String englishName;
}
