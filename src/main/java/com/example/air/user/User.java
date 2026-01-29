package com.example.air.user;

import com.example.air.token.Token;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "users") // MongoDB collection name
public class User implements UserDetails {

  @Id
  private String id; // MongoDB uses String for IDs
  private String firstname;
  private String lastname;
  @Indexed(unique = true)
  private String email;

  private String password;
  private String address;
  private Integer phoneNumber;
  private Role role; // Assuming Role is an Enum
  // Store token IDs instead of full Token objects
  @DBRef(lazy = true)
  private List<Token> tokens;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "User{id=" + id + ", firstname='" + firstname + "', lastname='" + lastname + "', email='" + email + "'}";
  }
}
