package com.example.securitystudy.book.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {
    private String title;
    private String date;
    private String time;
}
