package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRatingModel {
    private int idRekan;
    private String namaRekan;
    private String roleRekan;
    private String kodeProyek;
    private int ratingRekan;
    private String feedback;
}
