package org.clover.apiserver.repository.search;

import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.PageResponseDTO;
import org.clover.apiserver.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDTO<ProductDTO> searchList (PageRequestDTO pageRequestDTO);
}
