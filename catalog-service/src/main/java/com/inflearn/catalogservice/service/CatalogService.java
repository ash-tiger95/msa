package com.inflearn.catalogservice.service;

import com.inflearn.catalogservice.entity.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
