package com.example.fruitshop_be.service;

import com.example.fruitshop_be.dto.request.VoucherCreateRequest;
import com.example.fruitshop_be.dto.request.VoucherUpdateRequest;
import com.example.fruitshop_be.dto.response.VoucherResponse;
import com.example.fruitshop_be.entity.Voucher;
import com.example.fruitshop_be.enums.ErrorCode;
import com.example.fruitshop_be.exception.AppException;
import com.example.fruitshop_be.mapper.VoucherMapper;
import com.example.fruitshop_be.repository.VoucherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VoucherService {
    VoucherRepository voucherRepository;
    VoucherMapper voucherMapper;
    public VoucherResponse createVoucher(VoucherCreateRequest request){
        Voucher voucher = voucherRepository.save(voucherMapper.toVoucher(request));
        return voucherMapper.toVoucherResponse(voucher);
    }

    public List<VoucherResponse> getAllVouchers(){
        return voucherRepository.findAll().stream().map(voucherMapper::toVoucherResponse).collect(Collectors.toList());

    }

    public VoucherResponse updateVoucher(String id, VoucherUpdateRequest request){
        Voucher voucher = voucherRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));
        voucherMapper.updateVoucher((voucher), request);
        return voucherMapper.toVoucherResponse(voucherRepository.save(voucher));
    }
}
