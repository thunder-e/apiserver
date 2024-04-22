package org.clover.apiserver.service;

import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.PageResponseDTO;
import org.clover.apiserver.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductService {

    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    Long register(ProductDTO productDTO);

    ProductDTO get(Long pno);

    void modify(ProductDTO productDTO);

    void remove(Long pno);


}
