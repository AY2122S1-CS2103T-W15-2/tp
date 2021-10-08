package seedu.address.logic.commands.personcommand;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindPersonCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain all of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters Type 1: KEYWORD\n"
            + "Parameters Type 2: [KEYWORD]"
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]... (at least 1)\n"
            + "Example 1: " + COMMAND_WORD + " alice bob charlie\n"
            + "Example 2: " + COMMAND_WORD + " [" + PREFIX_PHONE + "91234567] " + "[" + PREFIX_EMAIL
            + "johndoe@example.com]\n"
            + "Example 3: " + COMMAND_WORD + " Bob " + "[" + PREFIX_PHONE + "91234567]\n";

    private final FindPersonDescriptor findPersonDescriptor;
    private final Predicate<Person> predicate;

    /**
     * Constructor for the FindPersonCommand that takes in a FindPersonDescriptor.
     */
    public FindPersonCommand(FindPersonDescriptor findPersonDescriptor) {
        requireNonNull(findPersonDescriptor);

        this.findPersonDescriptor = findPersonDescriptor;
        this.predicate = findPersonDescriptor.combinePredicates();
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindPersonCommand // instanceof handles nulls
                && findPersonDescriptor.equals(((FindPersonCommand) other).findPersonDescriptor)); // state check
    }

    /**
     * Stores the predicates to find a person with. Each non-empty field value will determine
     * the fields to search for a specific person.
     */
    public static class FindPersonDescriptor {
        private NameContainsKeywordsPredicate namePredicate;
        private PhoneContainsKeywordsPredicate phonePredicate;
        private AddressContainsKeywordsPredicate addressPredicate;
        private EmailContainsKeywordsPredicate emailPredicate;
        private TagContainsKeywordsPredicate tagPredicate;

        public FindPersonDescriptor() {
        }

        /**
         * Returns true if at least one predicate is present.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(namePredicate, phonePredicate, emailPredicate, addressPredicate,
                    tagPredicate);
        }

        public void setName(List<String> namePredicate) {
            this.namePredicate = new NameContainsKeywordsPredicate(namePredicate);
        }

        public Optional<NameContainsKeywordsPredicate> getName() {
            return Optional.ofNullable(namePredicate);
        }

        public void setPhone(List<String> phonePredicate) {
            this.phonePredicate = new PhoneContainsKeywordsPredicate(phonePredicate);
        }

        public Optional<PhoneContainsKeywordsPredicate> getPhone() {
            return Optional.ofNullable(phonePredicate);
        }

        public void setEmail(List<String> emailPredicate) {
            this.emailPredicate = new EmailContainsKeywordsPredicate(emailPredicate);
        }

        public Optional<EmailContainsKeywordsPredicate> getEmail() {
            return Optional.ofNullable(emailPredicate);
        }

        public void setAddress(List<String> addressPredicate) {
            this.addressPredicate = new AddressContainsKeywordsPredicate(addressPredicate);
        }

        public Optional<AddressContainsKeywordsPredicate> getAddress() {
            return Optional.ofNullable(addressPredicate);
        }

        public void setTags(List<String> tagPredicate) {
            this.tagPredicate = new TagContainsKeywordsPredicate(tagPredicate);
        }

        public Optional<TagContainsKeywordsPredicate> getTags() {
            return Optional.ofNullable(tagPredicate);
        }

        /**
         * This method takes all the conditions to check and combines them into one predicate.
         *
         * @return A combined predicate object to use on a filteredlist.
         */
        public Predicate<Person> combinePredicates() {
            Predicate<Person> result = x -> true;
            if (namePredicate != null) {
                result = result.and(namePredicate);
            }
            if (phonePredicate != null) {
                result = result.and(phonePredicate);
            }
            if (addressPredicate != null) {
                result = result.and(addressPredicate);
            }
            if (emailPredicate != null) {
                result = result.and(emailPredicate);
            }
            if (tagPredicate != null) {
                result = result.and(tagPredicate);
            }
            return result;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof FindPersonCommand.FindPersonDescriptor)) {
                return false;
            }

            // state check
            FindPersonCommand.FindPersonDescriptor f = (FindPersonCommand.FindPersonDescriptor) other;

            return getName().equals(f.getName())
                    && getPhone().equals(f.getPhone())
                    && getEmail().equals(f.getEmail())
                    && getAddress().equals(f.getAddress())
                    && getTags().equals(f.getTags());
        }
    }
}
