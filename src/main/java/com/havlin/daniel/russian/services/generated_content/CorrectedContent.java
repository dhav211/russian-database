package com.havlin.daniel.russian.services.generated_content;

import java.util.List;

/**
 * A simple POJO that is only used when returning the errors discovered when correcting. This will help us correct
 * any issues with a sentence by hand. When a sentence can't be fixed automatically, the error will be added to this
 * list with a simple explanation.
 * @param isCorrected Could the sentence could be corrected on its own, will be rejected if can't.
 * @param correctedText The best attempt at correcting the test, if there are no errors detected we can be reasonable confident it's correct.
 */
record CorrectedContent(boolean isCorrected, String correctedText) {}
