package com.github.fluent.hibernate.request.persistent;

/**
 *
 * @author V.Ladynev
 */
public class UserDto extends User {

    public UserDto() {
        setDepartment(new Department());
    }

    public void setDepartmentName(String departmentName) {
        getDepartment().setName(departmentName);
    }

}
