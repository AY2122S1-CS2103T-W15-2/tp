package dash.logic.parser.taskcommand;

import dash.commons.core.Messages;
import dash.logic.commands.taskcommand.CompleteTaskCommand;
import dash.logic.parser.CommandParserTestUtil;
import dash.testutil.TypicalIndexes;
import org.junit.jupiter.api.Test;

class CompleteTaskCommandParserTest {
    private CompleteTaskCommandParser parser = new CompleteTaskCommandParser();

    @Test
    public void parse_validArgs_returnsCompleteTaskCommand() {
        CommandParserTestUtil.assertParseSuccess(parser, "1",
                new CompleteTaskCommand(TypicalIndexes.INDEX_FIRST));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser, "a",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        CompleteTaskCommand.MESSAGE_USAGE));
    }

}
