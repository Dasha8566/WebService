package com.example.pdfservice.utils.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class EmailAttachment {
    @NonNull String fileName;
    @NonNull byte[] data;
}
