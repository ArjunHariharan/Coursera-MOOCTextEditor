package document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A naive implementation of the Document abstract class.
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 */
public class BasicDocument extends Document {
	/**
	 * Create a new BasicDocument object
	 * 
	 * @param text
	 *            The full text of the Document.
	 */
	long beforeTime, afterTime;

	public BasicDocument(String text) {
		super(text);
	}

	/**
	 * Get the number of words in the document. "Words" are defined as
	 * contiguous strings of alphabetic characters i.e. any upper or lower case
	 * characters a-z or A-Z
	 * 
	 * @return The number of words in the document.
	 */
	@Override
	public int getNumWords() {
		beforeTime = System.nanoTime();

		Pattern wordPattern = Pattern.compile("[a-zA-Z]+");
		Matcher wordMatcher = wordPattern.matcher(this.getText());
		int count = getNoOfMatches(wordMatcher);

		afterTime = System.nanoTime();

		double time = (double) (afterTime - beforeTime) / (1000 * 1000 * 1000);

		return count;
	}

	/**
	 * Get the number of sentences in the document. Sentences are defined as
	 * contiguous strings of characters ending in an end of sentence punctuation
	 * (. ! or ?) or the last contiguous set of characters in the document, even
	 * if they don't end with a punctuation mark.
	 * 
	 * @return The number of sentences in the document.
	 */
	@Override
	public int getNumSentences() {
		beforeTime = System.nanoTime();
		String text = this.getText();
		Pattern endOfLinePunctuationPattern = Pattern.compile("[.!?]+");
		Matcher endOfLineMatcher = endOfLinePunctuationPattern.matcher(text);

		int count = getNoOfMatches(endOfLineMatcher);

		if (!text.isEmpty()) {
			char endChar = text.charAt(text.length() - 1);

			/**
			 * last character could end without '.' or '!' or '?'
			 */
			if (endChar != '.' && endChar != '?' && endChar != '!') {
				count++;
			}
		}

		afterTime = System.nanoTime();
		double time = (double) (afterTime - beforeTime) / (1000 * 1000 * 1000);

		return count;
	}

	/**
	 * Get the number of syllables in the document. Words are defined as above.
	 * Syllables are defined as: a contiguous sequence of vowels, except for a
	 * lone "e" at the end of a word if the word has another set of contiguous
	 * vowels, makes up one syllable. y is considered a vowel.
	 * 
	 * @return The number of syllables in the document.
	 */
	@Override
	public int getNumSyllables() {
		beforeTime = System.nanoTime();
		Pattern wordPattern = Pattern.compile("[a-zA-Z]+");
		Matcher wordMatcher = wordPattern.matcher(this.getText());

		Pattern syllablePattern = Pattern.compile("[aeiouyAEIOUY]+");
		Matcher syllableMatcher;

		int syllablesCount = 0;
		int noOfSyllablesInWord;

		while (wordMatcher.find()) {
			syllableMatcher = syllablePattern.matcher(wordMatcher.group());
			noOfSyllablesInWord = getNoOfMatches(syllableMatcher);

			if (noOfSyllablesInWord > 1 && wordMatcher.group().endsWith("e")) {
				/**
				 * Check if the word ends with a lone 'e'.
				 */
				syllableMatcher = syllablePattern.matcher(wordMatcher.group());
				String lastSyllableGroup = getLastGroup(syllableMatcher, noOfSyllablesInWord);

				if (lastSyllableGroup.length() == 1) {
					syllablesCount += (noOfSyllablesInWord - 1);
				} else {
					syllablesCount += noOfSyllablesInWord;
				}
			} else {
				syllablesCount += noOfSyllablesInWord;
			}
		}

		afterTime = System.nanoTime();
		double time = (double) (afterTime - beforeTime) / (1000 * 1000 * 1000);

		return syllablesCount;
	}

	/*
	 * The main method for testing this class. You are encouraged to add your
	 * own tests.
	 */
	public static void main(String[] args) {
		testCase(
				new BasicDocument(
						"This is a test.  How many???  " + "Senteeeeeeeeeences are here... there should be 5!  Right?"),
				16, 13, 5);
		testCase(new BasicDocument(""), 0, 0, 0);
		testCase(new BasicDocument("sentence, with, lots, of, commas.!  " + "(And some poaren)).  The output is: 7.5."),
				15, 11, 4);
		testCase(new BasicDocument("many???  Senteeeeeeeeeences are"), 6, 3, 2);
		testCase(
				new BasicDocument("Here is a series of test sentences. Your program should "
						+ "find 3 sentences, 33 words, and 49 syllables. Not every word will have "
						+ "the correct amount of syllables (example, for example), " + "but most of them will."),
				49, 33, 3);
		testCase(new BasicDocument("Segue"), 2, 1, 1);
		testCase(new BasicDocument("Sentence"), 2, 1, 1);
		testCase(new BasicDocument("Sentences?!"), 3, 1, 1);
		testCase(
				new BasicDocument(
						"Lorem ipsum dolor sit amet, qui ex choro quodsi moderatius, nam dolores explicari forensibus ad."),
				32, 15, 1);
	}

	private int getNoOfMatches(Matcher matcher) {
		int count = 0;
		while (matcher.find()) {
			count++;
		}

		return count;
	}

	private String getLastGroup(Matcher matcher, int size) {
		matcher.find();

		while (size > 1) {
			matcher.find();
			size--;
		}

		return matcher.group();
	}
}
