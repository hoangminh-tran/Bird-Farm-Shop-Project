package com.tttm.birdfarmshop.Service;

import com.tttm.birdfarmshop.DTO.MailDTO;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Utils.Response.AuthenticationResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import jakarta.servlet.http.HttpSession;

public interface CodeStorageService {
     MessageResponse getCodeFromSession(MailDTO dto, HttpSession session);

     MessageResponse register(User dto, HttpSession session) throws CustomException;

}
