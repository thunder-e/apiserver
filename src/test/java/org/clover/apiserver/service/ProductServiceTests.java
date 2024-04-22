package org.clover.apiserver.service;

import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.domain.Product;
import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.PageResponseDTO;
import org.clover.apiserver.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO>responseDTO = productService.getList(pageRequestDTO);

        log.info(responseDTO);
    }


    @Test
    public void testRegister() {

        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운 상품")
                .pdesc("신규 추가 상품입니다.")
                .price(1000)
                .build();

        //uuid가 있어야 함
        productDTO.setUploadedFileNames(
                java.util.List.of(
                        UUID.randomUUID() + "_" + "Test1.jpg",
                        UUID.randomUUID() + "_" + "Test2.jpg")
        );


        productService.register(productDTO);
    }



    @Test
    public void getTest() {

        Long pno = 15L;

        ProductDTO productDTO = productService.get(pno);

        log.info(productDTO);

    }




}
