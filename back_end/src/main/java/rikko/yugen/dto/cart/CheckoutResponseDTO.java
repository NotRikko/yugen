package rikko.yugen.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponseDTO {
    private boolean success;
    private List<String> messages = new ArrayList<>();
}