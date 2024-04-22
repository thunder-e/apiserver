package org.clover.apiserver.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity //id가 필요
@Getter
@Table(name = "tbl_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag;

    //상품정보를 수정한다 -> 하위에 이미지 수정  :: ElementCollection
    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();


    public void changePrice(int price) {
        this.price = price;
    }

    public void changePdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public void changePname(String pname) {
        this.pname = pname;
    }

    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public void addImage(ProductImage image) {

        image.setOrd(imageList.size());
        imageList.add(image);

    }

    //기존에 저장된 파일 ( 기존 상품의 이미지 3개중 1개만 수정한다면 2개는 String )
    public void addImageString(String fileName) {

        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();

        addImage(productImage);
    }

    public void clearList() {
        this.imageList.clear();
    }



}
