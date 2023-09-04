package gr.aueb.cf.teacherapp.dto;

import javax.validation.constraints.NotNull;

public abstract class BaseDTO {
    @NotNull
    private Long id;

    public BaseDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
