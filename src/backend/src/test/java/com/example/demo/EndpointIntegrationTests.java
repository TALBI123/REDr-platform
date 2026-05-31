package com.example.demo;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.test.web.servlet.MockMvc;

// import com.example.demo.auth.dto.LoginRequest;
// import com.example.demo.auth.dto.LoginResult;
// import com.example.demo.auth.dto.RegisterAgencyManagerRequest;
// import com.example.demo.auth.dto.RegisterRequest;
// import com.example.demo.auth.principal.CustomUserDetails;
// import com.example.demo.auth.service.AuthService;
// import com.example.demo.auth.service.RegistrationService;
// import com.example.demo.models.agency.Agency;
// import com.example.demo.models.enums.AgencyStatus;
// import com.example.demo.models.enums.UserRole;
// import com.example.demo.models.enums.UserStatus;
// import com.example.demo.models.user.Admin;
// import com.example.demo.models.user.AgencyManager;
// import com.example.demo.models.user.AppUser;
// import com.example.demo.models.user.Client;
// import com.example.demo.user.service.AgencyAdminService;
// import com.example.demo.verification.dto.ResendVerificationRequest;
// import com.example.demo.verification.service.EmailVerificationService;
// import com.fasterxml.jackson.databind.ObjectMapper;

// @SpringBootTest
// @AutoConfigureMockMvc
// class EndpointIntegrationTests {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @MockBean
//     private AuthService authService;

//     @MockBean
//     private RegistrationService registrationService;

//     @MockBean
//     private EmailVerificationService emailVerificationService;

//     @MockBean
//     private AgencyAdminService agencyAdminService;

//     private Agency agency;
//     private Agency otherAgency;
//     private Admin admin;
//     private AgencyManager manager;
//     private Client client;

//     @BeforeEach
//     void setUp() {
//         agency = new Agency();
//         agency.setId("agency-1");
//         agency.setName("Dev Agency");
//         agency.setStatus(AgencyStatus.APPROVED);

//         otherAgency = new Agency();
//         otherAgency.setId("agency-2");
//         otherAgency.setName("Other Agency");
//         otherAgency.setStatus(AgencyStatus.APPROVED);

//         admin = new Admin();
//         admin.setId("admin-1");
//         admin.setEmail("admin@demo.com");
//         admin.setPassword("admin123");
//         admin.setRole(UserRole.SUPER_ADMIN);
//         admin.setAccountStatus(UserStatus.ACTIVE);

//         manager = new AgencyManager();
//         manager.setId("manager-1");
//         manager.setEmail("manager@demo.com");
//         manager.setPassword("manager123");
//         manager.setRole(UserRole.AGENCY_MANAGER);
//         manager.setAccountStatus(UserStatus.ACTIVE);
//         manager.setAgency(agency);

//         client = new Client();
//         client.setId("client-1");
//         client.setEmail("client@demo.com");
//         client.setPassword("client123");
//         client.setRole(UserRole.CLIENT);
//         client.setAccountStatus(UserStatus.ACTIVE);
//     }

//     private Authentication authFor(AppUser user) {
//         CustomUserDetails details = CustomUserDetails.from(user);
//         return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
//     }

//     @Test
//     void login_sets_cookie() throws Exception {
//         LoginRequest request = new LoginRequest("client@demo.com", "client123");
//         when(authService.login(any())).thenReturn(new LoginResult("token-123", CustomUserDetails.from(client)));

//         mockMvc.perform(post("/auth/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isOk())
//                 .andExpect(header().string(HttpHeaders.SET_COOKIE, org.hamcrest.Matchers.containsString("ACCESS_TOKEN=")));
//     }

//     @Test
//     void logout_clears_cookie() throws Exception {
//         mockMvc.perform(post("/auth/logout"))
//                 .andExpect(status().isOk())
//                 .andExpect(header().string(HttpHeaders.SET_COOKIE, org.hamcrest.Matchers.containsString("ACCESS_TOKEN=")));
//     }

//     @Test
//     void register_client_returns_created() throws Exception {
//         RegisterRequest request = new RegisterRequest(
//                 "client.new@example.com",
//                 "password123",
//                 "New",
//                 "Client",
//                 "0600000000",
//                 "LIC-123",
//                 "PASS-456",
//                 "Rabat"
//         );
//         when(registrationService.register(any())).thenReturn(client);

//         mockMvc.perform(post("/auth/register/client")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.email").value("client@demo.com"));
//     }

//     @Test
//     void register_agency_returns_created() throws Exception {
//         RegisterAgencyManagerRequest request = new RegisterAgencyManagerRequest(
//                 "manager.new@example.com",
//                 "password123",
//                 "New",
//                 "Manager",
//                 "0611111111",
//                 "LIC-MAN-001",
//                 "NID-001",
//                 "New Agency",
//                 "Casa",
//                 "0522000000",
//                 "agency.new@example.com",
//                 "10 Avenue",
//                 "Agence test"
//         );
//         when(registrationService.registerAgencyManager(any())).thenReturn(manager);

//         mockMvc.perform(post("/auth/register/agency")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.email").value("manager@demo.com"));
//     }

//     @Test
//     void verify_email_calls_service() throws Exception {
//         doNothing().when(emailVerificationService).verifyEmail("token-123");

//         mockMvc.perform(get("/verification/verify-email")
//                 .param("token", "token-123"))
//                 .andExpect(status().isOk());

//         verify(emailVerificationService).verifyEmail("token-123");
//     }

//     @Test
//     void resend_verification_calls_service() throws Exception {
//         ResendVerificationRequest request = new ResendVerificationRequest("client.new@example.com");

//         mockMvc.perform(post("/verification/resend")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isOk());

//         verify(emailVerificationService).sendVerificationEmail("client.new@example.com");
//     }

//     @Test
//     void agencies_requires_authentication() throws Exception {
//         mockMvc.perform(get("/agencies"))
//                 .andExpect(status().isUnauthorized());
//     }

//     @Test
//     void agencies_allows_authenticated_user() throws Exception {
//         mockMvc.perform(get("/agencies")
//                 .with(authentication(authFor(client))))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void agency_details_admin_allowed() throws Exception {
//         mockMvc.perform(get("/agencies/{agencyId}", agency.getId())
//                 .with(authentication(authFor(admin))))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void agency_details_manager_same_agency_allowed() throws Exception {
//         mockMvc.perform(get("/agencies/{agencyId}", agency.getId())
//                 .with(authentication(authFor(manager))))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void agency_details_manager_other_agency_forbidden() throws Exception {
//         mockMvc.perform(get("/agencies/{agencyId}", otherAgency.getId())
//                 .with(authentication(authFor(manager))))
//                 .andExpect(status().isForbidden());
//     }

//     @Test
//     void agency_details_client_forbidden() throws Exception {
//         mockMvc.perform(get("/agencies/{agencyId}", agency.getId())
//                 .with(authentication(authFor(client))))
//                 .andExpect(status().isForbidden());
//     }

//     @Test
//     void bookings_my_allows_client_and_admin() throws Exception {
//         mockMvc.perform(get("/bookings/my")
//                 .with(authentication(authFor(client))))
//                 .andExpect(status().isOk());

//         mockMvc.perform(get("/bookings/my")
//                 .with(authentication(authFor(admin))))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void bookings_my_forbidden_for_manager() throws Exception {
//         mockMvc.perform(get("/bookings/my")
//                 .with(authentication(authFor(manager))))
//                 .andExpect(status().isForbidden());
//     }

//     @Test
//     void admin_users_requires_super_admin() throws Exception {
//         mockMvc.perform(get("/admin/users")
//                 .with(authentication(authFor(admin))))
//                 .andExpect(status().isOk());

//         mockMvc.perform(get("/admin/users")
//                 .with(authentication(authFor(client))))
//                 .andExpect(status().isForbidden());
//     }

//     @Test
//     void admin_agencies_requires_super_admin() throws Exception {
//         mockMvc.perform(get("/admin/agencies")
//                 .with(authentication(authFor(admin))))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void admin_agencies_pending_requires_super_admin() throws Exception {
//         when(agencyAdminService.getPendingAgencies()).thenReturn(List.of(agency));

//         mockMvc.perform(get("/admin/agencies/pending")
//                 .with(authentication(authFor(admin))))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void admin_agency_approval_endpoints() throws Exception {
//         mockMvc.perform(post("/admin/agencies/{agencyId}/approve", agency.getId())
//                 .with(authentication(authFor(admin)))
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"comment\":\"OK\"}"))
//                 .andExpect(status().isOk());

//         mockMvc.perform(post("/admin/agencies/{agencyId}/reject", agency.getId())
//                 .with(authentication(authFor(admin)))
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"reason\":\"Reason is long enough for validation\"}"))
//                 .andExpect(status().isOk());

//         mockMvc.perform(post("/admin/agencies/{agencyId}/suspend", agency.getId())
//                 .with(authentication(authFor(admin)))
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"reason\":\"Suspension reason is long enough\"}"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     void test_endpoint_returns_authenticated_name() throws Exception {
//         mockMvc.perform(get("/test")
//                 .with(authentication(authFor(client))))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string(org.hamcrest.Matchers.containsString("Authenticated as:")));
//     }
// }
