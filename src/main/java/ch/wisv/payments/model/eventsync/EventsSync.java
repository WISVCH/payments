package ch.wisv.payments.model.eventsync;

import lombok.Data;
import lombok.Getter;

@Data
class EventsSync {

    @Getter
    private String trigger;
}
