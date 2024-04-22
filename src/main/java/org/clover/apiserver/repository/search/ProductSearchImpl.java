package org.clover.apiserver.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.domain.Product;
import org.clover.apiserver.domain.QProduct;
import org.clover.apiserver.domain.QProductImage;
import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.PageResponseDTO;
import org.clover.apiserver.dto.ProductDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {


    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {

        log.info("-------------------------searchList-------------------------");


        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);
        //Element Collection으로 queryDSL 사용시
        //파라미터는 엔티티가 아니기때문에
        //product의 imageList를 productImage로 간주할거야 하고 알려줘야함
        query.leftJoin(product.imageList, productImage);

        query.where(productImage.ord.eq(0));

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        List<Tuple> productList = query.select(product, productImage).fetch();
        //List<Product> productList = query.fetch();

        long count = query.fetchCount();

        log.info("=====================================");
        log.info(productList);

        return null;
    }
}
