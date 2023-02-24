package com.talixmines.management.inventorymanager.services;

import com.talixmines.management.inventorymanager.dto.ClientDto;
import java.util.List;

public interface ClientService {

  ClientDto save(ClientDto dto);

  ClientDto findById(Integer id);

  List<ClientDto> findAll();

  void delete(Integer id);

}
