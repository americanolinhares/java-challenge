package com.aubay.challenge.backend.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
    assertEquals("Admin", role.getName());
  }

  @Test
  void testSetId() {
    role.setId(2L);
    Assertions.assertEquals(2L, role.getId());
  }

  @Test
  void testSetName() {
    role.setName("User");
    assertEquals("User", role.getName());
  }

  @Test
  void testHashCode() {
    Role other = new Role(1L, "Admin");
    assertEquals(role.hashCode(), other.hashCode());
  }

  @Test
  void testNotEqualsHashCode() {
    Role other = new Role(1L, "User");
    assertNotEquals(role.hashCode(), other.hashCode());
  }

  @Test
  void testEquals() {
    Role other = new Role(1L, "Admin");
    assertEquals(role, other);
  }

  @Test
  void testEqualsSameInstance() {
    assertEquals(role, role);
  }

  @Test
  void testEqualsDifferentClass() {
    assertNotEquals(role, "Admin");
  }

  @Test
  void testEqualsNull() {
    assertNotEquals(role, null);
  }
}
