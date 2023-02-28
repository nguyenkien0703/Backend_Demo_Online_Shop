package com.app.my_app.service;

import com.app.my_app.domain.Category;
import com.app.my_app.domain.OrderItem;
import com.app.my_app.domain.Product;
import com.app.my_app.model.ProductDTO;
import com.app.my_app.repos.CategoryRepository;
import com.app.my_app.repos.OrderItemRepository;
import com.app.my_app.repos.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProductService(final ProductRepository productRepository,
            final OrderItemRepository orderItemRepository,
            final CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Tìm tất cả các sản phẩm và trả lại chúng dưới dạng danh sách.
     *
     * @return Danh sách tất cả các sản phẩm trong cơ sở dữ liệu.
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product get(final Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getId();
    }

    public void update(final Long id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }

    /*
     * Đoạn code trên có chức năng ánh xạ dữ liệu từ đối tượng ProductDTO sang đối
     * tượng Product, và thực hiện một số kiểm tra và xử lý trước khi lưu đối tượng
     * Product vào cơ sở dữ liệu.
     * 
     * Cụ thể, đầu tiên nó tạo một đối tượng Product mới (hoặc sử dụng đối tượng đã
     * được truyền vào), sau đó sử dụng ModelMapper để ánh xạ các thuộc tính từ đối
     * tượng ProductDTO sang đối tượng Product.
     * 
     * Tiếp theo, nếu đối tượng ProductDTO có thông tin về danh mục sản phẩm, và nếu
     * đối tượng Product chưa có thông tin về danh mục hoặc đã có nhưng khác với
     * danh mục được chỉ định trong ProductDTO, thì đoạn code này sẽ tìm kiếm danh
     * mục tương ứng trong cơ sở dữ liệu bằng cách gọi phương thức findById() trên
     * repository của Category và truyền vào id của danh mục được chỉ định trong
     * ProductDTO. Nếu danh mục không tồn tại, đoạn code sẽ ném ra một ngoại lệ
     * ResponseStatusException với mã trạng thái HTTP là NOT_FOUND và thông báo lỗi
     * là "category not found". Nếu danh mục tồn tại, đoạn code sẽ gán danh mục này
     * vào đối tượng Product.
     * 
     * Cuối cùng, đoạn code trả về đối tượng Product đã được ánh xạ và xử lý.
     */
    private Product mapToEntity(final ProductDTO productDTO, Product product) {
        product = modelMapper.map(productDTO, Product.class);
        if (productDTO.getCategory() != null
                && (product.getCategory() == null || !product.getCategory().getId().equals(productDTO.getCategory()))) {
            final Category category = categoryRepository.findById(productDTO.getCategory())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));
            product.setCategory(category);
        }
        
        return product;

    }

}
