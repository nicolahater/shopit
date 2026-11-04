package dev.backend.manager.controller.payload;

public record NewProductPayload(
        // TODO: now validation is executed in catalogue-service
//        @NotNull(message = "{catalogue.products.create.errors.title_is_null}")
//        @Size(min = 3, max = 50, message = "{catalogue.products.create.errors.title_size_is_invalid}")
        String title,
//        @Size(max = 1000, message = "{catalogue.products.create.errors.details_size_is_invalid}")
        String details
) {
}
