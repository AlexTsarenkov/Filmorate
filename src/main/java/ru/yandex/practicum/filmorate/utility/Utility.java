package ru.yandex.practicum.filmorate.utility;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
public class Utility {
    public static Long getIdForEntity() {
        return new BigDecimal(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                .setScale(-4, BigDecimal.ROUND_UP)
                .longValue();
    }
}
