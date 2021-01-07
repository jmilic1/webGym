package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PlanDto {
    private Long id;
    private String coachUsername;
    private String description;
    private String dateFrom;
    private String dateTo;
    private Double price;
    private Boolean isTraining;

    @Override
    public String toString() {
        return "PlanDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", price=" + price +
                ", isTraining=" + isTraining +
                '}';
    }
}
