package uz.pdp.apisecurityhrmanagement.payload;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO {
    private String name;
    private String description;
    private Date deadline;
    private String taskTakerEmail;
}
