package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.InventoryAdjustmentDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;

import java.util.List;

public interface InventoryAdjustmentService {
    ResponseDto enviarControlInventarioLi(List<InventoryAdjustmentDto> inventoryAdjustmentDtoList);
}
