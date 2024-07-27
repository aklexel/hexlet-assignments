package exercise.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// BEGIN
@Getter
@Setter
public class GuestCreateDTO {
    @NotBlank
    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @Pattern(regexp = "[+][0-9]{11,13}")
    @NotNull
    private String phoneNumber;

    @Pattern(regexp = "[0-9]{4}")
    private String clubCard;

    @Future
    private LocalDate cardValidUntil;
}
// END
