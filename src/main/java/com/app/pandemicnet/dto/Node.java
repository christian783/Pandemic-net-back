package com.app.pandemicnet.dto;

import com.app.pandemicnet.model.User;
import lombok.Data;

@Data
public class Node {
    private Long id;
    private String label;
    private User.Status status;
}
