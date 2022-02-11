package com.farmaciasperuanas.pmmli.merchandisemovements.service;

import com.farmaciasperuanas.pmmli.merchandisemovements.dto.InventoryAdjustmentDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.MerchandiseTransferDto;
import com.farmaciasperuanas.pmmli.merchandisemovements.dto.ResponseDto;

import java.util.List;

public interface MerchandiseTransferService {
    ResponseDto enviarMerchandiseTransfer(List<MerchandiseTransferDto> merchandiseTransferDtoList);
}
