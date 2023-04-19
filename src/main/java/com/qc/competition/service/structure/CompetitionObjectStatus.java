package com.qc.competition.service.structure;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Duncan on 21/03/2015.
 */
public enum CompetitionObjectStatus {
    CLOSED(false, true),
    IN_PROGRESS(true, false),
    IN_PROGRESS_CLOSING_ALLOWED(true, false),
    NOT_INITIALIZED(false, false),
    WAIT_FOR_START(false, false),
    CANCELLED(false, false);

    final boolean open;
    final boolean closed;

    CompetitionObjectStatus(boolean open, boolean closed) {
        this.open = open;
        this.closed = closed;
    }

    public static Set<CompetitionObjectStatus> getIdleStatuses() {
        Set<CompetitionObjectStatus> competitionObjectStatuses = new HashSet<>();
        competitionObjectStatuses.add(CompetitionObjectStatus.IN_PROGRESS);
        competitionObjectStatuses.add(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
        competitionObjectStatuses.add(CompetitionObjectStatus.NOT_INITIALIZED);
        competitionObjectStatuses.add(CompetitionObjectStatus.WAIT_FOR_START);
        return competitionObjectStatuses;
    }

    public static Object getTransmissionStatuses() {
        Set<CompetitionObjectStatus> competitionObjectStatuses = new HashSet<>();
        competitionObjectStatuses.add(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
        return competitionObjectStatuses;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isNotYetAvailable() {
        return !isClosed() && !isOpen();
    }

//    public boolean isCancelled() {
//        return this.compareTo(CompetitionObjectStatus.CANCELLED) == 0;
//    }
}
