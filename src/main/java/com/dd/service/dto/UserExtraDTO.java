package com.dd.service.dto;

import javax.validation.constraints.Size;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class UserExtraDTO {

    private String userType;

    private Long userId;

    private String phone;

    private UserDTO userDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserExtraDTO() {
        // Empty constructor needed for Jackson.
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "UserExtraDTO{" +
            "userType='" + userType + '\'' +
            ", userId='" + userId + '\'' +
            ", phone='" + phone + '\'' +
            ", userDTO=" + userDTO +
            '}';
    }
}
