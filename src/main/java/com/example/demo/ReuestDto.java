package com.example.demo;

public class ReuestDto {
    String ParnetNodel;
    String role;

    public ReuestDto(String parnetNodel, String role) {
        ParnetNodel = parnetNodel;
        this.role = role;
    }

    public ReuestDto() {
    }

    public String getParnetNodel() {
        return ParnetNodel;
    }

    public void setParnetNodel(String parnetNodel) {
        ParnetNodel = parnetNodel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
