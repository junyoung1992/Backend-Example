package com.example.kafkaspringproducerwithrestcontroller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserEventVO implements Serializable {

    private String timestamp;
    private String userAgent;
    private String colorName;
    private String userName;

}
