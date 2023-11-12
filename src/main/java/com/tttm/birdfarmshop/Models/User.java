package com.tttm.birdfarmshop.Models;

import com.tttm.birdfarmshop.Enums.AccountStatus;
import com.tttm.birdfarmshop.Enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "[Users]")
public class User implements UserDetails {
  @Id
  @Column(name = "userID", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userID;

  @Column(name = "firstName", nullable = true, unique = false)
  private String firstName;

  @Column(name = "lastName", nullable = true, unique = false)
  private String lastName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "phone", nullable = false, unique = true)
  private String phone;

  @Column(name = "password", nullable = false, unique = false)
  private String password;

  @Column(name = "gender", nullable = true, unique = false)
  private Boolean gender;

  @Column(name = "dateOfBirth", nullable = true, unique = false)
  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  @Column(name = "address", nullable = false, unique = false)
  private String address;

  @Column(name = "accountStatus", nullable = false, unique = false)
  @Enumerated(EnumType.STRING)
  private AccountStatus accountStatus;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;
  public User(String firstName, String lastName, String email, String phone, String password, Boolean gender, Date dateOfBirth, String address, AccountStatus accountStatus, ERole role) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.gender = gender;
    this.dateOfBirth = dateOfBirth;
    this.address = address;
    this.accountStatus = accountStatus;
    this.role = role;
  }

  public User(String firstName) {
    this.firstName = firstName;
  }

  //  @OneToOne
//  @JoinColumn(name = "roleID", referencedColumnName = "roleID", insertable = false, updatable = false)
//  private Role role;

  @Enumerated(EnumType.STRING)
  private ERole role;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public boolean isAccountNonExpired() {
    return true;
  }

  public boolean isAccountNonLocked() {
    return true;
  }

  public boolean isCredentialsNonExpired() {
    return true;
  }

  public boolean isEnabled() {
    return true;
  }


}
