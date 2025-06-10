package br.com.realtour.util;

public record CreateUnitDTO(
        Float latitude,
        Float longitude,
        Float price,
        String number) {
}
