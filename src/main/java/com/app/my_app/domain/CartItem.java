package com.app.my_app.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class CartItem {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    /*
    @JsonIgnore là một annotation trong Java để chỉ định rằng một trường (field) hoặc phương thức (method) nào đó của một đối tượng (object) sẽ không được sử dụng trong quá trình serialize hoặc deserialize dữ liệu từ JSON (hoặc các định dạng tương tự) sang đối tượng Java và ngược lại.

Khi một đối tượng được serialize hoặc deserialize,
các trường được đánh dấu bằng @JsonIgnore sẽ được bỏ qua và không tham gia quá trình này.
Điều này có thể hữu ích khi muốn giấu các trường nhạy cảm hoặc không cần thiết của
 đối tượng để bảo mật thông tin hay giảm dung lượng dữ liệu truyền tải.

    * */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}
