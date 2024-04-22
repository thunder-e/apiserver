package org.clover.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.domain.Product;
import org.clover.apiserver.domain.ProductImage;
import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.PageResponseDTO;
import org.clover.apiserver.dto.ProductDTO;
import org.clover.apiserver.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);


        //Object[] => 0 product 1 productImage
        //Object[] => 0 product 1 productImage
        //Object[] => 0 product 1 productImage
        //map으로 변환시켜 주었지만 Projection으로도 해볼것
        List<ProductDTO> dtoList = result.get().map(arr -> {

            ProductDTO productDTO = null;

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDTO.setUploadedFileNames(List.of(imageStr));
            
            return productDTO;

        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                 .dtoList(dtoList)
                 .total(totalCount)
                 .pageRequestDTO(pageRequestDTO)
                 .build();

    }


    @Override
    public Long register(ProductDTO productDTO) {

        Product product = dtoToEntity(productDTO);

        log.info("--------------------------");
        log.info(product);
        log.info(product.getImageList());

        Long pno = productRepository.save(product).getPno();

        return pno;
    }

    @Override
    public ProductDTO get(Long pno) {

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        return entityToDto(product);
    }

    @Override
    public void modify(ProductDTO productDTO) {

        //조회
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();
        //변경 내용 반영
        product.changePrice(productDTO.getPrice());
        product.changePname(product.getPname());
        product.changePdesc(product.getPdesc());
        product.changeDel(productDTO.isDelFlag());
        
        //이미지 처리
        List<String> uploadFileNames = productDTO.getUploadedFileNames();

        product.clearList();

        if(uploadFileNames != null && !uploadFileNames.isEmpty()) {

            uploadFileNames.forEach(uploadName -> {
                product.addImageString(uploadName);
            });

        }

        //저장
        productRepository.save(product);

    }

    @Override
    public void remove(Long pno) {

        //원래는 delFlag만 바꾸는 방식
        //여기서만 delete
        productRepository.deleteById(pno);

    }


    private Product dtoToEntity(ProductDTO productDTO) {

        //컬렉션 처리가 아닌 것들
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        //엔티티 안의 컬렉션은 새로 추가하면 안됨(새로운 객체로 바꿔치기 X)
        List<String> uploadedFileNames = productDTO.getUploadedFileNames();

        if(uploadedFileNames == null || uploadedFileNames.isEmpty()) {
            return product;
        }

        uploadedFileNames.forEach(fileName -> {
            product.addImageString(fileName);
        });


        return product;
    }

    private ProductDTO entityToDto(Product product) {

        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(productImage ->
                productImage.getFileName()).toList();

        productDTO.setUploadedFileNames(fileNameList);


        return productDTO;
    }



}
