package com.tttm.birdfarmshop.Utils.Request;

import com.tttm.birdfarmshop.Enums.BirdColor;
import com.tttm.birdfarmshop.Models.Bird;
import com.tttm.birdfarmshop.Models.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdMatchingRequest {
    BirdMatching firstBird;
    BirdMatching secondBird;
}
