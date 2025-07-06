package com.fashionstore.fashionstore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {

    private Integer productDetailId;
    private Integer quantity;

}
