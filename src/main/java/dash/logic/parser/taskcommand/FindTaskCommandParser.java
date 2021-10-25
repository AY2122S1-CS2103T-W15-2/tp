package dash.logic.parser.taskcommand;

import static dash.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static dash.logic.parser.CliSyntax.PREFIX_COMPLETION_STATUS;
import static dash.logic.parser.CliSyntax.PREFIX_PERSON;
import static dash.logic.parser.CliSyntax.PREFIX_TAG;
import static dash.logic.parser.CliSyntax.PREFIX_TASK_DATE;
import static dash.logic.parser.CliSyntax.PREFIX_TASK_DESCRIPTION;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import dash.logic.commands.taskcommand.FindTaskCommand;
import dash.logic.commands.taskcommand.FindTaskCommand.FindTaskDescriptor;
import dash.logic.parser.ArgumentMultimap;
import dash.logic.parser.ArgumentTokenizer;
import dash.logic.parser.Parser;
import dash.logic.parser.ParserUtil;
import dash.logic.parser.exceptions.ParseException;
import dash.model.task.TaskDate;


/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindTaskCommandParser implements Parser<FindTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindTaskCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TASK_DESCRIPTION, PREFIX_TASK_DATE, PREFIX_TAG, PREFIX_PERSON,
                        PREFIX_COMPLETION_STATUS);
        String preamble = argMultimap.getPreamble();

        FindTaskDescriptor findTaskDescriptor = new FindTaskDescriptor();
        boolean descPresent = argMultimap.getValue(PREFIX_TASK_DESCRIPTION).isPresent();
        boolean datePresent = argMultimap.getValue(PREFIX_TASK_DATE).isPresent();
        boolean tagPresent = argMultimap.getValue(PREFIX_TAG).isPresent();
        boolean personPresent = argMultimap.getValue(PREFIX_PERSON).isPresent();
        boolean completionStatusPresent = argMultimap.getValue(PREFIX_COMPLETION_STATUS).isPresent();
        if (preamble.isEmpty() && !descPresent && !tagPresent && !datePresent && !personPresent
                && !completionStatusPresent) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTaskCommand.MESSAGE_USAGE));
        } else if (!preamble.isEmpty()) {
            String[] preambleKeywords = preamble.split("\\s+");
            findTaskDescriptor.setDesc(Arrays.asList(preambleKeywords));
        }
        //if both preamble and desc prefix specified, desc prefix will override
        if (descPresent) {
            if (argMultimap.getValue(PREFIX_TASK_DESCRIPTION).get().isEmpty()) {
                throw new ParseException("Arguments cannot be empty");
            }
            String[] nameKeywords = argMultimap.getValue(PREFIX_TASK_DESCRIPTION).get().split("\\s+");
            findTaskDescriptor.setDesc(Arrays.asList(nameKeywords));
        }
        if (datePresent) {
            if (argMultimap.getValue(PREFIX_TASK_DATE).get().isEmpty()) {
                throw new ParseException("Arguments cannot be empty");
            }
            TaskDate taskDateArg = ParserUtil.parseTaskDateToEdit(argMultimap.getValue(PREFIX_TASK_DATE).get());
            findTaskDescriptor.setDate(taskDateArg);
        }
        if (tagPresent) {
            if (argMultimap.getValue(PREFIX_TAG).get().isEmpty()) {
                throw new ParseException("Arguments cannot be empty");
            }
            String[] tagKeywords = argMultimap.getValue(PREFIX_TAG).get().split("\\s+");
            findTaskDescriptor.setTags(Arrays.asList(tagKeywords));
        }
        if (personPresent) {
            if (argMultimap.getValue(PREFIX_PERSON).get().isEmpty()) {
                throw new ParseException("Arguments cannot be empty");
            }
            String[] personKeywords = argMultimap.getValue(PREFIX_PERSON).get().split("\\s+");
            findTaskDescriptor.setPerson(Arrays.asList(personKeywords));
        }
        if (completionStatusPresent) {
            if (argMultimap.getValue(PREFIX_COMPLETION_STATUS).get().isEmpty()) {
                throw new ParseException("Arguments cannot be empty");
            }
            String[] completionStatusKeywords = argMultimap.getValue(PREFIX_COMPLETION_STATUS).get().split("\\s+");
            String firstKeyword = completionStatusKeywords[0];
            findTaskDescriptor.setCompletionStatus(ParserUtil.parseCompletionStatus(firstKeyword).get());
        }

        return new FindTaskCommand(findTaskDescriptor);

    }

}
