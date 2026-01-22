package com.thomaslent.tcltravels.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.security.SecurityConfig;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.UserService;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerSecurityTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Test
  void showAccountPage_allowsCustomer() throws Exception {
    UserPrincipal principal = new UserPrincipal(1L, 2L, 3L, "Test", "User",
        "test@example.com", "secret", "CUSTOMER",
        List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));

    when(userService.getUserDto(2L)).thenReturn(new UserDto());

    mockMvc.perform(get("/account").with(user(principal)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "MANAGER")
  void showAccountPage_forbidsManager() throws Exception {
    mockMvc.perform(get("/account"))
        .andExpect(status().isForbidden());
  }

  @Test
  void showAccountPage_redirectsAnonymous() throws Exception {
    mockMvc.perform(get("/account"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"));
  }
}
