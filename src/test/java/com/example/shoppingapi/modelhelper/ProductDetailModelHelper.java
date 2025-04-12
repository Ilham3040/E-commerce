package com.example.shoppingapi.modelhelper;

import com.example.shoppingapi.model.ProductDetail;
import com.example.shoppingapi.model.Product;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ProductDetailModelHelper implements ModelHelper<ProductDetail> {

    private final Long productDetailId1 = 1L;
    private final Long productDetailId2 = 2L;

    private final List<String> attachmentUrls1 = Arrays.asList("http://example.com/image1.jpg", "http://example.com/image2.jpg");
    private final List<String> attachmentUrls2 = Arrays.asList("http://example.com/image3.jpg", "http://example.com/image4.jpg");

    private final Integer totalSold1 = 100;
    private final Integer totalSold2 = 200;

    private final BigDecimal reviewRating1 = BigDecimal.valueOf(4.50);
    private final BigDecimal reviewRating2 = BigDecimal.valueOf(3.75);

    public ProductDetailModelHelper() {
    }

    @Override
    public ProductDetail createModel(Integer num) {
        if (num == 1) {
            ProductDetail pd1 = new ProductDetail();
            pd1.setProductDetailId(productDetailId1);
            Product product1 = new Product();
            product1.setProductId(1L);
            product1.setProductName("Cat Food");
            product1.setPrice(BigDecimal.valueOf(40000));
            pd1.setProduct(product1);
            pd1.setAttachmentUrls(attachmentUrls1);
            pd1.setTotalSold(totalSold1);
            pd1.setReviewRating(reviewRating1);
            return pd1;
        } else {
            ProductDetail pd2 = new ProductDetail();
            pd2.setProductDetailId(productDetailId2);
            Product product2 = new Product();
            product2.setProductId(2L);
            product2.setProductName("Fish Food");
            product2.setPrice(BigDecimal.valueOf(5000));
            pd2.setProduct(product2);
            pd2.setAttachmentUrls(attachmentUrls2);
            pd2.setTotalSold(totalSold2);
            pd2.setReviewRating(reviewRating2);
            return pd2;
        }
    }
}
