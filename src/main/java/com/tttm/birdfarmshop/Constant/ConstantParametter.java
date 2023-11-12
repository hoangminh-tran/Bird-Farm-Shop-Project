package com.tttm.birdfarmshop.Constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantParametter {
    public static final String USER_ID = "/{UserID}";
    public static final String SHIPPER_ID = "/{ShipperID}";
    public static final String TYPE_OF_BIRD_ID = "/{TypeID}";
    public static final String FOOD_ID = "/{FoodID}";
    public static final String NEST_ID = "/{NestID}";
    public static final String BIRD_ID = "/{BirdID}";
    public static final String ORDER_ID = "/{OrderID}";
    public static final String VOUCHER_ID = "/{VoucherID}";
    public static final String VOUCHER_NAME = "/{VoucherName}";
    public static final String TYPE_OF_BIRD_NAME = "/{TypeOfBirdName}";
    public static final String BIRD_NAME = "/{BirdName}";
    public static final String HEALTH_CARE_ID = "/{healthcareID}";
}
