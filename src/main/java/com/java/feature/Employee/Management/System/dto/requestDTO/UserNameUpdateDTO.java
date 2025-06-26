package com.java.feature.Employee.Management.System.dto.requestDTO;

public record UserNameUpdateDTO(
        String firstName,
        String middleName,
        String lastName,
        String userName)
 {
     public UserNameUpdateDTO{
         if (firstName == null || firstName.trim().isEmpty()
                 || middleName == null || middleName.trim().isEmpty()
                 || lastName == null || lastName.trim().isEmpty()) {
             throw new IllegalArgumentException("Name cannot be empty");
         }
         if (userName == null || userName.trim().isEmpty()) {
             throw new IllegalArgumentException("User name cannot be empty");
         }

     }

}
