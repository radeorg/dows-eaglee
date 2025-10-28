package com.hina.eaglee;

import org.springframework.web.bind.annotation.GetMapping;

public interface EagleeApi {

    @GetMapping("/api")
    void api();

}
