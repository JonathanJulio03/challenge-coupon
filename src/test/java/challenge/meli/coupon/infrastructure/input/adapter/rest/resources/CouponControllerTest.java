package challenge.meli.coupon.infrastructure.input.adapter.rest.resources;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import challenge.meli.coupon.application.input.CouponUseCase;
import challenge.meli.coupon.infrastructure.input.adapter.rest.command.request.CouponRequest;
import challenge.meli.coupon.infrastructure.input.adapter.rest.command.response.CouponResponse;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

  @InjectMocks
  private CouponController controller;

  @Mock
  private CouponUseCase useCase;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void testCoupon_Success() throws Exception {
    CouponResponse response = CouponResponse.builder()
        .itemIds(Arrays.asList("item1", "item2"))
        .total(100.0)
        .build();

    when(useCase.coupon(any(CouponRequest.class))).thenReturn(response);

    mockMvc.perform(post("/coupon")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"item_ids\": [\"item1\", \"item2\"], \"amount\": 100.0}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.total").value(100.0));
  }

  @Test
  void testCoupon_InvalidRequest() throws Exception {
    mockMvc.perform(post("/coupon")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"amount\": 100.0}"))
        .andExpect(status().isBadRequest());

    mockMvc.perform(post("/coupon")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"item_ids\": [\"item1\"]}"))
        .andExpect(status().isBadRequest());
  }
}
