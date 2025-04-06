package com.scm.forms;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserUpdateForm {

    private boolean enabled;
    private boolean emailVarified;
    private boolean phoneVarified;

    @Builder.Default
    private List<String> roleList = new ArrayList<>();

}
