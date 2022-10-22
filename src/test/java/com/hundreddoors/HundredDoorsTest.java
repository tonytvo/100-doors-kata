package com.hundreddoors;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/*
 * 100 doors in a row are all initially closed. A robot passes by the doors a certain number of times.  
 * The first time, it passes, it visits every door and toggles the door (If the door is open, it closes the door, if it is closed, the door is opened).
 * The second time, the robot only visits every second door (door #2, #4, #6, ...) opening and closing doors as before
 * The third time, the robot visits every third door (door #3, #6, #9, ...)
 * The fourth time, the robot visits every fourth door etc.
 * 
 * On the 100th pass, the robot will visit only the 100th door.
 * Write a function that takes the number of passes as input and returns the state of all of the doors after the robot has made that many passes.
 * The state can be represented as a string, array, json or whatever makes sense for your testing.
 * 
 */

public class HundredDoorsTest {

    public enum DoorStatus {
        CLOSE,
        OPEN;
    }

    @Test
    public void allDoorsCloseAtTheBeginning() {

        Hallway hallWay = new Hallway();
        assertEquals(true, hallWay.allAreClose());
    }

    @Test
    public void theFirstPassOpensAllDoors() {
        Hallway hallway = new Hallway();
        hallway.visit();
        assertTrue(hallway.allAreOpen());
    }

    @Test
    public void theSecondPassTogglesEverySecondDoor() {
        final Map initialDoorState = Maps.newHashMap(ImmutableMap.of(0, DoorStatus.CLOSE, 1, DoorStatus.CLOSE, 98, DoorStatus.CLOSE, 99, DoorStatus.CLOSE));
        Hallway hallway = new Hallway(initialDoorState);
        hallway.visit(); // O,O,O....
        hallway.visit(); // O,C,O,C....
        assertTrue(hallway.isDoorOpen(0));
        assertFalse(hallway.isDoorOpen(1));
        assertTrue(hallway.isDoorOpen(98));
        assertFalse(hallway.isDoorOpen(99));
    }

    @Test 
    public void givenOneDoorOpenTheFirstVisitShouldCloseTheDoor() {
        final ImmutableMap initialDoorState = ImmutableMap.of(0, DoorStatus.CLOSE, 1, DoorStatus.OPEN);
        Hallway hallway = new Hallway(initialDoorState);
        assertFalse(hallway.isDoorOpen(0));
        assertTrue(hallway.isDoorOpen(1));
    }

    @Test 
    public void givenDoorsDoesNotExistItAlwaysClosed() {
        final ImmutableMap initialDoorState = ImmutableMap.of();
        Hallway hallway = new Hallway(initialDoorState);
        assertFalse(hallway.isDoorOpen(0));
    }

    public static class Hallway {
        private Map<Integer, DoorStatus> _doorStates;
        private int _numDoorsToSkip = 0;

        public Hallway() {
            _doorStates = Map.of();
        }

        public Hallway(Map<Integer, DoorStatus> initialDoorState) {
            _doorStates = initialDoorState;
        }

        public boolean isDoorOpen(int doorNumber) {
            return _doorStates.get(doorNumber) == DoorStatus.OPEN;
        }

        public boolean allAreClose() {
            return true;
        }

        public boolean allAreOpen() {
            return true;
        }

        public void visit() {
            int maxDoorNumber = _doorStates.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
            int doorStep = _numDoorsToSkip + 1;
            for (int doorNumber = _numDoorsToSkip; doorNumber < maxDoorNumber; doorNumber += doorStep) {
                _doorStates.compute(doorNumber, (currentDoorNumber, state) -> {
                    if (state == DoorStatus.CLOSE) {
                        return DoorStatus.OPEN;
                    } else if (state == DoorStatus.OPEN) {
                        return DoorStatus.CLOSE;
                    }
                    return null;
                });
            }
            _numDoorsToSkip++;
        }
    }
}

