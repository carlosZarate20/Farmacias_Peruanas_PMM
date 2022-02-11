package com.farmaciasperuanas.pmmli.merchandisemovements.rest;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.InventoryAdjustmentDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.service.InventoryAdjustmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class InventoryAdjustmentController {

    @Autowired
    private InventoryAdjustmentService inventoryAdjustmentService;

    @PostMapping("/controlInventarioLi")
    public ResponseDto enviarControlInventarioLi(@RequestBody List<InventoryAdjustmentDto> inventoryAdjustmentDtoList){
        return inventoryAdjustmentService.enviarControlInventarioLi(inventoryAdjustmentDtoList);
    }
}
