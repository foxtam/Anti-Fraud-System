package net.foxtam.antifraudsystem.controller;

import net.foxtam.antifraudsystem.Lock;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UserLock(@NotBlank String username, @NotNull Lock operation) {
}
