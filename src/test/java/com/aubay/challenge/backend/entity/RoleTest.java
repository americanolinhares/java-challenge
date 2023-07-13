package com.aubay.challenge.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleTest {

  private Role role;

  @BeforeEach
  void setUp() {
    role = new Role(1L, "Admin");
  }

  @Test
  void testGetId() {
    Assertions.assertEquals(1L, role.getId());
  }

  @Test
  void testGetName() {
    Assertions.assertEquals("Admin", role.getName());
  }

  @Test
  void testSetId() {
    role.setId(2L);
    Assertions.assertEquals(2L, role.getId());
  }

  @Test
  void testSetName() {
    role.setName("User");
    Assertions.assertEquals("User", role.getName());
  }

  @Test
  void testHashCode() {
    Role other = new Role(1L, "Admin");
    Assertions.assertEquals(role.hashCode(), other.hashCode());
  }

  @Test
  void testEquals() {
    Role other = new Role(1L, "Admin");
    Assertions.assertEquals(role, other);
  }

  @Test
  void testEqualsSameInstance() {
    Assertions.assertEquals(role, role);
  }

  @Test
  void testEqualsDifferentClass() {
    Assertions.assertNotEquals(role, "Admin");
  }

  @Test
  void testEqualsNull() {
    Assertions.assertNotEquals(role, null);
  }
}
