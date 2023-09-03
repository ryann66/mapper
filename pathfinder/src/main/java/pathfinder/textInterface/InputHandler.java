/*
 * Copyright (C) 2023 Soham Pardeshi.  All rights reserved.
 */

package pathfinder.textInterface;

/**
 * An {@link InputHandler} is capable of responding to a {@link String} command being input.
 */
public interface InputHandler {

    /**
     * Responds to the text of the command, in some way.
     *
     * @param input The command text that was input.
     */
    void handleInput(String input);

}
