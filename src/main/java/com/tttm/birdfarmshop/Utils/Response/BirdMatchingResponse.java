package com.tttm.birdfarmshop.Utils.Response;

import com.tttm.birdfarmshop.Enums.BirdColor;
import com.tttm.birdfarmshop.Enums.BirdMatchingStatus;
import com.tttm.birdfarmshop.Models.Bird;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BirdMatchingResponse {
    private Bird bird;
    private Float successRate;
}
