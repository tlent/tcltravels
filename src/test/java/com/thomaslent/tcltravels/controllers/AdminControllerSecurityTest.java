package com.thomaslent.tcltravels.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.thomaslent.tcltravels.dto.TopRevenueView;
import com.thomaslent.tcltravels.repositories.ReservationPassengerRepository;
import com.thomaslent.tcltravels.security.SecurityConfig;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.AdminEmployeeService;
import com.thomaslent.tcltravels.services.AdminFlightsService;
import com.thomaslent.tcltravels.services.AdminReportingService;
import com.thomaslent.tcltravels.services.AdminSelectionService;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminReportingService adminReportingService;

    @MockitoBean
    private AdminEmployeeService adminEmployeeService;

    @MockitoBean
    private AdminFlightsService adminFlightsService;

    @MockitoBean
    private AdminSelectionService adminSelectionService;

    @MockitoBean
    private ReservationPassengerRepository reservationPassengerRepository;

    @BeforeEach
    void setUp() {
        when(adminReportingService.getTopCustomer())
                .thenReturn(new TopRevenueView("Test", "Customer", BigDecimal.ZERO));
        when(adminReportingService.getTopEmployee())
                .thenReturn(new TopRevenueView("Test", "Employee", BigDecimal.ZERO));
        when(adminReportingService.getMonthlySales(anyInt(), anyInt()))
                .thenReturn(List.of());
        when(adminReportingService.getMonthlySalesTotal(anyInt(), anyInt()))
                .thenReturn(BigDecimal.ZERO);
        when(adminEmployeeService.getEmployees()).thenReturn(List.of());
    }

    @Test
    void getAdminDashboard_allowsManager() throws Exception {
        UserPrincipal principal = new UserPrincipal(1L, 2L, null, "Test", "Manager",
                "manager@example.com", "secret", "MANAGER",
                List.of(new SimpleGrantedAuthority("ROLE_MANAGER")));

        mockMvc.perform(get("/admin").with(user(principal)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getAdminDashboard_forbidsCustomer() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAdminDashboard_redirectsAnonymous() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
