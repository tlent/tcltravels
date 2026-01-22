package com.thomaslent.tcltravels.controllers;

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

import com.thomaslent.tcltravels.security.SecurityConfig;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.AdminEmployeeService;

@WebMvcTest(AdminEmployeesController.class)
@Import(SecurityConfig.class)
class AdminEmployeesControllerSecurityTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AdminEmployeeService adminEmployeeService;

  @Test
  void showCreateForm_allowsManager() throws Exception {
    UserPrincipal principal = new UserPrincipal(1L, 2L, null, "Test", "Manager",
        "manager@example.com", "secret", "MANAGER",
        List.of(new SimpleGrantedAuthority("ROLE_MANAGER")));

    mockMvc.perform(get("/admin/employees/new").with(user(principal)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  void showCreateForm_forbidsEmployee() throws Exception {
    mockMvc.perform(get("/admin/employees/new"))
        .andExpect(status().isForbidden());
  }

  @Test
  void showCreateForm_redirectsAnonymous() throws Exception {
    mockMvc.perform(get("/admin/employees/new"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"));
  }
}
