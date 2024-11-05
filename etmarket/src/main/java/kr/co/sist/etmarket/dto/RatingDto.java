package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDto {
    private Long dealId;
    private Long reviewerId;
    private Long targetId;
    private Double rating;
    private String comment;
    private Timestamp ratingDate;
}

