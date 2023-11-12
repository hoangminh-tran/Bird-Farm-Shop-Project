package com.tttm.birdfarmshop.Constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantAPI {
    // Request Mapping
    public static final String CUSTOMER = "/customer";
    public static final String SHIPPER = "/shipper";
    public static final String SELLER = "/seller";
    public static final String ADMIN = "/admin";
    public static final String HEALTH_CARE_PROFESSIONAL = "/healthCareProfestional";
    public static final String AUTH = "/auth";
    public static final String EMAIL = "/email";
    public static final String TYPE_OF_BIRD = "/typeOfBird";
    public static final String FOOD = "/food";
    public static final String NEST = "/nest";
    public static final String BIRD = "/bird";
    public static final String ORDER = "/order";
    public static final String VOUCHER = "/voucher";

    // Function
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String FORGOT_PASSWORD = "/forgotPassword";
    public static final String SEND_CODE = "/sendCode";
    public static final String VERIFY_CODE = "/verifyCode";
    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String LOG_OUT = "/refresh-token";
    public static final String GET_ALL_CUSTOMERS = "/getAllCustomers";
    public static final String GET_ALL_HEALTHCARES = "/getAllHealthcares";
    public static final String GET_ALL_SHIPPERS = "/getAllShippers";
    public static final String GET_ALL_USERS = "/getAllUsers";
    public static final String BAN_USER_ACCOUNT = "/banUserAccount";
    public static final String UNBAN_USER_ACCOUNT = "/unBanUserAccount";

    // Type Of Bird Function
    public static final String ADD_TYPE_OF_BIRD = "/addTypeOfBird";
    public static final String UPDATE_TYPE_OF_BIRD = "/updateTypeOfBird";
    public static final String GET_TYPE_OF_BIRD_BY_ID = "/getTypeOfBirdByID";
    public static final String GET_ALL_TYPE_OF_BIRD = "/getAllTypeOfBird";
    public static final String GET_TYPE_OF_BIRD_BY_NAME = "/getTypeOfBirdByName";

    // Food Function
    public static final String ADD_FOOD = "/addFood";
    public static final String UPDATE_FOOD = "/updateFood";
    public static final String GET_FOOD_BY_ID = "/getFoodByID";
    public static final String GET_ALL_FOOD = "/getAllFood";
    public static final String FILTER_FOOD_BY_PRICE = "/filterFoodByPrice";
    public static final String GET_FOOD_BY_NAME = "/getFoodByName";
    public static final String SORT_FOOD_BY_PRICE_ASC = "/sortFoodByPriceAscending";
    public static final String SORT_FOOD_BY_PRICE_DESC = "/sortFoodByPriceDescending";
    public static final String SORT_FOOD_BY_ALPHABET_ASC = "/sortFoodByAlphabetAscending";
    public static final String SORT_FOOD_BY_ALPHABET_DESC = "/sortFoodByAlphabetDescending";

    // Nest Function
    public static final String ADD_NEST = "/addNest";
    public static final String UPDATE_NEST = "/updateNest";
    public static final String GET_NEST_BY_ID = "/getNestByID";
    public static final String GET_ALL_NEST = "/getAllNest";
    public static final String FILTER_NEST_BY_PRICE = "/filterNestByPrice";
    public static final String GET_NEST_BY_NAME = "/getNestByName";
    public static final String SORT_NEST_BY_PRICE_ASC = "/sortNestByPriceAscending";
    public static final String SORT_NEST_BY_PRICE_DESC = "/sortNestByPriceDescending";
    public static final String SORT_NEST_BY_ALPHABET_ASC = "/sortNestByAlphabetAscending";
    public static final String SORT_NEST_BY_ALPHABET_DESC = "/sortNestByAlphabetDescending";

    // Bird Function
    public static final String ADD_BIRD = "/addBird";
    public static final String ADD_BIRD_LIST = "/addBirdList";
    public static final String UPDATE_BIRD = "/updateBird";
    public static final String GET_BIRD_BY_ID = "/getBirdByID";
    public static final String GET_ALL_BIRD = "/getAllBird";
    public static final String MATCHING_BIRD_FROM_SAME_OWNER = "/matchingSameOwner";
    public static final String MATCHING_BIRD_FROM_DIFFERENT_OWNER = "/matchingDifferentOwner";
    public static final String MATCHING_BIRD_IN_SHOP = "/matchingBirdInShop";
    public static final String GET_BIRD_BY_NAME = "/getBirdByName";
    public static final String SORT_BIRD_BY_PRICE_ASC = "/sortBirdByPriceAscending";
    public static final String SORT_BIRD_BY_PRICE_DESC = "/sortBirdByPriceDescending";
    public static final String SORT_BIRD_BY_ALPHABET_ASC = "/sortBirdByAlphabetAscending";
    public static final String SORT_BIRD_BY_ALPHABET_DESC = "/sortBirdByAlphabetDescending";
    public static final String FILTER_BIRD_BY_REQUEST = "/filterBirdByRequest";
    public static final String UPDATE_BIRD_OWNER = "/updateBirdOwner";

    //Order Function
    public static final String CREATE_ORDER = "/createOrder";
    public static final String UPDATE_PAYMENT_STATUS = "/updatePaymentStatus";
    public static final String GET_ORDER_BY_ID = "/getOrderByID";
    public static final String GET_ALL_ORDER = "/getAllOrder";
    public static final String DELETE_ORDER = "/deleteOrder";
    public static final String SEND_MAIL_CANCEL_ORDER = "/sendMailForCancelOrder";
    public static final String SEND_MAIL_COMPLETE_ORDER = "/sendMailCompleteOrder";
    public static final String VIEW_ORDER_HISTORY_CUSTOMER = "/viewOrderHistoryCustomer";

    //Voucher Function
    public static final String CREATE_VOUCHER = "/createVoucher";
    public static final String GET_VOUCHER_BY_ID = "/getVoucherByID";
    public static final String GET_ALL_VOUCHER = "/getAllVoucher";
    public static final String UPDATE_VOUCHER = "/updateVoucher";
    public static final String GET_VOUCHER_BY_NAME = "/getVoucherByName";
    public static final String FILTER_VOUCHER_BY_DAY = "/filterVoucherByDate";
    public static final String SORT_VOUCHER_BY_PRICE_ASC = "/sortVoucherByPriceAscending";
    public static final String SORT_VOUCHER_BY_PRICE_DESC = "/sortVoucherByPriceDescending";
    public static final String SORT_VOUCHER_BY_ALPHABET_ASC = "/sortVoucherByAlphabetAscending";
    public static final String SORT_VOUCHER_BY_ALPHABET_DESC = "/sortVoucherByAlphabetDescending";

    //Invoice Function
    public static final String INVOICE = "/invoice";
    public static final String CREATE_INVOICE = "/createInvoice";
    public static final String GET_INVOICE_BY_ID = "/getVoucherByID";
    public static final String GET_ALL_INVOICE = "/getAllVoucher";
    public static final String UPDATE_INVOICE = "/updateVoucher";

    //Shipper Function
    public static final String GET_SHIPPED_ORDER = "/getShippedOrder";
    public static final String UPDATE_ORDER_STATUS_SHIPPER = "/updateShippedOrder";

    // Healthcare Function
    public static final String VIEW_ALL_BIRD_BELONG_TO_HEALTHCARE = "/viewAllBirdBelongToHealthCare";
    public static final String UPDATE_BIRD_STATUS_HEALTHCARE = "/updateBirdStatusHealthcare";
}
