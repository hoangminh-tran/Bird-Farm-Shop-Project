package com.tttm.birdfarmshop.Utils.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypeOfBirdResponse {
    private String typeID;

    private String typeName;

    private Integer quantity;
}
