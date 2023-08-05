package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.support.SpringWebConstraintValidatorFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;
import com.alvinxu.TheDailyGrind.services.AccountService;
import com.alvinxu.TheDailyGrind.validators.EmailValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class TestRegisterDtoValidation {
  @Autowired AccountRepository repository;
  private RegisterDto dto;
  
  @Autowired WebApplicationContext wac;
  
  private ValidatorFactory factory;
  private Validator validator;
  
  private class MockConstraintValidatorFactory extends SpringWebConstraintValidatorFactory {
    private AccountService service;
    private WebApplicationContext ctx;
    
    public MockConstraintValidatorFactory(WebApplicationContext wac, AccountService service) {
      this.ctx = wac;
      this.service = service;
    }
    
    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
      // TODO Auto-generated method stub
      ConstraintValidator instance = super.getInstance(key);
      if (instance instanceof EmailValidator) {
        EmailValidator v = (EmailValidator) instance;
        v.setAccountService(this.service);
        instance = v;
      }
      return (T) instance;
    }
    
    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return ctx;
    }
    
  }
  
  @BeforeAll
  void factorySetup() {
    AccountService service = new AccountService(repository, new BCryptPasswordEncoder());
    ConstraintValidatorFactory cvf = new MockConstraintValidatorFactory(wac, service);
    
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.usingContext()
                       .constraintValidatorFactory(cvf)
                       .getValidator();
  }
  
  @AfterAll
  void factoryTeardown() {
    factory.close();
  }
  
  @BeforeEach
  void setup() {
    dto = new RegisterDto();
    dto.setEmail("hello@world");
    dto.setUsername("My user");
    dto.setPassword(new char[] {'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1'});
    dto.setDateOfBirth(LocalDate.of(2012, 6, 9));
    dto.setTos_agree(true);
  }
  
  @AfterEach
  void teardown() {
    repository.deleteAll();
  }
  
  @Test
  void testValidation() {
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isTrue();
  }
  
  @Test
  void testEmailNotEmpty() {
    dto.setEmail("");
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{email.not.empty}");
    }
  }
  
  @Test
  void testEmailIsValidEmail() {
    dto.setEmail("hello world");
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{email.not.valid}");
    }
  }
  
  @Test
  void testEmailValidator() {
    Account user = new Account();
    user.setAuthority("USER");
    user.setPassword("password");
    user.setUsername("username");
    user.setDateOfBirth(LocalDateTime.of(2014, 5, 30, 16, 07));
    user.setEmail("hello@world");
    repository.save(user);
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{email.already.exists}");
    }
  }
  
  @Test
  void testUsernameSizeTooSmall() {
    dto.setUsername("us");
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{username.size}");
    }
  }
  
  @Test
  void testUsernameSizeTooLarge() {
    dto.setUsername("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{username.size}");
    }
  }
  
  @Test
  void testPasswordValidator() {
    dto.setPassword(new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'});
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{password.not.valid}");
    }
  }
  
  @Test
  void testPasswordSizeTooSmall() {
    dto.setPassword(new char[] {'P', 'a', 's', 's', '1', '2', '3'});
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{password.size}");
    }
  }
  
  @Test
  void testPasswordSizeTooLarge() {
    dto.setPassword("Pass12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890".toCharArray());
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{password.size}");
    }
  }
  
  @Test
  void testDateOfBirthInTheFuture() {
    dto.setDateOfBirth(LocalDate.of(4000, 12, 31));
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{date.of.birth.future}");
    }
  }
  
  @Test
  void testDateOfBirthNotNull() {
    dto.setDateOfBirth(null);
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{date.of.birth.not.null}");
    }
  }
  
  @Test
  void testTermsOfServiceNotAgreed() {
    dto.setTos_agree(false);
    
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<RegisterDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{terms.of.service}");
    }
  }
}
