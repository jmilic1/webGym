package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PlanDto {
    private Long id;
    private String description;
    private Date dateFrom;
    private Date dateTo;
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
