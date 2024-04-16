package org.clover.apiserver.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String fileName;

    //순번_0번이 출력될 수 있게끔
    private int ord;

    public void setOrd(int ord) {
        this.ord = ord;
    }

}
