package exercise.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import exercise.dto.ProductParamsDTO;
import exercise.model.Product;

// BEGIN
@Component
public class ProductSpecification {
    public Specification<Product> build(ProductParamsDTO params) {
        return withCategoryId(params.getCategoryId())
                .and(withPriceGt(params.getPriceGt()))
                .and(withPriceLt(params.getPriceLt()))
                .and(withRatingGt(params.getRatingGt()))
                .and(withTitleCont(params.getTitleCont()));
    }

    private Specification<Product> withCategoryId(Long value) {
        return skipIfNull(value, (root, query, criteria) ->
                criteria.equal(root.get("category").get("id"), value)
        );
    }

    private Specification<Product> withPriceGt(Integer value) {
        return skipIfNull(value, (root, query, criteria) ->
                criteria.greaterThan(root.get("price"), value)
        );
    }

    private Specification<Product> withPriceLt(Integer value) {
        return skipIfNull(value, (root, query, criteria) ->
                criteria.lessThan(root.get("price"), value)
        );
    }

    private Specification<Product> withRatingGt(Double value) {
        return skipIfNull(value, (root, query, criteria) ->
                criteria.greaterThan(root.get("rating"), value)
        );
    }

    private Specification<Product> withTitleCont(String value) {
        return skipIfNull(value, (root, query, criteria) ->
                criteria.like(root.get("title"), "%" + value + "%")
        );
    }

    private <T> Specification<Product> skipIfNull(T value, Specification<Product> spec) {
        return value == null
                ? ((root, query, criteria) -> criteria.conjunction())
                : spec;
    }
}
// END
