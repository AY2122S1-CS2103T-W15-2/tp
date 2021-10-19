package dash.logic.parser.taskcommand;

import static dash.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static dash.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import dash.commons.core.index.Index;
import dash.logic.commands.taskcommand.EditTaskCommand.EditTaskDescriptor;
import dash.logic.commands.taskcommand.TagTaskCommand;
import dash.logic.parser.ArgumentMultimap;
import dash.logic.parser.ArgumentTokenizer;
import dash.logic.parser.ParserRequiringPersonList;
import dash.logic.parser.ParserUtil;
import dash.logic.parser.exceptions.ParseException;
import dash.model.person.Person;
import dash.model.tag.Tag;
import javafx.collections.ObservableList;

public class TagTaskCommandParser implements ParserRequiringPersonList<TagTaskCommand> {
    @Override
    public TagTaskCommand parse(String userInput) throws ParseException {
        return null;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the TagTaskCommand
     * and returns a TagTaskCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public TagTaskCommand parse(String args, ObservableList<Person> filteredPersonList) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TagTaskCommand.MESSAGE_USAGE),
                    pe);
        }

        EditTaskDescriptor editTaskDescriptor = new EditTaskDescriptor();
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editTaskDescriptor::setTags);

        if (!editTaskDescriptor.isAnyFieldEdited()) {
            throw new ParseException(TagTaskCommand.MESSAGE_NOT_EDITED);
        }

        return new TagTaskCommand(index, editTaskDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }
}
