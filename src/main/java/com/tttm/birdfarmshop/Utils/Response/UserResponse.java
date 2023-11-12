package com.tttm.birdfarmshop.Utils.Response;

import com.tttm.birdfarmshop.Enums.AccountStatus;
import com.tttm.birdfarmshop.Enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Integer userID;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private Boolean gender;

    private Date dateOfBirth;

    private String address;

    private AccountStatus accountStatus;
    private ERole role;
}
