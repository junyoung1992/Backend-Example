package com.example.catalogservice.service;

import com.example.catalogservice.dto.CatalogDto;
import com.example.catalogservice.jpa.CatalogEntity;

public interface CatalogService {

    public CatalogDto createCatalog(CatalogDto catalogDto);
    public CatalogDto getCatalogByProductId(String productId);
    public Iterable<CatalogEntity> getAllCatalogs();

}
