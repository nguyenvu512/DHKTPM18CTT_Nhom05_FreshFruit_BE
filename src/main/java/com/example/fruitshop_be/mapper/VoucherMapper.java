package com.example.fruitshop_be.mapper;

import com.example.fruitshop_be.dto.request.VoucherCreateRequest;
import com.example.fruitshop_be.dto.request.VoucherUpdateRequest;
import com.example.fruitshop_be.dto.response.VoucherResponse;
import com.example.fruitshop_be.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher toVoucher(VoucherCreateRequest request);

    @Mapping(source = "id", target = "id")
    VoucherResponse toVoucherResponse(Voucher voucher);
    void updateVoucher(@MappingTarget Voucher voucher, VoucherUpdateRequest request);
}

