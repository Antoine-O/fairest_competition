package com.qc.competition.service.structure;

import java.util.*;

/**
 * Created by Duncan on 20/03/2015.
 */
public enum RegistrationStatus {
    REVERTED(true), // Cancel request confirmation after any effective payment
    REVERT(false, REVERTED), // Cancel request after effective payment
    CANCEL(false, REVERT), // Cancelled BEFORE any effective payment
    CONFIRMED(true, REVERT), // Registration Accepted and Confirmed
    NOT_CONFIRMED(true, REVERT), // Registration Accepted but not confirmed or Blocked by manager
    CHECKED_IN(false, CONFIRMED, NOT_CONFIRMED, REVERT), // player confirmed his registration (if needed)
    WAIT_FOR_CHECK_IN(false, CHECKED_IN, NOT_CONFIRMED, REVERT), // wait for player check in
    ACCEPTED(false, WAIT_FOR_CHECK_IN, CANCEL, REVERT), // Registration Accepted
    REFUSED(false, REVERT), // Registration Refused
    FAILED_OPERATION_PENDING(true, REVERT), // At least one account operation exists in non final state
    FAILED_NEGATIVE_BALANCE(true, REVERT), // User Balance is less than 0
    FAILED_PAYMENT(true, REVERT), // Payment (if needed) has failed (user or program) and the registration fails
    VALIDATED(false, ACCEPTED, REFUSED, CANCEL, REVERT), // Payment transfer (if needed) has been successfully prepared (a subsequent deposit has been successfully done) or No payment is needed
    FUND_NEEDED(false, CANCEL, VALIDATED, CANCEL, REVERT), /* If segistration is not free and not enough fund are on the user account
                    The Amount needed is the amount to set the account to zero */
    NO_SEATS_LEFT(false, REVERT), // Submitted state
    SUBMITTED(false, FUND_NEEDED, VALIDATED, FAILED_NEGATIVE_BALANCE, FAILED_PAYMENT, FAILED_OPERATION_PENDING, REVERT, CANCEL), // Submitted state
    USER_VERIFICATION_REQUIRED(false, SUBMITTED, ACCEPTED, REFUSED, NO_SEATS_LEFT, CANCEL, REVERT), // User not verified
    NEW(false, SUBMITTED, USER_VERIFICATION_REQUIRED, NO_SEATS_LEFT, REVERT, REFUSED, CANCEL), // First State
    ;
    boolean finalStatus;

    List<RegistrationStatus> nextRegistrationStatuses = new ArrayList<>();

    RegistrationStatus(boolean finalStatus, RegistrationStatus... nextRegistrationStatuses) {
        this.nextRegistrationStatuses.addAll(Arrays.asList(nextRegistrationStatuses));
        this.finalStatus = finalStatus;
    }

    public static Set<RegistrationStatus> getFinalRegistrationStatuses() {
        Set<RegistrationStatus> registrationStatuses = new HashSet<>();
        for (RegistrationStatus registrationStatus : RegistrationStatus.values()) {
            if (registrationStatus.finalStatus)
                registrationStatuses.add(registrationStatus);
        }
        return registrationStatuses;
    }

    public static Set<RegistrationStatus> getMainlyNegativeRegistrationStatuses() {
        Set<RegistrationStatus> registrationStatuses = new HashSet<>();
        registrationStatuses.add(CANCEL);
        registrationStatuses.add(NO_SEATS_LEFT);
        registrationStatuses.add(REFUSED);
        registrationStatuses.add(FAILED_PAYMENT);
        registrationStatuses.add(FAILED_NEGATIVE_BALANCE);
        registrationStatuses.add(FAILED_OPERATION_PENDING);
        registrationStatuses.add(NOT_CONFIRMED);
        registrationStatuses.add(REVERT);
        registrationStatuses.add(REVERTED);
        return registrationStatuses;
    }

    public static List<RegistrationStatus> getMainlyPositiveRegistrationStatuses() {
        List<RegistrationStatus> registrationStatuses = new ArrayList<>();
        registrationStatuses.add(CONFIRMED);
        registrationStatuses.add(CHECKED_IN);
        registrationStatuses.add(ACCEPTED);
        registrationStatuses.add(SUBMITTED);
        registrationStatuses.add(VALIDATED);
        registrationStatuses.add(FUND_NEEDED);
        registrationStatuses.add(WAIT_FOR_CHECK_IN);
        return registrationStatuses;
    }

    public static Set<RegistrationStatus> getNotFinalRegistrationStatuses() {
        Set<RegistrationStatus> registrationStatuses = new HashSet<>();
        for (RegistrationStatus registrationStatus : RegistrationStatus.values()) {
            if (!registrationStatus.finalStatus)
                registrationStatuses.add(registrationStatus);
        }
        return registrationStatuses;
    }

    public static Set<RegistrationStatus> getCancelledOrFailedFinalStatuses() {
        Set<RegistrationStatus> registrationStatuses = new HashSet<>();
        registrationStatuses.add(NO_SEATS_LEFT);
        registrationStatuses.add(CANCEL);
        registrationStatuses.add(REVERTED);
        registrationStatuses.add(FAILED_PAYMENT);
        registrationStatuses.add(FAILED_NEGATIVE_BALANCE);
        registrationStatuses.add(FAILED_OPERATION_PENDING);
        return registrationStatuses;
    }

    public static List<RegistrationStatus> getTransmissionStatuses() {
        List<RegistrationStatus> registrationStatuses = new ArrayList<>();
        registrationStatuses.add(CONFIRMED);
        registrationStatuses.add(NOT_CONFIRMED);
        registrationStatuses.add(CANCEL);
        registrationStatuses.add(REVERTED);
        registrationStatuses.add(FAILED_OPERATION_PENDING);
        registrationStatuses.add(FAILED_NEGATIVE_BALANCE);
        registrationStatuses.add(FAILED_PAYMENT);
        registrationStatuses.add(NO_SEATS_LEFT);
        return registrationStatuses;
    }

    public static List<RegistrationStatus> getNotStableBeforeCheckInClosingStatuses() {
        List<RegistrationStatus> registrationStatuses = new ArrayList<>();
        registrationStatuses.add(SUBMITTED);
        registrationStatuses.add(VALIDATED);
        registrationStatuses.add(ACCEPTED);
        registrationStatuses.add(CANCEL);
        registrationStatuses.add(REVERT);
        registrationStatuses.add(FAILED_OPERATION_PENDING);
        registrationStatuses.add(FAILED_NEGATIVE_BALANCE);
        registrationStatuses.add(FAILED_PAYMENT);
        registrationStatuses.add(NEW);
        return registrationStatuses;
    }

    public static List<RegistrationStatus> getNameEditableStatus() {
        List<RegistrationStatus> registrationStatuses = new ArrayList<>();
        registrationStatuses.add(SUBMITTED);
        registrationStatuses.add(VALIDATED);
        registrationStatuses.add(ACCEPTED);
        registrationStatuses.add(NEW);
        registrationStatuses.add(CHECKED_IN);
        registrationStatuses.add(FUND_NEEDED);
        registrationStatuses.add(WAIT_FOR_CHECK_IN);
        return registrationStatuses;
    }

    public boolean isFinalStatus() {
        return finalStatus;
    }

    public List<RegistrationStatus> getNextRegistrationStatuses() {
        return nextRegistrationStatuses;
    }


}
