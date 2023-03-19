package net.foxtam.antifraudsystem.controller;

import net.foxtam.antifraudsystem.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UserRole(@NotBlank String username, @NotNull Role role) {
}
