package com.tttm.birdfarmshop.Service;


import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Utils.Request.AuthenticationRequest;
import com.tttm.birdfarmshop.Utils.Response.AuthenticationResponse;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface AuthenticationService {
   AuthenticationResponse login(AuthenticationRequest dto) throws CustomException;

   AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
