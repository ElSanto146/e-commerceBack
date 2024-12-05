package com.app.e_commerce.controller;

import com.app.e_commerce.dto.ProductDTO;
import com.app.e_commerce.model.Product;
import com.app.e_commerce.service.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService){
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        //Verificar si existe un producto con el mismo nombre y precio
        if (productService.existsProductByNameAndPrice(productDTO.getName(), productDTO.getPrice())){
            return ResponseEntity.status(409).body("Ya existe un producto con el mismo nombre y precio");
        }

        //Validar que el producto no sea nulo
        if (productDTO.getName().isBlank() || productDTO.getPrice() == null){
            return ResponseEntity.badRequest().body("El nombre y el precio no pueden estar vacios");
        }

        /*//Codificar la imagen a Base64
        String base64Image = Base64.getEncoder().encodeToString(productDTO.getImg().getBytes());

        //Limitar el tamaño de la imagen a 2MB
        if(base64Image.length()>2*1024*1024){
            return ResponseEntity.badRequest().body("La imagen no puede ser mayor de 2MB");
        }

        //Verificar el formato
       // if(!isValidImageFormat(productDTO.getImg())){
       //     return ResponseEntity.badRequest().body("El formato de la imagen no es válido. Solo se aceptan imágenes PNG y JPG. ");
       // }*/

        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .img(productDTO.getImg())
                .isSale(productDTO.getIsSale())
                .isNew(productDTO.getIsNew())
                .build();
        productService.createProduct(product);
        return ResponseEntity.created(new URI("/api/v1/products")).body(product);
    }

    /*Validar el formato de la img
    private boolean isValidImageFormat(String base64Image){
        //Obtener el prefijo de la imagen y verificar que sea PNG o JPG
        String imagePrefix = base64Image.split(",")[0];
        return imagePrefix.startsWith("data:image/png") || imagePrefix.startsWith("data:image/jpeg");
    }*/

    @GetMapping("")
    public  ResponseEntity<Object> getAllProducts (){
        List<ProductDTO> productDTOList = productService.allProduct()
                .stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .img(product.getImg())
                        .isSale(product.getIsSale())
                        .isNew(product.getIsNew())
                        .build())
                .toList();
        return ResponseEntity.ok(productDTOList);
    }

    /*private String encodeToBase64(byte[] imageBytes){
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }*/

    @GetMapping("/find/{id}")
    public ResponseEntity<Object> findProduct (@PathVariable Long id){
        Optional<Product> productOptional = productService.findProduct(id);

        if (productOptional.isPresent()){
            Product product = productOptional.get();

            ProductDTO productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .img(product.getImg())
                    .isSale(product.getIsSale())
                    .isNew(product.getIsNew())
                    .build();
            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO){

        Optional<Product> productOptional = productService.findProduct(id);

        //Validar que el producto no sea nulo
        if (productOptional.isPresent()){
            Product product = productOptional.get();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setStock(productDTO.getStock());
            product.setImg(productDTO.getImg());
            product.setIsSale(productDTO.getIsSale());
            product.setIsNew(productDTO.getIsNew());

            productService.createProduct(product);

            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id){

        Product product = productService.findProduct(id).orElse(null);

        if (product == null){
            return ResponseEntity.notFound().build();
        }

        productService.deleteProduct(id);

        return  ResponseEntity.ok(product.getName()+ " eliminado");
    }

}
