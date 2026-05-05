package com.banking.demo.controller;

import com.banking.demo.dto.QrResquest;
import com.banking.demo.service.QrCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qrcode")
@RequiredArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @PostMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQrCode(@Valid @RequestBody QrResquest request) throws Exception {
        return qrCodeService.generateQrCode(request, 300, 300);
    }
}
