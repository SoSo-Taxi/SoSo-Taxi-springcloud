package com.apicaller.sosotaxi.project.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

/**
 * @author shuang.kou
 */
@Data
public class Role extends AbstractAuditBase {

    private Long id;
    private String name;
    private String description;

    @JsonIgnore
    private List<UserRole> userRoles = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
