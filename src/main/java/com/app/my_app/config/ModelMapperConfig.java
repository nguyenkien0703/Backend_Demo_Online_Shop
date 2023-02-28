package com.app.my_app.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        // kiểu như có hai lớp là person và personDTo,
        // ta muốn ánh xạ  dữ liệu từ person sang personDTo để trả về dữi liệu dưới dạng
//        Json từ 1 Api Restful

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
        /**
         * trong ddối tượng ModelMapper có đối tượng Configuration,
         * phương thức getConfiguration dc gọi để lấy ra đối tượng "Configuration" cuar
         * modelMapper, lowps
         * configuration nayf cho pheps tùy chính các modelMapper ánh xạ các thuộc tính
         * của doối tượng
         * ví dụ trong này, phương thức setMatchingStrategy của Configurationdc gọi để
         * thiết lâọp
         * chiến lược khớp nối của modelmapper thành MatchingStrategies.STRICT
         * MatchingStrategies.STRICT là 1 trong các chiến lược khớp nối dc cung cấp bởi ModelMapper.
         * nó nghĩa là MOdelMapper sẽ áp dụng quy tắc khớp nối nghiêm ngặt giữa các thuộc tính 
         * của dối tượng nguồn và dối tượng đích, chỉ khớp nối nếu các thuộc tính có cùng tên và kiểu dữ liệu 
         * 
         */
        
    }
}
