package br.com.realtour.util;

public record CreateUnitDTO(
    String address,
    String number,
    String neighbourhood,
    String city,
    Float price) {
}
