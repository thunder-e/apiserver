package org.clover.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.PageResponseDTO;
import org.clover.apiserver.dto.ProductDTO;
import org.clover.apiserver.service.ProductService;
import org.clover.apiserver.util.CustomFileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController //API서버
@Log4j2
@RequiredArgsConstructor //외부에서 주입 받으려면
@RequestMapping("/api/products")
public class ProductController {

    //final로 해서 외부에서 시작할때 자동 생성자 주입되도록 함
    private final CustomFileUtil fileUtil;
    private final ProductService productService;


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFiles(fileName);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files); //문자열인 이 파일이름은 db에 저장하기 위함

        productDTO.setUploadedFileNames(uploadFileNames);

        log.info(uploadFileNames);

        Long pno = productService.register(productDTO);

        return Map.of("result", pno);
    }


    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {

        return productService.get(pno);
    }


    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) {

        productDTO.setPno(pno);

        //old product : Database saved Product -> 어떤 파일이 지워졌는지 알기위해
        ProductDTO oldProductDTO = productService.get(pno);

        //file upload
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //keep files String
        List<String> uploadedFileNames = productDTO.getUploadedFileNames();

        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO);

        List<String> oldFileNames = oldProductDTO.getUploadedFileNames();

        if(oldFileNames != null && !oldFileNames.isEmpty()) {

            List<String> removeFiles =
                    oldFileNames.stream().filter(fileName -> !uploadedFileNames.contains(fileName)).toList();

            fileUtil.deleteFiles(removeFiles);

        }//end if

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {

        List<String> oldFileNames = productService.get(pno).getUploadedFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }




}

