package org.ynov.missionControl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ynov.communication.Connection;
import org.ynov.communication.ICommunicator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MissionControlTest {

    private ICommunicator communicatorMock;
    private Connection fakeConnection;
    private StringWriter outWriter;

    @BeforeEach
    void setup() {
        String consoleInput = "zq\n";
        System.setIn(new ByteArrayInputStream(consoleInput.getBytes()));

        BufferedReader inReader = new BufferedReader(new StringReader(""));
        outWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(outWriter, true);

        fakeConnection = new Connection(inReader, printWriter);
        fakeConnection.in = inReader;
        fakeConnection.out = printWriter;

        communicatorMock = Mockito.mock(ICommunicator.class);
        when(communicatorMock.HostCommunication()).thenReturn(fakeConnection);
    }

    @AfterEach
    void tearDown() {
        // rendre System.in neutre après chaque test
        System.setIn(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    @DisplayName("Constructor reads console and sends encoded instructions to connection.out")
    void constructorSendsInstructionsFromConsole() {
        // Création de l'instance under test
        MissionControl mc = new MissionControl(communicatorMock);

        String out = outWriter.toString();
        assertNotNull(out, "Le writer de sortie ne doit pas être null");
        assertFalse(out.isBlank(), "On doit avoir envoyé quelque chose sur la connexion (instructions).");

        assertFalse(MissionControl.isPromptingUser, "isPromptingUser doit être false après traitement.");
    }

    @Test
    @DisplayName("Key events add instructions and pressing ENTER sends them")
    void keyPressedAddsInstructionsAndSendsOnEnter() {
        StringWriter localOut = new StringWriter();
        fakeConnection.out = new PrintWriter(localOut, true);
        when(communicatorMock.HostCommunication()).thenReturn(fakeConnection);

        System.setIn(new ByteArrayInputStream("\n".getBytes()));

        MissionControl mc = new MissionControl(communicatorMock);

        MissionControl.isPromptingUser = true;

        Component dummySource = new Label();
        KeyEvent upEvent = new KeyEvent(dummySource, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
        mc.keyPressed(upEvent);

        KeyEvent rightEvent = new KeyEvent(dummySource, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        mc.keyPressed(rightEvent);

        KeyEvent enterEvent = new KeyEvent(dummySource, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
        mc.keyPressed(enterEvent);

        String sent = localOut.toString();
        assertNotNull(sent);
        assertFalse(sent.isBlank(), "Après ENTER, des instructions doivent avoir été envoyées sur la connexion.");
        assertFalse(MissionControl.isPromptingUser, "isPromptingUser doit être false après appui ENTER.");
    }
}
