package ru.hack.operator.dto;

import lombok.Builder;
import lombok.Data;
import ru.hack.operator.models.Trouble;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
public class TroubleDto {
    private Long id;
    private String imageUrl;
    private List<String> troubleTypes;
    private String troubleStatus;

    public static TroubleDto from(Trouble trouble) {
        return TroubleDto.builder()
                .id(trouble.getId())
                .troubleStatus(trouble.getStatus().toString())
                .troubleTypes(trouble.getTypes().stream().map(Objects::toString).collect(Collectors.toList()))
                .imageUrl(trouble.getImage().getFileInfo().getUrl())
                .build();
    }

    public static List<TroubleDto> from(List<Trouble> troubles) {
        return troubles.stream()
                .map(TroubleDto::from)
                .collect(Collectors.toList());
    }
}
